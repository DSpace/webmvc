package org.dspace.webmvc.processor;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.DSpaceObject;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class HandleRequestProcessor {
    private Context context;
    private HttpServletRequest request;
    private boolean pathParsed = false;

    private String handle;
    private String extraPathInfo;

    private DSpaceObject dspaceObject;

    public HandleRequestProcessor(Context pContext, HttpServletRequest pRequest) {
        context = pContext;
        request = pRequest;
    }

    public DSpaceObject getObject() throws SQLException {
        if (dspaceObject == null) {
            String handle = getHandle();
            if (!StringUtils.isEmpty(handle)) {
                dspaceObject = HandleManager.resolveToObject(context, handle);
            }
        }

        return dspaceObject;
    }

    public String getHandle() {
        if (!pathParsed) {
            parsePath();
        }

        return handle;
    }

    private void parsePath() {
        if (!pathParsed) {
            String path = request.getRequestURI();

            if (path != null) {
                if (path.startsWith("/handle/")) {
                    path = path.substring(8);
                } else if (path.contains("/handle/")) {
                    path = path.substring(path.indexOf("/handle/") + 8);
                } else if (path.startsWith("/" + ConfigurationManager.getProperty("handle.prefix"))) {
                    // substring(1) is to remove initial '/'
                    path = path.substring(1);
                } else {
                    pathParsed = true;
                    return;
                }

                try {
                    // Extract the Handle
                    int firstSlash = path.indexOf('/');
                    int secondSlash = path.indexOf('/', firstSlash + 1);

                    if (secondSlash != -1) {
                        // We have extra path info
                        handle = path.substring(0, secondSlash);
                        extraPathInfo = path.substring(secondSlash);
                    }
                    else {
                        // The path is just the Handle
                        handle = path;
                        extraPathInfo = null;
                    }
                }
                catch (NumberFormatException nfe) {
                    // Leave handle as null
                }
            }
            pathParsed = true;
        }
    }
}
