package org.dspace.webmvc.controller;

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.webmvc.model.TrailEntry;
import org.dspace.webmvc.utils.DSpaceRequestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.dspace.plugin.CommunityHomeProcessor;
import org.dspace.core.PluginManager;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import org.dspace.utils.DSpace;
import org.dspace.authorize.AuthorizeException;
import java.net.URLEncoder;
import org.dspace.usage.UsageEvent;
import org.dspace.core.Context;
import org.apache.log4j.Logger;
import org.dspace.core.LogManager;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.webmvc.utils.Authenticate;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.dspace.content.crosswalk.DisseminationCrosswalk;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.output.XMLOutputter;
import org.dspace.content.crosswalk.CrosswalkException;
import org.dspace.plugin.CollectionHomeProcessor;
import org.dspace.app.util.GoogleMetadata;
import java.io.StringWriter;
import org.dspace.browse.BrowseException;
import org.dspace.eperson.Group;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Subscribe;
import org.dspace.browse.BrowseIndex;
import org.dspace.browse.ItemCountException;
import org.dspace.browse.ItemCounter;
import org.dspace.content.Bitstream;
import org.dspace.core.ConfigurationManager;
import org.dspace.webmvc.utils.UIUtil;

@Controller
public class RenderDSpaceObjectController {

    /** log4j category */
    private static Logger log = Logger.getLogger(RenderDSpaceObjectController.class);
    /** For obtaining &lt;meta&gt; elements to put in the &lt;head&gt; */
    private DisseminationCrosswalk xHTMLHeadCrosswalk = (DisseminationCrosswalk) PluginManager.getNamedPlugin(DisseminationCrosswalk.class, "XHTML_HEAD_ITEM");

    @RequestMapping("/renderObject")
    public String renderObject(ModelMap model, HttpServletRequest request, HttpServletResponse response, Context context, @RequestParam(value = "", required = false, defaultValue = "") String button)
            throws SQLException, ServletException, IOException, AuthorizeException {

        DSpaceObject dso = DSpaceRequestUtils.getScopeObject(request);

        List<TrailEntry> trailList = buildTrail(dso.getParentObject());
        trailList.add(TrailEntry.createWithDSpaceObject(dso));
        model.addAttribute("trailList", trailList);
        String handle = null;

        String path = request.getPathInfo();
        String extraPathInfo = null;

        if (path != null) {
            // substring(1) is to remove initial '/'
            path = path.substring(1);

            try {
                // Extract the Handle
                int firstSlash = path.indexOf('/');
                int secondSlash = path.indexOf('/', firstSlash + 1);

                if (secondSlash != -1) {
                    // We have extra path info
                    handle = path.substring(0, secondSlash);
                    extraPathInfo = path.substring(secondSlash);
                } else {
                    // The path is just the Handle
                    handle = path;
                }
            } catch (NumberFormatException nfe) {
                // Leave handle as null
            }//end catch
        }//end if path!=null

        if (dso != null) {
            switch (dso.getType()) {
                case Constants.COLLECTION:
                    Collection col = (Collection) dso;
                    /*
                     * Find the "parent" community the collection, mainly for
                     * "breadcrumbs" FIXME: At the moment, just grab the first community
                     * the collection is in. This should probably be more context
                     * sensitive when we have multiple inclusion.
                     */
                    Community[] parents = col.getCommunities();
                    model.addAttribute("dspacecommunity", parents[0]);

                    /*
                     * Find all the "parent" communities for the collection for
                     * "breadcrumbs"
                     */
                    model.addAttribute("dspacecommunities", getParents(parents[0],
                            true));

                    new DSpace().getEventService().fireEvent(
                            new UsageEvent(
                            UsageEvent.Action.VIEW,
                            request,
                            context,
                            dso));
                    model.addAttribute("collection", (Collection) dso);
                    return collectionHome(context, model, request, response, parents[0], col, button);
                //return "pages/collection";

                case Constants.COMMUNITY:
                    Community c = (Community) dso;
                    model.addAttribute("community", (Community) dso);
                    /*
                     * Find all the "parent" communities for the community
                     */
                    model.addAttribute("dspacecommunities", getParents(c, false));
                    new DSpace().getEventService().fireEvent(new UsageEvent(
                            UsageEvent.Action.VIEW,
                            request,
                            context,
                            dso));
                    try {
                        return communityHome(context, model, request, response, c, button);
                    } catch (ServletException serEx) {
                        serEx.getMessage();
                    } catch (IOException ioEx) {
                        ioEx.getMessage();
                    } catch (SQLException sqlEx) {
                        sqlEx.getMessage();
                    }
                // return "pages/community-home";

                case Constants.ITEM:
                    Item item = (Item) dso;
                    model.addAttribute("item", (Item) dso);
                    new DSpace().getEventService().fireEvent(new UsageEvent(
                            UsageEvent.Action.VIEW,
                            request,
                            context,
                            dso));

                    // Only use last-modified if this is an anonymous access
                    // - caching content that may be generated under authorisation
                    //   is a security problem
                    if (context.getCurrentUser() == null) {
                        response.setDateHeader("Last-Modified", item.getLastModified().getTime());

                        // Check for if-modified-since header
                        long modSince = request.getDateHeader("If-Modified-Since");

                        if (modSince != -1 && item.getLastModified().getTime() < modSince) {
                            // Item has not been modified since requested date,
                            // hence bitstream has not; return 304
                            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                        } else {
                            // Display the item page
                            return displayItem(context, model, request, response, item, handle);
                        }
                    } else {
                        // Display the item page
                        return displayItem(context, model, request, response, item, handle);
                    }
                //return "pages/item";

                default:
                    return "pages/unknowntype";
            }
        }

        return "pages/invalidhandle";
    }

