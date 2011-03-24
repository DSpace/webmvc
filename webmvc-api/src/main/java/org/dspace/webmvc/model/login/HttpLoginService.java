package org.dspace.webmvc.model.login;

import org.dspace.core.Context;
import org.dspace.eperson.EPerson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class HttpLoginService implements LoginService {
    HttpServletRequest request;

    public HttpLoginService(HttpServletRequest pRequest) {
        request = pRequest;
    }

    public void createUserSession(Context context, EPerson person) {
        HttpSession session = request.getSession();

        // For security reasons after login, give the user a new session
        if ((!session.isNew()) && (session.getAttribute("dspace.current.user.id") == null)) {

            // Keep the user's locale setting if set
//            Locale sessionLocale = UIUtil.getSessionLocale(request);

            // Get info about the interrupted request, if set
//            RequestInfo requestInfo = (RequestInfo) session.getAttribute("interrupted.request.info");

            // Get the original URL of interrupted request, if set
//            String requestUrl = (String) session.getAttribute("interrupted.request.url");

            // Invalidate session unless dspace.cfg says not to
//            if(ConfigurationManager.getBooleanProperty("webui.session.invalidate", true)) {
//               session.invalidate();
//            }

            // Give the user a new session
//            session = request.getSession();

            // Restore the session locale
//            if (sessionLocale != null) {
//                Config.set(request.getSession(), Config.FMT_LOCALE, sessionLocale);
//            }

            // Restore interrupted request information and url to new session
//            if (requestInfo != null && requestUrl != null) {
//                session.setAttribute("interrupted.request.info", requestInfo);
//                session.setAttribute("interrupted.request.url", requestUrl);
//            }
        }

//        context.setCurrentUser(eperson);

//        boolean isAdmin = false;

//        try {
//            isAdmin = AuthorizeManager.isAdmin(context);
//        }
//        catch (SQLException se) {
//            log.warn("Unable to use AuthorizeManager " + se);
//        } finally {
//            request.setAttribute("is.admin", Boolean.valueOf(isAdmin));
//        }

        // We store the current user in the request as an EPerson object...
  //      request.setAttribute("dspace.current.user", eperson);

        // and in the session as an ID
//        session.setAttribute("dspace.current.user.id", Integer.valueOf(eperson.getID()));

        // and the remote IP address to compare against later requests
        // so we can detect session hijacking.
//        session.setAttribute("dspace.current.remote.addr",
//                             request.getRemoteAddr());
    }
}
