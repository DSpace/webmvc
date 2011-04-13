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

package org.dspace.webmvc.controller.admin;

import org.apache.commons.lang.StringUtils;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Community;
import org.dspace.core.Context;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Administrative tasks that can be done to a collection.
 */
@Controller
public class CommunityController {

    //need to init this with comm
    @ModelAttribute("communityMetadataForm")
    public CommunityMetadataForm createForm(Context context, HttpServletRequest request, ModelMap model) throws SQLException {
        CommunityMetadataForm communityMetadataForm = new CommunityMetadataForm();
        int commID = sanitizeToInt(request.getParameter("communityID"));
        if(commID < 0) {
            return null;
        }
        Community community = Community.find(context, commID);
        communityMetadataForm.init(community);
        model.addAttribute("communityMetadataForm", communityMetadataForm);
        model.addAttribute("communityID", commID);

        return communityMetadataForm;
    }

    //@TODO service?

    @RequestMapping
    public String showMetadataForm(CommunityMetadataForm communityMetadataForm, ModelMap model) throws SQLException {
        if(communityMetadataForm != null) {
            return "pages/admin/community-edit";
        } else {
            model.addAttribute("errorMessage", "community metadata form was null");
            return "pages/error";
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processCommunityUpdate(@RequestAttribute Context context, CommunityMetadataForm communityMetadataForm, ModelMap model, HttpServletRequest request) throws SQLException, AuthorizeException, IOException {
        if(communityMetadataForm == null) {
            model.addAttribute("errorMessage", "communityMetadataForm was null");
            return "pages/error";
        }


        //if (!bindingResult.hasErrors()) {
        Integer id = communityMetadataForm.getCommunityID();
        if(null == id) {
            model.addAttribute("errorMessage", "communityID was null");
            return "pages/error";
        }

        Community currentCommunity = Community.find(context, id);
        currentCommunity.getName();

        communityMetadataForm.save(context);
        return "forward:/handle"+currentCommunity.getHandle();
        //}

        //return "pages/admin/community-edit";

        //if(request.getParameter("submit_save") != null)
        //Does this user have permission to edit this community?
    }

    //public String processCommunityCreate() {}
    //public String processCommunityDelete() {}

    private int sanitizeToInt(String param) {
        if(!StringUtils.isEmpty(param)) {
            param = param.trim();
            return Integer.parseInt(param);
        }

        return -1;
    }

    public static class CommunityMetadataForm {
        //@NotEmpty
        private String name;
        private String short_description;
        private String introductory_text;
        private String copyright_text;
        private String side_bar_text;
        // @TODO Logo

        private Integer communityID;

        public Integer getCommunityID() {
            return communityID;
        }

        public void setCommunityID(Integer communityID) {
            this.communityID = communityID;
        }

        public CommunityMetadataForm() {}

        public void init(Community community) {
            setCommunityID(community.getID());
            setName(community.getName());
            setShort_description(community.getMetadata("short_description"));
            setIntroductory_text(community.getMetadata("introductory_text"));
            setCopyright_text(community.getMetadata("copyright_text"));
            setSide_bar_text(community.getMetadata("side_bar_text"));
        }

        public void save(Context context) throws SQLException, AuthorizeException, IOException {
            context.turnOffAuthorisationSystem();

            String name = getName();

            Community community = Community.find(context, getCommunityID());

            community.getName();

            if(name != null) {
                community.setMetadata("name", name);
            }
            community.setMetadata("short_description", getShort_description());
            community.setMetadata("introductory_text", getIntroductory_text());
            community.setMetadata("copyright_text", getCopyright_text());
            community.setMetadata("side_bar_text", getSide_bar_text());

            community.update();
            context.complete();
            context.restoreAuthSystemState();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShort_description() {
            return short_description;
        }

        public void setShort_description(String short_description) {
            this.short_description = short_description;
        }

        public String getIntroductory_text() {
            return introductory_text;
        }

        public void setIntroductory_text(String introductory_text) {
            this.introductory_text = introductory_text;
        }

        public String getCopyright_text() {
            return copyright_text;
        }

        public void setCopyright_text(String copyright_text) {
            this.copyright_text = copyright_text;
        }

        public String getSide_bar_text() {
            return side_bar_text;
        }

        public void setSide_bar_text(String side_bar_text) {
            this.side_bar_text = side_bar_text;
        }
    }
}
