package org.dspace.webmvc.view.helpers;

import org.dspace.browse.BrowseException;
import org.dspace.browse.BrowseIndex;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.webmvc.utils.DSpaceRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class NavigationHelper {
    private HttpServletRequest request;

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

    public Item getItemContainer() {
        DSpaceObject dso = DSpaceRequestUtils.getScopeObject(request);
        if (dso instanceof Item) {
            return (Item)dso;
        }
        return null;
    }

    public BrowseIndex[] getBrowseIndices() throws BrowseException {
        return BrowseIndex.getBrowseIndices();
    }
}
