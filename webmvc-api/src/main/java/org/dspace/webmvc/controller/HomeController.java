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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for the DSpace homepage
 */
@Controller
public class HomeController {
    /**
     * Handles the model processing required for the home page.
     *
     * TODO: Figure out something useful for this to do.
     * Until then, return an empty ModelAndView to pass control over to the view
     */
    @RequestMapping
    protected String displayHome() throws Exception {
        return "pages/home";
    }
}
