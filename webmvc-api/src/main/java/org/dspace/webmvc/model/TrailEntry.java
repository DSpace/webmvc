package org.dspace.webmvc.model;

import org.dspace.content.DSpaceObject;

public class TrailEntry {
    private String url;

    private String key;
    private String[] params;

    private String label;

    private DSpaceObject dso;

    private TrailEntry() {

    }

    public static TrailEntry createWithLabel(String label) {
        return createWithLabel(label, null);
    }

    public static TrailEntry createWithLabel(String label, String url) {
        TrailEntry entry = new TrailEntry();
        entry.label = label;
        entry.url = url;
        return entry;
    }

    public static TrailEntry createWithKey(String key) {
        return createWithKey(key, null, null);
    }

    public static TrailEntry createWithKey(String key, String url) {
        return createWithKey(key, null, url);
    }

    public static TrailEntry createWithKey(String key, String[] params) {
        return createWithKey(key, params, null);
    }

    public static TrailEntry createWithKey(String key, String[] params, String url) {
        TrailEntry entry = new TrailEntry();
        entry.key = key;
        entry.params = params;
        entry.url = url;
        return entry;
    }


    public static TrailEntry createWithDSpaceObject(DSpaceObject dso) {
        TrailEntry entry = new TrailEntry();
        entry.dso = dso;
        return entry;
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public String getKey() {
        return key;
    }

    public String[] getParams() {
        return params;
    }

    public DSpaceObject getDSpaceObject() {
        return dso;
    }
}
