package org.dspace.webmvc.filter;

import org.dspace.core.Context;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class DSpaceRequestContextFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Context context = null;
        try {
            context = new Context();
            request.setAttribute("context", context);
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
