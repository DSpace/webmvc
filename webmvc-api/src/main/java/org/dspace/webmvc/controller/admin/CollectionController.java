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
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.core.Context;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import javax.validation.Valid;

/**
 * Administrative tasks that can be done to a collection.
 */
@Controller
public class CollectionController {

    @RequestMapping(method = RequestMethod.GET)
    public String showMetadataForm(@RequestParam(value = "communityID", required = false) Integer communityID,
                                   @RequestParam(value = "collectionID", required = false) Integer collectionID,
                                   @RequestParam(value = "createNew", required = false) String createNew,
                                   Context context,
                                   ModelMap model
    ) throws SQLException {
        CollectionMetadataForm collectionMetadataForm = new CollectionMetadataForm();

        if(createNew != null) {
            assert communityID != null;
            Community parentCommunity = Community.find(context, communityID);
            collectionMetadataForm.setName("New Collection for " + parentCommunity.getName());
        } else {
            //Edit
            assert collectionID != null;
            collectionMetadataForm.init(context, collectionID);
        }
        model.addAttribute("collectionMetadataForm", collectionMetadataForm);
        return "pages/admin/collectionEdit";
    }

    @RequestMapping(params = "update", method = RequestMethod.POST)
    public String processCollectionUpdate(@RequestAttribute Context context,
                                         @RequestParam(value = "communityID", required = false) Integer communityID,
                                         @RequestParam(value = "collectionID", required = false) Integer collectionID,
                                         @RequestParam(value = "createNew", required = false) String createNew,
                                         @ModelAttribute("collectionMetadataForm") @Valid CollectionMetadataForm collectionMetadataForm,
                                         BindingResult bindingResult,
                                         SessionStatus status
    ) throws SQLException, AuthorizeException, IOException {
        if (bindingResult.hasErrors()) {
            return "pages/admin/collectionEdit";
        } else {
            Collection collection;
            if(createNew != null) {
                //Create
                assert communityID != null;
                Community community = Community.find(context, communityID);
                collection = community.createCollection();
            } else {
                //Edit
                assert collectionID != null;
                collection = Collection.find(context, collectionID);
            }

            collectionMetadataForm.setCollectionID(collection.getID());
            collectionMetadataForm.save(context);
            status.setComplete();

            return "redirect:/handle/"+collection.getHandle();
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "delete")
    public String processCollectionDelete(@RequestAttribute Context context, @RequestParam(value = "collectionID", required = false) Integer collectionID) throws SQLException, AuthorizeException, IOException {
        assert collectionID != null;

        Collection collection = Collection.find(context, collectionID);
        Community[] parents = collection.getCommunities();
        for(Community parent : parents)
        {
            parent.removeCollection(collection);
            parent.update();
        }

        context.commit();

        return "redirect:/handle/"+parents[0].getHandle();
    }

    public static class CollectionMetadataForm {
        @NotEmpty
        private String name;
        private String short_description;
        private String introductory_text;
        private String copyright_text;
        private String side_bar_text;
        private String license;
        private String provenance_description;
        private CommonsMultipartFile logo;

        private Integer collectionID;

        public Integer getCollectionID() {
            return collectionID;
        }

        public void setCollectionID(Integer collectionID) {
            this.collectionID = collectionID;
        }

        public CollectionMetadataForm() {}

        public void init(Context context, Integer collectionID) throws SQLException {
            init(Collection.find(context, collectionID));
        }

        public void init(Collection collection) {
            setCollectionID(collection.getID());
            setName(collection.getName());
            setShort_description(collection.getMetadata("short_description"));
            setIntroductory_text(collection.getMetadata("introductory_text"));
            setCopyright_text(collection.getMetadata("copyright_text"));
            setSide_bar_text(collection.getMetadata("side_bar_text"));
            setLicense(collection.getMetadata("license"));
            setProvenance_description(collection.getMetadata("provenance_description"));
            //setLogo(collection.getLogo().retrieve());
        }

        public void save(Context context) throws SQLException, AuthorizeException, IOException {
            context.turnOffAuthorisationSystem();

            Collection collection = Collection.find(context, getCollectionID());
            collection.setMetadata("name", getName());
            collection.setMetadata("short_description", getShort_description());
            collection.setMetadata("introductory_text", getIntroductory_text());
            collection.setMetadata("copyright_text", getCopyright_text());
            collection.setMetadata("side_bar_text", getSide_bar_text());
            collection.setMetadata("license", getLicense());
            collection.setMetadata("provenance_description", getProvenance_description());
            collection.setLogo(getLogo().getInputStream());

            collection.update();
            context.commit();
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

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getProvenance_description() {
            return provenance_description;
        }

        public void setProvenance_description(String provenance_description) {
            this.provenance_description = provenance_description;
        }

        public CommonsMultipartFile getLogo() {
            return logo;
        }

        public void setLogo(CommonsMultipartFile logo) {
            this.logo = logo;
        }
    }
}
