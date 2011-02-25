package org.dspace.webmvc.view.helpers;

import org.dspace.browse.BrowseException;
import org.dspace.browse.BrowseIndex;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.webmvc.utils.DSpaceRequestUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class NavigationHelper {
    HttpServletRequest request;

    public NavigationHelper(HttpServletRequest request) {
        this.request = request;
    }

    public Community getCommunityContainer() {
        DSpaceObject dso = DSpaceRequestUtils.getScopeObject(request);
        if (dso instanceof Community) {
            return (Community)dso;
        }
        return null;
    }

    public Collection getCollectionContainer() {
        DSpaceObject dso = DSpaceRequestUtils.getScopeObject(request);
        if (dso instanceof Collection) {
            return (Collection)dso;
        }
        return null;
    }

    public BrowseIndex[] getBrowseIndices() throws BrowseException {
        return BrowseIndex.getBrowseIndices();
    }
}
