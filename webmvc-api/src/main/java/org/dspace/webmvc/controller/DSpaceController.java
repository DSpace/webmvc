/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.webui.util.Authenticate;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.springframework.stereotype.Controller;

/**
 *
 * @author AdminNUS
 */
@Controller
public class DSpaceController extends HttpServlet{
    
    private static Logger log = Logger.getLogger(DSpaceController.class);
    
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    /**
     * Process an incoming request
     * 
     * @param request
     *            the request object
     * @param response
     *            the response object
     */
    private void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        Context context = null;

        // set all incoming encoding to UTF-8
        request.setCharacterEncoding("UTF-8");

        // Get the URL from the request immediately, since forwarding
        // loses that information
        UIUtil.storeOriginalURL(request);

        try
        {
            // Obtain a context - either create one, or get the one created by
            // an authentication filter
            context = UIUtil.obtainContext(request);

            // Are we resuming a previous request that was interrupted for
            // authentication?
            request = Authenticate.getRealRequest(request);

            if (log.isDebugEnabled())
            {
                log.debug(LogManager.getHeader(context, "http_request", UIUtil
                        .getRequestLogInfo(request)));
            }

            // Invoke the servlet code
            if (request.getMethod().equals("POST"))
            {
                doDSPost(context, request, response);
            }
            else
            {
 
                doDSGet(context, request, response);
            }
        }
        catch (SQLException se)
        {
            // For database errors, we log the exception and show a suitably
            // apologetic error
            log.warn(LogManager.getHeader(context, "database_error", se
                    .toString()), se);

            // Also email an alert
            UIUtil.sendAlert(request, se);

            JSPManager.showInternalError(request, response);
        }
        catch (AuthorizeException ae)
        {
            /*
             * If no user is logged in, we will start authentication, since if
             * they authenticate, they might be allowed to do what they tried to
             * do. If someone IS logged in, and we got this exception, we know
             * they tried to do something they aren't allowed to, so we display
             * an error in that case.
             */
            if (context.getCurrentUser() != null ||
                Authenticate.startAuthentication(context, request, response))
            {
                // FIXME: Log the right info?
                // Log the error
                log.info(LogManager.getHeader(context, "authorize_error", ae
                        .toString()));

                JSPManager.showAuthorizeError(request, response, ae);
            }
        }
        finally
        {
            // Abort the context if it's still valid
            if ((context != null) && context.isValid())
            {
                context.abort();
            }
        }
    }
    
      protected void doDSGet(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        // If this is not overridden, we invoke the raw HttpServlet "doGet" to
        // indicate that GET is not supported by this servlet.
        super.doGet(request, response);
    }

    /**
     * Process an incoming HTTP POST. If an exception is thrown, or for some
     * other reason the passed in context is not completed, it will be aborted
     * and any changes made by this method discarded when this method returns.
     * 
     * @param context
     *            a DSpace Context object
     * @param request
     *            the HTTP request
     * @param response
     *            the HTTP response
     * 
     * @throws SQLException
     *             if a database error occurs
     * @throws AuthorizeException
     *             if some authorization error occurs
     */
    protected void doDSPost(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        // If this is not overridden, we invoke the raw HttpServlet "doGet" to
        // indicate that POST is not supported by this servlet.
        super.doGet(request, response);
    }
    
    
}
