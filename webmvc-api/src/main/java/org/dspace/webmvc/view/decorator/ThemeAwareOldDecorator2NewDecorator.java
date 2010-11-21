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
import com.opensymphony.module.sitemesh.RequestConstants;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.ContentProcessor;
import com.opensymphony.sitemesh.compatability.Content2HTMLPage;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;
import com.opensymphony.sitemesh.webapp.ContentBufferingResponse;
import com.opensymphony.sitemesh.webapp.decorator.BaseWebAppDecorator;
import com.opensymphony.sitemesh.webapp.decorator.NoDecorator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ThemeAwareOldDecorator2NewDecorator extends BaseWebAppDecorator implements RequestConstants, ThemeAwareChainingDecorator
{
    private static NoDecorator noDecorator = new NoDecorator();

    private ContentProcessor contentProcessor;
    private final Decorator oldDecorator;

    public ThemeAwareOldDecorator2NewDecorator(Decorator oldDecorator) {
        this.oldDecorator = oldDecorator;
    }

    /**
     * Apply this decorator
     *
     * @param content
     * @param request
     * @param response
     * @param servletContext
     * @param webAppContext
     * @throws IOException
     * @throws ServletException
     */
    protected void render(Content content, HttpServletRequest request, HttpServletResponse response,
                          ServletContext servletContext, SiteMeshWebAppContext webAppContext)
            throws IOException, ServletException {

        ThemeAwareChainingDecorator extendedDecorator = null;

        // If it's an ExtendedDecorator, cast for handle to the methods
        if (oldDecorator instanceof ThemeAwareChainingDecorator) {
            extendedDecorator = (ThemeAwareChainingDecorator)oldDecorator;
        }

        // If we have an ExtendedDecorator, and it has a chain of decorators to execute
        if (extendedDecorator != null && extendedDecorator.getChainedDecorators() != null) {
            ContentBufferingResponse contentBufferingResponse;

            // First, render the current decorator, if a template / page is provided
            // Capture the output to run through the chained decorators
            if (oldDecorator.getPage() != null) {
                contentBufferingResponse = new ContentBufferingResponse(response, contentProcessor, webAppContext);
                doRender(oldDecorator, content, request, contentBufferingResponse, servletContext, webAppContext);
                webAppContext.setUsingStream(contentBufferingResponse.isUsingStream());
                content = contentBufferingResponse.getContent();
            }

            // Go through the chain of decorators - except the last one
            // Render the decorator if a template / page is configured
            // and capture the output for the next decorator
            Decorator[] chainedDecorators = extendedDecorator.getChainedDecorators();
            for (int i = 0; content != null && i < chainedDecorators.length - 1; i++) {
                if (chainedDecorators[i].getPage() != null) {
                    contentBufferingResponse = new ContentBufferingResponse(response, contentProcessor, webAppContext);
                    doRender(chainedDecorators[i], content, request, contentBufferingResponse, servletContext, webAppContext);
                    webAppContext.setUsingStream(contentBufferingResponse.isUsingStream());
                    content = contentBufferingResponse.getContent();
                }
            }

            // For the last decorator, render it straight to output if it has a template / page
            // Otherwise, do the 'null' handling
            if (chainedDecorators[chainedDecorators.length - 1].getPage() != null) {
                doRender(chainedDecorators[chainedDecorators.length - 1], content, request, response, servletContext, webAppContext);
            } else {
                noDecorator.render(content, webAppContext);
            }
        } else {
            // Standard decorator or no chain, render it straight to output if it has a template / page
            // Otherwise, do the 'null' handling
            if (oldDecorator.getPage() != null) {
                doRender(oldDecorator, content, request, response, servletContext, webAppContext);
            } else {
                noDecorator.render(content, webAppContext);
            }
        }
    }

    /**
     * Apply the supplied decorator
     *
     * @param decorator
     * @param content
     * @param request
     * @param response
     * @param servletContext
     * @param webAppContext
     * @throws IOException
     * @throws ServletException
     */
    protected void doRender(Decorator decorator, Content content, HttpServletRequest request, HttpServletResponse response,
                          ServletContext servletContext, SiteMeshWebAppContext webAppContext)
            throws IOException, ServletException {

        request.setAttribute(PAGE, new Content2HTMLPage(content));

        // see if the URI path (webapp) is set
        if (decorator.getURIPath() != null) {
            // in a security conscious environment, the servlet container
            // may return null for a given URL
            if (servletContext.getContext(decorator.getURIPath()) != null) {
                servletContext = servletContext.getContext(decorator.getURIPath());
            }
        }

        // get the dispatcher for the decorator
        RequestDispatcher dispatcher = servletContext.getRequestDispatcher(decorator.getPage());
        dispatcher.include(request, response);

        request.removeAttribute(PAGE);
    }

    public Decorator[] getChainedDecorators() {
        if (oldDecorator instanceof ThemeAwareChainingDecorator) {
            return ((ThemeAwareChainingDecorator)oldDecorator).getChainedDecorators();
        }

        return null;
    }

    public String resolveThemeName() {
        if (oldDecorator instanceof ThemeAwareChainingDecorator) {
            return ((ThemeAwareChainingDecorator)oldDecorator).resolveThemeName();
        }

        return null;
    }

    public void setContentProcessor(ContentProcessor processor) {
        contentProcessor = processor;
    }
}
