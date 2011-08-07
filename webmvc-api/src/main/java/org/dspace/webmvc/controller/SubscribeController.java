/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dspace.webmvc.utils.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Collection;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Subscribe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.dspace.webmvc.bind.annotation.RequestAttribute;

/**
 *
 * @author AdminNUS
 */
@Controller
public class SubscribeController {

    @RequestMapping(method = RequestMethod.GET)
    protected String processGet(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {


        return // Simply show list of subscriptions
                showSubscriptions(context, model, request, response, false);

    }//end processGet

    private String showSubscriptions(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, boolean updated)
            throws ServletException, IOException, SQLException {
        // Subscribed collections
        Collection[] subs = Subscribe.getSubscriptions(context, context.getCurrentUser());

        model.addAttribute("subscriptions", subs);
        model.addAttribute("updated", Boolean.valueOf(updated));

        return "pages/mydspace/subscriptions";

    }

    
    @RequestMapping(method = RequestMethod.POST, params = "submit_unsubscribe")
    protected String submitUnsubscribe(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        EPerson e = context.getCurrentUser();

        int collID = UIUtil.getIntParameter(request, "collection");
            Collection c = Collection.find(context, collID);

            // Sanity check - ignore duff values
            if (c != null)
            {
                Subscribe.unsubscribe(context, e, c);
            }

            context.commit();
            // Show the list of subscriptions
            return showSubscriptions(context, model, request, response, true);
            
    }//end submitUnsubscribe
    
    
    
}
