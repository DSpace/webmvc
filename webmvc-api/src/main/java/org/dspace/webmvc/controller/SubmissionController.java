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
 * Controller for Submissions / My DSpace
 */
@Controller
public class SubmissionController {
    /**
     * Handles the model processing required for the submissions page.
     *
     * TODO: Should this be renamed to SubmissionPortal to not be confused with submit steps.
     * TODO: Need to perform auth check
     * Until then, return an empty ModelAndView to pass control over to the view
     */
    @RequestMapping("/submissions/**")
    protected String displaySubmissions() throws Exception {
        return "pages/submissions";
    }
}