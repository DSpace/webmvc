/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.dspace.core.*;
import org.dspace.authorize.AuthorizeManager;
import org.springframework.web.bind.annotation.RequestParam;
import org.dspace.authorize.AuthorizeException;
import java.sql.SQLException;
import org.dspace.app.util.Util;
import org.dspace.eperson.Group;
import org.dspace.eperson.EPerson;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author AdminNUS
 */
@Controller
public class GroupAdminController extends AdminController {

    /** Logger */
    private static Logger log = Logger.getLogger(GroupAdminController.class);
    private Group group = null;

    @RequestMapping(params = "submit_add")
    protected String submitAddGroup(@RequestAttribute Context context, ModelMap model, @RequestParam(value = "group_id", required = false, defaultValue = "-1") int groupID, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        group = Group.create(context);

        group.setName("new group" + group.getID());
        group.update();



        model.addAttribute("group", group);
        model.addAttribute("members", group.getMembers());
        model.addAttribute("membergroups", group.getMemberGroups());

        String utilsGrpName = Utils.addEntities(group.getName());
        model.addAttribute("utilsGrpName", utilsGrpName);

        context.complete();

        return "pages/admin/group-edit";

    }//end submitAddGroup

    @RequestMapping(params = "submit_edit")
    protected String submitEdit(@RequestAttribute Context context, ModelMap model, @RequestParam(value = "group_id", required = false, defaultValue = "-1") int groupID, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        group = initGroup(context, groupID);

        model.addAttribute("group", group);
        model.addAttribute("members", group.getMembers());
        model.addAttribute("membergroups", group.getMemberGroups());

        String utilsGrpName = Utils.addEntities(group.getName());
        model.addAttribute("utilsGrpName", utilsGrpName);

        return "pages/admin/group-edit";

    }//end submitEdit

    @RequestMapping(params = "submit_group_update")
    protected String submitGroupUpdate(@RequestAttribute Context context, ModelMap model, @RequestParam(value = "group_id", required = false, defaultValue = "-1") int groupID, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // first off, did we change the group name?
        String newName = request.getParameter("group_name");
        group = initGroup(context, groupID);

        if (!newName.equals(group.getName())) {
            group.setName(newName);
            group.update();
        }

        int[] eperson_ids = Util.getIntParameters(request, "eperson_id");
        int[] group_ids = Util.getIntParameters(request, "group_ids");
        // now get members, and add new ones and remove missing ones
        EPerson[] members = group.getMembers();
        Group[] membergroups = group.getMemberGroups();

        if (eperson_ids != null) {
            // some epeople were listed, now make group's epeople match
            // given epeople
            Set memberSet = new HashSet();
            Set epersonIDSet = new HashSet();

            // add all members to a set
            for (int x = 0; x < members.length; x++) {
                Integer epersonID = Integer.valueOf(members[x].getID());
                memberSet.add(epersonID);
            }

            // now all eperson_ids are put in a set
            for (int x = 0; x < eperson_ids.length; x++) {
                epersonIDSet.add(Integer.valueOf(eperson_ids[x]));
            }

            // process eperson_ids, adding those to group not already
            // members
            Iterator i = epersonIDSet.iterator();

            while (i.hasNext()) {
                Integer currentID = (Integer) i.next();

                if (!memberSet.contains(currentID)) {
                    group.addMember(EPerson.find(context, currentID.intValue()));
                }
            }

            // process members, removing any that aren't in eperson_ids
            for (int x = 0; x < members.length; x++) {
                EPerson e = members[x];

                if (!epersonIDSet.contains(Integer.valueOf(e.getID()))) {
                    group.removeMember(e);
                }
            }
        } else {
            // no members found (ids == null), remove them all!

            for (int y = 0; y < members.length; y++) {
                group.removeMember(members[y]);
            }
        }

        if (group_ids != null) {
            // some groups were listed, now make group's member groups
            // match given group IDs
            Set memberSet = new HashSet();
            Set groupIDSet = new HashSet();

            // add all members to a set
            for (int x = 0; x < membergroups.length; x++) {
                Integer myID = Integer.valueOf(membergroups[x].getID());
                memberSet.add(myID);
            }

            // now all eperson_ids are put in a set
            for (int x = 0; x < group_ids.length; x++) {
                groupIDSet.add(Integer.valueOf(group_ids[x]));
            }

            // process group_ids, adding those to group not already
            // members
            Iterator i = groupIDSet.iterator();

            while (i.hasNext()) {
                Integer currentID = (Integer) i.next();

                if (!memberSet.contains(currentID)) {
                    group.addMember(Group.find(context, currentID.intValue()));
                }
            }

            // process members, removing any that aren't in eperson_ids
            for (int x = 0; x < membergroups.length; x++) {
                Group g = membergroups[x];

                if (!groupIDSet.contains(Integer.valueOf(g.getID()))) {
                    group.removeMember(g);
                }
            }

        } else {
            // no members found (ids == null), remove them all!
            for (int y = 0; y < membergroups.length; y++) {
                group.removeMember(membergroups[y]);
            }
        }

        group.update();

        model.addAttribute("group", group);
        model.addAttribute("members", group.getMembers());
        model.addAttribute("membergroups", group.getMemberGroups());

        String utilsGrpName = Utils.addEntities(group.getName());
        model.addAttribute("utilsGrpName", utilsGrpName);
        context.complete();

        return "pages/admin/group-edit";

    }//end submitGroupUpdate

