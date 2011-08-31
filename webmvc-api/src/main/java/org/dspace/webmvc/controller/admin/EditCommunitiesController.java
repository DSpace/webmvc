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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;

import org.apache.log4j.Logger;
import org.dspace.app.util.AuthorizeUtil;
import org.dspace.app.util.Util;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Bitstream;
import org.dspace.content.BitstreamFormat;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.FormatIdentifier;
import org.dspace.content.Item;
import org.dspace.core.Utils;
import org.dspace.harvest.HarvestedCollection;
import org.springframework.web.bind.annotation.*;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.eperson.Group;
import org.springframework.ui.ModelMap;
import org.dspace.webmvc.utils.UploadItem;
import org.dspace.webmvc.utils.WebMVCManager;

import java.util.Enumeration;
import org.dspace.core.ConfigurationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 *
 * @author Robert Qin
 */
@Controller
public class EditCommunitiesController {

    /** Logger */
    private static Logger log = Logger.getLogger(EditCommunitiesController.class);

    @RequestMapping(method = RequestMethod.GET)
    protected String processGet(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        // GET just displays the list of communities and collections
        return showControls(context, model, request, response);

    }//end processGet
    
    
    @RequestMapping(method = RequestMethod.POST, params = "submit_upload")
    protected String uploadFile(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, @RequestParam("file") CommonsMultipartFile file) throws ServletException, IOException, SQLException, AuthorizeException {

        // First, see if we have a multipart request (uploading a logo)
        String contentType = request.getContentType();

        if ((contentType != null) && (contentType.indexOf("multipart/form-data") != -1)) {
            // This is a multipart request, so it's a file upload
            return processUploadLogo(context, model, request, response, file);

        }//end if
        return "";
    }//end uploadFile

    @RequestMapping(method = RequestMethod.POST, params = "submit_cancel")
    protected String startCancel(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        return showControls(context, model, request, response);

    }//end startCancel

    @RequestMapping(method = RequestMethod.POST, params = "submit_edit_community")
    protected String startEditCommunity(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);
        processCommunityAttributes(context, model, request, response, community);
        storeAuthorizeAttributeCommunityEdit(context, model, request, community);
        
