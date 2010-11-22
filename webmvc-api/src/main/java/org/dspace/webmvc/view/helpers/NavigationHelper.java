package org.dspace.webmvc.view.helpers;

import org.dspace.browse.BrowseException;
import org.dspace.browse.BrowseIndex;

public class NavigationHelper {
    public BrowseIndex[] getBrowseIndices() throws BrowseException {
        return BrowseIndex.getBrowseIndices();
    }
}
