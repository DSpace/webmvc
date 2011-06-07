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
import org.dspace.core.Context;
import org.dspace.core.LogManager;

/**
 *
 * @author Robert Qin
 */
public class Manager {
    
    /** log4j logger */
    private static Logger log = Logger.getLogger(Manager.class);
    
    public static String showInternalError(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return "/error/internal";
    }
    
    public static String showIntegrityError(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "/error/integrity";
    }
    
    
}
