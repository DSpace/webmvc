package org.dspace.webmvc.view.helpers;

import org.dspace.browse.BrowseItem;
import org.dspace.content.DCValue;
import org.dspace.content.Item;

import java.sql.SQLException;

public class MetadataHelper {
    public DCValue[] getMetadata(Item item, String field) {
        return item.getMetadata(field);
    }

    public DCValue[] getMetadata(Item item, String schema, String element, String qualifier, String language) {
        return item.getMetadata(schema, element, qualifier, language);
    }

    public DCValue[] getMetadata(BrowseItem item, String field) {
        String[] mdArray = field.split("\\.", 3);
        switch (mdArray.length) {
            case 3:
                return getMetadata(item, mdArray[0], mdArray[1], mdArray[2], Item.ANY);

            case 2:
                return getMetadata(item, mdArray[0], mdArray[1], null, Item.ANY);

            default:
                return null;
        }
    }

    public DCValue[] getMetadata(BrowseItem item, String schema, String element, String qualifier, String language) {
        try {
            return item.getMetadata(schema, element, qualifier, language);
        } catch (SQLException e) {
            return null;
        }
    }
}
