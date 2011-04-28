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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Administrative tasks that can be done to a bitstream.
 */
@Controller
public class BitstreamController {
    //@TODO Check user is authorized, and that bitstream exists.

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

    //@TODO Add bitstream edit
}
