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

package org.dspace.webmvc.view.decorator;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.sitemesh.ContentProcessor;

public interface ExtendedDecorator {
    Decorator[] getChainedDecorators();

    String resolveThemeName();

    void setContentProcessor(ContentProcessor processor);
}
