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
import org.dspace.content.DCValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import org.dspace.core.ConfigurationManager;
import java.util.*;


import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.dspace.core.Context;

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
        DCValue[] values = item.getMetadata(Item.ANY, Item.ANY, Item.ANY, Item.ANY);
        model.addAttribute("values", values);
        return "pages/admin/item-metadata";
    }

    @RequestMapping(method = RequestMethod.GET, params = "edit", value = "/admin/item/{id}/edit")
    public String processItemEdit(Model model, @PathVariable(value="id") Integer itemID, Context context) throws SQLException {
        Item item = Item.find(context, itemID);
        model.addAttribute("item", item);
        
        DCValue[] values = item.getMetadata(Item.ANY, Item.ANY, Item.ANY, Item.ANY);
        model.addAttribute("values", values);
        model.addAttribute("prefix", ConfigurationManager.getProperty("handle.prefix"));
        return "pages/admin/edit-item-form";
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

    @RequestMapping(method = RequestMethod.POST, params = "update", value = "/admin/item/{id}/metadata/**")
    public String processItemMetadataUpdate(@PathVariable(value="id") Integer itemID, Context context, HttpServletRequest request) throws SQLException, AuthorizeException, IOException {
        Item item = Item.find(context, itemID);
        context.turnOffAuthorisationSystem();
        item.clearMetadata(Item.ANY, Item.ANY, Item.ANY, Item.ANY);

        /*
        INPUT:
        name_# == dc_element_qualifier OR dc_element_ OR dc_element
        value_#
        language_#
         */

        Enumeration parameterNames = request.getParameterNames();

        Map<Integer, ItemMetadataTuple> entries = new HashMap<Integer, ItemMetadataTuple>();

        while (parameterNames.hasMoreElements())
        {
            String parameter = (String) parameterNames.nextElement();
            if(!parameter.contains("_")) {
                continue;
            }

            String parameterValue = request.getParameter(parameter);
            if(StringUtils.isEmpty(parameterValue)) {
                continue;
            }

            String[] parameterPair = parameter.split("_");
            String type = parameterPair[0];
            Integer row = Integer.parseInt(parameterPair[1]);

            ItemMetadataTuple tuple = new ItemMetadataTuple();
            if(entries.containsKey(row)) {
                tuple = entries.get(row);
            }

            if(type.startsWith("name")) {
                String[] namePieces = parameterValue.split("_");
                tuple.setSchema(namePieces[0]);
                tuple.setElement(namePieces[1]);
                if(namePieces.length>2) {
                    tuple.setQualifier(namePieces[2]);
                }
            } else if(type.startsWith("value")) {
                tuple.setValue(parameterValue);
            } else if(type.startsWith("language")) {
                tuple.setLanguage(parameterValue);
            }
            entries.put(row, tuple);
        }

        for (ItemMetadataTuple tuple : entries.values()) {
            if(tuple.isValid()) {
                item.addMetadata(tuple.getSchema(), tuple.getElement(), tuple.getQualifier(), tuple.getLanguage(), tuple.getValue());
            }
        }

        item.update();
        context.commit();
        context.restoreAuthSystemState();
        return "redirect:/admin/item/" + itemID + "/bitstreams";
    }

    //@TODO Cancel
}

class ItemMetadataTuple {
    private String schema;
    private String element;
    private String qualifier;
    private String value;
    private String language;

    public ItemMetadataTuple() {}

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Checks if there is atleast a schema.element, and that there is a value.
     * @return
     */
    public boolean isValid() {
        return (StringUtils.isNotBlank(schema) && StringUtils.isNotBlank(element) && StringUtils.isNotBlank(value));
    }
}
