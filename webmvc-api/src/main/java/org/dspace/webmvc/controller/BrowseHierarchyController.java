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

import org.apache.log4j.Logger;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;
import org.dspace.webmvc.utils.DSpaceRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BrowseHierarchyController extends AbstractController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        BrowseHierarchyRequestProcessor bhrp = new BrowseHierarchyRequestProcessor(DSpaceRequestUtils.getDSpaceContext(request), request);

        mav.addObject("collectionMap", bhrp.getCollectionMap());
        mav.addObject("communityMap", bhrp.getCommunityMap());
        mav.addObject("topLevelCommunities", bhrp.getTopLevelCommunities());

        mav.setViewName("pages/hierarchy");

        return mav;
    }

    static class BrowseHierarchyRequestProcessor {
        private static Logger log = Logger.getLogger(BrowseHierarchyRequestProcessor.class);

        private Context context;
        private HttpServletRequest request;

        Community[] topLevelCommunities;

        Map<Integer, Collection[]> collectionMap = new HashMap<Integer, Collection[]>();
        Map<Integer, Community[]>  communityMap  = new HashMap<Integer, Community[]>();

        BrowseHierarchyRequestProcessor(Context pContext, HttpServletRequest pRequest) {
            context = pContext;
            request = pRequest;

        }

        private void initializeMappings() throws SQLException {
            if (topLevelCommunities == null) {
                topLevelCommunities = Community.findAllTop(context);
                for (Community comm : topLevelCommunities) {
                    Integer commId  = Integer.valueOf(comm.getID());

                    // Find collections in community
                    collectionMap.put(commId, comm.getCollections());

                    // Find subcommunties in community
                    communityMap.put(commId, comm.getSubcommunities());
                }
            }
        }

        Map<Integer, Collection[]> getCollectionMap() throws SQLException {
            initializeMappings();
            return collectionMap;
        }

        Map<Integer, Community[]> getCommunityMap() throws SQLException {
            initializeMappings();
            return communityMap;
        }

        Community[] getTopLevelCommunities() throws SQLException {
            initializeMappings();
            return topLevelCommunities;
        }
    }
}
