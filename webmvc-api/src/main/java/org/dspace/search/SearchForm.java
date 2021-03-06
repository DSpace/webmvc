package org.dspace.search;

import org.dspace.content.DSpaceObject;
import org.dspace.core.Constants;
import org.dspace.sort.SortOption;


public class SearchForm {
    private int numAdvancedFields = 3;

    private String query;
    private AdvancedField[] advancedFields = new AdvancedField[numAdvancedFields];

    private SortOption sortOption;
    private String sortOrder;

    private DSpaceObject scope;

    private int etAl = -1;

    private int resultsPerPage = 10;

    private boolean advancedForm = false;

    public int getNumAdvancedFields() {
        return numAdvancedFields;
    }

    public void setNumAdvancedFields(int fieldCount) {
        numAdvancedFields = fieldCount;
        AdvancedField[] tempFields = new AdvancedField[numAdvancedFields];
        System.arraycopy(advancedFields, 0, tempFields, 0, Math.min(numAdvancedFields, advancedFields.length));
        advancedFields = tempFields;
    }

    public boolean isAscending() {
        return SortOption.ASCENDING.equalsIgnoreCase(sortOrder);
    }

    public boolean isAdvancedForm() {
        return advancedForm;
    }

    public void setAdvancedForm(boolean advancedForm) {
        this.advancedForm = advancedForm;
    }

    public AdvancedField[] getAdvancedFields() {
        return advancedFields;
    }

    public void setAdvancedField(int pos, String conjunction, String field, String query) {
        advancedFields[pos] = new AdvancedField(conjunction, field, query);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SortOption getSortOption() {
        return sortOption;
    }

    public void setSortOption(SortOption sortOption) {
        this.sortOption = sortOption;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public int getEtAl() {
        return etAl;
    }

    public void setEtAl(int etAl) {
        this.etAl = etAl;
    }

    public DSpaceObject getScope() {
        return scope;
    }

    public void setScope(DSpaceObject dso) {
        if (dso.getType() == Constants.COLLECTION || dso.getType() == Constants.COMMUNITY) {
            this.scope = dso;
        }
    }
}
