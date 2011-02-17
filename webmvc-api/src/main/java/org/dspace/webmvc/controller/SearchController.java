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
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.search.*;
import org.dspace.sort.SortOption;
import org.dspace.webmvc.processor.HandleRequestProcessor;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchController extends AbstractController {
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
//        mav.addObject("topLevelCommunities", Community.findAllTop((Context) request.getAttribute("context")));

        SearchRequestProcessor srp = new SearchRequestProcessor((Context)request.getAttribute("context"), request);

        SearchForm searchForm = srp.getSearchForm();

        if (!StringUtils.isEmpty(request.getParameter("submit"))) {
            SearchInfo sinfo = srp.doSearch(searchForm);
            if (sinfo != null) {
                if (sinfo.requiresRedirect()) {
                    String redirectBase;
                    if (sinfo.getSearchContainer() != null) {
                        redirectBase = "/handle/" + sinfo.getSearchContainer().getHandle() + "/simple-search?query=";
                    } else {
                        redirectBase = "/simple-search?query=";
                    }

                    if (!StringUtils.isEmpty(sinfo.getAdvancedQuery())) {
                        mav.setViewName("redirect:" + redirectBase +
                                URLEncoder.encode(sinfo.getQuery(), Constants.DEFAULT_ENCODING) +
                                "&from_advanced=true&" + sinfo.getAdvancedQuery());
                    } else {
                        mav.setViewName("redirect:" + redirectBase + URLEncoder.encode(sinfo.getQuery(), Constants.DEFAULT_ENCODING));
                    }
                } else {
                    mav.addObject("searchInfo", sinfo);
                    if (sinfo.hasResults()) {
                        mav.setViewName("pages/search/results");
                    } else {
                        mav.setViewName("pages/search/empty");
                    }
                }
            } else {
                searchForm.setAdvancedForm(true);
                mav.setViewName("pages/search/form");
            }
        } else {
            searchForm.setAdvancedForm(true);
            mav.setViewName("pages/search/form");
        }

        mav.addObject("searchForm", searchForm);
        return mav;
    }

    static class SearchRequestProcessor {
        private static Logger log = Logger.getLogger(SearchRequestProcessor.class);

        private Context context;
        private HttpServletRequest request;

        SearchRequestProcessor(Context pContext, HttpServletRequest pRequest) {
            context = pContext;
            request = pRequest;
        }

        SearchForm getSearchForm() {
            SearchForm searchForm = new SearchForm();

            String advanced = request.getParameter("advanced");
            if (!StringUtils.isEmpty(advanced)) {
                searchForm.setAdvancedForm(true);
                int numSearchFields = ServletRequestUtils.getIntParameter(request, "num_search_field", -1);
                if (numSearchFields > 0) {
                    searchForm.setNumAdvancedFields(numSearchFields);
                    for (int fieldIdx = 1; fieldIdx <= numSearchFields; fieldIdx++) {
                        String conjunction = request.getParameter("conjunction" + fieldIdx);
                        String field       = request.getParameter("field" + fieldIdx);
                        String query       = request.getParameter("query" + fieldIdx);

                        searchForm.setAdvancedField(fieldIdx - 1, conjunction, field, query);
                    }
                }
            } else {
                searchForm.setQuery(request.getParameter("query"));
            }

            String order = request.getParameter("order");
            int sortBy = ServletRequestUtils.getIntParameter(request, "sort_by", -1);

            SortOption sortOption = null;
            try {
                if (sortBy > 0) {
                    sortOption = SortOption.getSortOption(sortBy);
                    searchForm.setSortOption(sortOption);
                }

                if (SortOption.ASCENDING.equalsIgnoreCase(order)) {
                    searchForm.setSortOrder(SortOption.ASCENDING);
                } else {
                    searchForm.setSortOrder(SortOption.DESCENDING);
                }
            } catch (Exception e) {
            }

            if (request.getParameter("etal") != null) {
                searchForm.setEtAl(ServletRequestUtils.getIntParameter(request, "etal", -1));
            }

            int rpp = ServletRequestUtils.getIntParameter(request, "rpp", -1);
            if (rpp > 0) {
                searchForm.setResultsPerPage(rpp);
            }

            searchForm.setScope(request.getParameter("scope"));

            return searchForm;
        }

        SearchInfo doSearch(SearchForm searchForm) throws IOException, SQLException {
            List<Item> itemResults = new ArrayList<Item>();
            List<Collection> collectionResults = new ArrayList<Collection>();
            List<Community> communityResults = new ArrayList<Community>();

            QueryArgs qArgs = new QueryArgs();
            String query;

            int start = ServletRequestUtils.getIntParameter(request, "start", -1);
            if (start < 0) {
                start = 0;
            }
            qArgs.setStart(start);

            // if the "advanced" flag is set, build the query string from the
            // multiple query fields
            if (searchForm.isAdvancedForm()) {
                query = qArgs.buildQuery(request);
            } else {
                query = searchForm.getQuery();
            }

            // Ensure the query is non-null
            if (query == null) {
                query = "";
            }

            qArgs.setQuery(query);

            qArgs.setSortOption(searchForm.getSortOption());
            qArgs.setSortOrder(searchForm.getSortOrder());

            // Override the page setting if exporting metadata
//            if ("submit_export_metadata".equals(ServletRequestUtils.getSubmitButton(request, "submit")))
//            {
//                qArgs.setPageSize(Integer.MAX_VALUE);
//            } else

            qArgs.setPageSize(searchForm.getResultsPerPage());
            qArgs.setEtAl(searchForm.getEtAl());

            DSpaceObject container = null;

            QueryResults qResults;

            if (!StringUtils.isEmpty(searchForm.getScope()) && !"/".equals(searchForm.getScope())) {
                container = HandleManager.resolveToObject(context, searchForm.getScope());
            }

            if (container instanceof Community) {
                qResults = DSQuery.doQuery(context, qArgs, (Community)container);
            } else if (container instanceof  Collection) {
                qResults = DSQuery.doQuery(context, qArgs, (Collection)container);
            } else {
                qResults = DSQuery.doQuery(context, qArgs);
            }

            for (int i = 0; i < qResults.getHitTypes().size(); i++) {
                Integer currentId    = qResults.getHitIds().get(i);
                String  currentHandle = qResults.getHitHandles().get(i);
                Integer currentDsoType  = qResults.getHitTypes().get(i);

                // add the handle to the appropriate lists
                switch (currentDsoType.intValue()) {
                    case Constants.ITEM:
                        itemResults.add(resolveItem(currentId, currentHandle));
                        break;

                    case Constants.COLLECTION:
                        collectionResults.add(resolveCollection(currentId, currentHandle));
                        break;

                    case Constants.COMMUNITY:
                        communityResults.add(resolveCommunity(currentId, currentHandle));
                        break;

                    default:
                        throw new SQLException("Unknown item type");
                }
            }

            SearchInfo searchInfo = new SearchInfo(itemResults, collectionResults, communityResults);

            searchInfo.setTotal(qResults.getHitCount());
            searchInfo.setOverallPosition(start);

            if (start + searchForm.getResultsPerPage() < qResults.getHitCount()) {
                searchInfo.setNextOffset(start + searchForm.getResultsPerPage());
            }

            if (start - searchForm.getResultsPerPage() > -1) {
                searchInfo.setPrevOffset(start - searchForm.getResultsPerPage());
            }

            return searchInfo;
        }

        private Item resolveItem(Integer itemId, String handle) throws SQLException {
            Item item;
            if (itemId != null) {
                item = Item.find(context, itemId);
            } else {
                item = (Item) HandleManager.resolveToObject(context, handle);
            }

            if (item == null) {
                throw new SQLException("Query returned unresolvable item");
            }

            return item;
        }

        private Collection resolveCollection(Integer collectionId, String handle) throws SQLException {
            Collection collection;
            if (collectionId != null) {
                collection = Collection.find(context, collectionId);
            } else {
                collection = (Collection)HandleManager.resolveToObject(context, handle);
            }

            if (collection == null) {
                throw new SQLException("Query returned unresolvable collection");
            }

            return collection;
        }

        private Community resolveCommunity(Integer communityId, String handle) throws SQLException {
            Community community;
            if (communityId != null) {
                community = Community.find(context, communityId);
            } else {
                community = (Community)HandleManager.resolveToObject(context, handle);
            }

            if (community == null) {
                throw new SQLException("Query returned unresolvable community");
            }

            return community;
        }
    }
}

/*
            String fromAdvanced = request.getParameter("from_advanced");

            // Get the location parameter, if any
            String location = request.getParameter("location");

            // If there is a location parameter, we should redirect to
            // do the search with the correct location.
            if (!StringUtils.isEmpty(location)) {
                String url = "";

                // Do the redirect
//                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + url + "/simple-search?query=" + query));
                return;
            }

            // get the start of the query results page
            //        List resultObjects = null;
            qArgs.setQuery(query);
            qArgs.setStart(start);


            // Pass in some page qualities
            // total number of pages
            int pageTotal = 1 + ((qResults.getHitCount() - 1) / qResults.getPageSize());

            // current page being displayed
            int pageCurrent = 1 + (qResults.getStart() / qResults.getPageSize());

            // pageLast = min(pageCurrent+9,pageTotal)
            int pageLast = ((pageCurrent + 9) > pageTotal) ? pageTotal : (pageCurrent + 9);

            // pageFirst = max(1,pageCurrent-9)
            int pageFirst = ((pageCurrent - 9) > 1) ? (pageCurrent - 9) : 1;
*/