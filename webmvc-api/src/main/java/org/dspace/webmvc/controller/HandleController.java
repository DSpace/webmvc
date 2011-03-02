/**
 * $Id: $
 * $URL: $
 * *************************************************************************
 * Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 * Licensed under the DuraSpace License.
 *
 * A copy of the DuraSpace License has been included in this
 * distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
 */

package org.dspace.webmvc.controller;

import org.apache.commons.lang.StringUtils;
import org.dspace.content.DSpaceObject;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.webmvc.utils.DSpaceRequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

@Controller
public class HandleController {

    @RequestMapping("/handle/**")
    public String processHandle(Context context, HttpServletRequest request) throws SQLException {
        String handle = null;
        String servletPath = request.getServletPath();

        if (servletPath != null) {
            if (servletPath.startsWith("/handle/")) {
                servletPath = servletPath.substring(8);
            }

            if (servletPath.startsWith(ConfigurationManager.getProperty("handle.prefix"))) {
                int handleEndPos = servletPath.indexOf("/", ConfigurationManager.getProperty("handle.prefix").length() + 1);
                if (handleEndPos > 0) {
                    if (handleEndPos + 1 < servletPath.length()) {
                        handle = servletPath.substring(0, handleEndPos);
                        servletPath = servletPath.substring(handleEndPos);
                    } else {
                        handle = servletPath.substring(0, handleEndPos);
                        servletPath = null;
                    }
                } else {
                    handle = servletPath;
                    servletPath = null;
                }
            }

            if (!StringUtils.isEmpty(handle)) {
                DSpaceObject dspaceObject = HandleManager.resolveToObject(context, handle);

                DSpaceRequestUtils.setScopeHandle(request, handle);
                DSpaceRequestUtils.setScopeObject(request, dspaceObject);

                if (!StringUtils.isEmpty(servletPath)) {
                    return "forward:" + servletPath;
                } else {
                    return "forward:/renderObject";
                }
            }
        }

        return "pages/invalidhandle";
    }
}
