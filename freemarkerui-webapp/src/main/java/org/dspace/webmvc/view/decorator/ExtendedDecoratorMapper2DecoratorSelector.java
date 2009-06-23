package org.dspace.webmvc.view.decorator;

import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.Decorator;
import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.SiteMeshContext;
import com.opensymphony.sitemesh.compatability.Content2HTMLPage;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;
import com.opensymphony.sitemesh.webapp.decorator.NoDecorator;

public class ExtendedDecoratorMapper2DecoratorSelector implements DecoratorSelector {

    private final DecoratorMapper decoratorMapper;

    public ExtendedDecoratorMapper2DecoratorSelector(DecoratorMapper decoratorMapper) {
        this.decoratorMapper = decoratorMapper;
    }

    public Decorator selectDecorator(Content content, SiteMeshContext context) {
        SiteMeshWebAppContext webAppContext = (SiteMeshWebAppContext) context;
        com.opensymphony.module.sitemesh.Decorator decorator =
                decoratorMapper.getDecorator(webAppContext.getRequest(), new Content2HTMLPage(content));
        if (decorator == null) {
            return new NoDecorator();
        } else {
            return new ExtendedOldDecorator2NewDecorator(decorator);
        }
    }
}
