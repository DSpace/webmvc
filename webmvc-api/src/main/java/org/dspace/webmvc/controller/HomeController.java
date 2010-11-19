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

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for the DSpace homepage
 */
public class HomeController extends AbstractController {
    /**
     * Handles the model processing required for the home page.
     *
     * TODO: Figure out something useful for this to do.
     * Until then, return an empty ModelAndView to pass control over to the view
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Possible requirements:
        // - get top communtities       (Model should allow arbitrary commands to retrieve comm/col/items?)
        // - get news
        // - get sidebar
        // - latest submissions

        return new ModelAndView("home");
    }
}
