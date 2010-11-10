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
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.JarURLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.GZIPOutputStream;

/**
 * Handles requests to static resources (images, css, etc.)
 *
 * This allows all such requests to be mapped into the Spring WebMVC, instead
 * of being handled by the container's default servlet.
 *
 * Further, this controller will search locations on the classpath, so that
 * content can be served from within JAR files.
 */
public class ResourceController extends AbstractController
{
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        if ("HEAD".equals(request.getMethod()))
            lookup(request).respondHead(response);
        else
            lookup(request).respondGet(response);

        // Responded to request, so don't pass to a view
        return null;
    }

    protected LookupResult lookup(HttpServletRequest req)
    {
        LookupResult r = (LookupResult)req.getAttribute("lookupResult");
        if (r == null)
        {
            r = lookupNoCache(req);
            req.setAttribute("lookupResult", r);
        }
        
        return r;
    }

    protected LookupResult lookupNoCache(HttpServletRequest req)
    {
        final String path = getPath(req);
        if (isForbidden(path))
            return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden");

        final URL url;
        try
        {
            url = getServletContext().getResource(path);
        }
        catch (MalformedURLException e)
        {
            return new Error(HttpServletResponse.SC_BAD_REQUEST, "Malformed path");
        }

        final String mimeType = getMimeType(path);

        final String realpath = getServletContext().getRealPath(path);
        if (url != null && realpath != null)
        {
            // Try as an ordinary file
            File f = new File(realpath);
            if (!f.isFile())
                return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            else
                return new StaticFile(f.lastModified(),mimeType,(int)f.length(),acceptsDeflate(req),url);
        }
        else
        {
            ClassPathResource cpr = new ClassPathResource(path);
            if (cpr.exists())
            {
                URL cprURL = null;
                try
                {
                    cprURL = cpr.getURL();

                    // Try as a JAR Entry
                    final ZipEntry ze = ((JarURLConnection)cprURL.openConnection()).getJarEntry();
                    if (ze != null)
                    {
                        if(ze.isDirectory())
                            return new Error(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                        else
                            return new StaticFile(ze.getTime(),mimeType,(int)ze.getSize(),acceptsDeflate(req),cprURL);
                    }
                    else
                        // Unexpected?
                        return new StaticFile(-1,mimeType,-1,acceptsDeflate(req),cprURL);
                }
                catch (ClassCastException e)
                {
                    // Unknown resource type
                    if (url != null)
                        return new StaticFile(-1,mimeType,-1,acceptsDeflate(req),cprURL);
                    else
                        return new Error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
                }
                catch (IOException e)
                {
                    return new Error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
                }
            }
            else
            {
                return new Error(HttpServletResponse.SC_NOT_FOUND, "Not found");
            }
        }
    }

    protected String getPath(HttpServletRequest req)
    {
        String servletPath = req.getServletPath();
        return servletPath + (StringUtils.isEmpty(req.getPathInfo()) ? "" : req.getPathInfo());
    }

    protected boolean isForbidden(String path)
    {
        String lpath = path.toLowerCase();
        return lpath.startsWith("/web-inf/") || lpath.startsWith("/meta-inf/");
    }

    protected String getMimeType(String path)
    {
        return (StringUtils.isEmpty(getServletContext().getMimeType(path)) ? "application/octet-stream" : getServletContext().getMimeType(path));
    }

    protected static boolean acceptsDeflate(HttpServletRequest req)
    {
        final String ae = req.getHeader("Accept-Encoding");
        return ae != null && ae.contains("gzip");
    }

    protected static boolean deflatable(String mimetype)
    {
        return mimetype.startsWith("text/")
            || mimetype.equals("application/postscript")
            || mimetype.startsWith("application/ms")
            || mimetype.startsWith("application/vnd")
            || mimetype.endsWith("xml");
    }

    protected static final int deflateThreshold = 4*1024;

    protected static final int bufferSize = 4*1024;

    protected static void transferStreams(InputStream is, OutputStream os) throws IOException
    {
        try
        {
            byte[] buf = new byte[bufferSize];
            int bytesRead;
            while ((bytesRead = is.read(buf)) != -1)
                os.write(buf, 0, bytesRead);
        }
        finally
        {
            is.close();
            os.close();
        }
    }

    public static interface LookupResult
    {
        public void respondGet(HttpServletResponse resp) throws IOException;
        public void respondHead(HttpServletResponse resp);
        public long getLastModified();
    }

    public static class Error implements LookupResult
    {
        protected final int statusCode;
        protected final String message;

        public Error(int statusCode, String message)
        {
            this.statusCode = statusCode;
            this.message = message;
        }

        public long getLastModified()
        {
            return -1;
        }

        public void respondGet(HttpServletResponse resp) throws IOException
        {
            resp.sendError(statusCode,message);
        }

        public void respondHead(HttpServletResponse resp)
        {
            throw new UnsupportedOperationException();
        }
    }

    public static class StaticFile implements LookupResult
    {
        protected final long lastModified;
        protected final String mimeType;
        protected final int contentLength;
        protected final boolean acceptsDeflate;
        protected final URL url;

        public StaticFile(long lastModified, String mimeType, int contentLength, boolean acceptsDeflate, URL url)
        {
            this.lastModified = lastModified;
            this.mimeType = mimeType;
            this.contentLength = contentLength;
            this.acceptsDeflate = acceptsDeflate;
            this.url = url;
        }

        public long getLastModified()
        {
            return lastModified;
        }

        protected boolean willDeflate()
        {
            return acceptsDeflate && deflatable(mimeType) && contentLength >= deflateThreshold;
        }

        protected void setHeaders(HttpServletResponse resp)
        {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType(mimeType);
            if(contentLength >= 0 && !willDeflate())
                resp.setContentLength(contentLength);
        }

        public void respondGet(HttpServletResponse resp) throws IOException
        {
            setHeaders(resp);
            final OutputStream os;
            if(willDeflate())
            {
                resp.setHeader("Content-Encoding", "gzip");
                os = new GZIPOutputStream(resp.getOutputStream(), bufferSize);
            }
            else
                os = resp.getOutputStream();

            transferStreams(url.openStream(),os);
        }

        public void respondHead(HttpServletResponse resp)
        {
            if(willDeflate())
                throw new UnsupportedOperationException();
            
            setHeaders(resp);
        }
    }
}
