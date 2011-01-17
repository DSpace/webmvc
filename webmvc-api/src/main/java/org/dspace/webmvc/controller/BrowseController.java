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
import org.dspace.authorize.AuthorizeException;
import org.dspace.browse.BrowseException;
import org.dspace.browse.BrowseIndex;
import org.dspace.browse.BrowserScope;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.sort.SortException;
import org.dspace.sort.SortOption;
import org.springframework.web.bind.RequestUtils;
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
        BrowseRequestProcessor brp = new BrowseRequestProcessor((Context)request.getAttribute("context"), request);

        return null;
    }

    static class BrowseRequestProcessor {
        private Context context;
        private HttpServletRequest request;
        private boolean pathParsed = false;

        private String handle;

        private DSpaceObject dspaceObject;

        BrowseRequestProcessor(Context pContext, HttpServletRequest pRequest) {
            context = pContext;
            request = pRequest;
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

                ServletRequestUtils.getIntParameter(request, "focus");

                int focus = ServletRequestUtils.getIntParameter(request, "focus");
                int offset = ServletRequestUtils.getIntParameter(request, "offset");
                int resultsperpage = ServletRequestUtils.getIntParameter(request, "rpp");
                int sortBy = ServletRequestUtils.getIntParameter(request, "sort_by");
                int etAl = ServletRequestUtils.getIntParameter(request, "etal");

                // get the community or collection location for the browse request
                // Note that we are only interested in getting the "smallest" container,
                // so if we find a collection, we don't bother looking up the community
                Collection collection = null;
                Community community = null;
                collection = UIUtil.getCollectionLocation(request);
                if (collection == null)
                {
                    community = UIUtil.getCommunityLocation(request);
                }

                // process the input, performing some inline validation
                BrowseIndex bi = null;
                if (type != null && !"".equals(type))
                {
                    bi = BrowseIndex.getBrowseIndex(type);
                }

                if (bi == null)
                {
                    if (sortBy > 0)
                    {
                        bi = BrowseIndex.getBrowseIndex(SortOption.getSortOption(sortBy));
                    }
                    else
                    {
                        bi = BrowseIndex.getBrowseIndex(SortOption.getDefaultSortOption());
                    }
                }

                // If we don't have a sort column
                if (bi != null && sortBy == -1)
                {
                    // Get the default one
                    SortOption so = bi.getSortOption();
                    if (so != null)
                    {
                        sortBy = so.getNumber();
                    }
                }
                else if (bi != null && bi.isItemIndex() && !bi.isInternalIndex())
                {
                    // If a default sort option is specified by the index, but it isn't
                    // the same as sort option requested, attempt to find an index that
                    // is configured to use that sort by default
                    // This is so that we can then highlight the correct option in the navigation
                    SortOption bso = bi.getSortOption();
                    SortOption so = SortOption.getSortOption(sortBy);
                    if ( bso != null && bso.equals(so))
                    {
                        BrowseIndex newBi = BrowseIndex.getBrowseIndex(so);
                        if (newBi != null)
                        {
                            bi   = newBi;
                            type = bi.getName();
                        }
                    }
                }

                if (order == null && bi != null)
                {
                    order = bi.getDefaultOrder();
                }

                // If the offset is invalid, reset to 0
                if (offset < 0)
                {
                    offset = 0;
                }

                // if no resultsperpage set, default to 20
                if (resultsperpage < 0)
                {
                    resultsperpage = 20;
                    int columns = ConfigurationManager.getIntProperty("webui.browse." + type + ".columns");
                    if (columns > 1 && StringUtils.isEmpty(value))
                    {
                        resultsperpage = resultsperpage * columns;
                    }
                }

                // if year and perhaps month have been selected, we translate these into "startsWith"
                // if startsWith has already been defined then it is overwritten
                if (year != null && !"".equals(year) && !"-1".equals(year))
                {
                    startsWith = year;
                    if ((month != null) && !"-1".equals(month) && !"".equals(month))
                    {
                        // subtract 1 from the month, so the match works appropriately
                        if ("ASC".equals(order))
                        {
                            month = Integer.toString((Integer.parseInt(month) - 1));
                        }

                        // They've selected a month as well
                        if (month.length() == 1)
                        {
                            // Ensure double-digit month number
                            month = "0" + month;
                        }

                        startsWith = year + "-" + month;

                        if ("ASC".equals(order))
                        {
                            startsWith = startsWith + "-32";
                        }
                    }
                }

                // determine which level of the browse we are at: 0 for top, 1 for second
                int level = 0;
                if (value != null || authority != null)
                {
                    level = 1;
                }

                // if sortBy is still not set, set it to 0, which is default to use the primary index value
                if (sortBy == -1)
                {
                    sortBy = 0;
                }

                // figure out the setting for author list truncation
                if (etAl == -1)     // there is no limit, or the UI says to use the default
                {
                    int limitLine = ConfigurationManager.getIntProperty("webui.browse.author-limit");
                    if (limitLine != 0)
                    {
                        etAl = limitLine;
                    }
                }
                else  // if the user has set a limit
                {
                    if (etAl == 0)  // 0 is the user setting for unlimited
                    {
                        etAl = -1;  // but -1 is the application setting for unlimited
                    }
                }

                // log the request
                String comHandle = "n/a";
                if (community != null)
                {
                    comHandle = community.getHandle();
                }
                String colHandle = "n/a";
                if (collection != null)
                {
                    colHandle = collection.getHandle();
                }

                String arguments = "type=" + type + ",order=" + order + ",value=" + value +
                    ",month=" + month + ",year=" + year + ",starts_with=" + startsWith +
                    ",vfocus=" + valueFocus + ",focus=" + focus + ",rpp=" + resultsperpage +
                    ",sort_by=" + sortBy + ",community=" + comHandle + ",collection=" + colHandle +
                    ",level=" + level + ",etal=" + etAl;

                log.info(LogManager.getHeader(context, "browse", arguments));

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
                if (community != null)
                {
                    scope.setBrowseContainer(community);
                }
                else if (collection != null)
                {
                    scope.setBrowseContainer(collection);
                }

                // For second level browses on metadata indexes, we need to adjust the default sorting
                if (bi != null && bi.isMetadataIndex() && scope.isSecondLevel() && scope.getSortBy() <= 0)
                {
                    scope.setSortBy(1);
                }

                return scope;
            }
            catch (SortException se)
            {
                log.error("caught exception: ", se);
                throw new ServletException(se);
            }
            catch (BrowseException e)
            {
                log.error("caught exception: ", e);
                throw new ServletException(e);
            }
        }
    }
}
