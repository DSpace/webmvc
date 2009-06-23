package org.dspace.webmvc.view;

import org.dspace.utils.DSpace;
import org.dspace.services.model.StorageEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: graham
 * Date: Apr 17, 2009
 * Time: 11:27:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityViewHelper {
    private DSpace dspace = new DSpace();

    public List<StorageEntity> getEntities() {
        return getEntities("/");
    }

    public List<StorageEntity> getEntities(String path) {
        return dspace.getStorageService().getEntities(path);
    }
}
