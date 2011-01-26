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

import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BitstreamController extends AbstractController {
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BitstreamRequestProcessor hrp = new BitstreamRequestProcessor((Context)request.getAttribute("context"), request);
        return null;
    }

    static class BitstreamRequestProcessor {
        private Context context;
        private HttpServletRequest request;
        private boolean pathParsed = false;

        private String handle;
        private String extraPathInfo;

        private DSpaceObject dspaceObject;

        BitstreamRequestProcessor(Context pContext, HttpServletRequest pRequest) {
            context = pContext;
            request = pRequest;
        }
    }
}
