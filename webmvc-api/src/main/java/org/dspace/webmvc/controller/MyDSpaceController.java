/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.dspace.app.itemexport.ItemExport;
import org.dspace.app.itemexport.ItemExportException;
import org.dspace.app.util.SubmissionConfigReader;
import org.dspace.app.util.SubmissionConfig;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
import org.dspace.content.SupervisedItem;
import org.dspace.content.WorkspaceItem;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;
import org.dspace.handle.HandleManager;
import org.dspace.submit.AbstractProcessingStep;
import org.dspace.workflow.WorkflowItem;
import org.dspace.workflow.WorkflowManager;
import org.dspace.app.util.Util;
import org.dspace.webmvc.utils.WebMVCManager;
import org.dspace.webmvc.utils.UIUtil;

/**
 *
 * @author AdminNUS
 */
@Controller
public class MyDSpaceController {

    /** Logger */
    private static Logger log = Logger.getLogger(MyDSpaceController.class);
    /** The main screen */
    public static final int MAIN_PAGE = 0;
    /** The remove item page */
    public static final int REMOVE_ITEM_PAGE = 1;
    /** The preview task page */
    public static final int PREVIEW_TASK_PAGE = 2;
    /** The perform task page */
    public static final int PERFORM_TASK_PAGE = 3;
    /** The "reason for rejection" page */
    public static final int REJECT_REASON_PAGE = 4;
    /** The "request export archive for download" page */
    public static final int REQUEST_EXPORT_ARCHIVE = 5;
    /** The "request export migrate archive for download" page */
    public static final int REQUEST_MIGRATE_ARCHIVE = 6;

