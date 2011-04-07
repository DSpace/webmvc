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

import org.dspace.core.Context;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.dspace.workflow.WorkflowItem;
import org.dspace.workflow.WorkflowManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller for Submissions / My DSpace
 */
@Controller
public class SubmissionController {
    /**
     * Handles the model processing required for the submissions page.
     *  - Workflow tasks owned by user
     *  - Workflow tasks available in pool
     *  - Unfinished submissions
     *  - Archived submissions
     *
     * TODO: Need to perform auth check
     */
    @RequestMapping("/submissions/**")
    protected String displaySubmissions(@RequestAttribute Context context, ModelMap model, HttpServletRequest request) throws Exception {
        List<WorkflowItem> ownedItems = WorkflowManager.getOwnedTasks(context, context.getCurrentUser());
        model.addAttribute("ownedItems", ownedItems);

        List<WorkflowItem> pooledItems = WorkflowManager.getPooledTasks(context, context.getCurrentUser());
        model.addAttribute("pooledItems", pooledItems);

        return "pages/submissions";
    }
}