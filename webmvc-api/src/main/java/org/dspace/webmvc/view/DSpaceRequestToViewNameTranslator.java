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

package org.dspace.webmvc.view;

import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Translates a request to a view name.
 * Hook point to allow for the choice of view to be determined outside of the controller.
 * Possible use would be to choose between a gallery or article list template for a given collection.
 */
public class DSpaceRequestToViewNameTranslator extends DefaultRequestToViewNameTranslator
{
    /**
     * Returns the name of the view to use for this request.
     *
     * @param request
     * @return
     */
    @Override
    public String getViewName(HttpServletRequest request)
    {
        // First, do the standard processing
        String viewName = super.getViewName(request);

        // If we have a view name, return it
        if (!StringUtils.isEmpty(viewName)) {
            return viewName;
        }

        // No view name, so default to 'home'
        return "home";
    }
}