    @RequestMapping(method = RequestMethod.GET)
    protected String processGet(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws java.sql.SQLException,
            javax.servlet.ServletException, java.io.IOException,
            AuthorizeException {

        return showMainPage(context, model, request, response);

    }//end processGet

    @RequestMapping(method = RequestMethod.POST)
    protected String processPost(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {
        // First get the step
        int step = Util.getIntParameter(request, "step");

        switch (step) {
            case MAIN_PAGE:
                return processMainPage(context, model, request, response);



            case REMOVE_ITEM_PAGE:
                return processRemoveItem(context, model, request, response);



            case PREVIEW_TASK_PAGE:
                return processPreviewTask(context, model, request, response);



            case PERFORM_TASK_PAGE:
                return processPerformTask(context, model, request, response);



            case REJECT_REASON_PAGE:
                return processRejectReason(context, model, request, response);


            case REQUEST_EXPORT_ARCHIVE:
                return processExportArchive(context, model, request, response, false);



            case REQUEST_MIGRATE_ARCHIVE:
                return processExportArchive(context, model, request, response, true);



            default:
                log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);
        }
    }

    protected String processMainPage(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {
        String buttonPressed = Util.getSubmitButton(request, "submit_own");
        String page = "";

        // Get workspace item, if any
        WorkspaceItem workspaceItem;

        try {
            int wsID = Integer.parseInt(request.getParameter("workspace_id"));
            workspaceItem = WorkspaceItem.find(context, wsID);
        } catch (NumberFormatException nfe) {
            workspaceItem = null;
        }

        // Get workflow item specified, if any
        WorkflowItem workflowItem;

        try {
            int wfID = Integer.parseInt(request.getParameter("workflow_id"));
            workflowItem = WorkflowItem.find(context, wfID);
        } catch (NumberFormatException nfe) {
            workflowItem = null;
        }

        // Respond to button press
        boolean ok = false;

        if (buttonPressed.equals("submit_new")) {
            // New submission: Redirect to submit           
            ok = true;
            page = "redirect:/submit/";
        } else if (buttonPressed.equals("submit_own")) {
            // Review own submissions            
            ok = true;
            return showPreviousSubmissions(context, model, request, response);
        } else if (buttonPressed.equals("submit_resume")) {
            // User clicked on a "Resume" button for a workspace item.
            String wsID = request.getParameter("workspace_id");
            ok = true;
            page = "redirect:/submit?resume=" + wsID;
        } else if (buttonPressed.equals("submit_delete")) {
            // User clicked on a "Remove" button for a workspace item
            if (workspaceItem != null) {
                log.info(LogManager.getHeader(context, "confirm_removal",
                        "workspace_item_id=" + workspaceItem.getID()));

                model.addAttribute("workspaceitem", workspaceItem);
                page = "pages/mydspace/remove-item";

            } else {
                log.warn(LogManager.getHeader(context, "integrity_error",
                        UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);
            }

            ok = true;
        } else if (buttonPressed.equals("submit_claim")) {
            // User clicked "take task" button on workflow task
            if (workflowItem != null) {
                log.info(LogManager.getHeader(context, "view_workflow",
                        "workflow_id=" + workflowItem.getID()));

                model.addAttribute("workflowitem", workflowItem);
                ok = true;
                page = "pages/mydspace/preview-task";
            }
        } else if (buttonPressed.equals("submit_perform")) {
            // User clicked "Do This Task" button on workflow task
            if (workflowItem != null) {
                log.info(LogManager.getHeader(context, "view_workflow",
                        "workflow_id=" + workflowItem.getID()));

                model.addAttribute("workflowitem", workflowItem);
                ok = true;
                page = "pages/mydspace/perform-task";
            }
        } else if (buttonPressed.equals("submit_return")) {
            // User clicked "Return to pool" button on workflow task
            if (workflowItem != null) {
                log.info(LogManager.getHeader(context, "unclaim_workflow",
                        "workflow_id=" + workflowItem.getID()));

                WorkflowManager.unclaim(context, workflowItem, context.getCurrentUser());
                ok = true;
                context.commit();
                return showMainPage(context, model, request, response);

            }
        }

        if (!ok) {
            log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
            return WebMVCManager.showIntegrityError(request, response);
        } else {
            return page;
        }
    }

    private String processRemoveItem(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {
        String buttonPressed = UIUtil.getSubmitButton(request, "submit_cancel");

        // Get workspace item
        WorkspaceItem workspaceItem;

        try {
            int wsID = Integer.parseInt(request.getParameter("workspace_id"));
            workspaceItem = WorkspaceItem.find(context, wsID);
        } catch (NumberFormatException nfe) {
            workspaceItem = null;
        }

        if (workspaceItem == null) {
            log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
            return WebMVCManager.showIntegrityError(request, response);

        }

        // We have a workspace item
        if (buttonPressed.equals("submit_delete")) {
            // User has clicked on "delete"
            log.info(LogManager.getHeader(context, "remove_submission",
                    "workspace_item_id=" + workspaceItem.getID() + ",item_id="
                    + workspaceItem.getItem().getID()));
            workspaceItem.deleteAll();
            context.commit();
            return showMainPage(context, model, request, response);

        } else {
            // User has cancelled. Back to main page.
            return showMainPage(context, model, request, response);
        }

    }

    private String processPreviewTask(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException {
        String buttonPressed = Util.getSubmitButton(request, "submit_cancel");

        // Get workflow item
        WorkflowItem workflowItem;

        try {
            int wfID = Integer.parseInt(request.getParameter("workflow_id"));
            workflowItem = WorkflowItem.find(context, wfID);
        } catch (NumberFormatException nfe) {
            workflowItem = null;
        }

        if (workflowItem == null) {
            log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
            return WebMVCManager.showIntegrityError(request, response);

        }

        if (buttonPressed.equals("submit_start")) {
            // User clicked "start" button to claim the task
            WorkflowManager.claim(context, workflowItem, context.getCurrentUser());

            // Display "perform task" page
            model.addAttribute("workflowitem", workflowItem);
            context.commit();
            return "pages/mydspace/perform-task";
        } else {
            // Return them to main page
            return showMainPage(context, model, request, response);
        }
    }

    private String processPerformTask(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException {
        String buttonPressed = Util.getSubmitButton(request, "submit_cancel");

        // Get workflow item
        WorkflowItem workflowItem;

        try {
            int wfID = Integer.parseInt(request.getParameter("workflow_id"));
            workflowItem = WorkflowItem.find(context, wfID);
        } catch (NumberFormatException nfe) {
            workflowItem = null;
        }

        if (workflowItem == null) {
            log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
            return WebMVCManager.showIntegrityError(request, response);

        }

        if (buttonPressed.equals("submit_approve")) {
            Item item = workflowItem.getItem();

            // Advance the item along the workflow
            WorkflowManager.advance(context, workflowItem, context.getCurrentUser());

            // FIXME: This should be a return value from advance()
            // See if that gave the item a Handle. If it did,
            // the item made it into the archive, so we
            // should display a suitable page.
            String handle = HandleManager.findHandle(context, item);
            context.commit();

            if (handle != null) {
                String displayHandle = HandleManager.getCanonicalForm(handle);

                model.addAttribute("handle", displayHandle);
                return "pages/mydspace/in-archive";
            } else {

                return "pages/mydspace/task-complete";
            }

        } else if (buttonPressed.equals("submit_reject")) {
            // Submission rejected. Ask the user for a reason
            log.info(LogManager.getHeader(context, "get_reject_reason",
                    "workflow_id=" + workflowItem.getID() + ",item_id="
                    + workflowItem.getItem().getID()));

            model.addAttribute("workflowitem", workflowItem);
            return "pages/mydspace/reject-reason";
        } else if (buttonPressed.equals("submit_edit")) {
            // FIXME: Check auth
            log.info(LogManager.getHeader(context, "edit_workflow_item",
                    "workflow_id=" + workflowItem.getID() + ",item_id="
                    + workflowItem.getItem().getID()));

            // Forward control to the submission interface
            // with the relevant item

            return "redirect:/submit?workflow=" + workflowItem.getID();
        } else if (buttonPressed.equals("submit_pool")) {
            // Return task to pool
            WorkflowManager.unclaim(context, workflowItem, context.getCurrentUser());
            context.commit();
            return showMainPage(context, model, request, response);

        } else {
            // Cancelled. The user hasn't taken the task.
            // Just return to the main My DSpace page.
            return showMainPage(context, model, request, response);
        }
    }

    private String processRejectReason(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException {
        String buttonPressed = Util.getSubmitButton(request, "submit_cancel");

        // Get workflow item
        WorkflowItem workflowItem;

        try {
            int wfID = Integer.parseInt(request.getParameter("workflow_id"));
            workflowItem = WorkflowItem.find(context, wfID);
        } catch (NumberFormatException nfe) {
            workflowItem = null;
        }

        if (workflowItem == null) {
            log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
            return WebMVCManager.showIntegrityError(request, response);

        }

        if (buttonPressed.equals("submit_send")) {
            String reason = request.getParameter("reason");

            WorkspaceItem wsi = WorkflowManager.reject(context, workflowItem,
                    context.getCurrentUser(), reason);

            // Load the Submission Process for the collection this WSI is
            // associated with
            Collection c = wsi.getCollection();
            SubmissionConfigReader subConfigReader = new SubmissionConfigReader();
            SubmissionConfig subConfig = subConfigReader.getSubmissionConfig(c.getHandle(), false);

            // Set the "stage_reached" column on the workspace item
            // to the LAST page of the LAST step in the submission process
            // (i.e. the page just before "Complete")
            int lastStep = subConfig.getNumberOfSteps() - 2;
            wsi.setStageReached(lastStep);
            wsi.setPageReached(AbstractProcessingStep.LAST_PAGE_REACHED);
            wsi.update();
            context.commit();
            return "pages/mydspace/task-complete";
        } else {
            model.addAttribute("workflowitem", workflowItem);
            return "pages/mydspace/perform-task";
        }
    }

    private String processExportArchive(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response, boolean migrate) throws ServletException, IOException {

        if (request.getParameter("item_id") != null) {
            Item item = null;
            try {
                item = Item.find(context, Integer.parseInt(request.getParameter("item_id")));
            } catch (Exception e) {
                log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);
            }

            if (item == null) {
                log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);
            } else {
                try {
                    ItemExport.createDownloadableExport(item, context, migrate);
                } catch (ItemExportException iee) {
                    log.warn(LogManager.getHeader(context, "export_too_large_error", UIUtil.getRequestLogInfo(request)));                    
                    return "pages/mydspace/export-error";
                    
                } catch (Exception e) {
                    log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                    return WebMVCManager.showIntegrityError(request, response);
                    
                }
            }

            // success            
            return "pages/mydspace/task-complete";
        } else if (request.getParameter("collection_id") != null) {
            Collection col = null;
            try {
                col = Collection.find(context, Integer.parseInt(request.getParameter("collection_id")));
            } catch (Exception e) {
                log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);
            }

            if (col == null) {
                log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);
            } else {
                try {
                    ItemExport.createDownloadableExport(col, context, migrate);
                } catch (ItemExportException iee) {
                    log.warn(LogManager.getHeader(context, "export_too_large_error", UIUtil.getRequestLogInfo(request)));
                    return "pages/mydspace/export-error";
                } catch (Exception e) {
                    log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                    return WebMVCManager.showIntegrityError(request, response);
                }
            }
            return "pages/mydspace/task-complete";
        } else if (request.getParameter("community_id") != null) {
            Community com = null;
            try {
                com = Community.find(context, Integer.parseInt(request.getParameter("community_id")));
            } catch (Exception e) {
                log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);
            }

            if (com == null) {
                log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);
            } else {
                try {
                    org.dspace.app.itemexport.ItemExport.createDownloadableExport(com, context, migrate);
                } catch (ItemExportException iee) {
                    log.warn(LogManager.getHeader(context, "export_too_large_error", UIUtil.getRequestLogInfo(request)));
                    return "pages/mydspace/export-error";
                } catch (Exception e) {
                    log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                    return WebMVCManager.showIntegrityError(request, response);
                }
            }
            return "pages/mydspace/task-complete";
        }
        
        return "";


    }//

    private String showMainPage(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {
        log.info(LogManager.getHeader(context, "view_mydspace", ""));
        EPerson currentUser = context.getCurrentUser();

        // FIXME: WorkflowManager should return arrays
        List<WorkflowItem> ownedList = WorkflowManager.getOwnedTasks(context, currentUser);
        WorkflowItem[] owned = ownedList.toArray(new WorkflowItem[ownedList.size()]);

        // Pooled workflow items
        List<WorkflowItem> pooledList = WorkflowManager.getPooledTasks(context, currentUser);
        WorkflowItem[] pooled = pooledList.toArray(new WorkflowItem[pooledList.size()]);

        // User's WorkflowItems
        WorkflowItem[] workflowItems = WorkflowItem.findByEPerson(context, currentUser);

        // User's PersonalWorkspace
        WorkspaceItem[] workspaceItems = WorkspaceItem.findByEPerson(context, currentUser);

        // User's authorization groups
        Group[] memberships = Group.allMemberGroups(context, currentUser);

        // Should the group memberships be displayed
        boolean displayMemberships = ConfigurationManager.getBooleanProperty("webui.mydspace.showgroupmemberships", false);

        SupervisedItem[] supervisedItems = SupervisedItem.findbyEPerson(
                context, currentUser);
        // export archives available for download
        List<String> exportArchives = null;
        try {
            exportArchives = ItemExport.getExportsAvailable(currentUser);
        } catch (Exception e) {
            // nothing to do they just have no export archives available for download
        }


        // Set attributes
        model.addAttribute("mydspaceuser", currentUser);
        model.addAttribute("workspaceitems", workspaceItems);
        model.addAttribute("workflowitems", workflowItems);
        model.addAttribute("workflowowned", owned);
        model.addAttribute("workflowpooled", pooled);
        model.addAttribute("groupmemberships", memberships);
        model.addAttribute("displaygroupmemberships", Boolean.valueOf(displayMemberships));
        model.addAttribute("superviseditems", supervisedItems);
        model.addAttribute("exportarchives", exportArchives);

        // Forward to main mydspace page
        // JSPManager.showJSP(request, response, "/mydspace/main.jsp");
        model.addAttribute("WFSTATE_STEP1", WorkflowManager.WFSTATE_STEP1);
        model.addAttribute("WFSTATE_STEP2", WorkflowManager.WFSTATE_STEP2);
        model.addAttribute("WFSTATE_STEP3", WorkflowManager.WFSTATE_STEP3);
        
        model.addAttribute("WFSTATE_STEP1POOL", WorkflowManager.WFSTATE_STEP1POOL);
        model.addAttribute("WFSTATE_STEP2POOL", WorkflowManager.WFSTATE_STEP2POOL);
        model.addAttribute("WFSTATE_STEP3POOL", WorkflowManager.WFSTATE_STEP3POOL);

        return "pages/mydspace/main";
    }
    
    private String showPreviousSubmissions(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException,
            AuthorizeException
    {
        // Turn the iterator into a list
        List<Item> subList = new LinkedList<Item>();
        ItemIterator subs = Item.findBySubmitter(context, context.getCurrentUser());

        try
        {
            while (subs.hasNext())
            {
                subList.add(subs.next());
            }
        }
        finally
        {
            if (subs != null)
            {
                subs.close();
            }
        }

        Item[] items = new Item[subList.size()];

        for (int i = 0; i < subList.size(); i++)
        {
            items[i] = subList.get(i);
        }

        log.info(LogManager.getHeader(context, "view_own_submissions", "count="
                + items.length));

        model.addAttribute("user", context.getCurrentUser());
        model.addAttribute("items", items);

        return "pages/mydspace/own-submissions";
    }
}
