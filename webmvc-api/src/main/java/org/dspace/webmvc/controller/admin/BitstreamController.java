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
import org.dspace.content.*;
import org.dspace.core.Context;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Administrative tasks that can be done to a bitstream.
 */
@Controller
public class BitstreamController {
    //@TODO Check user is authorized, and that bitstream exists.

    @RequestMapping(method = RequestMethod.GET, value = "/admin/item/{itemID}/bitstream/new")
    public String showBitstreamCreate(Model model, @PathVariable(value="itemID") Integer itemID, Context context) {
        BitstreamForm bitstreamForm = new BitstreamForm();
        bitstreamForm.setItemID(itemID);
        model.addAttribute("bitstreamForm", bitstreamForm);
        return "pages/admin/bitstream-upload";
    }

    @RequestMapping(method= RequestMethod.GET, value = {"/admin/bitstream/{id}", "/admin/bitstream/{id}/edit"})
    public String showBitstreamEdit(Model model, @PathVariable(value="id") Integer bitstreamID, Context context) throws SQLException {
        Bitstream bitstream = Bitstream.find(context, bitstreamID);
        model.addAttribute("bitstream", bitstream);
        return "pages/admin/bitstream-edit";
    }

    @RequestMapping(method = RequestMethod.POST, params = "delete", value = "/admin/bitstream/{id}/**")
    public String processBitstreamDelete(@PathVariable(value="id") Integer bitstreamID, Context context, Model model) throws SQLException, AuthorizeException, IOException {
        Bitstream bitstream = Bitstream.find(context, bitstreamID);


        //Just handling case of bitstream for item.
        DSpaceObject dso = bitstream.getParentObject();
        Item item = null;
        if(dso instanceof Item) {
            item = (Item) dso;
            Bundle[] bundles = bitstream.getBundles();
            for(Bundle bundle : bundles) {
                bundle.removeBitstream(bitstream);
                if(bundle.getBitstreams().length == 0) {
                    item.removeBundle(bundle);
                }
            }
        }
        context.commit();
        model.addAttribute("event", "bitstream.deleted");
        return "redirect:/admin/item/"+item.getID()+"/bitstreams";
    }

    @RequestMapping(method = RequestMethod.POST, params = "create", value = "/admin/item/{itemID}/bitstream/new")
    public String processBitstreamCreate(@PathVariable(value="itemID") Integer itemID, Context context, @ModelAttribute("bitstreamForm") BitstreamForm bitstreamForm) throws SQLException, IOException, AuthorizeException {
        if(bitstreamForm.getFile().isEmpty()) {
            return "pages/error";
            //@TODO Add proper form validation
        }

        Item item = Item.find(context, itemID);
        Bitstream bitstream = null;

        Bundle[] bundles = item.getBundles(bitstreamForm.getBundle());
        if(bundles.length < 1) {
            bitstream = item.createSingleBitstream(bitstreamForm.getFile().getInputStream(), bitstreamForm.getBundle());

            // set the permission as defined in the owning collection
            Collection owningCollection = item.getOwningCollection();
            if (owningCollection != null)
            {
                Bundle bundle = bitstream.getBundles()[0];
                bundle.inheritCollectionDefaultPolicies(owningCollection);
            }
        } else {
            // we have a bundle already, just add bitstream
            bitstream = bundles[0].createBitstream(bitstreamForm.getFile().getInputStream());
        }

        bitstreamForm.setBitstreamID(bitstream.getID());
        bitstreamForm.save(context);
        return "redirect:/admin/item/"+item.getID()+"/bitstreams";
    }

    //@TODO Add bitstream edit

    public static class BitstreamForm {
        private String bundle;
        private String description;
        private String name;

        @NotEmpty
        private CommonsMultipartFile file;
        private Integer itemID;
        private Integer bitstreamID;
        //source
        //description
        //format

        public BitstreamForm() {}

        public void save(Context context) throws SQLException, AuthorizeException {
            context.turnOffAuthorisationSystem();

            Bitstream bitstream = Bitstream.find(context, getBitstreamID());
            bitstream.setName(getFile().getOriginalFilename());
			bitstream.setSource(getFile().getOriginalFilename());
			bitstream.setDescription(getDescription());

			// Identify the format
			BitstreamFormat format = FormatIdentifier.guessFormat(context, bitstream);
			bitstream.setFormat(format);

			// Update to DB
			bitstream.update();
            context.commit();
            context.restoreAuthSystemState();
        }

        public String getBundle() {
            return bundle;
        }

        public void setBundle(String bundle) {
            this.bundle = bundle;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public CommonsMultipartFile getFile() {
            return file;
        }

        public void setFile(CommonsMultipartFile file) {
            this.file = file;
        }

        public Integer getItemID() {
            return itemID;
        }

        public void setItemID(Integer itemID) {
            this.itemID = itemID;
        }

        public Integer getBitstreamID() {
            return bitstreamID;
        }

        public void setBitstreamID(Integer bitstreamID) {
            this.bitstreamID = bitstreamID;
        }
    }
}
