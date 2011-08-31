/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller.admin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.dspace.app.util.AuthorizeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.webmvc.utils.UIUtil;
import org.dspace.app.util.Util;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.content.Bitstream;
import org.dspace.content.BitstreamFormat;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.FormatIdentifier;
import org.dspace.content.MetadataField;
import org.dspace.content.MetadataSchema;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.eperson.Group;
import org.dspace.eperson.EPerson;
import org.springframework.web.bind.annotation.*;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.springframework.ui.ModelMap;
import org.dspace.webmvc.utils.WebMVCManager;
import org.dspace.content.Item;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.dspace.webmvc.utils.UploadItem;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Robert Qin
 */
@Controller
public class CollectionWizardController {

    /** Logger */
    private static Logger log = Logger.getLogger(CollectionWizardController.class);
    /** Initial questions page */
    public static final int INITIAL_QUESTIONS = 1;
    /** Basic information page */
    public static final int BASIC_INFO = 2;
    /** Permissions pages */
    public static final int PERMISSIONS = 3;
    /** Default item page */
    public static final int DEFAULT_ITEM = 4;
    /** Summary page */
    public static final int SUMMARY = 5;
    /** Permissions page for who gets read permissions on new items */
    public static final int PERM_READ = 10;
    /** Permissions page for submitters */
    public static final int PERM_SUBMIT = 11;
    /** Permissions page for workflow step 1 */
    public static final int PERM_WF1 = 12;
    /** Permissions page for workflow step 2 */
    public static final int PERM_WF2 = 13;
    /** Permissions page for workflow step 3 */
    public static final int PERM_WF3 = 14;
    /** Permissions page for collection administrators */
    public static final int PERM_ADMIN = 15;

    @RequestMapping(method = RequestMethod.GET)
    protected String processGet(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        return doDSPost(context, model, request, response);

    }//end processGet

