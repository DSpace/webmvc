/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller.admin;

import org.springframework.stereotype.Controller;
import org.dspace.content.Item;
import org.springframework.web.bind.annotation.RequestMapping;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.springframework.ui.ModelMap;
import org.dspace.app.util.Util;
import org.apache.log4j.Logger;
import org.dspace.core.*;
import org.dspace.authorize.AuthorizeException;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dspace.eperson.Group;
import org.dspace.eperson.Supervisor;
import org.dspace.content.SupervisedItem;
import org.dspace.content.WorkspaceItem;


/**
 *
 * @author Robert Qin
 */
@Controller
public class SuperviseController extends AdminController {

    /** Logger */
    private static Logger log = Logger.getLogger(SuperviseController.class);

    @RequestMapping(params = "submit_add")
    protected String showLinkPage(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // get all the groups
        Group[] groups = Group.findAll(context, 1);

        // get all the workspace items
        WorkspaceItem[] wsItems = WorkspaceItem.findAll(context);
                
        // set the attributes for the JSP
        model.addAttribute("groups", groups);
        model.addAttribute("wsItems", wsItems);
        model.addAttribute("policynone", Supervisor.POLICY_NONE);
        model.addAttribute("policyeditor", Supervisor.POLICY_EDITOR);
        model.addAttribute("policyobserver", Supervisor.POLICY_OBSERVER);
        model.addAttribute("itemany", Item.ANY); 

        //JSPManager.showJSP(request, response, "/dspace-admin/supervise-link.jsp");
        return "pages/admin/supervise-link";

    }//end showLinkPage

    @RequestMapping(params = "submit_view")
    protected String showListPage(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // get all the supervised items
        SupervisedItem[] si = SupervisedItem.getAll(context);

        // set the attributes for the JSP       
        model.addAttribute("supervised", si);
        model.addAttribute("itemany", Item.ANY);

        return "pages/admin/supervise-list";

    }//end showListPage

    @RequestMapping("/admin/supervise/main")
    protected String showMainPage() {

        return "pages/admin/supervise-main";

    }//end showMainPage
    
    @RequestMapping(params = "submit_base")
    protected String submitBase() {

        return showMainPage();

    }//end showMainPage

    @RequestMapping(params = "submit_link")
    protected String submitLink(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // do form validation before anything else
        if (validateAddForm(context, request, response)) {
            addSupervisionOrder(context, request, response);
            return showMainPage();
        } else {
            return "pages/admin/supervise-duplicate";
        }
    }//end submitLink

    @RequestMapping(params = "submit_remove")
    protected String showConfirmRemovePage(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // get the values from the request
        int wsItemID = Util.getIntParameter(request, "siID");
        int groupID = Util.getIntParameter(request, "gID");

        // get the workspace item and the group from the request values
        WorkspaceItem wsItem = WorkspaceItem.find(context, wsItemID);
        Group group = Group.find(context, groupID);

        // set the attributes for the JSP        
        model.addAttribute("wsItem", wsItem);
        model.addAttribute("group", group);
        model.addAttribute("itemany", Item.ANY);

        return "pages/admin/supervise-confirm-remove";
    }//end showConfirmRemovePage

    @RequestMapping(params = "submit_doremove")
    protected String submitDoRemove(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        removeSupervisionOrder(context, request, response);

        return showMainPage();
    }//end submitDoRemove

    @RequestMapping(params = "submit_clean")
    protected String submitClean(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        cleanSupervisorDatabase(context, request, response);

        return showMainPage();
    }//end submitClean

    protected void addSupervisionOrder(@RequestAttribute Context context, HttpServletRequest request, HttpServletResponse response)
            throws SQLException, AuthorizeException, ServletException, IOException {

        // get the values from the request
        int groupID = Util.getIntParameter(request, "TargetGroup");
        int wsItemID = Util.getIntParameter(request, "TargetWSItem");
        int policyType = Util.getIntParameter(request, "PolicyType");

        Supervisor.add(context, groupID, wsItemID, policyType);

        log.info(LogManager.getHeader(context,
                "Supervision Order Set",
                "workspace_item_id=" + wsItemID + ",eperson_group_id=" + groupID));

        context.complete();
    }//end addSupervisionOrder

    protected void removeSupervisionOrder(Context context, HttpServletRequest request, HttpServletResponse response)
            throws SQLException, AuthorizeException, ServletException, IOException {

        // get the values from the request
        int wsItemID = Util.getIntParameter(request, "siID");
        int groupID = Util.getIntParameter(request, "gID");

        Supervisor.remove(context, wsItemID, groupID);

        log.info(LogManager.getHeader(context,
                "Supervision Order Removed",
                "workspace_item_id=" + wsItemID + ",eperson_group_id=" + groupID));

        context.complete();
    }

    private boolean validateAddForm(@RequestAttribute Context context,
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, AuthorizeException {
        int groupID = Util.getIntParameter(request, "TargetGroup");
        int wsItemID = Util.getIntParameter(request, "TargetWSItem");

        boolean invalid = Supervisor.isOrder(context, wsItemID, groupID);

        if (invalid) {
            return false;
        } else {
            return true;
        }
    }//end validateAddForm

    private void cleanSupervisorDatabase(Context context, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, AuthorizeException {
        // ditch any supervision orders that are no longer relevant
        Supervisor.removeRedundant(context);

        context.complete();
    }
}//end SuperviseController
