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
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.search.DSQuery;
import org.dspace.search.QueryArgs;
import org.dspace.search.QueryResults;
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

public class SearchController extends AbstractController {
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();

        mav.addObject("topLevelCommunities", Community.findAllTop((Context) request.getAttribute("context")));
        mav.setViewName("pages/search/form");

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

        void doSearch() throws IOException, SQLException {
            // Get the query
            String advancedQuery = "";
            String query = request.getParameter("query");
            String advanced = request.getParameter("advanced");
            String fromAdvanced = request.getParameter("from_advanced");
            String order = request.getParameter("order");
            int start = ServletRequestUtils.getIntParameter(request, "start", -1);
            int sortBy = ServletRequestUtils.getIntParameter(request, "sort_by", -1);
            int rpp = ServletRequestUtils.getIntParameter(request, "rpp", -1);

            if (start < 0) {
                start = 0;
            }

            int collCount = 0;
            int commCount = 0;
            int itemCount = 0;

            Item[] resultsItems;
            Collection[] resultsCollections;
            Community[] resultsCommunities;

            QueryResults qResults = null;
            QueryArgs qArgs = new QueryArgs();
            SortOption sortOption = null;

            if (request.getParameter("etal") != null) {
                qArgs.setEtAl(ServletRequestUtils.getIntParameter(request, "etal", -1));
            }

            try {
                if (sortBy > 0) {
                    sortOption = SortOption.getSortOption(sortBy);
                    qArgs.setSortOption(sortOption);
                }

                if (SortOption.ASCENDING.equalsIgnoreCase(order)) {
                    qArgs.setSortOrder(SortOption.ASCENDING);
                } else {
                    qArgs.setSortOrder(SortOption.DESCENDING);
                }
            } catch (Exception e) {
            }

            // Override the page setting if exporting metadata
//            if ("submit_export_metadata".equals(ServletRequestUtils.getSubmitButton(request, "submit")))
//            {
//                qArgs.setPageSize(Integer.MAX_VALUE);
//            } else
            if (rpp > 0) {
                qArgs.setPageSize(rpp);
            }

            // if the "advanced" flag is set, build the query string from the
            // multiple query fields
            if (advanced != null) {
                query = qArgs.buildQuery(request);
                advancedQuery = qArgs.buildHTTPQuery(request);
            }

            // Ensure the query is non-null
            if (query == null) {
                query = "";
            }

            // Get the location parameter, if any
            String location = request.getParameter("location");

            // If there is a location parameter, we should redirect to
            // do the search with the correct location.
            if (!StringUtils.isEmpty(location)) {
                String url = "";

                if (!location.equals("/")) {
                    // Location is a Handle
                    url = "/handle/" + location;
                }

                // Encode the query
                query = URLEncoder.encode(query, Constants.DEFAULT_ENCODING);

                if (advancedQuery.length() > 0) {
                    query = query + "&from_advanced=true&" + advancedQuery;
                }

                // Do the redirect
//                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + url + "/simple-search?query=" + query));
                return;
            }

            // get the start of the query results page
            //        List resultObjects = null;
            qArgs.setQuery(query);
            qArgs.setStart(start);

            qResults = DSQuery.doQuery(context, qArgs); // And DSO for community / collection

            // now instantiate the results and put them in their buckets
            for (int i = 0; i < qResults.getHitTypes().size(); i++) {
                Integer myType = qResults.getHitTypes().get(i);

                // add the handle to the appropriate lists
                switch (myType.intValue()) {
                    case Constants.ITEM:
                        itemCount++;
                        break;

                    case Constants.COLLECTION:
                        collCount++;
                        break;

                    case Constants.COMMUNITY:
                        commCount++;
                        break;
                }
            }

            // Make objects from the handles - make arrays, fill them out
            resultsCommunities = new Community[commCount];
            resultsCollections = new Collection[collCount];
            resultsItems = new Item[itemCount];

            collCount = 0;
            commCount = 0;
            itemCount = 0;

            for (int i = 0; i < qResults.getHitTypes().size(); i++) {
                Integer myId    = qResults.getHitIds().get(i);
                String myHandle = qResults.getHitHandles().get(i);
                Integer myType  = qResults.getHitTypes().get(i);

                // add the handle to the appropriate lists
                switch (myType.intValue()) {
                    case Constants.ITEM:
                        if (myId != null) {
                            resultsItems[itemCount] = Item.find(context, myId);
                        } else {
                            resultsItems[itemCount] = (Item) HandleManager.resolveToObject(context, myHandle);
                        }

                        if (resultsItems[itemCount] == null) {
                            throw new SQLException("Query \"" + query + "\" returned unresolvable item");
                        }

                        itemCount++;
                        break;

                    case Constants.COLLECTION:
                        if (myId != null) {
                            resultsCollections[collCount] = Collection.find(context, myId);
                        } else {
                            resultsCollections[collCount] = (Collection)HandleManager.resolveToObject(context, myHandle);
                        }

                        if (resultsCollections[collCount] == null) {
                            throw new SQLException("Query \"" + query + "\" returned unresolvable collection");
                        }

                        collCount++;
                        break;

                    case Constants.COMMUNITY:
                        if (myId != null) {
                            resultsCommunities[commCount] = Community.find(context, myId);
                        } else {
                            resultsCommunities[commCount] = (Community)HandleManager.resolveToObject(context, myHandle);
                        }

                        if (resultsCommunities[commCount] == null) {
                            throw new SQLException("Query \"" + query + "\" returned unresolvable community");
                        }

                        commCount++;
                        break;
                }
            }

            // Pass in some page qualities
            // total number of pages
            int pageTotal = 1 + ((qResults.getHitCount() - 1) / qResults.getPageSize());

            // current page being displayed
            int pageCurrent = 1 + (qResults.getStart() / qResults.getPageSize());

            // pageLast = min(pageCurrent+9,pageTotal)
            int pageLast = ((pageCurrent + 9) > pageTotal) ? pageTotal : (pageCurrent + 9);

            // pageFirst = max(1,pageCurrent-9)
            int pageFirst = ((pageCurrent - 9) > 1) ? (pageCurrent - 9) : 1;
        }
    }
}