    @RequestMapping(method = RequestMethod.POST)
    protected String doDSPost(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {
        /*
         * For POST, we expect from the form:
         * 
         * community_id DB ID if it was a 'create a new collection' button press
         * 
         * OR
         * 
         * collection_id DB ID of collection we're dealing with stage Stage
         * we're at (from constants above)
         */

        // First, see if we have a multipart request
        // (the 'basic info' page which might include uploading a logo)


        int communityID = Util.getIntParameter(request, "community_id");

        if (communityID > -1) {
            // We have a community ID, "create new collection" button pressed
            Community c = Community.find(context, communityID);

            if (c == null) {
                log.warn(LogManager.getHeader(context, "integrity_error",
                        UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);

            }

            // Create the collection
            Collection newCollection = c.createCollection();
            model.addAttribute("collection", newCollection);

            if (AuthorizeManager.isAdmin(context)) {
                // set a variable to show all buttons
                model.addAttribute("sysadmin_button", Boolean.TRUE);
            }

            try {
                AuthorizeUtil.authorizeManageAdminGroup(context, newCollection);
                model.addAttribute("admin_create_button", Boolean.TRUE);
            } catch (AuthorizeException authex) {
                model.addAttribute("admin_create_button", Boolean.FALSE);
            }

            try {
                AuthorizeUtil.authorizeManageSubmittersGroup(context, newCollection);
                model.addAttribute("submitters_button", Boolean.TRUE);
            } catch (AuthorizeException authex) {
                model.addAttribute("submitters_button", Boolean.FALSE);
            }

            try {
                AuthorizeUtil.authorizeManageWorkflowsGroup(context, newCollection);
                model.addAttribute("workflows_button", Boolean.TRUE);
            } catch (AuthorizeException authex) {
                model.addAttribute("workflows_button", Boolean.FALSE);
            }

            try {
                AuthorizeUtil.authorizeManageTemplateItem(context, newCollection);
                model.addAttribute("template_button", Boolean.TRUE);
            } catch (AuthorizeException authex) {
                model.addAttribute("template_button", Boolean.FALSE);
            }

            context.commit();
            return "pages/admin/wizard-questions";

        } else {
            // Collection already created, dealing with one of the wizard pages
            int collectionID = Util.getIntParameter(request, "collection_id");
            int stage = Util.getIntParameter(request, "stage");

            // Get the collection
            Collection collection = Collection.find(context, collectionID);

            // Put it in request attributes, as most JSPs will need it
            model.addAttribute("collection", collection);

            if (collection == null) {
                log.warn(LogManager.getHeader(context, "integrity_error",
                        UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);

            }

            // All pages will need this attribute
            model.addAttribute("collectionid", String.valueOf(collection.getID()));

            switch (stage) {
                case INITIAL_QUESTIONS:
                    return processInitialQuestions(context, model, request, response, collection);

                case PERMISSIONS:
                    return processPermissions(context, model, request, response, collection);


                case DEFAULT_ITEM:
                    return processDefaultItem(context, model, request, response, collection);



                default:
                    log.warn(LogManager.getHeader(context, "integrity_error",
                            UIUtil.getRequestLogInfo(request)));
                    return WebMVCManager.showIntegrityError(request, response);
            }
        }
    }

    @RequestMapping(method = RequestMethod.POST, params = "submit_next_logo")
    protected String uploadFile(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, @RequestParam("file") CommonsMultipartFile file) throws ServletException, IOException, SQLException, AuthorizeException {

        // First, see if we have a multipart request (uploading a logo)
        String contentType = request.getContentType();

        if ((contentType != null) && (contentType.indexOf("multipart/form-data") != -1)) {
            // This is a multipart request, so it's a file upload
            return processBasicInfo(context, model, request, response, file);

        }//end if
        return "";
    }//end uploadFile

    private String processBasicInfo(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, CommonsMultipartFile file) throws SQLException,
            ServletException, IOException, AuthorizeException {
        try {

            Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));

            if (file != null) {
                // Wrap multipart request to get the submission info
                UploadItem wrapper = new UploadItem(request, file);

                File temp = wrapper.getFile();

                if (temp != null) {
                    // Read the temp file as logo
                    InputStream is = new BufferedInputStream(new FileInputStream(temp));
                    Bitstream logoBS = collection.setLogo(is);

                    // Strip all but the last filename. It would be nice
                    // to know which OS the file came from.
                    String noPath = wrapper.getName();

                    while (noPath.indexOf('/') > -1) {
                        noPath = noPath.substring(noPath.indexOf('/') + 1);
                    }

                    while (noPath.indexOf('\\') > -1) {
                        noPath = noPath.substring(noPath.indexOf('\\') + 1);
                    }

                    logoBS.setName(noPath);
                    logoBS.setSource(wrapper.getName());

                    // Identify the format
                    BitstreamFormat bf = FormatIdentifier.guessFormat(context, logoBS);
                    logoBS.setFormat(bf);
                    AuthorizeManager.addPolicy(context, logoBS, Constants.WRITE, context.getCurrentUser());
                    logoBS.update();

                    // Remove temp file
                    if (!temp.delete()) {
                        log.trace("Unable to delete temporary file");
                    }
                }//end if
            }//end if file not null

            if (collection == null) {
                log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                return WebMVCManager.showIntegrityError(request, response);

            }

            // Get metadata
            collection.setMetadata("name", request.getParameter("name"));
            collection.setMetadata("short_description", request.getParameter("short_description"));
            collection.setMetadata("introductory_text", request.getParameter("introductory_text"));
            collection.setMetadata("copyright_text", request.getParameter("copyright_text"));
            collection.setMetadata("side_bar_text", request.getParameter("side_bar_text"));
            collection.setMetadata("provenance_description", request.getParameter("provenance_description"));
            // Need to be more careful about license -- make sure it's null if
            // nothing was entered
            String license = request.getParameter("license");

            if (!StringUtils.isEmpty(license)) {
                collection.setLicense(license);
            }



            collection.update();
            context.commit();
            // Now work out what next page is
            return showNextPage(context, model, request, response, collection, BASIC_INFO);

        } catch (FileSizeLimitExceededException ex) {
            log.warn("Upload exceeded upload.max");
            return WebMVCManager.showFileSizeLimitExceededError(request, response, model, ex.getMessage(), ex.getActualSize(), ex.getPermittedSize());
        }
    }

    private String showNextPage(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, Collection collection, int stage)
            throws SQLException, ServletException, IOException,
            AuthorizeException {
        // Put collection in request attributes, as most JSPs will need it
        model.addAttribute("collection", collection);

        // FIXME: Not a nice hack -- do we show the MIT users checkbox?
        if (Group.findByName(context, "MIT Users") != null) {
            model.addAttribute("mitgroup", Boolean.TRUE);
        }

        log.debug(LogManager.getHeader(context, "nextpage", "stage=" + stage));

        switch (stage) {
            case BASIC_INFO:

                // Next page is 'permission to read' page iff ITEM_DEFAULT_READ
                // for anonymous group is NOT there
                List<ResourcePolicy> anonReadPols = AuthorizeManager.getPoliciesActionFilter(
                        context, collection, Constants.DEFAULT_ITEM_READ);

                // At this stage, if there's any ITEM_DEFAULT_READ, it can only
                // be an anonymous one.
                if (anonReadPols.isEmpty()) {
                    model.addAttribute("permission", Integer.valueOf(PERM_READ));
                    return "pages/admin/wizard-permissions";

                }

            case PERM_READ:

                // Next page is 'permission to submit' iff there's a submit group
                // defined
                if (collection.getSubmitters() != null) {
                    model.addAttribute("permission", Integer.valueOf(PERM_SUBMIT));
                    return "pages/admin/wizard-permissions";

                }

            case PERM_SUBMIT:

                // Next page is 'workflow step 1' iff there's a wf step 1 group
                // defined
                if (collection.getWorkflowGroup(1) != null) {
                    model.addAttribute("permission", Integer.valueOf(PERM_WF1));
                    return "pages/admin/wizard-permissions";

                }

            case PERM_WF1:

                // Next page is 'workflow step 2' iff there's a wf step 2 group
                // defined
                if (collection.getWorkflowGroup(2) != null) {
                    model.addAttribute("permission", Integer.valueOf(PERM_WF2));
                    return "pages/admin/wizard-permissions";

                }

            case PERM_WF2:

                // Next page is 'workflow step 3' iff there's a wf step 2 group
                // defined
                if (collection.getWorkflowGroup(3) != null) {
                    model.addAttribute("permission", Integer.valueOf(PERM_WF3));
                    return "pages/admin/wizard-permissions";

                }

            case PERM_WF3:

                // Next page is 'collection administrator' iff there's a collection
                // administrator group
                if (collection.getAdministrators() != null) {
                    model.addAttribute("permission", Integer.valueOf(PERM_ADMIN));
                    return "pages/admin/wizard-permissions";
                }

            case PERM_ADMIN:

                // Next page is 'default item' iff there's a default item
                if (collection.getTemplateItem() != null) {
                    MetadataField[] types = MetadataField.findAll(context);
                    model.addAttribute("dctypes", types);

                    String value = "";
                    List<String> metaDataValues = new ArrayList<String>();

                    for (int dc = 0; dc < types.length; dc++) {

                        if (types[dc].getQualifier() == null) {

                            value = MetadataSchema.find(UIUtil.obtainContext(request), types[dc].getSchemaID()).getName() + "." + types[dc].getElement();

                        } else {

                            value = MetadataSchema.find(UIUtil.obtainContext(request), types[dc].getSchemaID()).getName() + "." + types[dc].getElement() + "." + types[dc].getQualifier();
                        }

                        metaDataValues.add(value);

                    }//end for

                    model.addAttribute("metaDataValues", metaDataValues);

                    return "pages/admin/wizard-default-item";

                }

            case DEFAULT_ITEM:

                // Next page is 'summary page (the last page)
                // sort of a hack to pass the community ID to the edit collection
                // page,
                // which needs it in other contexts
                if (collection != null) {
                    Community[] communities = collection.getCommunities();
                    model.addAttribute("community", communities[0]);

                    EditCommunitiesController.storeAuthorizeAttributeCollectionEdit(context, model, request, collection);
                }
                
                EditCommunitiesController.processCollectionAttributes(context, model, request, response, collection);
                return "tools/edit-collection";

        }//end switch

        return "";

    }//end showNextPage

    private String processInitialQuestions(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response,
            Collection collection) throws SQLException, ServletException,
            IOException, AuthorizeException {
        // "Public read" checkbox. Only need to do anything
        // if it's not checked (only system admin can uncheck this!).
        if (!Util.getBoolParameter(request, "public_read") && AuthorizeManager.isAdmin(context)) {
            // Remove anonymous default policies for new items
            AuthorizeManager.removePoliciesActionFilter(context, collection,
                    Constants.DEFAULT_ITEM_READ);
            AuthorizeManager.removePoliciesActionFilter(context, collection,
                    Constants.DEFAULT_BITSTREAM_READ);
        }

        // Some people authorised to submit
        if (Util.getBoolParameter(request, "submitters")) {
            // Create submitters group
            collection.createSubmitters();
        }

        // Check for the workflow steps
        for (int i = 1; i <= 3; i++) {
            if (Util.getBoolParameter(request, "workflow" + i)) {
                // should have workflow step i
                collection.createWorkflowGroup(i);
            }
        }

        // Check for collection administrators
        if (Util.getBoolParameter(request, "admins")) {
            // Create administrators group
            collection.createAdministrators();
        }

        // Default item stuff?
        if (Util.getBoolParameter(request, "default.item")) {
            collection.createTemplateItem();
        }

        // Need to set a name so that the indexer won't throw an exception
        collection.setMetadata("name", "");
        collection.update();
        context.commit();
        // Now display "basic info" screen
        return "pages/admin/wizard-basicinfo";

    }// processInitialQuestions

    private String processPermissions(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response,
            Collection collection) throws SQLException, ServletException,
            IOException, AuthorizeException {
        // Which permission are we dealing with?
        int permission = Util.getIntParameter(request, "permission");

        // First, we deal with the special case of the MIT group...
        if (Util.getBoolParameter(request, "mitgroup")) {
            Group mitGroup = Group.findByName(context, "MIT Users");

            if (permission == PERM_READ) {
                // assign default item and bitstream read to mitGroup
                AuthorizeManager.addPolicy(context, collection,
                        Constants.DEFAULT_ITEM_READ, mitGroup);
                AuthorizeManager.addPolicy(context, collection,
                        Constants.DEFAULT_BITSTREAM_READ, mitGroup);
            } else {
                // Must be submit
                AuthorizeManager.addPolicy(context, collection, Constants.ADD,
                        mitGroup);
            }
        }

        //We need to add the selected people to the group.
        // First, get the relevant group
        Group g = null;

        switch (permission) {
            case PERM_READ:

                // Actually need to create a group for this.
                g = Group.create(context);

                // Name it according to our conventions
                g.setName("COLLECTION_" + collection.getID()
                        + "_DEFAULT_ITEM_READ");

                // Give it the needed permission
                AuthorizeManager.addPolicy(context, collection,
                        Constants.DEFAULT_ITEM_READ, g);
                AuthorizeManager.addPolicy(context, collection,
                        Constants.DEFAULT_BITSTREAM_READ, g);

                break;

            case PERM_SUBMIT:
                g = collection.getSubmitters();

                break;

            case PERM_WF1:
                g = collection.getWorkflowGroup(1);

                break;

            case PERM_WF2:
                g = collection.getWorkflowGroup(2);

                break;

            case PERM_WF3:
                g = collection.getWorkflowGroup(3);

                break;

            case PERM_ADMIN:
                g = collection.getAdministrators();

                break;
        }

        // Add people and groups from the form to the group
        int[] epersonIds = Util.getIntParameters(request, "eperson_id");
        int[] groupIds = Util.getIntParameters(request, "group_ids");

        if (epersonIds != null) {
            for (int i = 0; i < epersonIds.length; i++) {
                EPerson eperson = EPerson.find(context, epersonIds[i]);

                if (eperson != null) {
                    g.addMember(eperson);
                }
            }
        }

        if (groupIds != null) {
            for (int i = 0; i < groupIds.length; i++) {
                Group group = Group.find(context, groupIds[i]);

                if (group != null) {
                    g.addMember(group);
                }
            }
        }


        // Update group
        g.update();
        context.commit();
        return showNextPage(context, model, request, response, collection, permission);

    }

    private String processDefaultItem(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response,
            Collection collection) throws SQLException, ServletException,
            IOException, AuthorizeException {
        Item item = collection.getTemplateItem();

        for (int i = 0; i < 10; i++) {
            int dcTypeID = Util.getIntParameter(request, "dctype_" + i);
            String value = request.getParameter("value_" + i);
            String lang = request.getParameter("lang_" + i);

            if ((dcTypeID != -1) && (value != null) && !value.equals("")) {
                MetadataField field = MetadataField.find(context, dcTypeID);
                MetadataSchema schema = MetadataSchema.find(context, field.getSchemaID());
                item.addMetadata(schema.getName(), field.getElement(), field.getQualifier(), lang, value);
            }
        }

        item.update();
        context.commit();

        // Now work out what next page is
        return showNextPage(context, model, request, response, collection, DEFAULT_ITEM);

    }//end processDefaultItem
}