        return "tools/edit-community";

    }//end startEditCommunity

    @RequestMapping(method = RequestMethod.POST, params = "submit_delete_community")
    protected String startDeleteCommunity(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        Community parentCommunity = Community.find(context, Util.getIntParameter(request, "parent_community_id"));
        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));

        // Just about every JSP will need the values we received
        model.addAttribute("community", community);
        model.addAttribute("parent", parentCommunity);
        model.addAttribute("collection", collection);

        return "/tools/confirm-delete-community";
    }//end startDeleteCommunity

    @RequestMapping(method = RequestMethod.POST, params = "submit_create_community")
    protected String startCreateCommunity() {

        return "tools/edit-community";
    }//end startCreateCommunity

    @RequestMapping(method = RequestMethod.POST, params = "submit_edit_collection")
    protected String startEditCollection(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        Community parentCommunity = Community.find(context, Util.getIntParameter(request, "parent_community_id"));
        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));


        model.addAttribute("community", community);
        model.addAttribute("parent", parentCommunity);
        model.addAttribute("collection", collection);

        processCollectionAttributes(context, model, request, response, collection);
        storeAuthorizeAttributeCollectionEdit(context, model, request, collection);
        

        return "tools/edit-collection";

    }//end startEditCollection

    static void processCollectionAttributes(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, Collection collection) throws ServletException, IOException, SQLException, AuthorizeException {

        //Start
        HarvestedCollection hc = HarvestedCollection.find(context, Util.getIntParameter(request, "collection_id"));
        model.addAttribute("harvestInstance", hc);
        String name = "";
        String shortDesc = "";
        String intro = "";
        String copy = "";
        String side = "";
        String license = "";
        String provenance = "";

        String oaiProviderValue = "";
        String oaiSetIdValue = "";
        String metadataFormatValue = "";
        String lastHarvestMsg = "";
        int harvestLevelValue = 0;
        int harvestStatus = 0;

        Group[] wfGroups = new Group[3];
        wfGroups[0] = null;
        wfGroups[1] = null;
        wfGroups[2] = null;

        Group admins = null;
        Group submitters = null;

        Item template = null;

        Bitstream logo = null;

        if (collection != null) {
            name = collection.getMetadata("name");
            model.addAttribute("name", Utils.addEntities(name));

            shortDesc = collection.getMetadata("short_description");
            model.addAttribute("shortDesc", Utils.addEntities(shortDesc));

            intro = collection.getMetadata("introductory_text");
            model.addAttribute("intro", Utils.addEntities(intro));

            copy = collection.getMetadata("copyright_text");
            model.addAttribute("copy", Utils.addEntities(copy));

            side = collection.getMetadata("side_bar_text");
            model.addAttribute("side", Utils.addEntities(side));

            provenance = collection.getMetadata("provenance_description");
            model.addAttribute("provenance", Utils.addEntities(provenance));

            if (collection.hasCustomLicense()) {
                license = collection.getLicense();
                model.addAttribute("license", Utils.addEntities(license));
            }

            wfGroups[0] = collection.getWorkflowGroup(1);
            wfGroups[1] = collection.getWorkflowGroup(2);
            wfGroups[2] = collection.getWorkflowGroup(3);

            model.addAttribute("wfGroups", wfGroups);

            admins = collection.getAdministrators();
            model.addAttribute("admins", admins);

            submitters = collection.getSubmitters();
            model.addAttribute("submitters", submitters);

            template = collection.getTemplateItem();

            logo = collection.getLogo();
            model.addAttribute("logo", logo);

            /* Harvesting stuff */
            if (hc != null) {
                oaiProviderValue = hc.getOaiSource();
                oaiSetIdValue = hc.getOaiSetId();
                metadataFormatValue = hc.getHarvestMetadataConfig();
                harvestLevelValue = hc.getHarvestType();
                lastHarvestMsg = hc.getHarvestMessage();
                harvestStatus = hc.getHarvestStatus();

            }//end hc != null

            model.addAttribute("harvestLevelValue", harvestLevelValue);
            model.addAttribute("oaiProviderValue", oaiProviderValue);
            model.addAttribute("oaiSetIdValue", oaiSetIdValue);
            model.addAttribute("metadataFormatValue", metadataFormatValue);
            model.addAttribute("lastHarvestMsg", lastHarvestMsg);


        }// end if
        Enumeration pe = ConfigurationManager.propertyNames();
        model.addAttribute("pe", pe);
        //End
    }

    static void processCommunityAttributes(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, Community community) throws ServletException, IOException, SQLException, AuthorizeException {

        // Start
        String name = "";
        String shortDesc = "";
        String intro = "";
        String copy = "";
        String side = "";
        Group admins = null;

        Bitstream logo = null;

        if (community != null) {
            name = community.getMetadata("name");
            model.addAttribute("name", Utils.addEntities(name));

            shortDesc = community.getMetadata("short_description");
            model.addAttribute("shortDesc", Utils.addEntities(shortDesc));

            intro = community.getMetadata("introductory_text");
            model.addAttribute("intro", Utils.addEntities(intro));

            copy = community.getMetadata("copyright_text");
            model.addAttribute("copy", Utils.addEntities(copy));

            side = community.getMetadata("side_bar_text");
            model.addAttribute("side", Utils.addEntities(side));

            logo = community.getLogo();

            admins = community.getAdministrators();
            model.addAttribute("admins", admins);

        }//end community
        int parentID = Util.getIntParameter(request, "parent_community_id");
        model.addAttribute("parentID", parentID);
        // End 
    }

    @RequestMapping(method = RequestMethod.POST, params = "submit_delete_collection")
    protected String startDeleteCollection(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        Community parentCommunity = Community.find(context, Util.getIntParameter(request, "parent_community_id"));
        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));

        // Just about every JSP will need the values we received
        model.addAttribute("community", community);
        model.addAttribute("parent", parentCommunity);
        model.addAttribute("collection", collection);

        return "tools/confirm-delete-collection";
    }//end startDeleteCollection

    @RequestMapping(method = RequestMethod.POST, params = "submit_create_collection")
    protected String startCreateCollection(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);

        // Forward to collection creation wizard
        return "redirect:/tools/collection-wizard?community_id=" + community.getID();

    }//end startCreateCollection

    @RequestMapping(method = RequestMethod.POST, params = "submit_confirm_edit_community")
    protected String confirmEditCommunity(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);
        // Edit or creation of a community confirmed
        community = processConfirmEditCommunity(context, model, request, response, community);
        context.commit();
        return showControls(context, model, request, response);

    }//end confirmEditCommunity

    @RequestMapping(method = RequestMethod.POST, params = "submit_community_set_logo")
    protected String submitCommunitySetLogo(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);
        community = processConfirmEditCommunity(context, model, request, response, community);

        community.setLogo(null);
        community.update();
        context.commit();

        return "pages/admin/upload-logo";

    }//end submitSetLogo

    @RequestMapping(method = RequestMethod.POST, params = "submit_community_delete_logo")
    protected String submitCommunityDeleteLogo(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);
        community = processConfirmEditCommunity(context, model, request, response, community);

        community.setLogo(null);
        community.update();
        context.commit();
        processCommunityAttributes(context, model, request, response, community);
        return "tools/edit-community";

    }//end submitDeleteLogo

    @RequestMapping(method = RequestMethod.POST, params = "submit_community_authorization_edit")
    protected String submitCommunityAuthorizationEdit(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);
        community = processConfirmEditCommunity(context, model, request, response, community);
        context.commit();
        return "redirect:/admin/authorize/?community_id=" + community.getID() + "&submit_community_select=1";

    }//end submitAuthorizationEdit

    @RequestMapping(method = RequestMethod.POST, params = "submit_admins_community_create")
    protected String submitAdminCommunityCreate(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);
        community = processConfirmEditCommunity(context, model, request, response, community);

        // Create new group
        Group newGroup = community.createAdministrators();
        community.update();
        context.commit();
        return "redirect:/tools/group-edit?group_id=" + newGroup.getID();

    }//end submitAdminCreate

    @RequestMapping(method = RequestMethod.POST, params = "submit_admins_community_remove")
    protected String submitAdminCommunityRemove(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);
        community = processConfirmEditCommunity(context, model, request, response, community);

        Group g = community.getAdministrators();
        community.removeAdministrators();
        community.update();
        g.delete();

        // Start
        String name = "";
        String shortDesc = "";
        String intro = "";
        String copy = "";
        String side = "";
        Group admins = null;

        Bitstream logo = null;

        if (community != null) {
            name = community.getMetadata("name");
            model.addAttribute("name", Utils.addEntities(name));

            shortDesc = community.getMetadata("short_description");
            model.addAttribute("shortDesc", Utils.addEntities(shortDesc));

            intro = community.getMetadata("introductory_text");
            model.addAttribute("intro", Utils.addEntities(intro));

            copy = community.getMetadata("copyright_text");
            model.addAttribute("copy", Utils.addEntities(copy));

            side = community.getMetadata("side_bar_text");
            model.addAttribute("side", Utils.addEntities(side));

            logo = community.getLogo();

            admins = community.getAdministrators();
            model.addAttribute("admins", admins);

        }//end community
        int parentID = Util.getIntParameter(request, "parent_community_id");
        model.addAttribute("parentID", parentID);
        // End 

        context.commit();
        processCommunityAttributes(context, model, request, response, community);
        return "tools/edit-community";

    }//end submitAdminRemove

    @RequestMapping(method = RequestMethod.POST, params = "submit_admins_community_edit")
    protected String submitAdminCommunityEdit(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);
        community = processConfirmEditCommunity(context, model, request, response, community);

        // Edit 'community administrators' group
        Group g = community.getAdministrators();
        context.commit();
        return "redirect:/tools/group-edit?group_id=" + g.getID();

    }//end submitAdminEdit

    @RequestMapping(method = RequestMethod.POST, params = "submit_confirm_delete_community")
    protected String confirmDeleteCommunity(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);
        // remember the parent community, if any
        Community parent = community.getParentCommunity();
        // Delete the community
        community.delete();
        context.commit();

        // if community was top-level, redirect to community-list page
        if (parent == null) {
            return "redirect:/community-list";
        } else // redirect to parent community page
        {
            return "redirect:/handle/" + parent.getHandle();
        }

    }//end confirmDeleteCommunity

    @RequestMapping(method = RequestMethod.POST, params = "submit_confirm_edit_collection")
    protected String confirmEditCollection(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {


        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));
        model.addAttribute("collection", collection);
        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        collection = processConfirmEditCollection(context, model, request, response, community, collection);
        // Plain old "create/update" button pressed - go back to main page
        // Commit changes to DB
        collection.update();
        context.commit();
        // context.complete();
        return showControls(context, model, request, response);
    }//end confirmEditCollection

    @RequestMapping(method = RequestMethod.POST, params = "submit_confirm_delete_collection")
    protected String confirmDeleteCollection(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));
        model.addAttribute("collection", collection);
        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);

        // Delete the collection
        community.removeCollection(collection);
        // remove the collection object from the request, so that the user
        // will be redirected on the community home page
        request.removeAttribute("collection");

        // Commit changes to DB
        context.commit();
        // Show main control page
        return showControls(context, model, request, response);

    }//end confirmEditCollection

    @RequestMapping(method = RequestMethod.POST, params = "submit_collection_set_logo")
    protected String submitCollectionSetLogo(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));
        model.addAttribute("collection", collection);
        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        collection = processConfirmEditCollection(context, model, request, response, community, collection);
        // Change the logo - delete any that might be there first
        collection.setLogo(null);

        // Display "upload logo" page. Necessary attributes already set by
        // doDSPost()
        return "pages/admin/upload-logo";

    }//end submitCollectionSetLogo

    @RequestMapping(method = RequestMethod.POST, params = "submit_collection_delete_logo")
    protected String submitCollectionDeleteLogo(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));
        model.addAttribute("collection", collection);
        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        collection = processConfirmEditCollection(context, model, request, response, community, collection);
        // Simply delete logo
        collection.setLogo(null);
        processCollectionAttributes(context, model, request, response, collection);
        return "tools/edit-collection";


    }//end submitCollectionSetLogo
    
    
    
        
    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/wfgroupscreate/{rTindex}/{commid}/{colid}")
    protected String submitCollectionWFCreate(@RequestAttribute Context context, ModelMap model, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID, HttpServletRequest request, HttpServletResponse response, @PathVariable(value="rTindex") Integer step) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);

        // int step = Integer.parseInt(request.getParameter("step"));
        // Create new group
        Group newGroup = collection.createWorkflowGroup(step);
        collection.update();
        context.commit();
        return "redirect:/tools/group-edit?group_id=" + newGroup.getID();

    }//end submitCollectionWFCreate

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/wfgroupsedit/{rTindex}/{commid}/{colid}")
    protected String submitCollectionWFEdit(@RequestAttribute Context context, ModelMap model, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID, HttpServletRequest request, HttpServletResponse response, @PathVariable(value="rTindex") Integer step) throws ServletException, IOException, SQLException, AuthorizeException {

        
        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);

        // int step = Integer.parseInt(request.getParameter("step"));
        
        // Edit workflow group
        Group g = collection.getWorkflowGroup(step);
        return "redirect:/tools/group-edit?group_id=" + g.getID();

    }//end submitCollectionWFEdit

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/wfgroupsdelete/{rTindex}/{commid}/{colid}")
    protected String submitCollectionWFDelete(@RequestAttribute Context context, ModelMap model, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID, HttpServletRequest request, HttpServletResponse response, @PathVariable(value="rTindex") Integer step) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
       
        Group g = collection.getWorkflowGroup(step);
        collection.setWorkflowGroup(step, null);

        // Have to update to avoid ref. integrity error
        collection.update();
        g.delete();

        // Show edit page again - attributes set in doDSPost()    
        context.commit();
        processCollectionAttributes(context, model, request, response, collection);
        storeAuthorizeAttributeCollectionEdit(context, model, request, collection);
        return "tools/edit-collection";

    }//end submitCollectionWFDelete

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/admincolcreate/{commid}/{colid}")
    protected String submitAdminCollectionCreate(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);
        Group newGroup = collection.createAdministrators();
        collection.update();
        context.commit();
        // Forward to group edit page
        return "redirect:/tools/group-edit?group_id=" + newGroup.getID();

    }//end submitAdminCollectionCreate

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/admincoledit/{commid}/{colid}")
    protected String submitAdminCollectionEdit(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);
        Group g = collection.getAdministrators();
        
        return "redirect:/tools/group-edit?group_id=" + g.getID();

    }//end submitAdminCollectionEdit

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/admincoldelete/{commid}/{colid}")
    protected String submitAdminCollectionDelete(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);
        
        Group g = collection.getAdministrators();
        collection.removeAdministrators();
        collection.update();
        g.delete();


        // Show edit page again - attributes set in doDSPost()
        //JSPManager.showJSP(request, response, "/tools/edit-collection.jsp");
        
        context.commit();
        processCollectionAttributes(context, model, request, response, collection);
        storeAuthorizeAttributeCollectionEdit(context, model, request, collection);
        return "tools/edit-collection";

    }//end submitAdminCollectionDelete

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/colsubcreate/{commid}/{colid}")
    protected String submitCollectionSubmittersCreate(@RequestAttribute Context context, ModelMap model, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);
        // Create new group
        
        Group newGroup = collection.createSubmitters();
        collection.update();
        context.commit();

        // Forward to group edit page
        return "redirect:/tools/group-edit?group_id=" + newGroup.getID();

    }//end submitCollectionSubmittersCreate

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/colsubdelete/{commid}/{colid}")
    protected String submitCollectionSubmittersDelete(@RequestAttribute Context context, ModelMap model, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);
        Group g = collection.getSubmitters();
        collection.removeSubmitters();
        collection.update();
        g.delete();
        
        // Show edit page again - attributes set in doDSPost()
        // JSPManager.showJSP(request, response, "/tools/edit-collection.jsp");
        
        context.commit();
        processCollectionAttributes(context, model, request, response, collection);
        storeAuthorizeAttributeCollectionEdit(context, model, request, collection);
        return "tools/edit-collection";

    }//end submitCollectionSubmittersDelete

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/colsubedit/{commid}/{colid}")
    protected String submitCollectionSubmittersEdit(@RequestAttribute Context context, ModelMap model, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        //Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));
        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);
        // Edit submitters group
        Group g = collection.getSubmitters();
        return "redirect:/tools/group-edit?group_id=" + g.getID();

    }//end submitCollectionSubmittersEdit

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/editauthorization/{commid}/{colid}")
    protected String submitCollectionAuthorizationEdit(@RequestAttribute Context context, ModelMap model, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);
        // Forward to policy edit page
        return "redirect:/admin/authorize/?collection_id=" + collection.getID() + "&submit_collection_select=1";

    }//end submitCollectionAuthorizationEdit

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/createtemplate/{commid}/{colid}")
    protected String submitCollectionCreateTemplate(@RequestAttribute Context context, ModelMap model, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);
        // Create a template item
        collection.createTemplateItem();

        // Forward to edit page for new template item
        Item i = collection.getTemplateItem();

        // save the changes
        collection.update();
        context.commit();
        return "redirect:/tools/edit-item?item_id=" + i.getID();

    }//end submitCollectionCreateTemplate

    
    
    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/edittemplate/{commid}/{colid}")
    protected String submitCollectionEditTemplate(@RequestAttribute Context context, ModelMap model, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);
        // Forward to edit page for template item
        Item i = collection.getTemplateItem();
        return "redirect:/tools/edit-item?item_id=" + i.getID();

    }//end submitCollectionEditTemplate

    @RequestMapping(method= RequestMethod.GET, value = "/admin/editcommunities/deletetemplate/{commid}/{colid}")
    protected String submitCollectionDeleteTemplate(@RequestAttribute Context context, ModelMap model, @PathVariable(value="commid") Integer commID, @PathVariable(value="colid") Integer colID, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, colID);
        model.addAttribute("collection", collection);
        Community community = Community.find(context, commID);
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        // collection = processConfirmEditCollection(context, model, request, response, community, collection);
        collection.removeTemplateItem();

        // Show edit page again - attributes set in doDSPost()

        // Commit changes to DB
        collection.update();
        context.commit();
        processCollectionAttributes(context, model, request, response, collection);
        storeAuthorizeAttributeCollectionEdit(context, model, request, collection);
               
        return "tools/edit-collection";

    }//end submitCollectionDeleteTemplate

    @RequestMapping(method = RequestMethod.POST, params = "create_collection")
    protected String submitCreateUpdateCollection(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {

        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));
        model.addAttribute("collection", collection);
        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
        model.addAttribute("community", community);

        // Edit or creation of a community confirmed
        collection = processConfirmEditCollection(context, model, request, response, community, collection);
        // Commit changes to DB
        collection.update();
        // context.complete();
        context.commit();
        return showControls(context, model, request, response);

    }//end submitCreateUpdateCollection

    private Community processConfirmEditCommunity(Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response, Community community) throws ServletException, IOException,
            SQLException, AuthorizeException {

        if (request.getParameter("create").equals("true")) {
            // if there is a parent community id specified, create community
            // as its child; otherwise, create it as a top-level community
            int parentCommunityID = Util.getIntParameter(request, "parent_community_id");

            if (parentCommunityID != -1) {
                Community parent = Community.find(context, parentCommunityID);

                if (parent != null) {
                    community = parent.createSubcommunity();
                }
            } else {
                community = Community.create(null, context);
            }

            // Set attribute
            // request.setAttribute("community", community);
            model.addAttribute("community", community);
        }

        storeAuthorizeAttributeCommunityEdit(context, model, request, community);

        community.setMetadata("name", request.getParameter("name"));
        community.setMetadata("short_description", request.getParameter("short_description"));

        String intro = request.getParameter("introductory_text");

        if (intro.equals("")) {
            intro = null;
        }

        String copy = request.getParameter("copyright_text");

        if (copy.equals("")) {
            copy = null;
        }

        String side = request.getParameter("side_bar_text");

        if (side.equals("")) {
            side = null;
        }

        community.setMetadata("introductory_text", intro);
        community.setMetadata("copyright_text", copy);
        community.setMetadata("side_bar_text", side);
        community.update();
        return community;

        // Commit changes to DB       
        //context.complete();
    }//end processConfirmEditCommunity

    private void storeAuthorizeAttributeCommunityEdit(Context context, ModelMap model, HttpServletRequest request, Community community) throws SQLException {
        try {
            AuthorizeUtil.authorizeManageAdminGroup(context, community);
            //request.setAttribute("admin_create_button", Boolean.TRUE);
            model.addAttribute("admin_create_button", Boolean.TRUE);
        } catch (AuthorizeException authex) {
            //request.setAttribute("admin_create_button", Boolean.FALSE);
            model.addAttribute("admin_create_button", Boolean.FALSE);
        }

        try {
            AuthorizeUtil.authorizeRemoveAdminGroup(context, community);
            //request.setAttribute("admin_remove_button", Boolean.TRUE);
            model.addAttribute("admin_remove_button", Boolean.TRUE);
        } catch (AuthorizeException authex) {
            //request.setAttribute("admin_remove_button", Boolean.FALSE);
            model.addAttribute("admin_remove_button", Boolean.FALSE);
        }

        if (AuthorizeManager.authorizeActionBoolean(context, community, Constants.DELETE)) {
            //request.setAttribute("delete_button", Boolean.TRUE);
            model.addAttribute("delete_button", Boolean.TRUE);
        } else {
            //request.setAttribute("delete_button", Boolean.FALSE);
            model.addAttribute("delete_button", Boolean.FALSE);
        }

        try {
            AuthorizeUtil.authorizeManageCommunityPolicy(context, community);
            model.addAttribute("policy_button", Boolean.TRUE);
            //request.setAttribute("policy_button", Boolean.TRUE);
        } catch (AuthorizeException authex) {
            model.addAttribute("policy_button", Boolean.FALSE);
        }
    }//end storeAuthorizeAttributeCommunityEdit

    private String showControls(Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {
        // new approach - eliminate the 'list-communities' page in favor of the
        // community home page, enhanced with admin controls. If no community,
        // or no parent community, just fall back to the community-list page
        Community community = (Community) request.getAttribute("community");
        Collection collection = (Collection) request.getAttribute("collection");

        if (collection != null) {

            return "redirect:/handle/" + collection.getHandle();

        } else if (community != null) {

            return "redirect:/handle/" + community.getHandle();

        } else {
            // see if a parent community was specified
            Community parent = (Community) request.getAttribute("parent");

            if (parent != null) {

                return "redirect:/handle/" + parent.getHandle();

            } else {

                // fall back on community-list page             
                return "redirect:/community-list";
            }
        }
    }//end showControls

    protected String processUploadLogo(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, CommonsMultipartFile file) throws ServletException, IOException,
            SQLException, AuthorizeException {

        try {
            Community community = Community.find(context, Util.getIntParameter(request, "community_id"));
            Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));

            // Wrap multipart request to get the submission info
            if (file != null) {
                UploadItem wrapper = new UploadItem(request, file);
                File temp = wrapper.getFile();
                // Read the temp file as logo
                InputStream is = new BufferedInputStream(new FileInputStream(temp));
                Bitstream logoBS;

                if (collection == null) {
                    logoBS = community.setLogo(is);
                } else {
                    logoBS = collection.setLogo(is);
                }

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
                    log.error("Unable to delete temporary file");
                }
            }//end if file not null

            String page;
            DSpaceObject dso;
            if (collection == null) {
                community.update();

                // Show community edit page
                model.addAttribute("community", community);
                storeAuthorizeAttributeCommunityEdit(context, model, request, community);
                dso = community;
                processCommunityAttributes(context, model, request, response, community);
                page = "tools/edit-community";
            } else {
                collection.update();
                HarvestedCollection hc = HarvestedCollection.find(context, Util.getIntParameter(request, "collection_id"));

                // Show collection edit page                
                model.addAttribute("collection", collection);
                model.addAttribute("community", community);
                storeAuthorizeAttributeCollectionEdit(context, model, request, collection);
                dso = collection;
                processCollectionAttributes(context, model, request, response, collection);
                page = "tools/edit-collection";
            }

            if (AuthorizeManager.isAdmin(context, dso)) {
                // set a variable to show all buttons                
                model.addAttribute("admin_button", Boolean.TRUE);
            }



            // Update DB
            context.commit();

            //JSPManager.showJSP(request, response, jsp);
            return page;


        } catch (FileSizeLimitExceededException ex) {
            log.warn("Upload exceeded upload.max");
            return WebMVCManager.showFileSizeLimitExceededError(request, response, model, ex.getMessage(), ex.getActualSize(), ex.getPermittedSize());
        }
    }//end processUploadLogo

    private Collection processConfirmEditCollection(Context context, ModelMap model,
            HttpServletRequest request, HttpServletResponse response,
            Community community, Collection collection)
            throws ServletException, IOException, SQLException,
            AuthorizeException {
        if (request.getParameter("create").equals("true")) {
            // We need to create a new community
            collection = community.createCollection();
            model.addAttribute("collection", collection);
        }

        storeAuthorizeAttributeCollectionEdit(context, model, request, collection);

        // Update the basic metadata
        collection.setMetadata("name", request.getParameter("name"));
        collection.setMetadata("short_description", request.getParameter("short_description"));

        String intro = request.getParameter("introductory_text");

        if (intro.equals("")) {
            intro = null;
        }

        String copy = request.getParameter("copyright_text");

        if (copy.equals("")) {
            copy = null;
        }

        String side = request.getParameter("side_bar_text");

        if (side.equals("")) {
            side = null;
        }

        String license = request.getParameter("license");

        if (license.equals("")) {
            license = null;
        }

        String provenance = request.getParameter("provenance_description");

        if (provenance.equals("")) {
            provenance = null;
        }

        collection.setMetadata("introductory_text", intro);
        collection.setMetadata("copyright_text", copy);
        collection.setMetadata("side_bar_text", side);
        collection.setMetadata("license", license);
        collection.setMetadata("provenance_description", provenance);

        // Set the harvesting settings

        HarvestedCollection hc = HarvestedCollection.find(context, collection.getID());
        String contentSource = request.getParameter("source");

        // First, if this is not a harvested collection (anymore), set the harvest type to 0; wipe harvest settings  
        if (contentSource.equals("source_normal")) {
            if (hc != null) {
                hc.delete();
            }
        } else {
            // create a new harvest instance if all the settings check out
            if (hc == null) {
                hc = HarvestedCollection.create(context, collection.getID());
            }

            String oaiProvider = request.getParameter("oai_provider");
            String oaiSetId = request.getParameter("oai_setid");
            String metadataKey = request.getParameter("metadata_format");
            String harvestType = request.getParameter("harvest_level");

            hc.setHarvestParams(Integer.parseInt(harvestType), oaiProvider, oaiSetId, metadataKey);
            hc.setHarvestStatus(HarvestedCollection.STATUS_READY);

            hc.update();
        }

        return collection;

    }// processConfirmEditCollection

    static void storeAuthorizeAttributeCollectionEdit(Context context, ModelMap model,
            HttpServletRequest request, Collection collection) throws SQLException {
        if (AuthorizeManager.isAdmin(context, collection)) {
            //request.setAttribute("admin_collection", Boolean.TRUE);
            model.addAttribute("admin_collection", Boolean.TRUE);
        } else {
            //request.setAttribute("admin_collection", Boolean.FALSE);
            model.addAttribute("admin_collection", Boolean.FALSE);
        }

        try {
            AuthorizeUtil.authorizeManageAdminGroup(context, collection);
            //request.setAttribute("admin_create_button", Boolean.TRUE);
            model.addAttribute("admin_create_button", Boolean.TRUE);
        } catch (AuthorizeException authex) {
            //request.setAttribute("admin_create_button", Boolean.FALSE);
            model.addAttribute("admin_create_button", Boolean.FALSE);
        }

        try {
            AuthorizeUtil.authorizeRemoveAdminGroup(context, collection);
            //request.setAttribute("admin_remove_button", Boolean.TRUE);
            model.addAttribute("admin_remove_button", Boolean.TRUE);
        } catch (AuthorizeException authex) {
            //request.setAttribute("admin_remove_button", Boolean.FALSE);
            model.addAttribute("admin_remove_button", Boolean.FALSE);
        }

        try {
            AuthorizeUtil.authorizeManageSubmittersGroup(context, collection);
            //request.setAttribute("submitters_button", Boolean.TRUE);
            model.addAttribute("submitters_button", Boolean.TRUE);
        } catch (AuthorizeException authex) {
            //request.setAttribute("submitters_button", Boolean.FALSE);
            model.addAttribute("submitters_button", Boolean.FALSE);
        }

        try {
            AuthorizeUtil.authorizeManageWorkflowsGroup(context, collection);
            //request.setAttribute("workflows_button", Boolean.TRUE);
            model.addAttribute("workflows_button", Boolean.TRUE);
        } catch (AuthorizeException authex) {
            //request.setAttribute("workflows_button", Boolean.FALSE);
            model.addAttribute("workflows_button", Boolean.FALSE);
        }

        try {
            AuthorizeUtil.authorizeManageTemplateItem(context, collection);
            //request.setAttribute("template_button", Boolean.TRUE);
            model.addAttribute("template_button", Boolean.TRUE);
        } catch (AuthorizeException authex) {
            //request.setAttribute("template_button", Boolean.FALSE);
            model.addAttribute("template_button", Boolean.FALSE);
        }

        if (AuthorizeManager.authorizeActionBoolean(context, collection.getParentObject(), Constants.REMOVE)) {
            //request.setAttribute("delete_button", Boolean.TRUE);
            model.addAttribute("delete_button", Boolean.TRUE);
        } else {
            //request.setAttribute("delete_button", Boolean.FALSE);
            model.addAttribute("delete_button", Boolean.FALSE);
        }

        try {
            AuthorizeUtil.authorizeManageCollectionPolicy(context, collection);
            //request.setAttribute("policy_button", Boolean.TRUE);
            model.addAttribute("policy_button", Boolean.TRUE);
        } catch (AuthorizeException authex) {
            //request.setAttribute("policy_button", Boolean.FALSE);
            model.addAttribute("policy_button", Boolean.FALSE);
        }
    }
}//end EditCommunitiesController
