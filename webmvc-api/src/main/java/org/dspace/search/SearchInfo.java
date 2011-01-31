package org.dspace.search;

import org.dspace.sort.SortOption;

import java.util.List;

public class SearchInfo {
    /**
     * The results of the browse.
     * FIXME: Unable to generify due to mixed usage
     */
    private List results;

    /** the sort option being used */
    private SortOption sortOption;

    /** is the browse ascending or descending */
    private boolean ascending;

    /** offset of the item at the top of the next page */
    private int nextOffset = -1;

    /** offset of the item at the top of the previous page */
    private int prevOffset = -1;

    /** number of results to display per page */
    private int resultsPerPage = -1;

    /** number of metadata elements to display before truncating using "et al" */
    private int etAl = -1;

    /**
     * @return	the number of metadata fields at which to truncate with "et al"
     */
    public int getEtAl() {
    	return etAl;
    }

    /**
     * set the number of metadata fields at which to truncate with "et al"
     *
     * @param etAl
     */
    public void setEtAl(int etAl) {
    	this.etAl = etAl;
    }
}
