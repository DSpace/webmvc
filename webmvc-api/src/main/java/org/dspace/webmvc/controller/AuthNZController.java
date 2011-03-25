package org.dspace.webmvc.controller;

import org.apache.log4j.Logger;
import org.dspace.authenticate.AuthenticationManager;
import org.dspace.authenticate.AuthenticationMethod;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthNZController {
    private static Logger log = Logger.getLogger(AuthNZController.class);

    @RequestMapping("/unauthorized")
    public String handleAuthorizeException(Context context, HttpServletRequest request, HttpServletResponse response) {
        if (context.getCurrentUser() == null) {

            String referer = (String)request.getHeader("Referer");

            if (AuthenticationManager.authenticateImplicit(context, null, null, null, request) == AuthenticationMethod.SUCCESS) {

                // handle logged in
                log.info(LogManager.getHeader(context, "login", "type=implicit"));
            }

            response.addDateHeader("expires", 1);
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Cache-control", "no-store");


            // Start authentication
            return "redirect:/login";
        }

        return "/pages/unauthorized";
    }
}
