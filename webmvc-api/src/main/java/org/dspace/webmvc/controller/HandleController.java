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

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.kernel.DSpaceKernel;
import org.dspace.kernel.DSpaceKernelManager;
import org.dspace.webmvc.utils.DSpaceModelUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class HandleController extends AbstractController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandleRequestProcessor hrp = new HandleRequestProcessor((Context)request.getAttribute("context"), request);

        ModelAndView mav = new ModelAndView();

        DSpaceObject dso = hrp.getObject();
        if (dso != null) {
            switch (dso.getType()) {
                case Constants.COLLECTION:
                    mav.setViewName("pages/collection");
                    mav.addObject("collection", (Collection)dso);
                    break;

                case Constants.COMMUNITY:
                    mav.setViewName("pages/community");
                    mav.addObject("community", (Community)dso);
                    break;

                case Constants.ITEM:
                    mav.setViewName("pages/item");
                    mav.addObject("item", (Item)dso);
                    break;

                default:
                    mav.setViewName("pages/unknowntype");
            }
        } else {
            mav.setViewName("pages/invalidhandle");
            mav.addObject("handle", hrp.getHandle());
        }

        return mav;
    }

    static class HandleRequestProcessor {
        private Context context;
        private HttpServletRequest request;
        private boolean pathParsed = false;

        private String handle;
        private String extraPathInfo;

        private DSpaceObject dspaceObject;

        HandleRequestProcessor(Context pContext, HttpServletRequest pRequest) {
            context = pContext;
            request = pRequest;
        }

        DSpaceObject getObject() throws SQLException {
            if (dspaceObject == null) {
                String handle = getHandle();
                dspaceObject = HandleManager.resolveToObject(context, handle);
            }

            return dspaceObject;
        }

        String getHandle() {
            if (!pathParsed) {
                parsePath();
            }

            return handle;
        }

        private void parsePath() {
            if (!pathParsed) {
                String path = request.getRequestURI();

                if (path != null) {
                    if (path.startsWith("/handle/")) {
                        path = path.substring(8);
                    } else if (path.contains("/handle/")) {
                        path = path.substring(path.indexOf("/handle/") + 8);
                    } else {
                        // substring(1) is to remove initial '/'
                        path = path.substring(1);
                    }

                    try {
                        // Extract the Handle
                        int firstSlash = path.indexOf('/');
                        int secondSlash = path.indexOf('/', firstSlash + 1);

                        if (secondSlash != -1) {
                            // We have extra path info
                            handle = path.substring(0, secondSlash);
                            extraPathInfo = path.substring(secondSlash);
                        }
                        else {
                            // The path is just the Handle
                            handle = path;
                            extraPathInfo = null;
                        }
                    }
                    catch (NumberFormatException nfe) {
                        // Leave handle as null
                    }
                }
                pathParsed = true;
            }
        }
    }
}
