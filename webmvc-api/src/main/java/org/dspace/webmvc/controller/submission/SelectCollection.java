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

package org.dspace.webmvc.controller.submission;

import org.dspace.app.util.SubmissionInfo;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.DSpaceObject;
import org.dspace.content.InProgressSubmission;
import org.dspace.content.WorkspaceItem;
import org.dspace.core.Constants;
import org.dspace.content.Collection;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.webmvc.controller.HandleController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Submission Step: Select the collection (that the user is authorized to submit to) that the item will be submitted to.
 */
@Controller
public class SelectCollection {

    @RequestMapping(method = RequestMethod.GET, value = "/submit/")
    public String showSelectCollection(Context context, Model model) throws SQLException {
        //Assuming that all coming from submissions->new submission.
        //TODO allow user to start from a community or collection to give either restriction on submitable collections, or this step is skipped.
        //What is difference between org.dspace.constants.Constants and org.dspace.core.Constants ?
        Collection[] collections = Collection.findAuthorized(context, null, Constants.ADD);
        model.addAttribute("collections", collections);

        return "pages/submission/select-collection";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/submit/")
    public String processSelectCollection(@RequestParam(value = "collectionHandle", required = true) String collectionHandle, Context context, Model model) throws SQLException, AuthorizeException, IOException {
        //TODO Validate and respond with error if no collection handle submitted.
        //TODO Check that user is authorized
        DSpaceObject dso = HandleManager.resolveToObject(context, collectionHandle);
        if(dso.getType() == Constants.COLLECTION) {
            Collection collection = (Collection) dso;
            WorkspaceItem workspaceItem = WorkspaceItem.create(context, collection, true);
            model.addAttribute("workspaceitem", workspaceItem);
            return "pages/submission/workspaceitem";
        } else {
            return "error";
        }
    }

}
