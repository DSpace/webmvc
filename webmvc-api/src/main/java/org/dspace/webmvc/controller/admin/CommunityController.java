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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Administrative tasks that can be done to a collection.
 */
@Controller
public class CommunityController {

    //@TODO service?

    @RequestMapping(method = RequestMethod.GET)
    public String showMetadataForm(@RequestParam(value = "communityID", required = false) Integer communityID, Context context, ModelMap model) throws SQLException {
        CommunityMetadataForm communityMetadataForm = new CommunityMetadataForm();
        if(communityID != null) {
            communityMetadataForm.init(context, communityID);
        }
        model.addAttribute("communityMetadataForm", communityMetadataForm);
        return "pages/admin/communityEdit";
    }

    //params = "update" | create | delete
    @RequestMapping(params = "update", method = RequestMethod.POST)
    public String processCommunityUpdate(@RequestAttribute Context context, @ModelAttribute("communityMetadataForm") CommunityMetadataForm communityMetadataForm, BindingResult bindingResult, SessionStatus status) throws SQLException, AuthorizeException, IOException {
        if (bindingResult.hasErrors()) {
            return "pages/admin/communityEdit";
        } else {
            int id = communityMetadataForm.getCommunityID();
            Community community = Community.find(context, id);

            communityMetadataForm.save(context);
            status.setComplete();

            return "forward:/handle/"+community.getHandle();
        }
    }

    //public String processCommunityCreate() {}
    //public String processCommunityDelete() {}

    public static class CommunityMetadataForm {
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

        public void init(Context context, Integer communityID) throws SQLException {
            init(Community.find(context, communityID));
        }

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

            Community community = Community.find(context, getCommunityID());
            community.setMetadata("name", getName());
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

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setRequiredFields(new String[] {"name"});
    }
}
