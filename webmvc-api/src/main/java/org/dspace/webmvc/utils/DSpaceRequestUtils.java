package org.dspace.webmvc.utils;

import org.dspace.content.Collection;
import org.dspace.content.Community;

import javax.servlet.ServletRequest;

public class DSpaceRequestUtils {
    public static Collection getCollectionLocation(ServletRequest request) {
        return ((Collection) request.getAttribute("dspace.collection"));
    }

    public static Community getCommunityLocation(ServletRequest request) {
        return ((Community) request.getAttribute("dspace.community"));
    }

    public static void setCollectionLocation(ServletRequest request, Collection collection) {
        request.setAttribute("dspace.collection", collection);
    }

    public static void setCommunityLocation(ServletRequest request, Community community) {
        request.setAttribute("dspace.community", community);
    }
}
