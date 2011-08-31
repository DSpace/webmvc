/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller.admin;

import org.apache.log4j.Logger;
import org.dspace.core.*;
import org.dspace.app.util.Util;
import org.dspace.authorize.AuthorizeException;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.dspace.webmvc.bind.annotation.RequestAttribute;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.app.util.AuthorizeUtil;
import org.dspace.authorize.PolicySet;
import org.dspace.eperson.EPerson;
import org.dspace.content.Bundle;
import org.dspace.content.Bitstream;
import org.dspace.authorize.ResourcePolicy;
import org.dspace.content.DSpaceObject;
import org.dspace.handle.HandleManager;
import org.springframework.ui.ModelMap;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.eperson.Group;
import org.dspace.content.Item;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Robert Qin
 */
public class AuthorizeAdminController {

    /** Logger */
    private static Logger log = Logger.getLogger(AuthorizeAdminController.class);

    @RequestMapping(params = "submit_collection")
    protected String submitCollection(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // select a collection to work on
        Collection[] collections = Collection.findAll(context);
        context.commit();
        model.addAttribute("collections", collections);
        return "pages/admin/collection-select";

    }//end submitCollection

    @RequestMapping(params = "submit_community")
    protected String submitCommunity(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // select a community to work on
        Community[] communities = Community.findAll(context);
        context.commit();
        model.addAttribute("communities", communities);        
        return "pages/admin/community-select";

    }//end submitCommunity

    @RequestMapping(params = "submit_advanced")
    protected String submitAdvanced(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // select a collections to work on
        Collection[] collections = Collection.findAll(context);
        Group[] groups = Group.findAll(context, Group.NAME);
        context.commit();
        model.addAttribute("collections", collections);
        model.addAttribute("groups", groups);
        model.addAttribute("item", Constants.ITEM);
        model.addAttribute("bitstream", Constants.BITSTREAM);
        model.addAttribute("actiontext", Constants.actionText);
        
        return "pages/admin/authorize-advanced";

    }//end submitAdvanced

    @RequestMapping(params = "submit_item")
    protected String submitItem(ModelMap model) {

        model.addAttribute("handleprefix", ConfigurationManager.getProperty("handle.prefix"));        
        return "pages/admin/item-select";

    }//end submitItem

    @RequestMapping(params = "submit_item_select")
    protected String submitItemSelect(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        Item item = null;

        int itemId = Util.getIntParameter(request, "item_id");
        String handle = request.getParameter("handle");

        // if id is set, use it
        if (itemId > 0) {
            item = Item.find(context, itemId);
        } else if ((handle != null) && !handle.equals("")) {
            // otherwise, attempt to resolve handle
            DSpaceObject dso = HandleManager.resolveToObject(context, handle);

            // make sure it's an item
            if ((dso != null) && (dso.getType() == Constants.ITEM)) {
                item = (Item) dso;
            }
        }

        // no item set yet, failed ID & handle, ask user to try again
        if (item == null) {
            context.commit();
            model.addAttribute("invalidid", Boolean.TRUE);
            model.addAttribute("handleprefix", ConfigurationManager.getProperty("handle.prefix"));
            return "pages/admin/item-select";

        } else {
            // show edit form!
            prepItemEditForm(context, model, request, item);
            
            return "pages/admin/authorize-item-edit";

        }

    }//end submitItemSelect

    @RequestMapping(params = "submit_item_add_policy")
    protected String submitItemAddPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // want to add a policy, create an empty one and invoke editor
        Item item = Item.find(context, Util.getIntParameter(request, "item_id"));

        AuthorizeUtil.authorizeManageItemPolicy(context, item);
        ResourcePolicy policy = ResourcePolicy.create(context);
        policy.setResource(item);
        policy.update();

        Group[] groups = Group.findAll(context, Group.NAME);
        EPerson[] epeople = EPerson.findAll(context, EPerson.EMAIL);

