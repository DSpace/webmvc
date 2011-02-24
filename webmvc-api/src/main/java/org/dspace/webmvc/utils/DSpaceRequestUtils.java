package org.dspace.webmvc.utils;

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;

import javax.servlet.ServletRequest;

public class DSpaceRequestUtils {
    public static Context getDSpaceContext(ServletRequest request) {
        return (Context) request.getAttribute("context");
    }

    public static void setDSpaceContext(ServletRequest request, Context context) {
        request.setAttribute("context", context);
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
