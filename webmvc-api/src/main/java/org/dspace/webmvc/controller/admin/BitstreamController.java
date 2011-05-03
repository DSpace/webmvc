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
        BitstreamForm bitstreamForm = new BitstreamForm();
        bitstreamForm.init(bitstream);
        model.addAttribute("bitstreamForm", bitstreamForm);
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

    @RequestMapping(method = RequestMethod.POST, params = "update", value = {"/admin/bitstream/{bitstreamID}/edit", "/admin/bitstream/{bitstreamID}/"})
    public String processBitstreamUpdate(@PathVariable(value="bitstreamID") Integer bitstreamID, Context context, @ModelAttribute("bitstreamForm") BitstreamForm bitstreamForm) throws SQLException, AuthorizeException {
        //@TODO Add proper validation
        Bitstream bitstream = Bitstream.find(context, bitstreamID);
        bitstreamForm.update(bitstream, context);
        return "redirect:/admin/item/" + bitstream.getParentObject().getID() + "/bitstreams";
    }

    public static class BitstreamForm {
        private String bundle;
        private String description;
        private String name;

        @NotEmpty
        private CommonsMultipartFile file;
        private Integer itemID;
        private Integer bitstreamID;
        private String primary;
        private Integer formatID;
        private String user_format;

        //source
        //description
        //format

        public BitstreamForm() {}

        public void init(Bitstream bitstream) throws SQLException {
            setBitstreamID(bitstream.getID());
            setName(bitstream.getName());
            setDescription(bitstream.getDescription());
            setBitstreamID(bitstream.getID());
            setFormatID(bitstream.getFormat().getID());
            setUser_format(bitstream.getUserFormatDescription());

            if(bitstream.getBundles()[0].getPrimaryBitstreamID() == bitstream.getID()) {
                setPrimary("yes");
            } else {
                setPrimary("no");
            }
        }

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

        public void update(Bitstream bitstream, Context context) throws SQLException, AuthorizeException {
            context.turnOffAuthorisationSystem();
			bitstream.setDescription(getDescription());

            if(getFormatID() > 0) {
                BitstreamFormat newFormat = BitstreamFormat.find(context, getFormatID());
                if (newFormat != null) {
                    bitstream.setFormat(newFormat);
                }
            } else if(getUser_format() != null && getUser_format().length() > 0) {
                bitstream.setUserFormatDescription(getUser_format());
                //@TODO Need to set bitstream format to -1?
            }

            Bundle[] bundles = bitstream.getBundles();
            if (bundles != null && bundles.length > 0)
            {
                if (bitstream.getID() == bundles[0].getPrimaryBitstreamID()) {
                    // currently the bitstream is primary
                    if ("no".equals(getPrimary())) {
                        // However the user has removed this bitstream as a primary bitstream.
                        bundles[0].unsetPrimaryBitstreamID();
                        bundles[0].update();
                    }
                }
                else if ("yes".equals(getPrimary())) {
                    // currently the bitstream is non-primary
                    // However the user has set this bitstream as primary.
                    bundles[0].setPrimaryBitstreamID(bitstream.getID());
                    bundles[0].update();
                }
            }

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

        public String getPrimary() {
            return primary;
        }

        public void setPrimary(String primary) {
            this.primary = primary;
        }

        public Integer getFormatID() {
            return formatID;
        }

        public void setFormatID(Integer formatID) {
            this.formatID = formatID;
        }

        public String getUser_format() {
            return user_format;
        }

        public void setUser_format(String user_format) {
            this.user_format = user_format;
        }
    }
}
