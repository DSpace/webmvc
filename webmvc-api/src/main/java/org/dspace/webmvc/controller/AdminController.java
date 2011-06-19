/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.log4j.Logger;

/**
 *
 * @author AdminNUS
 */
@Controller
public class AdminController {
    
    
    /** Logger */
    private static Logger log = Logger.getLogger(EPersonAdminController.class);
    
    //Display Admin Tools
    @RequestMapping
    protected String showAdminView() {

        return "pages/admin/navbar";

    }//end showAdminView
    
}
