package org.dspace.webmvc.utils;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.DSpaceObject;
import org.dspace.core.Context;
import org.dspace.core.I18nUtil;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public final class DSpaceRequestUtils {
    public static final String CURRENT_LOCALE = "dspace.current.locale";

    private DSpaceRequestUtils() {
    }

    // Do not change the value of this entry - the value 'context' allows Spring to automatically map
    // the request attribute to a parameter defined as Context context in the controllers.
    private static final String CONTEXT_NAME = "context";

    public static Context getDSpaceContext(ServletRequest request) {
        return (Context) request.getAttribute(CONTEXT_NAME);
    }

    public static void setDSpaceContext(ServletRequest request, Context context) {
        request.setAttribute(CONTEXT_NAME, context);
    }

    public static Context getDSpaceContext(WebRequest request) {
        return (Context) request.getAttribute(CONTEXT_NAME, WebRequest.SCOPE_REQUEST);
    }

    public static void setDSpaceContext(WebRequest request, Context context) {
        request.setAttribute(CONTEXT_NAME, context, WebRequest.SCOPE_REQUEST);
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

    public static void setSessionLocale(ServletRequest request, Locale sessionLocale) {
        if (request instanceof HttpServletRequest) {
            /* get session locale set by application */
            HttpSession session = ((HttpServletRequest)request).getSession();
            session.setAttribute(CURRENT_LOCALE, sessionLocale);
        }
    }

    public static Locale getSessionLocale(ServletRequest request) {
        String paramLocale = request.getParameter("locale");
        Locale sessionLocale = null;
        Locale supportedLocale = null;

        if (!StringUtils.isEmpty(paramLocale)) {
            /* get session locale according to user selection */
            sessionLocale = new Locale(paramLocale);
        }


        if (sessionLocale == null && request instanceof HttpServletRequest) {
            /* get session locale set by application */
            HttpSession session = ((HttpServletRequest)request).getSession();
            sessionLocale = (Locale)session.getAttribute(CURRENT_LOCALE);
        }

        /*
         * if session not set by selection or application then default browser
         * locale
         */
        if (sessionLocale == null)
        {
            sessionLocale = request.getLocale();
        }

        if (sessionLocale == null)
        {
            sessionLocale = I18nUtil.DEFAULTLOCALE;
        }
        return I18nUtil.getSupportedLocale(sessionLocale);
    }
}