    private List<TrailEntry> buildTrail(DSpaceObject dso) throws SQLException {
        if (dso != null) {
            List<TrailEntry> trail = buildTrail(dso.getParentObject());
            trail.add(TrailEntry.createWithDSpaceObject(dso));
            return trail;
        }

        return new ArrayList<TrailEntry>();
    }

    private Community[] getParents(Community c, boolean include)
            throws SQLException {
        // Find all the "parent" communities for the community
        Community[] parents = c.getAllParents();

        // put into an array in reverse order
        int revLength = include ? (parents.length + 1) : parents.length;
        Community[] reversedParents = new Community[revLength];
        int index = parents.length - 1;

        for (int i = 0; i < parents.length; i++) {
            reversedParents[i] = parents[index - i];
        }

        if (include) {
            reversedParents[revLength - 1] = c;
        }

        return reversedParents;
    }

    private String communityHome(Context context, ModelMap model, HttpServletRequest request, HttpServletResponse response, Community community, String button)
            throws ServletException, IOException, SQLException {
        // Handle click on a browse or search button
        if (!handleButton(request, community.getHandle(), button)) {
            // No button pressed, display community home page
            log.info(LogManager.getHeader(context, "view_community",
                    "community_id=" + community.getID()));

            // Get the collections within the community
            Collection[] collections = community.getCollections();

            // get any subcommunities of the community
            Community[] subcommunities = community.getSubcommunities();

            // perform any necessary pre-processing
            // preProcessCommunityHome(context, request, response, community);

            // is the user a COMMUNITY_EDITOR?
            if (community.canEditBoolean()) {
                // set a variable to create an edit button
                model.addAttribute("editor_button", Boolean.TRUE);
            }

            // can they add to this community?
            if (AuthorizeManager.authorizeActionBoolean(context, community,
                    Constants.ADD)) {
                // set a variable to create an edit button
                model.addAttribute("add_button", Boolean.TRUE);
            }

            // can they remove from this community?
            if (AuthorizeManager.authorizeActionBoolean(context, community,
                    Constants.REMOVE)) {
                // set a variable to create an edit button
                model.addAttribute("remove_button", Boolean.TRUE);
            }

            // Forward to community home page
            model.addAttribute("community", community);
            model.addAttribute("collections", collections);
            model.addAttribute("subcommunities", subcommunities);

            try {
                // get the browse indices
                BrowseIndex[] bis = BrowseIndex.getBrowseIndices();
                ItemCounter ic = new ItemCounter(UIUtil.obtainContext(request));
                model.addAttribute("bis", bis);
                model.addAttribute("ic", ic);
            } catch (BrowseException browseEx) {
                browseEx.getLocalizedMessage();
            } catch (ItemCountException itemCountEx) {
                itemCountEx.getLocalizedMessage();
            }

            // Put the metadata values into guaranteed non-null variables
            String name = community.getMetadata("name");
            String intro = community.getMetadata("introductory_text");
            String copyright = community.getMetadata("copyright_text");
            String sidebar = community.getMetadata("side_bar_text");
            Bitstream logo = community.getLogo();

            boolean feedEnabled = ConfigurationManager.getBooleanProperty("webui.feed.enable");
            boolean strengthsShow = ConfigurationManager.getBooleanProperty("webui.strengths.show");
            String feedData = "NONE";
            if (feedEnabled) {
                feedData = "comm:" + ConfigurationManager.getProperty("webui.feed.formats");
            }
            model.addAttribute("name", name);
            model.addAttribute("intro", intro);
            model.addAttribute("copyright", copyright);
            model.addAttribute("sidebar", sidebar);
            model.addAttribute("logo", logo);
            model.addAttribute("feedData", feedData);
            model.addAttribute("feedEnabled", feedEnabled);
            model.addAttribute("strengthsShow", strengthsShow);

            return "pages/community-home";

        }//end if
        else {

            return "redirect:" + returnURL(request, response, community.getHandle(), button);
        }
    }

