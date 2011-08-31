/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller.admin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;
import org.dspace.webmvc.utils.UIUtil;
import org.dspace.app.util.Util;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.dspace.core.Utils;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Robert Qin
 */
@Controller
public class GroupEditController {

    /** Logger */
    private static Logger log = Logger.getLogger(GroupEditController.class);

    @RequestMapping(method = RequestMethod.GET)
    protected String processGet(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Group group = null;
        group = checkGroup(context, request);

        if (group != null) {

            // unknown action, show edit page
            model.addAttribute("group", group);
            model.addAttribute("members", group.getMembers());
            model.addAttribute("membergroups", group.getMemberGroups());

            String utilsGrpName = Utils.addEntities(group.getName());
            model.addAttribute("utilsGrpName", utilsGrpName);
            return "pages/admin/group-edit";

        } else {

            return showMainPage(context, model, request, response);
        }

    }//end processGet

    @RequestMapping(method = RequestMethod.POST)
    protected String processPost(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Group group = null;
        group = checkGroup(context, request);

        if (group != null) {

            // is this user authorized to edit this group?
            AuthorizeManager.authorizeAction(context, group, Constants.ADD);

            boolean submit_edit = (request.getParameter("submit_edit") != null);
            boolean submit_group_update = (request.getParameter("submit_group_update") != null);
            boolean submit_group_delete = (request.getParameter("submit_group_delete") != null);
            boolean submit_confirm_delete = (request.getParameter("submit_confirm_delete") != null);
            boolean submit_cancel_delete = (request.getParameter("submit_cancel_delete") != null);


            // just chosen a group to edit - get group and pass it to
            // group-edit.jsp
            if (submit_edit && !submit_group_update && !submit_group_delete) {
                model.addAttribute("group", group);
                model.addAttribute("members", group.getMembers());
                model.addAttribute("membergroups", group.getMemberGroups());
                String utilsGrpName = Utils.addEntities(group.getName());
                model.addAttribute("utilsGrpName", utilsGrpName);

                return "pages/admin/group-edit";
            } // update the members of the group
            else if (submit_group_update) {
                // first off, did we change the group name?
                String newName = request.getParameter("group_name");

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

                context.commit();
                return "pages/admin/group-edit";
            } else if (submit_group_delete) {
                // direct to a confirmation step
                model.addAttribute("group", group);
                return "pages/admin/group-confirm-delete";
            } else if (submit_confirm_delete) {
                // phony authorize, only admins can do this
                AuthorizeManager.authorizeAction(context, group, Constants.WRITE);

                // delete group, return to group-list.jsp
                group.delete();

                return showMainPage(context, model, request, response);
            } else if (submit_cancel_delete) {
                // show group list
                return showMainPage(context, model, request, response);
            } else {
                // unknown action, show edit page
                model.addAttribute("group", group);
                model.addAttribute("members", group.getMembers());
                model.addAttribute("membergroups", group.getMemberGroups());
                String utilsGrpName = Utils.addEntities(group.getName());
                model.addAttribute("utilsGrpName", utilsGrpName);

                return "pages/admin/group-edit";
            }
        } else {

            // want to add a group - create a blank one, and pass to
            // group_edit.jsp
            String button = UIUtil.getSubmitButton(request, "submit");

            if (button.equals("submit_add")) {
                group = Group.create(context);

                group.setName("new group" + group.getID());
                group.update();

                model.addAttribute("group", group);
                model.addAttribute("members", group.getMembers());
                model.addAttribute("membergroups", group.getMemberGroups());
                String utilsGrpName = Utils.addEntities(group.getName());
                model.addAttribute("utilsGrpName", utilsGrpName);

                context.commit();
                return "pages/admin/group-edit";

            } else {
                // show the main page (select groups)
                return showMainPage(context, model, request, response);
            }

        }//end

    }//end processGet

    private String showMainPage(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {
        Group[] groups = Group.findAll(context, Group.NAME);

        // if( groups == null ) { System.out.println("groups are null"); }
        // else System.out.println("# of groups: " + groups.length);
        model.addAttribute("groups", groups);
        context.commit();
        return "pages/admin/group-list";
    }

    protected Group checkGroup(Context context, HttpServletRequest request) throws ServletException, IOException, SQLException, AuthorizeException {

        // Find out if there's a group parameter
        int groupID = Util.getIntParameter(request, "group_id");
        Group group = null;

        if (groupID >= 0) {
            group = Group.find(context, groupID);
        }

        return group;
    }//end checkGroup
}//end GroupEditController