        // return to item permission page
        context.commit();
        model.addAttribute("edit_title", "Item " + item.getID());
        model.addAttribute("policy", policy);
        model.addAttribute("groups", groups);
        model.addAttribute("epeople", epeople);
        model.addAttribute("id_name", "item_id");
        model.addAttribute("id", "" + item.getID());
        model.addAttribute("newpolicy", "true");
        model.addAttribute("actionText", Constants.actionText);
        
        return "pages/admin/authorize-policy-edit";

    }//end submitItemAddPolicy

    @RequestMapping(params = "submit_item_edit_policy")
    protected String submitItemEditPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // edit an item's policy - set up and call policy editor
        Item item = Item.find(context, Util.getIntParameter(request, "item_id"));

        AuthorizeUtil.authorizeManageItemPolicy(context, item);
        int policyId = Util.getIntParameter(request, "policy_id");
        ResourcePolicy policy = null;

        policy = ResourcePolicy.find(context, policyId);

        Group[] groups = Group.findAll(context, Group.NAME);
        EPerson[] epeople = EPerson.findAll(context, EPerson.EMAIL);

        // return to collection permission page
        context.commit();
        model.addAttribute("edit_title", "Item " + item.getID());
        model.addAttribute("policy", policy);
        model.addAttribute("groups", groups);
        model.addAttribute("epeople", epeople);
        model.addAttribute("id_name", "item_id");
        model.addAttribute("id", "" + item.getID());
        model.addAttribute("actionText", Constants.actionText);
        
        return "pages/admin/authorize-policy-edit";

    }//end submitItemEditPolicy

    @RequestMapping(params = "submit_bundle_add_policy")
    protected String submitBundleAddPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // want to add a policy, create an empty one and invoke editor
        Item item = Item.find(context, Util.getIntParameter(request, "item_id"));
        Bundle bundle = Bundle.find(context, Util.getIntParameter(request, "bundle_id"));

        AuthorizeUtil.authorizeManageBundlePolicy(context, bundle);
        ResourcePolicy policy = ResourcePolicy.create(context);
        policy.setResource(bundle);
        policy.update();

        Group[] groups = Group.findAll(context, Group.NAME);
        EPerson[] epeople = EPerson.findAll(context, EPerson.EMAIL);
        
        context.commit();
        // return to item permission page        
        model.addAttribute("edit_title", "(Item, Bundle) = (" + item.getID() + "," + bundle.getID() + ")");
        model.addAttribute("policy", policy);
        model.addAttribute("groups", groups);
        model.addAttribute("epeople", epeople);
        model.addAttribute("id_name", "item_id");
        model.addAttribute("id", "" + item.getID());
        model.addAttribute("newpolicy", "true");
        model.addAttribute("actionText", Constants.actionText);

        return "pages/admin/authorize-policy-edit";

    }//end submitBundleAddPolicy

    @RequestMapping(params = "submit_bitstream_add_policy")
    protected String submitBitstreamAddPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // want to add a policy, create an empty one and invoke editor
        Item item = Item.find(context, Util.getIntParameter(request, "item_id"));
        Bitstream bitstream = Bitstream.find(context, Util.getIntParameter(request, "bitstream_id"));

        AuthorizeUtil.authorizeManageBitstreamPolicy(context, bitstream);
        ResourcePolicy policy = ResourcePolicy.create(context);
        policy.setResource(bitstream);
        policy.update();

        Group[] groups = Group.findAll(context, Group.NAME);
        EPerson[] epeople = EPerson.findAll(context, EPerson.EMAIL);
        
        context.commit();
        // return to item permission page        
        model.addAttribute("edit_title", "(Item,Bitstream) = (" + item.getID() + "," + bitstream.getID() + ")");
        model.addAttribute("policy", policy);
        model.addAttribute("groups", groups);
        model.addAttribute("epeople", epeople);
        model.addAttribute("id_name", "item_id");
        model.addAttribute("id", "" + item.getID());
        model.addAttribute("newpolicy", "true");
        model.addAttribute("actionText", Constants.actionText);
        
        return "pages/admin/authorize-policy-edit";

    }//end submitBitstreamAddPolicy

    @RequestMapping(params = "submit_item_delete_policy")
    protected String submitItemDeletePolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // delete a permission from an item
        Item item = Item.find(context, Util.getIntParameter(request, "item_id"));

        AuthorizeUtil.authorizeManageItemPolicy(context, item);
        ResourcePolicy policy = ResourcePolicy.find(context, Util.getIntParameter(request, "policy_id"));

        // do the remove
        policy.delete();

        // show edit form!
        prepItemEditForm(context, model, request, item);
        
        return "pages/admin/authorize-item-edit";
    }//end submitItemDeletePolicy

    @RequestMapping(params = "submit_collection_add_policy")
    protected String submitCollectionAddPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // want to add a policy, create an empty one and invoke editor
        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));

        AuthorizeUtil.authorizeManageCollectionPolicy(context, collection);
        ResourcePolicy policy = ResourcePolicy.create(context);
        policy.setResource(collection);
        policy.update();

        Group[] groups = Group.findAll(context, Group.NAME);
        EPerson[] epeople = EPerson.findAll(context, EPerson.EMAIL);

        // return to collection permission page
        context.commit();
        model.addAttribute("edit_title", "Collection " + collection.getID());
        model.addAttribute("policy", policy);
        model.addAttribute("groups", groups);
        model.addAttribute("epeople", epeople);
        model.addAttribute("id_name", "collection_id");
        model.addAttribute("id", "" + collection.getID());
        model.addAttribute("newpolicy", "true");
        model.addAttribute("actionText", Constants.actionText);
        
        return "pages/admin/authorize-policy-edit";

    }//end submitCollectionAddPolicy

    @RequestMapping(params = "submit_community_select")
    protected String submitCommunitySelect(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // edit the collection's permissions
        Community target = Community.find(context, Util.getIntParameter(request, "community_id"));
        
        List<ResourcePolicy> policies = AuthorizeManager.getPolicies(context, target);

        context.commit();
        model.addAttribute("community", target);
        model.addAttribute("policies", policies);
        
        return "pages/admin/authorize-community-edit";

    }//end submitCommunitySelect

    @RequestMapping(params = "submit_collection_delete_policy")
    protected String submitCollectionDelectPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // delete a permission from a collection
        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));

        AuthorizeUtil.authorizeManageCollectionPolicy(context, collection);
        ResourcePolicy policy = ResourcePolicy.find(context, Util.getIntParameter(request, "policy_id"));

        // do the remove
        policy.delete();

        // return to collection permission page       
        

        List<ResourcePolicy> policies = AuthorizeManager.getPolicies(context, collection);
        
        context.commit();
        model.addAttribute("collection", collection);
        model.addAttribute("policies", policies);
        
        return "pages/admin/authorize-collection-edit";

    }//end submitCollectionDelectPolicy

    @RequestMapping(params = "submit_community_delete_policy")
    protected String submitCommunityDelectPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // delete a permission from a community
        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));

        AuthorizeUtil.authorizeManageCommunityPolicy(context, community);
        ResourcePolicy policy = ResourcePolicy.find(context, Util.getIntParameter(request, "policy_id"));

        // do the remove
        policy.delete();

        // return to collection permission page
        

        List<ResourcePolicy> policies = AuthorizeManager.getPolicies(context, community);
        context.commit();
        model.addAttribute("policies", policies);
        model.addAttribute("community", community);

        return "pages/admin/authorize-community-edit";

    }//end submitCommunityDelectPolicy

    @RequestMapping(params = "submit_collection_edit_policy")
    protected String submitCollectionEditPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // edit a collection's policy - set up and call policy editor
        Collection collection = Collection.find(context, Util.getIntParameter(request, "collection_id"));

        AuthorizeUtil.authorizeManageCollectionPolicy(context, collection);
        int policyId = Util.getIntParameter(request, "policy_id");
        ResourcePolicy policy = null;

        if (policyId == -1) {
            // create new policy
            policy = ResourcePolicy.create(context);
            policy.setResource(collection);
            policy.update();
        } else {
            policy = ResourcePolicy.find(context, policyId);
        }

        Group[] groups = Group.findAll(context, Group.NAME);
        EPerson[] epeople = EPerson.findAll(context, EPerson.EMAIL);

        // return to collection permission page
        context.commit();
        model.addAttribute("edit_title", "Collection " + collection.getID());
        model.addAttribute("policy", policy);
        model.addAttribute("groups", groups);
        model.addAttribute("epeople", epeople);
        model.addAttribute("id_name", "collection_id");
        model.addAttribute("id", "" + collection.getID());
        model.addAttribute("actionText", Constants.actionText);
        
        return "pages/admin/authorize-policy-edit";

    }//end submitCollectionEditPolicy

    @RequestMapping(params = "submit_community_edit_policy")
    protected String submitCommunityEditPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // edit a community's policy - set up and call policy editor
        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));

        AuthorizeUtil.authorizeManageCommunityPolicy(context, community);

        int policyId = Util.getIntParameter(request, "policy_id");
        ResourcePolicy policy = null;

        if (policyId == -1) {
            // create new policy
            policy = ResourcePolicy.create(context);
            policy.setResource(community);
            policy.update();
        } else {
            policy = ResourcePolicy.find(context, policyId);
        }

        Group[] groups = Group.findAll(context, Group.NAME);
        EPerson[] epeople = EPerson.findAll(context, EPerson.EMAIL);

        // return to collection permission page
        context.commit();
        model.addAttribute("edit_title", "Community " + community.getID());
        model.addAttribute("policy", policy);
        model.addAttribute("groups", groups);
        model.addAttribute("epeople", epeople);
        model.addAttribute("id_name", "community_id");
        model.addAttribute("id", "" + community.getID());
        model.addAttribute("actionText", Constants.actionText);
        
        return "pages/admin/authorize-policy-edit";

    }//end submitCommunityEditPolicy

    @RequestMapping(params = "submit_community_add_policy")
    protected String submitCommunityAddPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // want to add a policy, create an empty one and invoke editor
        Community community = Community.find(context, Util.getIntParameter(request, "community_id"));

        AuthorizeUtil.authorizeManageCommunityPolicy(context, community);
        ResourcePolicy policy = ResourcePolicy.create(context);
        policy.setResource(community);
        policy.update();

        Group[] groups = Group.findAll(context, Group.NAME);
        EPerson[] epeople = EPerson.findAll(context, EPerson.EMAIL);
  
        // return to community permission page
        context.commit();
        model.addAttribute("edit_title", "Community " + community.getID());
        model.addAttribute("policy", policy);
        model.addAttribute("groups", groups);
        model.addAttribute("epeople", epeople);
        model.addAttribute("id_name", "community_id");
        model.addAttribute("id", "" + community.getID());
        model.addAttribute("newpolicy", "true");
        model.addAttribute("actionText", Constants.actionText);
        
        return "pages/admin/authorize-policy-edit";

    }//end submitCommunityAddPolicy

    @RequestMapping(params = "submit_save_policy")
    protected String submitSavePolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        int policyId = Util.getIntParameter(request, "policy_id");
        int actionId = Util.getIntParameter(request, "action_id");
        int groupId = Util.getIntParameter(request, "group_id");
        int collectionId = Util.getIntParameter(request, "collection_id");
        int communityId = Util.getIntParameter(request, "community_id");
        
        int itemId = Util.getIntParameter(request, "item_id");

        Item item = null;
        Collection collection = null;
        Community community = null;
        
        ResourcePolicy policy = ResourcePolicy.find(context, policyId);
               
        AuthorizeUtil.authorizeManagePolicy(context, policy);
        
 
        Group group = Group.find(context, groupId);
       
        if (collectionId != -1) {
            
            collection = Collection.find(context, collectionId);

            // modify the policy
            policy.setAction(actionId);
            policy.setGroup(group);
            policy.update();

            // if it is a read, policy, modify the logo policy to match
            if (actionId == Constants.READ) {
                // first get a list of READ policies from collection
                List<ResourcePolicy> rps = AuthorizeManager.getPoliciesActionFilter(context, collection, Constants.READ);

                // remove all bitstream policies, then add READs
                Bitstream bs = collection.getLogo();

                if (bs != null) {
                    AuthorizeManager.removeAllPolicies(context, bs);
                    AuthorizeManager.addPolicies(context, rps, bs);
                }
            }

            // set up page attributes
            context.commit();
            model.addAttribute("collection", collection);
            model.addAttribute("policies", AuthorizeManager.getPolicies(context, collection));
            
            return "pages/admin/authorize-collection-edit";

        } else if (communityId != -1) {
            
            community = Community.find(context, communityId);
            
            // modify the policy
            policy.setAction(actionId);
            policy.setGroup(group);
            policy.update();

            // if it is a read, policy, modify the logo policy to match
            if (actionId == Constants.READ) {
                // first get a list of READ policies from collection
                List<ResourcePolicy> rps = AuthorizeManager.getPoliciesActionFilter(context, community, Constants.READ);

                // remove all bitstream policies, then add READs
                Bitstream bs = community.getLogo();

                if (bs != null) {
                    AuthorizeManager.removeAllPolicies(context, bs);
                    AuthorizeManager.addPolicies(context, rps, bs);
                }
            }

            // set up page attributes
            context.commit();
            model.addAttribute("community", community);
            model.addAttribute("policies", AuthorizeManager.getPolicies(context, community));
            
            return "pages/admin/authorize-community-edit";

        } else if (itemId != -1) {
            item = Item.find(context, itemId);

            // modify the policy
            policy.setAction(actionId);
            policy.setGroup(group);
            policy.update();

            // show edit form!
            prepItemEditForm(context, model, request, item);
            
            return "pages/admin/authorize-item-edit";
        }
        return "";

    }//end submitSavePolicy

    @RequestMapping(params = "submit_cancel_policy")
    protected String submitCancelPolicy(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // delete the policy that we created if it's a new one
        if ((request.getParameter("newpolicy") != null)) {
            int policyId = Util.getIntParameter(request, "policy_id");
            ResourcePolicy rp = ResourcePolicy.find(context, policyId);
            AuthorizeUtil.authorizeManagePolicy(context, rp);
            rp.delete();
        }

        // return to the previous page
        int collectionId = Util.getIntParameter(request, "collection_id");
        int communityId = Util.getIntParameter(request, "community_id");
        int itemId = Util.getIntParameter(request, "item_id");
        String displayPage = null;

        if (collectionId != -1) {
            // set up for return to collection edit page
            Collection t = Collection.find(context, collectionId);
            context.commit();
            model.addAttribute("collection", t);
            model.addAttribute("policies", AuthorizeManager.getPolicies(context, t));
            
            return "pages/admin/authorize-collection-edit";

        } else if (communityId != -1) {
            // set up for return to community edit page
            Community t = Community.find(context, communityId);
            context.commit();
            model.addAttribute("community", t);
            model.addAttribute("policies", AuthorizeManager.getPolicies(context, t));
            
            return "pages/admin/authorize-community-edit";

        } else if (itemId != -1) {
            // set up for return to item edit page
            Item t = Item.find(context, itemId);

            // show edit form!
            prepItemEditForm(context, model, request, t);
            
            return "pages/admin/authorize-item-edit";
        }

        return "";

    }//end submitCancelPolicy

    @RequestMapping(params = "submit_advanced_clear")
    protected String submitAdvancedClear(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        AuthorizeUtil.requireAdminRole(context);
        // remove all policies for a set of objects
        int collectionId = Util.getIntParameter(request, "collection_id");
        int resourceType = Util.getIntParameter(request, "resource_type");

        // if it's to bitstreams, do it to bundles too
        PolicySet.setPolicies(context, Constants.COLLECTION, collectionId,
                resourceType, 0, 0, false, true);

        if (resourceType == Constants.BITSTREAM) {
            PolicySet.setPolicies(context, Constants.COLLECTION, collectionId,
                    Constants.BUNDLE, 0, 0, false, true);
        }

        
        // return to the main page
        return showMainPage(context, request, response);

    }//end submitAdvancedClear

    @RequestMapping(params = "submit_advanced_add")
    protected String submitAdvancedAdd(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        AuthorizeUtil.requireAdminRole(context);
        // add a policy to a set of objects
        int collectionId = Util.getIntParameter(request, "collection_id");
        int resourceType = Util.getIntParameter(request, "resource_type");
        int actionId = Util.getIntParameter(request, "action_id");
        int groupId = Util.getIntParameter(request, "group_id");

        PolicySet.setPolicies(context, Constants.COLLECTION, collectionId, resourceType, actionId, groupId, false, false);

        // if it's a bitstream, do it to the bundle too
        if (resourceType == Constants.BITSTREAM) {
            PolicySet.setPolicies(context, Constants.COLLECTION, collectionId,
                    Constants.BUNDLE, actionId, groupId, false, false);
        }

        
        // return to the main page
        return showMainPage(context, request, response);

    }//end submitAdvancedAdd

    @RequestMapping(params = "submit_collection_select")
    protected String submitCollectionSelect(@RequestAttribute Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {

        // edit the collection's permissions
        Collection collection = Collection.find(context, Util.getIntParameter(
                request, "collection_id"));
        List<ResourcePolicy> policies = AuthorizeManager.getPolicies(context, collection);
        context.commit();
        model.addAttribute("collection", collection);
        model.addAttribute("policies", policies);
        
        return "pages/admin/authorize-collection-edit";

    }//end submitCollectionSelect

    @RequestMapping("/admin/authorize/main")
    protected String showMainPage(@RequestAttribute Context context, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException,
            SQLException, AuthorizeException {
        context.commit();
        return "pages/admin/authorize-main";

    }//end showMainPage

    protected void prepItemEditForm(@RequestAttribute Context context, ModelMap model, HttpServletRequest request, Item item)
            throws SQLException {
        List<ResourcePolicy> itemPolicies = AuthorizeManager.getPolicies(context, item);

        // Put bundle and bitstream policies in their own hashes
        Map<Integer, List<ResourcePolicy>> bundlePolicies = new HashMap<Integer, List<ResourcePolicy>>();
        Map<Integer, List<ResourcePolicy>> bitstreamPolicies = new HashMap<Integer, List<ResourcePolicy>>();

        Bundle[] bundles = item.getBundles();

        for (int i = 0; i < bundles.length; i++) {
            Bundle myBundle = bundles[i];
            List<ResourcePolicy> myPolicies = AuthorizeManager.getPolicies(context, myBundle);

            // add bundle's policies to bundle_policies map
            bundlePolicies.put(Integer.valueOf(myBundle.getID()), myPolicies);

            // go through all bundle's bitstreams, add to bitstream map
            Bitstream[] bitstreams = myBundle.getBitstreams();

            for (int j = 0; j < bitstreams.length; j++) {
                Bitstream myBitstream = bitstreams[j];
                myPolicies = AuthorizeManager.getPolicies(context, myBitstream);
                bitstreamPolicies.put(Integer.valueOf(myBitstream.getID()),
                        myPolicies);
            }
        }
        context.commit();
        model.addAttribute("item", item);
        model.addAttribute("item_policies", itemPolicies);
        model.addAttribute("bundles", bundles);
        model.addAttribute("bundle_policies", bundlePolicies);
        model.addAttribute("bitstream_policies", bitstreamPolicies);
    }//end prepItemEditForm
}// AuthorizeAdminController