    private String collectionHome(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, Community community,
            Collection collection, String button) throws ServletException, IOException,
            SQLException, AuthorizeException {
        // Handle click on a browse or search button
        if (!handleButton(request, collection.getHandle(), button)) {
            // Will need to know whether to commit to DB
            boolean updated = false;

            // No search or browse button pressed, check for
            if (request.getParameter("submit_subscribe") != null) {
                // Subscribe button pressed.
                // Only registered can subscribe, so redirect unless logged in.
                if (context.getCurrentUser() == null && !Authenticate.startAuthentication(context, request, response)) {

                    return Authenticate.redirectLoginPage(context, request, response);

                } else {
                    Subscribe.subscribe(context, context.getCurrentUser(),
                            collection);
                    updated = true;
                }
            } else if (request.getParameter("submit_unsubscribe") != null) {
                Subscribe.unsubscribe(context, context.getCurrentUser(),
                        collection);
                updated = true;
            }

            // display collection home page
            log.info(LogManager.getHeader(context, "view_collection",
                    "collection_id=" + collection.getID()));

            // perform any necessary pre-processing
            // preProcessCollectionHome(context, request, response, collection);

            // Is the user logged in/subscribed?
            EPerson e = context.getCurrentUser();
            boolean subscribed = false;

            if (e != null) {
                subscribed = Subscribe.isSubscribed(context, e, collection);

                // is the user a COLLECTION_EDITOR?
                if (collection.canEditBoolean(true)) {
                    // set a variable to create an edit button
                    model.addAttribute("editor_button", Boolean.TRUE);
                }

                // can they admin this collection?
                if (AuthorizeManager.authorizeActionBoolean(context,
                        collection, Constants.ADMIN)) {
                    model.addAttribute("admin_button", Boolean.TRUE);

                    // give them a button to manage submitter list
                    // what group is the submitter?
                    Group group = collection.getSubmitters();

                    if (group != null) {
                        model.addAttribute("submitters", group);
                    }
                }

                // can they submit to this collection?
                if (AuthorizeManager.authorizeActionBoolean(context,
                        collection, Constants.ADD)) {
                    model.addAttribute("can_submit_button",
                            Boolean.TRUE);

                } else {
                    model.addAttribute("can_submit_button",
                            Boolean.FALSE);
                }
            }

            // Forward to collection home page
            model.addAttribute("collection", collection);
            model.addAttribute("community", community);
            model.addAttribute("loggedin", Boolean.valueOf(e != null));
            model.addAttribute("subscribed", Boolean.valueOf(subscribed));

            try {
                // get the browse indices
                BrowseIndex[] bis = BrowseIndex.getBrowseIndices();
                ItemCounter ic = new ItemCounter(UIUtil.obtainContext(request));
                model.addAttribute("bis", bis);
                model.addAttribute("ic", ic);
            } catch (BrowseException browseEx) {
                browseEx.getLocalizedMessage();
            } catch (ItemCountException itemCountEx) {
                itemCountEx.getLocalizedMessage();
            }

            // Put the metadata values into guaranteed non-null variables
            String name = collection.getMetadata("name");
            String intro = collection.getMetadata("introductory_text") == null ? "" : collection.getMetadata("introductory_text");

            String copyright = collection.getMetadata("copyright_text") == null ? "" : collection.getMetadata("copyright_text");
            String sidebar = collection.getMetadata("side_bar_text") == null ? "" : collection.getMetadata("side_bar_text");

            String communityName = community.getMetadata("name");
            String communityLink = "/handle/" + community.getHandle();

            Bitstream logo = collection.getLogo();

            boolean strengthsShow = ConfigurationManager.getBooleanProperty("webui.strengths.show");
            boolean feedEnabled = ConfigurationManager.getBooleanProperty("webui.feed.enable");

            String feedData = "NONE";
            if (feedEnabled) {
                feedData = "coll:" + ConfigurationManager.getProperty("webui.feed.formats");
            }
            model.addAttribute("name", name);
            model.addAttribute("intro", intro);
            model.addAttribute("copyright", copyright);
            model.addAttribute("sidebar", sidebar);
            model.addAttribute("logo", logo);
            model.addAttribute("feedData", feedData);
            model.addAttribute("feedEnabled", feedEnabled);
            model.addAttribute("strengthsShow", strengthsShow);
            
            model.addAttribute("communityName", communityName);
            model.addAttribute("communityLink", communityLink);


            if (updated) {
                context.commit();
            }
            return "pages/collection-home";
        } else {

            return "redirect:" + returnURL(request, response, collection.getHandle(), button);
        }
    }

