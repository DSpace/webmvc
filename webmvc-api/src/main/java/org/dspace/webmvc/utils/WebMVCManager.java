/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.springframework.ui.ModelMap;
/**
 *
 * @author AdminNUS
 */
public class WebMVCManager {
    
    /** log4j logger */
    private static Logger log = Logger.getLogger(WebMVCManager.class);
    
    public static String showInternalError(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "error/internal";
        
    }
    
    public static String showIntegrityError(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "error/integrity";
    }
    
    public static String showAuthorizeError(HttpServletRequest request,
            HttpServletResponse response, AuthorizeException exception)
            throws ServletException, IOException
    {
        // FIXME: Need to work out which error message to display?
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);        
        return "error/authorize";
    }
    
    public static String showInvalidIDError(ModelMap model, HttpServletRequest request,
            HttpServletResponse response, String badID, int type)
            throws ServletException, IOException
    {
        model.addAttribute("bad.id", StringEscapeUtils.escapeHtml(badID));
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        if (type != -1)
        {
            request.setAttribute("bad.type", Integer.valueOf(type));
        }
        
        return "error/invalid-id";
    }
    
    public static String showFileSizeLimitExceededError(HttpServletRequest request, HttpServletResponse response, ModelMap model, String message, long actualSize, long permittedSize) throws ServletException, IOException {

        model.addAttribute("errormessage", message);
        model.addAttribute("actualSize", actualSize);
        model.addAttribute("actualSize", actualSize);
        response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);

        return "error/exceeded-size";
    }
    
}
