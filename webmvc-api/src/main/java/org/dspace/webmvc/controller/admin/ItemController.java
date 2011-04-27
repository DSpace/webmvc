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

import org.dspace.content.Item;
import org.dspace.core.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.SQLException;

/**
 * Administrative tasks that can be done to an item.
 */
@Controller
public class ItemController {

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
}
