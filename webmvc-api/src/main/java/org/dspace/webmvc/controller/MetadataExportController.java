/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller;

import org.springframework.stereotype.Controller;
import org.apache.log4j.Logger;
import org.dspace.webmvc.utils.WebMVCManager;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.*;
import org.dspace.content.DSpaceObject;
import org.dspace.content.ItemIterator;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.handle.HandleManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.dspace.app.bulkedit.MetadataExport;
import org.dspace.app.bulkedit.DSpaceCSV;

/**
 *
 * @author Robert Qin
 */
@Controller
public class MetadataExportController {
    
     /** log4j category */
    private static Logger log = Logger.getLogger(MetadataExportController.class);
    
    @RequestMapping(method = RequestMethod.POST)
    protected void processPost(Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {
        // Get the handle requested for the export
        String handle = request.getParameter("handle");
        MetadataExport exporter = null;
        if (handle != null)
        {
            log.info(LogManager.getHeader(context, "metadataexport", "exporting_handle:" + handle));
            DSpaceObject thing = HandleManager.resolveToObject(context, handle);
            if (thing != null)
            {
                if (thing.getType() == Constants.ITEM)
                {
                    List<Integer> item = new ArrayList<Integer>();
                    item.add(thing.getID());
                    exporter = new MetadataExport(context, new ItemIterator(context, item), false);
                }
                else if (thing.getType() == Constants.COLLECTION)
                {
                    Collection collection = (Collection)thing;
                    ItemIterator toExport = collection.getAllItems();
                    exporter = new MetadataExport(context, toExport, false);
                }
                else if (thing.getType() == Constants.COMMUNITY)
                {
                    exporter = new MetadataExport(context, (Community)thing, false);
                }

                if (exporter != null)
                {
                    // Perform the export
                    DSpaceCSV csv = exporter.export();

                    // Return the csv file
                    response.setContentType("text/csv; charset=UTF-8");
                    String filename = handle.replaceAll("/", "-") + ".csv";
                    response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                    PrintWriter out = response.getWriter();
                    out.write(csv.toString());
                    out.flush();
                    out.close();
                    log.info(LogManager.getHeader(context, "metadataexport", "exported_file:" + filename));
                    return;
                }
            }
        }

        // Something has gone wrong
        WebMVCManager.showIntegrityError(request, response);
    }
    
    
}
