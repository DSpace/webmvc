package org.dspace.webmvc.theme;

import org.springframework.web.servlet.theme.SessionThemeResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestThemeResolver extends SessionThemeResolver {
    private static final String CURRENT_THEME = RequestThemeResolver.class.getName() + ".CURRENT_THEME";

    @Override
    public String resolveThemeName(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getAttribute(RequestThemeResolver.CURRENT_THEME) != null) {
            return (String)httpServletRequest.getAttribute(RequestThemeResolver.CURRENT_THEME);
        }

        return getDefaultThemeName();
    }

    @Override
    public void setThemeName(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) {
        httpServletRequest.setAttribute(RequestThemeResolver.CURRENT_THEME, s);
    }
}
