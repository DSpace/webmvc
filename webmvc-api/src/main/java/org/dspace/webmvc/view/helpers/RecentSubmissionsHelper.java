package org.dspace.webmvc.view.helpers;

import org.apache.log4j.Logger;
import org.dspace.browse.*;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.sort.SortException;
import org.dspace.sort.SortOption;

import javax.servlet.ServletException;

public class RecentSubmissionsHelper {
    private Logger log = Logger.getLogger(RecentSubmissionsHelper.class);
    private Context context;

    public RecentSubmissionsHelper(Context context) {
        this.context = context;
    }

    public Item[] allRepository() throws ServletException {
        try {
            // get our configuration
            String source = ConfigurationManager.getProperty("recent.submissions.sort-option");
            String count = ConfigurationManager.getProperty("recent.submissions.count");

            // prep our engine and scope
            BrowseEngine be = new BrowseEngine(context);
            BrowserScope bs = new BrowserScope(context);
            BrowseIndex bi = BrowseIndex.getItemBrowseIndex();

            // fill in the scope with the relevant gubbins
            bs.setBrowseIndex(bi);
            bs.setOrder(SortOption.DESCENDING);
            bs.setResultsPerPage(Integer.parseInt(count));
            for (SortOption so : SortOption.getSortOptions()) {
                if (so.getName().equals(source)) {
                    bs.setSortBy(so.getNumber());
                }
            }

            BrowseInfo results = be.browseMini(bs);

            return results.getItemResults(context);
        } catch (SortException se) {
            log.error("caught exception: ", se);
            throw new ServletException(se);
        } catch (BrowseException e) {
            log.error("caught exception: ", e);
            throw new ServletException(e);
        }
    }

    /**
     * Obtain the recent submissions from the given container object.  This
     * method uses the configuration to determine which field and how many
     * items to retrieve from the DSpace Object.
     *
     * If the object you pass in is not a Community or Collection (e.g. an Item
     * is a DSpaceObject which cannot be used here), an exception will be thrown
     *
     * @param dso	DSpaceObject: Community or Collection
     * @return		The recently submitted items
     * @throws RecentSubmissionsException
     */
    public Item[] within(DSpaceObject dso) throws ServletException {
        try {
            // get our configuration
            String source = ConfigurationManager.getProperty("recent.submissions.sort-option");
            String count = ConfigurationManager.getProperty("recent.submissions.count");

            // prep our engine and scope
            BrowseEngine be = new BrowseEngine(context);
            BrowserScope bs = new BrowserScope(context);
            BrowseIndex bi = BrowseIndex.getItemBrowseIndex();

            // fill in the scope with the relevant gubbins
            bs.setBrowseIndex(bi);
            bs.setOrder(SortOption.DESCENDING);
            bs.setResultsPerPage(Integer.parseInt(count));
            bs.setBrowseContainer(dso);
            for (SortOption so : SortOption.getSortOptions()) {
                if (so.getName().equals(source)) {
                    bs.setSortBy(so.getNumber());
                }
            }

            BrowseInfo results = be.browseMini(bs);

            return results.getItemResults(context);
        } catch (SortException se) {
            log.error("caught exception: ", se);
            throw new ServletException(se);
        } catch (BrowseException e) {
            log.error("caught exception: ", e);
            throw new ServletException(e);
        }
    }
}