    private void preProcessCollectionHome(Context context, HttpServletRequest request,
            HttpServletResponse response, Collection collection)
            throws ServletException, IOException, SQLException {
        try {
            CollectionHomeProcessor[] chp = (CollectionHomeProcessor[]) PluginManager.getPluginSequence(CollectionHomeProcessor.class);
            for (int i = 0; i < chp.length; i++) {
                chp[i].process(context, request, response, collection);
            }
        } catch (Exception e) {
            log.error("caught exception: ", e);
            throw new ServletException(e);
        }
    }

    private void preProcessCommunityHome(Context context, HttpServletRequest request,
            HttpServletResponse response, Community community)
            throws ServletException, IOException, SQLException {
        try {
            CommunityHomeProcessor[] chp = (CommunityHomeProcessor[]) PluginManager.getPluginSequence(CommunityHomeProcessor.class);
            for (int i = 0; i < chp.length; i++) {
                chp[i].process(context, request, response, community);
            }
        } catch (Exception e) {
            log.error("caught exception: ", e);
            //e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private boolean handleButton(HttpServletRequest request, String handle, String button) throws IOException {
        //String button = Util.getSubmitButton(request, "");
        String location = request.getParameter("location");
        String prefix = "/";
        String url = null;

        if (location == null) {
            return false;
        }

        /*
         * Work out the "prefix" to which to redirect If "/", scope is all of
         * DSpace, so prefix is "/" If prefix is a handle, scope is a community
         * or collection, so "/handle/1721.x/xxxx/" is the prefix.
         */
        if (!location.equals("/")) {
            prefix = "/handle/" + location + "/";
        }

        if (button.equals("submit_search")
                || (request.getParameter("query") != null)) {
            /*
             * Have to check for search button and query - in some browsers,
             * typing a query into the box and hitting return doesn't produce a
             * submit button parameter. Redirect to appropriate search page
             */
            return true;
        }

        return false;
    }//end handleButton

    private String returnURL(HttpServletRequest request, HttpServletResponse response, String handle, @RequestParam(value = "", required = false, defaultValue = "") String button) throws IOException {

        //String button = Util.getSubmitButton(request, "");
        String location = request.getParameter("location");
        String prefix = "/";
        String url = null;


        /*
         * Work out the "prefix" to which to redirect If "/", scope is all of
         * DSpace, so prefix is "/" If prefix is a handle, scope is a community
         * or collection, so "/handle/1721.x/xxxx/" is the prefix.
         */
        if (!location.equals("/")) {
            prefix = "/handle/" + location + "/";
        }

        if (button.equals("submit_search")
                || (request.getParameter("query") != null)) {
            /*
             * Have to check for search button and query - in some browsers,
             * typing a query into the box and hitting return doesn't produce a
             * submit button parameter. Redirect to appropriate search page
             */
            url = prefix + "simple-search?query=" + URLEncoder.encode(request.getParameter("query"), Constants.DEFAULT_ENCODING);
        }

        // If a button was pressed, redirect to appropriate page
        if (url != null) {

            return response.encodeRedirectURL(url);
        }

        return "";

    }//end returnURL

    private String displayItem(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, Item item, String handle)
            throws ServletException, IOException, SQLException,
            AuthorizeException {
        // Tombstone?
        if (item.isWithdrawn()) {

            return "tombstone";
        }

        // Ensure the user has authorisation
        AuthorizeManager.authorizeAction(context, item, Constants.READ);

        log.info(LogManager.getHeader(context, "view_item", "handle="
                + handle));

        // show edit link
        if (item.canEdit()) {
            // set a variable to create an edit button
            request.setAttribute("admin_button", Boolean.TRUE);
        }

        // Get the collections
        Collection[] collections = item.getCollections();

        // For the breadcrumbs, get the first collection and the first community
        // that is in. FIXME: Not multiple-inclusion friendly--should be
        // smarter, context-sensitive

        model.addAttribute("dspacecollection", item.getOwningCollection());

        Community[] comms = item.getOwningCollection().getCommunities();

        model.addAttribute("dspacecommunity", comms[0]);

        /*
         * Find all the "parent" communities for the collection
         */

        model.addAttribute("dspacecommunities", getParents(comms[0], true));

        // Full or simple display?
        boolean displayAll = false;
        String modeParam = request.getParameter("mode");

        if ((modeParam != null) && modeParam.equalsIgnoreCase("full")) {
            displayAll = true;
        }

        String headMetadata = "";

        // Produce <meta> elements for header from crosswalk
        try {
            List<Element> l = xHTMLHeadCrosswalk.disseminateList(item);
            StringWriter sw = new StringWriter();

            XMLOutputter xmlo = new XMLOutputter();
            xmlo.output(new Text("\n"), sw);
            for (Element e : l) {
                // FIXME: we unset the Namespace so it's not printed.
                // This is fairly yucky, but means the same crosswalk should
                // work for Manakin as well as the JSP-based UI.
                e.setNamespace(null);
                xmlo.output(e, sw);
                xmlo.output(new Text("\n"), sw);
            }
            boolean googleEnabled = ConfigurationManager.getBooleanProperty("google-metadata.enable", false);
            if (googleEnabled) {
                // Add Google metadata field names & values
                GoogleMetadata gmd = new GoogleMetadata(context, item);
                xmlo.output(new Text("\n"), sw);

                for (Element e : gmd.disseminateList()) {
                    xmlo.output(e, sw);
                    xmlo.output(new Text("\n"), sw);
                }
            }
            headMetadata = sw.toString();
        } catch (CrosswalkException ce) {
            throw new ServletException(ce);
        }

        // Enable suggest link or not
        boolean suggestEnable = false;
        if (!ConfigurationManager.getBooleanProperty("webui.suggest.enable")) {
            // do nothing, the suggestLink is allready set to false 
        } else {
            // it is in general enabled
            suggestEnable = true;

            // check for the enable only for logged in users option
            if (!ConfigurationManager.getBooleanProperty("webui.suggest.loggedinusers.only")) {
                // do nothing, the suggestLink stays as it is
            } else {
                // check whether there is a logged in user
                suggestEnable = (context.getCurrentUser() == null ? false : true);
            }
        }

        // Set attributes and display
        model.addAttribute("suggestenable", Boolean.valueOf(suggestEnable));
        model.addAttribute("displayall", Boolean.valueOf(displayAll));
        model.addAttribute("item", item);
        model.addAttribute("collections", collections);
        model.addAttribute("dspacelayouthead", headMetadata);

        return "display-item";
    }
}
