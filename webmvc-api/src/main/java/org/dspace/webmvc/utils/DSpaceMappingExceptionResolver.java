package org.dspace.webmvc.utils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DSpaceMappingExceptionResolver extends SimpleMappingExceptionResolver {
    private RequestInfoService ris = new RequestInfoService();

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ris.setRequestInfoAttribute(request, new RequestInfo(request));

        return super.doResolveException(request, response, handler, ex);
    }
}
