package org.dspace.webmvc.view.decorator;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.sitemesh.ContentProcessor;

public interface ExtendedDecorator {
    Decorator[] getChainedDecorators();

    String resolveThemeName();

    void setContentProcessor(ContentProcessor processor);
}
