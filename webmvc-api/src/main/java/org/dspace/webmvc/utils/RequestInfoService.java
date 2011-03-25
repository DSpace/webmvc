package org.dspace.webmvc.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestInfoService {
    public static final String STORED_REQUEST = "dspace.request.stored";

    public void setRequestInfoAttribute(HttpServletRequest request, RequestInfo info) {
        // Set the request as interrupted
        request.setAttribute(STORED_REQUEST, info);
    }

    public RequestInfo getRequestInfoAttribute(HttpServletRequest request) {
        return (RequestInfo)request.getAttribute(STORED_REQUEST);
    }

    public void setRequestInfoSession(HttpServletRequest request, RequestInfo info) {
        HttpSession session = request.getSession();

        // Set the request as interrupted
        session.setAttribute(STORED_REQUEST, info);
    }

    public RequestInfo getRequestInfoSession(HttpServletRequest request) {
        HttpSession session = request.getSession();

        return (RequestInfo)session.getAttribute(STORED_REQUEST);
    }
}

