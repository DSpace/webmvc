/**
 * $Id: $
 * $URL: $
 * *************************************************************************
 * Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 * Licensed under the DuraSpace License.
 *
 * A copy of the DuraSpace License has been included in this
 * distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
 */

package org.dspace.webmvc.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.browse.*;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.sort.SortException;
import org.dspace.sort.SortOption;
import org.dspace.webmvc.processor.HandleRequestProcessor;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class BrowseController extends AbstractController {
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        BrowseRequestProcessor brp = new BrowseRequestProcessor((Context)request.getAttribute("context"), request);

        BrowserScope scope = brp.getBrowserScopeForRequest();

        if (scope.getBrowseIndex() == null) {
            return null; // Go to browse index not found page
        }

        BrowseInfo binfo = brp.processBrowse(scope);

        request.setAttribute("browse.info", binfo);

        if (binfo.hasResults()) {
            if (binfo.getBrowseIndex().isMetadataIndex() && !scope.isSecondLevel()) {
                mav.setViewName("pages/browse/metadata");
            } else {
                mav.setViewName("pages/browse/items");
            }
        } else {
            mav.setViewName("pages/browse/empty");
        }

        mav.addObject("browseScope", scope);
        mav.addObject("browseInfo", binfo);

        return mav;
    }

    static class BrowseRequestProcessor {
        private static Logger log = Logger.getLogger(BrowseRequestProcessor.class);

        private Context context;
        private HttpServletRequest request;

        private HandleRequestProcessor hrp;

        BrowseRequestProcessor(Context pContext, HttpServletRequest pRequest) {
            context = pContext;
            request = pRequest;

            hrp = new HandleRequestProcessor((Context)request.getAttribute("context"), request);
        }

        protected BrowserScope getBrowserScopeForRequest() throws ServletException, IOException, SQLException, AuthorizeException {
            try {
                // first, lift all the stuff out of the request that we might need
                String type = request.getParameter("type");
                String order = request.getParameter("order");
                String value = request.getParameter("value");
                String valueLang = request.getParameter("value_lang");
                String month = request.getParameter("month");
                String year = request.getParameter("year");
                String startsWith = request.getParameter("starts_with");
                String valueFocus = request.getParameter("vfocus");
                String valueFocusLang = request.getParameter("vfocus_lang");
                String authority = request.getParameter("authority");

                int focus = ServletRequestUtils.getIntParameter(request, "focus", -1);
                int offset = ServletRequestUtils.getIntParameter(request, "offset", 0);
                int resultsperpage = ServletRequestUtils.getIntParameter(request, "rpp", -1);
                int sortBy = ServletRequestUtils.getIntParameter(request, "sort_by", -1);
                int etAl = ServletRequestUtils.getIntParameter(request, "etal", -1);

                // process the input, performing some inline validation
                BrowseIndex bi = null;
                if (!StringUtils.isEmpty(type)) {
                    bi = BrowseIndex.getBrowseIndex(type);
                }

                if (bi == null) {
                    if (sortBy > 0) {
                        bi = BrowseIndex.getBrowseIndex(SortOption.getSortOption(sortBy));
                    } else {
                        bi = BrowseIndex.getBrowseIndex(SortOption.getDefaultSortOption());
                    }
                }

                // If we don't have a sort column
                if (bi != null && sortBy < 0) {
                    // Get the default one
                    SortOption so = bi.getSortOption();
                    if (so != null) {
                        sortBy = so.getNumber();
                    }
                } else if (bi != null && bi.isItemIndex() && !bi.isInternalIndex()) {
                    // If a default sort option is specified by the index, but it isn't
                    // the same as sort option requested, attempt to find an index that
                    // is configured to use that sort by default
                    // This is so that we can then highlight the correct option in the navigation
                    SortOption bso = bi.getSortOption();
                    SortOption so = SortOption.getSortOption(sortBy);
                    if ( bso != null && bso.equals(so)) {
                        BrowseIndex newBi = BrowseIndex.getBrowseIndex(so);
                        if (newBi != null) {
                            bi   = newBi;
                            type = bi.getName();
                        }
                    }
                }

                if (order == null && bi != null) {
                    order = bi.getDefaultOrder();
                }

                // if no resultsperpage set, default to 20
                if (resultsperpage < 0) {
                    resultsperpage = 20;
                    int columns = ConfigurationManager.getIntProperty("webui.browse." + type + ".columns");
                    if (columns > 1 && StringUtils.isEmpty(value)) {
                        resultsperpage = resultsperpage * columns;
                    }
                }

                // if year and perhaps month have been selected, we translate these into "startsWith"
                // if startsWith has already been defined then it is overwritten
                if (year != null && !"".equals(year) && !"-1".equals(year)) {
                    startsWith = year;
                    if ((month != null) && !"-1".equals(month) && !"".equals(month)) {
                        // subtract 1 from the month, so the match works appropriately
                        if ("ASC".equals(order)) {
                            month = Integer.toString((Integer.parseInt(month) - 1));
                        }

                        // They've selected a month as well
                        if (month.length() == 1) {
                            // Ensure double-digit month number
                            month = "0" + month;
                        }

                        startsWith = year + "-" + month;

                        if ("ASC".equals(order)) {
                            startsWith = startsWith + "-32";
                        }
                    }
                }

                // determine which level of the browse we are at: 0 for top, 1 for second
                int level = 0;
                if (value != null || authority != null) {
                    level = 1;
                }

                // if sortBy is still not set, set it to 0, which is default to use the primary index value
                if (sortBy == -1) {
                    sortBy = 0;
                }

                // figure out the setting for author list truncation
                if (etAl == -1) {
                    // there is no limit, or the UI says to use the default
                    int limitLine = ConfigurationManager.getIntProperty("webui.browse.author-limit");
                    if (limitLine != 0) {
                        etAl = limitLine;
                    }
                }
                else {
                    // if the user has set a limit
                    if (etAl == 0) {    // 0 is the user setting for unlimited
                        etAl = -1;      // but -1 is the application setting for unlimited
                    }
                }

                // set up a BrowseScope and start loading the values into it
                BrowserScope scope = new BrowserScope(context);

                scope.setBrowseIndex(bi);
                scope.setOrder(order);
                scope.setFilterValue(value != null?value:authority);
                scope.setFilterValueLang(valueLang);
                scope.setJumpToItem(focus);
                scope.setJumpToValue(valueFocus);
                scope.setJumpToValueLang(valueFocusLang);
                scope.setStartsWith(startsWith);
                scope.setOffset(offset);
                scope.setResultsPerPage(resultsperpage);
                scope.setSortBy(sortBy);
                scope.setBrowseLevel(level);
                scope.setEtAl(etAl);
                scope.setAuthorityValue(authority);

                // assign the scope of either Community or Collection if necessary
                DSpaceObject dso = hrp.getObject();
                if (dso instanceof Community || dso instanceof Collection) {
                    scope.setBrowseContainer(dso);
                }

                // For second level browses on metadata indexes, we need to adjust the default sorting
                if (bi != null && bi.isMetadataIndex() && scope.isSecondLevel() && scope.getSortBy() <= 0) {
                    scope.setSortBy(1);
                }

                return scope;
            }
            catch (SortException se) {
                log.error("caught exception: ", se);
                throw new ServletException(se);
            }
            catch (BrowseException e) {
                log.error("caught exception: ", e);
                throw new ServletException(e);
            }
        }

        protected BrowseInfo processBrowse(BrowserScope scope) throws BrowseException {
            // now start up a browse engine and get it to do the work for us
            BrowseEngine be = new BrowseEngine(context);
            return be.browse(scope);
        }
    }
}
