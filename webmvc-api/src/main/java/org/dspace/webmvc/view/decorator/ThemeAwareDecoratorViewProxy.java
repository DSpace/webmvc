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

import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;
import com.opensymphony.sitemesh.webapp.ContainerTweaks;
import com.opensymphony.sitemesh.webapp.ContentBufferingResponse;
import com.opensymphony.sitemesh.compatability.PageParser2ContentProcessor;
import com.opensymphony.sitemesh.compatability.HTMLPage2Content;
import com.opensymphony.sitemesh.ContentProcessor;
import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.parser.FastPage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Map;
import java.util.Enumeration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.dspace.webmvc.theme.SpringThemeContextUtils;

/**
 * Proxy that wraps a Spring WebMVC view, to apply SiteMesh Decorators
 * Includes awareness about themes, allowing us to apply themes based on decorator configuration
 */
@Aspect
public class ThemeAwareDecoratorViewProxy extends WebApplicationObjectSupport implements InitializingBean {
    private ContainerTweaks containerTweaks = new ContainerTweaks();
    private DummyFilterConfig filterConfig = new DummyFilterConfig();

    private ContentProcessor contentProcessor;
    private DecoratorSelector decoratorSelector;

    @Override
    protected void initServletContext(ServletContext servletContext) {
        filterConfig.setServletContext(servletContext);
        super.initServletContext(servletContext);
    }

    public void afterPropertiesSet() throws Exception {
        // TODO: Remove heavy coupling on horrible SM2 Factory
        Factory factory = Factory.getInstance(new Config(filterConfig));
        factory.refresh();

        contentProcessor  = new PageParser2ContentProcessor(factory);
        decoratorSelector = new ThemeAwareDecoratorMapper2DecoratorSelector(factory.getDecoratorMapper());
    }

    /**
     * Wrapping method that intercepts calls to a View's render method
     *
     * @param pjp
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */
//    @ -- Pointcut("execution(* org.springframework.web.servlet.View.render(..)) and args(model,request,response)")
    @Around("execution(* org.springframework.web.servlet.View.render(..)) and args(model,request,response)")
    public void render(ProceedingJoinPoint pjp, Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // Create the SiteMesh Context
        SiteMeshWebAppContext webAppContext = new SiteMeshWebAppContext(request, response, filterConfig.getServletContext());

        if (!contentProcessor.handles(webAppContext)) {
            // Optimization: If the content doesn't need to be processed, bypass SiteMesh.
            try {
                pjp.proceed(new Object[] {model, request, response});
            } catch (Throwable throwable) {
                // TODO deal with exception
            }
            return;
        }

        if (containerTweaks.shouldAutoCreateSession()) {
            // Some containers (such as Tomcat 4) will not allow sessions to be created in the decorator.
            // (i.e after the response has been committed).
            request.getSession(true);
        }

        try {
            // If we have a theme name for the decorator, set it as the theme for this context
            String themeName = resolveThemeName(decoratorSelector, webAppContext);
            if (themeName != null) {
                SpringThemeContextUtils.setThemeName(themeName, request, response);
            }

            // Create a buffer for the view rendering
            ContentBufferingResponse contentBufferingResponse = new ContentBufferingResponse(response, contentProcessor, webAppContext);

            // Call the wrapped view render, capturing the output in the buffer
            pjp.proceed(new Object[] {model, request, contentBufferingResponse});

            webAppContext.setUsingStream(contentBufferingResponse.isUsingStream());

            // Get the content that was rendered
            Content content = contentBufferingResponse.getContent();

            // If there was no content, abort now
            if (content == null) {
                return;
            }

            // Select a decorator for this request
            Decorator decorator = decoratorSelector.selectDecorator(content, webAppContext);
            if (decorator instanceof ThemeAwareChainingDecorator) {
                ((ThemeAwareChainingDecorator)decorator).setContentProcessor(contentProcessor);
            }

            // Decorate the captured content
            decorator.render(content, webAppContext);

        } catch (IllegalStateException e) {
            // Some containers (such as WebLogic) throw an IllegalStateException when an error page is served.
            // It may be ok to ignore this. However, for safety it is propegated if possible.
            if (!containerTweaks.shouldIgnoreIllegalStateExceptionOnErrorPage()) {
                throw e;
            }
        } catch (RuntimeException e) {
            if (containerTweaks.shouldLogUnhandledExceptions()) {
                // Some containers (such as Tomcat 4) swallow RuntimeExceptions in filters.
                filterConfig.getServletContext().log("Unhandled exception occurred whilst decorating page", e);
            }
            throw e;
        } catch (ServletException e) {
            throw e;
        } catch (Throwable throwable) {
            // TODO exception handling
        }
    }

    private String resolveThemeName(DecoratorSelector decoratorSelector, SiteMeshWebAppContext webAppContext) {
        Decorator testDecorator = decoratorSelector.selectDecorator(new HTMLPage2Content(new FastPage(null, null, null, null, null, null, null, false)), webAppContext);
        if (testDecorator instanceof ThemeAwareChainingDecorator) {
            ThemeAwareChainingDecorator tad = (ThemeAwareChainingDecorator)testDecorator;
                return tad.resolveThemeName();
        }

        return null;
    }

    private static class DummyFilterConfig implements FilterConfig {
        private ServletContext servletContext;
        private String configFile = null;

        DummyFilterConfig() {
        }

        void setServletContext(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        void setConfigFile(String configFile) {
            this.configFile  = configFile;
        }

        public String getFilterName() {
            return null;
        }

        public ServletContext getServletContext() {
            return servletContext;
        }

        public String getInitParameter(String name) {
            if ("configFile".equals(name)) {
                return configFile;
            }

            return null;
        }

        public Enumeration getInitParameterNames() {
            return null;
        }
    }
}

/* AOP Configuration for the Spring xml - replaced with annotations above
 *
 * <!-- aop:config>
 *    <aop:aspect ref="viewProxy">
 *        <aop:pointcut id="executeRender" expression="execution(* org.springframework.web.servlet.View.render(..)) and args(model,request,response)" />
 *
 *        <aop:around pointcut-ref="executeRender" method="render" />
 *
 *    </aop:aspect>
 *</aop:config -->
 */