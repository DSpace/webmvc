package org.dspace.webmvc.utils;

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;

import javax.servlet.ServletRequest;

public class DSpaceRequestUtils {
    // Do not change the value of this entry - the value 'context' allows Spring to automatically map
    // the request attribute to a parameter defined as Context context in the controllers.
    private final static String CONTEXT_NAME = "context";

    public static Context getDSpaceContext(ServletRequest request) {
        return (Context) request.getAttribute(CONTEXT_NAME);
    }

    public static void setDSpaceContext(ServletRequest request, Context context) {
        request.setAttribute(CONTEXT_NAME, context);
    }

    public static DSpaceObject getScopeObject(ServletRequest request) {
        return (DSpaceObject) request.getAttribute("scope.object");
    }

    public static void setScopeObject(ServletRequest request, DSpaceObject dso) {
        request.setAttribute("scope.object", dso);
    }

    public static String getScopeHandle(ServletRequest request) {
        return (String) request.getAttribute("scope.handle");
    }

    public static void setScopeHandle(ServletRequest request, String handle) {
        request.setAttribute("scope.handle", handle);
    }
}
