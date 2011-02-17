package org.dspace.webmvc.view.helpers;

import org.dspace.content.Community;
import org.dspace.core.Context;
import org.dspace.sort.SortException;
import org.dspace.sort.SortOption;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DSpaceHelper {
    private Context context;

    public DSpaceHelper(Context context) {
        this.context = context;
    }

    public NewsHelper getNews() {
        return new NewsHelper();
    }


    public RecentSubmissionsHelper getRecentSubmissions() {
        return new RecentSubmissionsHelper(context);
    }

    public Set<SortOption> getSortOptions() {
        try {
            return SortOption.getSortOptions();
        } catch (SortException se) {
            return new HashSet<SortOption>();
        }
    }

    public Community[] getTopLevelCommunities() {
        try {
            return Community.findAllTop(context);
        } catch (SQLException e) {
            return new Community[0];
        }
    }
}
