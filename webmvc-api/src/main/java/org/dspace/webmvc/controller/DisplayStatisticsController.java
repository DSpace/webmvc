/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller;

import java.io.IOException;
import java.util.List;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import org.dspace.authorize.AuthorizeException;

import org.apache.log4j.Logger;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.eperson.Group;

import org.dspace.content.DSpaceObject;
import org.dspace.handle.HandleManager;

import org.dspace.statistics.Dataset;
import org.dspace.statistics.content.DatasetDSpaceObjectGenerator;
import org.dspace.statistics.content.DatasetTimeGenerator;
import org.dspace.statistics.content.DatasetTypeGenerator;
import org.dspace.statistics.content.StatisticsDataVisits;
import org.dspace.statistics.content.StatisticsListing;
import org.dspace.statistics.content.StatisticsTable;

import org.dspace.webmvc.components.StatisticsBean;
import org.springframework.web.bind.annotation.*;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
/**
 *
 * @author AdminNUS
 */
@Controller
public class DisplayStatisticsController {

    /** log4j logger */
    private static Logger log = Logger.getLogger(DisplayStatisticsController.class);

    @RequestMapping(method = RequestMethod.GET)
    protected String processGet(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        // is the statistics data publically viewable?
	boolean privatereport = ConfigurationManager.getBooleanProperty("statistics.item.authorization.admin");

        // is the user a member of the Administrator (1) group?
        boolean admin = Group.isMember(context, 1);

        if (!privatereport || admin)
        {
            return displayStatistics(context, model, request, response);
        }
        else
        {
            throw new AuthorizeException();
        }

    }//end processGet
    
