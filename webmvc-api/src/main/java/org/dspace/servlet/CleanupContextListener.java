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

package org.dspace.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.DriverManager;
import java.sql.Driver;
import java.util.Enumeration;
import java.beans.Introspector;
import java.net.URL;
import java.net.URLConnection;

/**
 * Cleanup listener for general resource handling
 * NB: You MUST declare this as the first listener in web.xml
 */
public class CleanupContextListener implements ServletContextListener
{
    /**
     * Initialize any resources required by the application
     * @param event Servlet context for this event
     */
    public void contextInitialized(ServletContextEvent event)
    {

        // On Windows, URL caches can cause problems, particularly with undeployment
        // So, here we attempt to disable them if we detect that we are running on Windows
        try
        {
            String osName = System.getProperty("os.name");

            if (osName != null && osName.toLowerCase().contains("windows"))
            {
                URL url = new URL("http://localhost/");
                URLConnection urlConn = url.openConnection();
                urlConn.setDefaultUseCaches(false);
            }
        }
        catch (Throwable t)
        {
            // Any errors thrown in disabling the caches aren't significant to
            // the normal execution of the application, so we ignore them
        }
    }

    /**
     * Clean up resources used by the application when stopped
     *
     * @param event Servlet context for this event
     */
    public void contextDestroyed(ServletContextEvent event)
    {
        try
        {
            // Clean out the introspector
            Introspector.flushCaches();

            // Remove any drivers registered by this classloader
            for (Enumeration e = DriverManager.getDrivers(); e.hasMoreElements();)
            {
                Driver driver = (Driver) e.nextElement();
                if (driver.getClass().getClassLoader() == getClass().getClassLoader())
                {
                    DriverManager.deregisterDriver(driver);
                }
            }
        }
        catch (Throwable e)
        {
            // Any errors thrown in clearing the caches aren't significant to
            // the normal execution of the application, so we ignore them
        }
    }
}
