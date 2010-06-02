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

package org.dspace.webmvc.view.freemarker;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.IOException;

import freemarker.template.TemplateException;
import freemarker.template.Template;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;


public class I18nFreeMarkerView extends FreeMarkerView
{
    @Override
    protected void processTemplate(Template template, Map model, HttpServletResponse response) throws IOException, TemplateException
    {
        HttpRequestHashModel hrhm = (HttpRequestHashModel)model.get(FreemarkerServlet.KEY_REQUEST);
        if (hrhm != null)
        {
            HttpServletRequest request = hrhm.getRequest();
            if (request != null)
            {
                Locale locale = RequestContextUtils.getLocale(request);
                ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
                model.put("message", bundle);
            }
        }

        super.processTemplate(template, model, response);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
