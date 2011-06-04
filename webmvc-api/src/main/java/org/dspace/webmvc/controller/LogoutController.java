/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller;


import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.apache.log4j.Logger;
import org.dspace.app.webui.util.Authenticate;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.core.LogManager;

/**
 *
 * @author AdminNUS
 */
@Controller
public class LogoutController {
    
    private static Logger log = Logger.getLogger(LogoutController.class);
    //ServletException, IOException, SQLException, AuthorizeException
    
    @RequestMapping("/logout/**")
    protected String logOut(@RequestAttribute Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException
    {
        log.info(LogManager.getHeader(context, "logout", ""));

        Authenticate.loggedOut(context, request);

        return "pages/home";
    }
    
}
