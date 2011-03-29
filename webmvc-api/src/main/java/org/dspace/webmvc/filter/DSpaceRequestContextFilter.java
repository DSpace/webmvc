package org.dspace.webmvc.filter;

import org.apache.log4j.Logger;
import org.dspace.constants.Constants;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.webmvc.model.login.HttpLoginService;
import org.dspace.webmvc.utils.DSpaceRequestUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class DSpaceRequestContextFilter implements Filter {
    private static final Logger log = Logger.getLogger(DSpaceRequestContextFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Set encoding to UTF-8, if not set yet
        //This avoids problems of using the HttpServletRequest
        //in the getSpecialGroups() for an AuthenticationMethod,
        //which causes the HttpServletRequest to default to
        //non-UTF-8 encoding.
        try {
            if(request.getCharacterEncoding()==null) {
                request.setCharacterEncoding(Constants.DEFAULT_ENCODING);
            }
        } catch(Exception e) {
            log.error("Unable to set encoding to UTF-8.", e);
        }

        Context context = null;
        try {
            context = new Context();

            if (request instanceof HttpServletRequest) {
                HttpLoginService hls = new HttpLoginService((HttpServletRequest)request);
                EPerson person = hls.getUser(context);
                if (person != null) {
                    context.setCurrentUser(person);
                }
            }

            DSpaceRequestUtils.setDSpaceContext(request, context);
            chain.doFilter(request, response);
        } catch (SQLException e) {

        } finally {
            if (context != null) {
                context.abort();
            }
        }
    }

    public void destroy() {
    }
}
