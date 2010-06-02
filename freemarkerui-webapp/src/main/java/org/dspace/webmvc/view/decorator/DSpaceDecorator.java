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
import com.opensymphony.module.sitemesh.mapper.DefaultDecorator;
import com.opensymphony.sitemesh.ContentProcessor;

import java.util.Map;

public class DSpaceDecorator extends DefaultDecorator implements ExtendedDecorator {
    private String themeName;
    private Decorator parentDecorator;
    private Decorator[] chainedDecorators;

	/** Constructor to set name, page and parameters. */
	public DSpaceDecorator(String name, String page, Map parameters) {
        super(name, page, parameters);
	}

	/** Constructor to set all properties. */
	public DSpaceDecorator(String name, String page, String uriPath, Map parameters) {
        super(name, page, uriPath, null, parameters);
	}

    /** Constructor to set all properties. */
	public DSpaceDecorator(String name, String page, String uriPath, String role, Map parameters) {
        super(name, page, uriPath, role, parameters);
	}

    public void setParentDecorator(Decorator parent) {
        parentDecorator = parent;
    }

    public void setChainedDecorators(Decorator[] chain) {
        chainedDecorators = chain;
    }

    public void setThemeName(String name) {
        themeName = name;
    }

    public Decorator[] getChainedDecorators() {
        return chainedDecorators;
    }

    public String resolveThemeName() {
        // First, if we have a local theme definition, return it.
        if (themeName != null) {
            return themeName;
        }

        // If not, try our ancestor(s)
        if (parentDecorator != null) {
            if (parentDecorator instanceof ExtendedDecorator) {
                return ((ExtendedDecorator)parentDecorator).resolveThemeName();
            }
        }

        // Finally, try the rest of the decorator chain
        if (chainedDecorators != null) {
            for (Decorator decorator : chainedDecorators) {
                if (decorator instanceof ExtendedDecorator) {
                    return ((ExtendedDecorator)decorator).resolveThemeName();
                }
            }
        }
        
        return null;
    }

    public void setContentProcessor(ContentProcessor processor) {
        // Not needed
    }

    @Override
    public String getPage() {
        String page = super.getPage();

        if (page != null) {
            return page;
        }

        if (parentDecorator != null) {
            return parentDecorator.getPage();
        }

        return null;
    }

    @Override
    public String getName() {
        String name = super.getName();

        if (name != null) {
            return name;
        }

        if (parentDecorator != null) {
            return parentDecorator.getName();
        }

        return null;
    }

    @Override
    public String getURIPath() {
        String URIPath = super.getURIPath();

        if (URIPath != null) {
            return URIPath;
        }

        if (parentDecorator != null) {
            return parentDecorator.getURIPath();
        }

        return null;
    }

    @Override
    public String getRole() {
        String role = super.getRole();

        if (role != null) {
            return role;
        }

        if (parentDecorator != null) {
            return parentDecorator.getRole();
        }

        return null;
    }
}