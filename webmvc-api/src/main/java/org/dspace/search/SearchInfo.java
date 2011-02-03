package org.dspace.search;

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.sort.SortOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchInfo {
    /**
     * The results of the search.
     */
    private List<Community>  communityResults;
    private List<Collection> collectionResults;
    private List<Item>       itemResults;

    /** Collection we are constrained to */
    private Collection collection;

    /** Community we are constrained to */
	private Community community;

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

    private String query;
    private String advancedQuery;

    private boolean requiresRedirect = false;

    public SearchInfo(List<Item> pItemResults, List<Collection> pCollectionResults, List<Community> pCommunityResults) {
        itemResults       = Collections.unmodifiableList(pItemResults);
        collectionResults = Collections.unmodifiableList(pCollectionResults);
        communityResults  = Collections.unmodifiableList(pCommunityResults);
    }

    public List<Community> getCommunityResults() {
        return communityResults;
    }

    public List<Collection> getCollectionResults() {
        return collectionResults;
    }

    public List<Item> getItemResults() {
        return itemResults;
    }

    /**
      * @return Returns the ascending.
      */
     public boolean isAscending() {
         return ascending;
     }

     /**
      * @param ascending The ascending to set.
      */
     public void setAscending(boolean ascending) {
         this.ascending = ascending;
     }

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

    /**
     * @param offset	the database id of the item at the top of the next page
     */
    public void setNextOffset(int offset) {
    	this.nextOffset = offset;
    }

    /**
     * @return		the database id of the item at the top of the next page
     */
    public int getNextOffset() {
    	return this.nextOffset;
    }


    /**
     * @return Returns the prevItem.
     */
    public int getPrevOffset() {
        return prevOffset > -1 ? prevOffset : 0;
    }

    /**
     * @param prevOffset The prevOffset to set.
     */
    public void setPrevOffset(int prevOffset) {
        this.prevOffset = prevOffset;
    }


    /**
     * @return Returns the resultsPerPage.
     */
    public int getResultsPerPage() {
        return resultsPerPage;
    }

    /**
     * @param resultsPerPage The resultsPerPage to set.
     */
    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    /**
     * @return Returns the sortOption.
     */
    public SortOption getSortOption() {
        return sortOption;
    }

    /**
     * @param sortOption The sortOption to set.
     */
    public void setSortOption(SortOption sortOption) {
        this.sortOption = sortOption;
    }

    /**
     * Set the DSpaceObject that is the container for this browse.  If this
     * is not of type Collection or Community, this method will throw an
     * exception
     *
     * @param dso		the container object; a Community or Collection
     * @throws BrowseException
     */
    public void setSearchContainer(DSpaceObject dso) throws SearchException
    {
    	if (dso instanceof Collection) {
    		this.collection = (Collection) dso;
    	} else if (dso instanceof Community) {
    		this.community = (Community) dso;
    	} else {
    		throw new SearchException("The container must be a community or a collection");
    	}
    }

    /**
     * Obtain a DSpaceObject that represents the container object.  This will be
     * a Community or a Collection
     *
     * @return	A DSpaceObject representing a Community or a Collection
     */
    public DSpaceObject getSearchContainer() {
    	if (this.collection != null) {
    		return this.collection;
    	}

    	if (this.community != null) {
    		return this.community;
    	}
    	return null;
    }

    /**
     * are we browsing within a Community container?
     *
     * @return	true if in community, false if not
     */
    public boolean inCommunity() {
		if (this.community != null) {
			return true;
		}

		return false;
	}

    /**
     * are we browsing within a Collection container
     *
     * @return	true if in collection, false if not
     */
	public boolean inCollection() {
		if (this.collection != null) {
			return true;
		}

		return false;
	}

    public boolean hasResults() {
        return (communityResults != null && communityResults.size() > 0) ||
               (collectionResults != null && collectionResults.size() > 0) ||
               (itemResults != null && itemResults.size() > 0);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAdvancedQuery() {
        return advancedQuery;
    }

    public void setAdvancedQuery(String advancedQuery) {
        this.advancedQuery = advancedQuery;
    }

    public boolean requiresRedirect() {
        return requiresRedirect;
    }

    public void setRequiresRedirect(boolean requiresRedirect) {
        this.requiresRedirect = requiresRedirect;
    }
}
