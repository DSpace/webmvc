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
    private static Community currentCommunity;

    //need to init this with comm
    //@ModelAttribute("communityMetadataForm")
    //public CommunityMetadataForm createForm() {
    //    return new CommunityMetadataForm();
    //}

    //@TODO service?

    @RequestMapping(method = RequestMethod.GET)
    public String showMetadataForm(@RequestAttribute Context context, ModelMap model, HttpServletRequest request) throws SQLException {
        int commID = sanitizeToInt(request.getParameter("communityID"));
        currentCommunity = Community.find(context, commID);
        model.addAttribute("currentCommunity", currentCommunity);
        CommunityMetadataForm communityMetadataForm = new CommunityMetadataForm(currentCommunity);
        model.addAttribute("communityMetadataForm", communityMetadataForm);

        return "pages/admin/community-edit";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processCommunityUpdate(@RequestAttribute Context context, CommunityMetadataForm communityMetadataForm, BindingResult bindingResult, HttpServletRequest request) throws SQLException, AuthorizeException, IOException {

        if (!bindingResult.hasErrors()) {
            //Community currentCommunity = communityMetadataForm.getCommunity();
            communityMetadataForm.save(context);
            return "forward:/handle"+currentCommunity.getHandle();
        }

        return "pages/admin/community-edit";

        //if(request.getParameter("submit_save") != null)
        //Does this user have permission to edit this community?
    }

    //public String processCommunityCreate() {}
    //public String processCommunityDelete() {}

    private int sanitizeToInt(String param) {
        if(param.length() > 0) {
            param = param.trim();
            return Integer.parseInt(param);
        }

        return -1;
    }

    public static class CommunityMetadataForm {
        private String name;

        private String short_description;

        private String introductory_text;
        private String copyright_text;
        private String side_bar_text;

        public CommunityMetadataForm() {

        }


        // @TODO Logo

        public CommunityMetadataForm(Community community) {
            setName(community.getName());
            setShort_description(community.getMetadata("short_description"));
            setIntroductory_text(community.getMetadata("introductory_text"));
            setCopyright_text(community.getMetadata("copyright_text"));
            setSide_bar_text(community.getMetadata("side_bar_text"));
        }

        public void save(Context context) throws SQLException, AuthorizeException, IOException {
            context.turnOffAuthorisationSystem();

            String name = getName();


            currentCommunity.getName();

            if(name != null) {
                currentCommunity.setMetadata("name", name);
            }
            currentCommunity.setMetadata("short_description", getShort_description());
            currentCommunity.setMetadata("introductory_text", getIntroductory_text());
            currentCommunity.setMetadata("copyright_text", getCopyright_text());
            currentCommunity.setMetadata("side_bar_text", getSide_bar_text());

            currentCommunity.update();
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
