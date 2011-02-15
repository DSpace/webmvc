package org.dspace.search;

public class AdvancedField {
    private String conjunction;
    private String field;
    private String query;

    public AdvancedField(String conjunction, String field, String query) {
        this.conjunction = conjunction;
        this.field = field;
        this.query = query;
    }

    public String getConjunction() {
        return conjunction;
    }

    public void setConjunction(String conjunction) {
        this.conjunction = conjunction;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}