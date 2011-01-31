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
import org.dspace.webmvc.processor.HandleRequestProcessor;
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

}
