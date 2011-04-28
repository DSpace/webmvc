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
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Administrative tasks that can be done to an item.
 */
@Controller
public class ItemController {
    //@TODO Check user is authorized, and that item exists.

    @RequestMapping(method= RequestMethod.GET, value = {"/admin/item/{id}", "/admin/item/{id}/status"})
    public String showItemStatus(Model model, @PathVariable(value="id") Integer itemID, Context context) throws SQLException {
        Item item = Item.find(context, itemID);
        model.addAttribute("item", item);
        return "pages/admin/item-status";
    }

    @RequestMapping(method= RequestMethod.GET, value = "/admin/item/{id}/bitstreams")
    public String showItemBitstream(Model model, @PathVariable(value="id") Integer itemID, Context context) throws SQLException {
        Item item = Item.find(context, itemID);
        model.addAttribute("item", item);
        return "pages/admin/item-bitstream";
    }

    @RequestMapping(method= RequestMethod.GET, value = "/admin/item/{id}/metadata")
    public String showItemMetadata(Model model, @PathVariable(value="id") Integer itemID, Context context) throws SQLException {
        Item item = Item.find(context, itemID);
        model.addAttribute("item", item);
        return "pages/admin/item-metadata";
    }

    @RequestMapping(method = RequestMethod.POST, params = "withdraw", value = "/admin/item/{id}/**")
    public String processItemWithdraw(@PathVariable(value="id") Integer itemID, Context context, Model model) throws SQLException, AuthorizeException, IOException {
        Item item = Item.find(context, itemID);
        item.withdraw();
        context.commit();
        model.addAttribute("event", "item.withdrawn");
        return "redirect:/admin/item/"+itemID+"/status";
    }

    @RequestMapping(method = RequestMethod.POST, params = "reinstate", value = "/admin/item/{id}/**")
    public String processItemReinstate(@PathVariable(value="id") Integer itemID, Context context, Model model) throws SQLException, AuthorizeException, IOException {
        Item item = Item.find(context, itemID);
        item.reinstate();
        context.commit();
        model.addAttribute("event", "item.reinstated");
        return "redirect:/admin/item/"+itemID+"/status";
    }

    @RequestMapping(method = RequestMethod.POST, params = "delete", value = "/admin/item/{id}/**")
    public String processItemDelete(@PathVariable(value="id") Integer itemID, Context context, Model model) throws SQLException, AuthorizeException, IOException {
        Item item = Item.find(context, itemID);
        Collection[] collections = item.getCollections();
        for (Collection collection : collections) {
            collection.removeItem(item);
        }
        context.commit();
        return "redirect:/submissions";
    }
}