    protected String displayStatistics(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException
    {


        String handle = request.getParameter("handle");
        DSpaceObject dso = HandleManager.resolveToObject(context, handle);

	if(dso == null) {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	        //JSPManager.showJSP(request, response, "/error/404.jsp");
                return "/error/404";
	}

        boolean isItem = false;

        StatisticsBean statsVisits = new StatisticsBean();
        StatisticsBean statsMonthlyVisits = new StatisticsBean();
        StatisticsBean statsFileDownloads = new StatisticsBean();
        StatisticsBean statsCountryVisits = new StatisticsBean();
        StatisticsBean statsCityVisits = new StatisticsBean();

        try
        {
            StatisticsListing statListing = new StatisticsListing(
                                            new StatisticsDataVisits(dso));

            statListing.setTitle("Total Visits");
            statListing.setId("list1");

            DatasetDSpaceObjectGenerator dsoAxis = new DatasetDSpaceObjectGenerator();
            dsoAxis.addDsoChild(dso.getType(), 10, false, -1);
            statListing.addDatasetGenerator(dsoAxis);
            Dataset dataset = statListing.getDataset(context);

            dataset = statListing.getDataset();

            if (dataset == null)
            {
		
		dataset = statListing.getDataset(context);
            }

            if (dataset != null)
            {
                String[][] matrix = dataset.getMatrixFormatted();
                List<String> colLabels = dataset.getColLabels();
                List<String> rowLabels = dataset.getRowLabels();

                statsVisits.setMatrix(matrix);
                statsVisits.setColLabels(colLabels);
                statsVisits.setRowLabels(rowLabels);
            }


	} catch (Exception e)
        {
		log.error(
                    "Error occured while creating statistics for dso with ID: "
                            + dso.getID() + " and type " + dso.getType()
                            + " and handle: " + dso.getHandle(), e);
	}
        

	try
        {

            StatisticsTable statisticsTable = new StatisticsTable(new StatisticsDataVisits(dso));

            statisticsTable.setTitle("Total Visits Per Month");
            statisticsTable.setId("tab1");

            DatasetTimeGenerator timeAxis = new DatasetTimeGenerator();
            timeAxis.setDateInterval("month", "-6", "+1");
            statisticsTable.addDatasetGenerator(timeAxis);

            DatasetDSpaceObjectGenerator dsoAxis = new DatasetDSpaceObjectGenerator();
            dsoAxis.addDsoChild(dso.getType(), 10, false, -1);
            statisticsTable.addDatasetGenerator(dsoAxis);
            Dataset dataset = statisticsTable.getDataset(context);

            dataset = statisticsTable.getDataset();

            if (dataset == null)
            {
		
		dataset = statisticsTable.getDataset(context);
            }

            if (dataset != null)
            {
                String[][] matrix = dataset.getMatrixFormatted();
                List<String> colLabels = dataset.getColLabels();
                List<String> rowLabels = dataset.getRowLabels();

                statsMonthlyVisits.setMatrix(matrix);
                statsMonthlyVisits.setColLabels(colLabels);
                statsMonthlyVisits.setRowLabels(rowLabels);
            }
	} catch (Exception e)
        {
            log.error(
                "Error occured while creating statistics for dso with ID: "
                                + dso.getID() + " and type " + dso.getType()
                                + " and handle: " + dso.getHandle(), e);
	}

        if(dso instanceof org.dspace.content.Item)
        {
            isItem = true;

            try
            {

                StatisticsListing statisticsTable = new StatisticsListing(new StatisticsDataVisits(dso));

                statisticsTable.setTitle("File Downloads");
                statisticsTable.setId("tab1");

                DatasetDSpaceObjectGenerator dsoAxis = new DatasetDSpaceObjectGenerator();
                dsoAxis.addDsoChild(Constants.BITSTREAM, 10, false, -1);
                statisticsTable.addDatasetGenerator(dsoAxis);

                Dataset dataset = statisticsTable.getDataset(context);

                dataset = statisticsTable.getDataset();

                if (dataset == null)
                {

                    dataset = statisticsTable.getDataset(context);
                }

                if (dataset != null)
                {
                    String[][] matrix = dataset.getMatrixFormatted();
                    List<String> colLabels = dataset.getColLabels();
                    List<String> rowLabels = dataset.getRowLabels();

                    statsFileDownloads.setMatrix(matrix);
                    statsFileDownloads.setColLabels(colLabels);
                    statsFileDownloads.setRowLabels(rowLabels);
                }
            }
            catch (Exception e)
            {
                log.error(
                    "Error occured while creating statistics for dso with ID: "
                                    + dso.getID() + " and type " + dso.getType()
                                    + " and handle: " + dso.getHandle(), e);
            }
        }

        try
            {

                StatisticsListing statisticsTable = new StatisticsListing(new StatisticsDataVisits(dso));

                statisticsTable.setTitle("Top country views");
                statisticsTable.setId("tab1");

                DatasetTypeGenerator typeAxis = new DatasetTypeGenerator();
                typeAxis.setType("countryCode");
                typeAxis.setMax(10);
                statisticsTable.addDatasetGenerator(typeAxis);

                Dataset dataset = statisticsTable.getDataset(context);

                dataset = statisticsTable.getDataset();

                if (dataset == null)
                {

                    dataset = statisticsTable.getDataset(context);
                }

                if (dataset != null)
                {
                    String[][] matrix = dataset.getMatrixFormatted();
                    List<String> colLabels = dataset.getColLabels();
                    List<String> rowLabels = dataset.getRowLabels();

                    statsCountryVisits.setMatrix(matrix);
                    statsCountryVisits.setColLabels(colLabels);
                    statsCountryVisits.setRowLabels(rowLabels);
                }
            }
            catch (Exception e)
            {
                log.error(
                    "Error occured while creating statistics for dso with ID: "
                                    + dso.getID() + " and type " + dso.getType()
                                    + " and handle: " + dso.getHandle(), e);
            }

        try
            {

                StatisticsListing statisticsTable = new StatisticsListing(new StatisticsDataVisits(dso));

                statisticsTable.setTitle("Top city views");
                statisticsTable.setId("tab1");

                DatasetTypeGenerator typeAxis = new DatasetTypeGenerator();
                typeAxis.setType("city");
                typeAxis.setMax(10);
                statisticsTable.addDatasetGenerator(typeAxis);

                Dataset dataset = statisticsTable.getDataset(context);

                dataset = statisticsTable.getDataset();

                if (dataset == null)
                {

                    dataset = statisticsTable.getDataset(context);
                }

                if (dataset != null)
                {
                    String[][] matrix = dataset.getMatrixFormatted();
                    List<String> colLabels = dataset.getColLabels();
                    List<String> rowLabels = dataset.getRowLabels();

                    statsCityVisits.setMatrix(matrix);
                    statsCityVisits.setColLabels(colLabels);
                    statsCityVisits.setRowLabels(rowLabels);
                }
            }
            catch (Exception e)
            {
                log.error(
                    "Error occured while creating statistics for dso with ID: "
                                    + dso.getID() + " and type " + dso.getType()
                                    + " and handle: " + dso.getHandle(), e);
            }
   
        model.addAttribute("statsVisits", statsVisits);
        model.addAttribute("statsVisitsMatrix", statsVisits.getMatrix());
        model.addAttribute("statsMonthlyVisits", statsMonthlyVisits);
        model.addAttribute("statsMonthlyVisitsCol", statsMonthlyVisits.getColLabels());
        model.addAttribute("statsMonthlyVisitsMatrix", statsMonthlyVisits.getMatrix());
        model.addAttribute("statsFileDownloads", statsFileDownloads);
        model.addAttribute("statsFileDownloadsMatrix", statsFileDownloads.getMatrix());
        model.addAttribute("statsCountryVisits",statsCountryVisits);
        model.addAttribute("statsCountryVisitsMatrix",statsCountryVisits.getMatrix());
        model.addAttribute("statsCityVisits", statsCityVisits);
        model.addAttribute("statsCityVisitsMatrix",statsCityVisits.getMatrix());
        model.addAttribute("isItem", isItem);

        // JSPManager.showJSP(request, response, "display-statistics.jsp");
        return "pages/display-statistics";
        
    }
}