    @RequestMapping(params = "submit_group_delete")
    protected String submitGroupDelete(@RequestAttribute Context context, ModelMap model, @RequestParam(value = "group_id", required = false, defaultValue = "-1") int groupID, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        group = initGroup(context, groupID);
        model.addAttribute("group", group);

        return "pages/admin/group-confirm-delete";

    }//end submitGroupDelete

    @RequestMapping(params = "submit_confirm_delete")
    protected String submitConfirmGroupDelete(@RequestAttribute Context context, ModelMap model, @RequestParam(value = "group_id", required = false, defaultValue = "-1") int groupID, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        group = initGroup(context, groupID);
        // phony authorize, only admins can do this
        AuthorizeManager.authorizeAction(context, group, Constants.WRITE);
        // delete group, return to group-list.ftl
        group.delete();

        return showMainPage(context, model, request, response);

    }//end submitConfirmGroupDelete

    @RequestMapping(params = "submit_cancel_delete")
    protected String submitCancelDelete(@RequestAttribute Context context, ModelMap model, @RequestParam(value = "group_id", required = false, defaultValue = "-1") int groupID, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        return showMainPage(context, model, request, response);

    }//end submitCancelDelete
    
    @RequestMapping("/admin/group/group-select-list")
    protected String groupSelectList(@RequestAttribute Context context, ModelMap model, @RequestParam(value = "group_id", required = false, defaultValue = "-1") int groupID, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // Are we for selecting a single or multiple groups?
        boolean multiple = Util.getBoolParameter(request, "multiple");

        // What are we sorting by?  Name is default
        int sortBy = Group.NAME;

        String sbParam = request.getParameter("sortby");

        if (sbParam != null && sbParam.equals("id")) {
            sortBy = Group.ID;
        }

        // What's the index of the first group to show?  Default is 0
        int first = Util.getIntParameter(request, "first");
        if (first == -1) {
            first = 0;
        }

        // Retrieve the e-people in the specified order
        Group[] groups = Group.findAll(context, sortBy);

        // Set attributes for JSP        
        model.addAttribute("sortby", Integer.valueOf(sortBy));
        model.addAttribute("first", Integer.valueOf(first));
        model.addAttribute("groups", groups);
        
        if (multiple) {            
            model.addAttribute("multiple", Boolean.TRUE);
        }

        // Make sure we won't run over end of list
        int PAGESIZE = 50;
        int last = first + PAGESIZE;
        if (last >= groups.length) {
            last = groups.length - 1;
        }

        // Index of first group on last page
        int jumpEnd = ((groups.length - 1) / PAGESIZE) * PAGESIZE;

        // Now work out values for next/prev page buttons
        int jumpFiveBack = first - PAGESIZE * 5;
        if (jumpFiveBack < 0) {
            jumpFiveBack = 0;
        }

        int jumpOneBack = first - PAGESIZE;
        if (jumpOneBack < 0) {
            jumpOneBack = 0;
        }

        int jumpOneForward = first + PAGESIZE;
        if (jumpOneForward > groups.length) {
            jumpOneForward = first;
        }

        int jumpFiveForward = first + PAGESIZE * 5;
        if (jumpFiveForward > groups.length) {
            jumpFiveForward = jumpEnd;
        }

        // What's the link?
        String sortByParam = "name";
        if (sortBy == Group.ID) {
            sortByParam = "id";
        }

        String jumpLink = request.getContextPath() + "/admin/group/group-select-list?multiple=" + multiple + "&sortby=" + sortByParam + "&first=";
        String sortLink = request.getContextPath() + "/admin/group/group-select-list?multiple=" + multiple + "&first=" + first + "&sortby=";
        
        model.addAttribute("sortLink", sortLink);
        model.addAttribute("jumpLink", jumpLink);
        model.addAttribute("jumpOneForward", jumpOneForward);
        model.addAttribute("jumpFiveForward", jumpFiveForward);
        model.addAttribute("jumpOneBack", jumpOneBack);
        model.addAttribute("jumpFiveBack", jumpFiveBack);
        model.addAttribute("jumpEnd", jumpEnd);
        model.addAttribute("first", Integer.valueOf(first));
        model.addAttribute("last", last);
        model.addAttribute("sortby", Integer.valueOf(sortBy));
        
        Group g = Group.create(context);
        model.addAttribute("group", g);
                
        return "pages/admin/group-select-list";

    }//end groupSelectList

    protected Group initGroup(Context c, int groupID) {

        try {
            return Group.find(c, groupID);
        }//end try
        catch (SQLException sqlEx) {
            return null;
        }

    }//emd initGroup;

    @RequestMapping("/admin/group/main")
    protected String showMainPage(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        Group[] groups = Group.findAll(context, Group.NAME);
        model.addAttribute("groups", groups);
        context.complete();

        return "pages/admin/group-list";

    }//end showMainPage
}//end GroupAdminController
