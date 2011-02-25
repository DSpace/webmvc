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

import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.DSpaceObject;
import org.dspace.core.*;
import org.dspace.webmvc.utils.DSpaceRequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.sql.SQLException;

@Controller
public class BitstreamController {

    @RequestMapping
    protected void deliverBitstream(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int threshold = ConfigurationManager.getIntProperty("webui.content_disposition_threshold");
        boolean displayLicense = ConfigurationManager.getBooleanProperty("webui.licence_bundle.show", false);

        BitstreamRequestProcessor brp = new BitstreamRequestProcessor(DSpaceRequestUtils.getDSpaceContext(request), request);

        Bitstream bitstream = brp.getBitstream();

        boolean isLicense = false;
        if (bitstream != null) {
            // Check whether we got a License and if it should be displayed
            // (Note: list of bundles may be empty array, if a bitstream is a Community/Collection logo)
            Bundle bundle = bitstream.getBundles().length>0 ? bitstream.getBundles()[0] : null;
            if (bundle!=null && bundle.getName().equals(Constants.LICENSE_BUNDLE_NAME) && bitstream.getName().equals(Constants.LICENSE_BITSTREAM_NAME)) {
                isLicense = true;
            }

//            if (isLicense && !displayLicense && !AuthorizeManager.isAdmin(context)) {
//                throw new AuthorizeException();
//            }

            // Pipe the bits
            InputStream is = bitstream.retrieve();

            // Set the response MIME type
            response.setContentType(bitstream.getFormat().getMIMEType());

            // Response length
            response.setHeader("Content-Length", String.valueOf(bitstream.getSize()));

//    		if (threshold != -1 && bitstream.getSize() >= threshold) {
//    			UIUtil.setBitstreamDisposition(bitstream.getName(), request, response);
//    		}

            Utils.bufferedCopy(is, response.getOutputStream());
            is.close();
            response.getOutputStream().flush();
        }
    }

    static class BitstreamRequestProcessor {
        private Context context;
        private HttpServletRequest request;
        private boolean pathParsed = false;

        private String handle;
        private Integer bitstreamId;
        private String extraPathInfo;

        private DSpaceObject dspaceObject;

        BitstreamRequestProcessor(Context pContext, HttpServletRequest pRequest) {
            context = pContext;
            request = pRequest;
        }

        Bitstream getBitstream() {
            if (!pathParsed) {
                parsePath();
            }

            if (bitstreamId != null) {
                try {
                    return Bitstream.find(context, bitstreamId);
                } catch (SQLException e) {
                }
            }

            if (handle != null) {

            }

            return null;
        }

        private void parsePath() {
            if (!pathParsed) {
                String path = request.getRequestURI();

                if (path != null) {
                    if (path.contains("bitstream")) {
                        if (path.startsWith("/bitstream/")) {
                            path = path.substring(11);
                        } else if (path.contains("/bitstream/")) {
                            path = path.substring(path.indexOf("/bitstream/") + 11);
                        }

                        // Extract the Handle
                        int firstSlash = path.indexOf('/');
                        int secondSlash = path.indexOf('/', firstSlash + 1);

                        if (secondSlash != -1) {
                            // We have extra path info
                            handle = path.substring(0, secondSlash);
                            extraPathInfo = path.substring(secondSlash);
                        }
                        else {
                            // The path is just the Handle
                            handle = path;
                            extraPathInfo = null;
                        }
                    } else {
                        if (path.startsWith("/retrieve/")) {
                            path = path.substring(10);
                        } else if (path.contains("/retrieve/")) {
                            path = path.substring(path.indexOf("/retrieve/") + 10);
                        }

                        // Extract the id
                        int firstSlash = path.indexOf('/');

                        if (firstSlash != -1) {
                            // We have extra path info
                            bitstreamId = Integer.parseInt(path.substring(0, firstSlash));
                            extraPathInfo = path.substring(firstSlash);
                        }
                        else {
                            // The path is just the Handle
                            bitstreamId = Integer.parseInt(path);
                            extraPathInfo = null;
                        }
                    }

                }

                pathParsed = true;
            }
        }
    }
}
