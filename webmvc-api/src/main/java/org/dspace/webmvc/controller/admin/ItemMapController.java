/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dspace.webmvc.controller.admin;

import org.dspace.core.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Collection;
import org.dspace.app.util.Util;
import org.dspace.core.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;
import org.dspace.browse.*;
import org.dspace.sort.SortOption;
import org.dspace.core.ConfigurationManager;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Robert Qin
 */
@Controller
public class ItemMapController {

    /** Logger */
    private static Logger log = Logger.getLogger(ItemMapController.class);

    @RequestMapping(method = RequestMethod.GET)
    protected String processGet(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws java.sql.SQLException,
            javax.servlet.ServletException, java.io.IOException,
            AuthorizeException {

        // get with no action parameter set means to put up the main page
        // which is statistics and some command buttons to add/remove items
        int myID = Util.getIntParameter(request, "cid");

        // get collection
        Collection myCollection = Collection.find(context, myID);

        // authorize check
        AuthorizeManager.authorizeAction(context, myCollection, Constants.ADMIN);
        int count_native = 0; // # of items owned by this collection
        int count_import = 0; // # of virtual items
        Map<Integer, Item> myItems = new HashMap<Integer, Item>(); // # for the browser
        Map<Integer, Collection> myCollections = new HashMap<Integer, Collection>(); // collections for list
        Map<Integer, Integer> myCounts = new HashMap<Integer, Integer>(); // counts for each collection

        // get all items from that collection, add them to a hash
        ItemIterator i = myCollection.getItems();
        try {
            // iterate through the items in this collection, and count how many
            // are native, and how many are imports, and which collections they
            // came from
            while (i.hasNext()) {
                Item myItem = i.next();

                // get key for hash
                Integer myKey = Integer.valueOf(myItem.getID());

                if (myItem.isOwningCollection(myCollection)) {
                    count_native++;
                } else {
                    count_import++;
                }

                // is the collection in the hash?
                Collection owningCollection = myItem.getOwningCollection();
                Integer cKey = Integer.valueOf(owningCollection.getID());

                if (myCollections.containsKey(cKey)) {
                    Integer x = myCounts.get(cKey);
                    int myCount = x.intValue() + 1;

                    // increment count for that collection
                    myCounts.put(cKey, Integer.valueOf(myCount));
                } else {
                    // store and initialize count
                    myCollections.put(cKey, owningCollection);
                    myCounts.put(cKey, Integer.valueOf(1));
                }

                // store the item
                myItems.put(myKey, myItem);
            }
        } finally {
            if (i != null) {
                i.close();
            }
        }

        // remove this collection's entry because we already have a native
        // count
        myCollections.remove(Integer.valueOf(myCollection.getID()));

        // sort items - later
        // show page
        model.addAttribute("collection", myCollection);
        model.addAttribute("count_native", Integer.valueOf(count_native));
        model.addAttribute("count_import", Integer.valueOf(count_import));
        model.addAttribute("items", myItems);
        model.addAttribute("collections", myCollections);
        model.addAttribute("collection_counts", myCounts);
        model.addAttribute("all_collections", Collection.findAll(context));

        // show this page when we're done           
        context.commit();
        // show the page

        Iterator colKeys = myCollections.keySet().iterator();
        model.addAttribute("colKeys", colKeys);
       
        return "tools/itemmap-main";

    }//end processGet
    
    @RequestMapping(method = RequestMethod.POST, params = "item_mapper")
    protected String submitContinue(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws java.sql.SQLException,
            javax.servlet.ServletException, java.io.IOException,
            AuthorizeException {

        // get with no action parameter set means to put up the main page
        // which is statistics and some command buttons to add/remove items
        int myID = Util.getIntParameter(request, "cid");

        // get collection
        Collection myCollection = Collection.find(context, myID);

        // authorize check
        AuthorizeManager.authorizeAction(context, myCollection, Constants.ADMIN);
        int count_native = 0; // # of items owned by this collection
        int count_import = 0; // # of virtual items
        Map<Integer, Item> myItems = new HashMap<Integer, Item>(); // # for the browser
        Map<Integer, Collection> myCollections = new HashMap<Integer, Collection>(); // collections for list
        Map<Integer, Integer> myCounts = new HashMap<Integer, Integer>(); // counts for each collection

        // get all items from that collection, add them to a hash
        ItemIterator i = myCollection.getItems();
        try {
            // iterate through the items in this collection, and count how many
            // are native, and how many are imports, and which collections they
            // came from
            while (i.hasNext()) {
                Item myItem = i.next();

                // get key for hash
                Integer myKey = Integer.valueOf(myItem.getID());

                if (myItem.isOwningCollection(myCollection)) {
                    count_native++;
                } else {
                    count_import++;
                }

                // is the collection in the hash?
                Collection owningCollection = myItem.getOwningCollection();
                Integer cKey = Integer.valueOf(owningCollection.getID());

                if (myCollections.containsKey(cKey)) {
                    Integer x = myCounts.get(cKey);
                    int myCount = x.intValue() + 1;

                    // increment count for that collection
                    myCounts.put(cKey, Integer.valueOf(myCount));
                } else {
                    // store and initialize count
                    myCollections.put(cKey, owningCollection);
                    myCounts.put(cKey, Integer.valueOf(1));
                }

                // store the item
                myItems.put(myKey, myItem);
            }
        } finally {
            if (i != null) {
                i.close();
            }
        }

        // remove this collection's entry because we already have a native
        // count
        myCollections.remove(Integer.valueOf(myCollection.getID()));

        // sort items - later
        // show page
        model.addAttribute("collection", myCollection);
        model.addAttribute("count_native", Integer.valueOf(count_native));
        model.addAttribute("count_import", Integer.valueOf(count_import));
        model.addAttribute("items", myItems);
        model.addAttribute("collections", myCollections);
        model.addAttribute("collection_counts", myCounts);
        model.addAttribute("all_collections", Collection.findAll(context));

        // show this page when we're done           
        context.commit();
        // show the page

        Iterator colKeys = myCollections.keySet().iterator();
        model.addAttribute("colKeys", colKeys);
         
        return "tools/itemmap-main";

    }//end submitContinue

    @RequestMapping(method = RequestMethod.POST, params = "cancel")
    protected String submitCancel(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws java.sql.SQLException,
            javax.servlet.ServletException, java.io.IOException,
            AuthorizeException {

        Collection myCollection = processPost(context, model, request, response);

        int count_native = 0; // # of items owned by this collection
        int count_import = 0; // # of virtual items
        Map<Integer, Item> myItems = new HashMap<Integer, Item>(); // # for the browser
        Map<Integer, Collection> myCollections = new HashMap<Integer, Collection>(); // collections for list
        Map<Integer, Integer> myCounts = new HashMap<Integer, Integer>(); // counts for each collection

        // get all items from that collection, add them to a hash
        ItemIterator i = myCollection.getItems();
        try {
            // iterate through the items in this collection, and count how many
            // are native, and how many are imports, and which collections they
            // came from
            while (i.hasNext()) {
                Item myItem = i.next();

                // get key for hash
                Integer myKey = Integer.valueOf(myItem.getID());

                if (myItem.isOwningCollection(myCollection)) {
                    count_native++;
                } else {
                    count_import++;
                }

                // is the collection in the hash?
                Collection owningCollection = myItem.getOwningCollection();
                Integer cKey = Integer.valueOf(owningCollection.getID());

                if (myCollections.containsKey(cKey)) {
                    Integer x = myCounts.get(cKey);
                    int myCount = x.intValue() + 1;

                    // increment count for that collection
                    myCounts.put(cKey, Integer.valueOf(myCount));
                } else {
                    // store and initialize count
                    myCollections.put(cKey, owningCollection);
                    myCounts.put(cKey, Integer.valueOf(1));
                }

                // store the item
                myItems.put(myKey, myItem);
            }
        } finally {
            if (i != null) {
                i.close();
            }
        }

        // remove this collection's entry because we already have a native
        // count
        myCollections.remove(Integer.valueOf(myCollection.getID()));

        // sort items - later
        // show page
        model.addAttribute("collection", myCollection);
        model.addAttribute("count_native", Integer.valueOf(count_native));
        model.addAttribute("count_import", Integer.valueOf(count_import));
        model.addAttribute("items", myItems);
        model.addAttribute("collections", myCollections);
        model.addAttribute("collection_counts", myCounts);
        model.addAttribute("all_collections", Collection.findAll(context));

        // show this page when we're done      
        context.commit();
        
        Iterator colKeys = myCollections.keySet().iterator();
        model.addAttribute("colKeys", colKeys);
                
        // show the page
        return "tools/itemmap-main";
    }//end submit cancel

    @RequestMapping(method = RequestMethod.POST, params = "remove")
    protected String submitRemove(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws java.sql.SQLException,
            javax.servlet.ServletException, java.io.IOException,
            AuthorizeException {

        Collection myCollection = processPost(context, model, request, response);

        // get item IDs to remove
        String[] itemIDs = request.getParameterValues("item_ids");
        String message = "remove";
        LinkedList<String> removedItems = new LinkedList<String>();

        if (itemIDs == null) {
            message = "none-removed";
        } else {
            for (int j = 0; j < itemIDs.length; j++) {
                int i = Integer.parseInt(itemIDs[j]);
                removedItems.add(itemIDs[j]);

                Item myItem = Item.find(context, i);

                // make sure item doesn't belong to this collection
                if (!myItem.isOwningCollection(myCollection)) {
                    myCollection.removeItem(myItem);
                    try {
                        IndexBrowse ib = new IndexBrowse(context);
                        ib.indexItem(myItem);
                    } catch (BrowseException e) {
                        log.error("caught exception: ", e);
                        throw new ServletException(e);
                    }
                }
            }
        }

        model.addAttribute("message", message);
        model.addAttribute("collection", myCollection);
        model.addAttribute("processedItems", removedItems);

        // show this page when we're done
        context.commit();

        // show the page
        // JSPManager.showJSP(request, response, jspPage);
        return "tools/itemmap-info";

    }//end submit remove

    @RequestMapping(method = RequestMethod.POST, params = "add")
    protected String submitAdd(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws java.sql.SQLException,
            javax.servlet.ServletException, java.io.IOException,
            AuthorizeException {

        Collection myCollection = processPost(context, model, request, response);

        // get item IDs to add
        String[] itemIDs = request.getParameterValues("item_ids");
        String message = "added";
        LinkedList<String> addedItems = new LinkedList<String>();


        if (itemIDs == null) {
            message = "none-selected";
        } else {
            for (int j = 0; j < itemIDs.length; j++) {
                int i = Integer.parseInt(itemIDs[j]);

                Item myItem = Item.find(context, i);

                if (AuthorizeManager.authorizeActionBoolean(context, myItem, Constants.READ)) {
                    // make sure item doesn't belong to this collection
                    if (!myItem.isOwningCollection(myCollection)) {
                        myCollection.addItem(myItem);
                        try {
                            IndexBrowse ib = new IndexBrowse(context);
                            ib.indexItem(myItem);
                        } catch (BrowseException e) {
                            log.error("caught exception: ", e);
                            throw new ServletException(e);
                        }
                        addedItems.add(itemIDs[j]);
                    }
                }
            }
        }

        model.addAttribute("message", message);
        model.addAttribute("collection", myCollection);
        model.addAttribute("processedItems", addedItems);

        // show this page when we're done
        context.commit();

        // show the page
        return "tools/itemmap-info";
    }//end submit add

    @RequestMapping(method = RequestMethod.POST, params = "search_authors")
    protected String submitSearchAuthors(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws java.sql.SQLException,
            javax.servlet.ServletException, java.io.IOException,
            AuthorizeException {

        Collection myCollection = processPost(context, model, request, response);

        String name = (String) request.getParameter("namepart");
        String bidx = ConfigurationManager.getProperty("itemmap.author.index");
        if (bidx == null) {
            throw new ServletException("There is no configuration for itemmap.author.index");
        }
        Map<Integer, Item> items = new HashMap<Integer, Item>();
        try {
            BrowserScope bs = new BrowserScope(context);
            BrowseIndex bi = BrowseIndex.getBrowseIndex(bidx);

            // set up the browse scope
            bs.setBrowseIndex(bi);
            bs.setOrder(SortOption.ASCENDING);
            bs.setFilterValue(name);
            bs.setFilterValuePartial(true);
            bs.setJumpToValue(null);
            bs.setResultsPerPage(10000);	// an arbitrary number (large) for the time being
            bs.setBrowseLevel(1);

            BrowseEngine be = new BrowseEngine(context);
            BrowseInfo results = be.browse(bs);
            Item[] browseItems = results.getItemResults(context);

            // FIXME: oh god this is so annoying - what an API /Richard
            // we need to deduplicate against existing items in this collection
            ItemIterator itr = myCollection.getItems();
            try {
                ArrayList<Integer> idslist = new ArrayList<Integer>();
                while (itr.hasNext()) {
                    idslist.add(Integer.valueOf(itr.nextID()));
                }

                for (int i = 0; i < browseItems.length; i++) {
                    // only if it isn't already in this collection
                    if (!idslist.contains(Integer.valueOf(browseItems[i].getID()))) {
                        // only put on list if you can read item
                        if (AuthorizeManager.authorizeActionBoolean(context, browseItems[i], Constants.READ)) {
                            items.put(Integer.valueOf(browseItems[i].getID()), browseItems[i]);
                        }
                    }
                }
            } finally {
                if (itr != null) {
                    itr.close();
                }
            }
        } catch (BrowseException e) {
            log.error("caught exception: ", e);
            throw new ServletException(e);
        }

        model.addAttribute("collection", myCollection);
        model.addAttribute("browsetext", name);
        model.addAttribute("items", items);
        model.addAttribute("browsetype", "Add");

        context.commit();
        
        Iterator iteratorSet = items.keySet().iterator();
        List<Item> itemList = new ArrayList<Item>();
        while(iteratorSet.hasNext())
        {
          Item item = (Item)items.get(iteratorSet.next());
          itemList.add(item);
                   
        }//end while
        model.addAttribute("itemList", itemList);
        
        return "tools/itemmap-browse";

    }//end submit search authors

    @RequestMapping("/tools/itemmap/browse/{cid}/{t}")
    protected String submitBrowse(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response, @PathVariable(value = "cid") Integer cid, @PathVariable(value = "t") Integer t) throws java.sql.SQLException,
            javax.servlet.ServletException, java.io.IOException,
            AuthorizeException {

        // Collection myCollection = processPost(context, model, request, response);

        // target collection to browse
        // int t = Util.getIntParameter(request, "t");
        // get collection
        Collection myCollection = Collection.find(context, cid);
        // authorize check
        AuthorizeManager.authorizeAction(context, myCollection, Constants.ADMIN);
        Collection targetCollection = Collection.find(context, t);

        // now find all imported items from that collection
        // seemingly inefficient, but database should have this query cached
        Map<Integer, Item> items = new HashMap<Integer, Item>();
        ItemIterator i = myCollection.getItems();
        try {
            while (i.hasNext()) {
                Item myItem = i.next();

                if (myItem.isOwningCollection(targetCollection)) {
                    Integer myKey = Integer.valueOf(myItem.getID());
                    items.put(myKey, myItem);
                }
            }
        } finally {
            if (i != null) {
                i.close();
            }
        }

        model.addAttribute("collection", myCollection);
        model.addAttribute("browsetext", targetCollection.getMetadata("name"));
        model.addAttribute("items", items);
        model.addAttribute("browsetype", "Remove");
        context.commit();

        Iterator iteratorSet = items.keySet().iterator();
        List<Item> itemList = new ArrayList<Item>();
        while(iteratorSet.hasNext())
        {
          Item item = (Item)items.get(iteratorSet.next());
          itemList.add(item);
                   
        }//end while
        model.addAttribute("itemList", itemList);
        // show the page
        return "tools/itemmap-browse";

    }//end submit browse

    protected Collection processPost(Context context, ModelMap model, HttpServletRequest request,
            HttpServletResponse response) throws java.sql.SQLException,
            javax.servlet.ServletException, java.io.IOException,
            AuthorizeException {


        // get with a collection ID means put up browse window
        int myID = Util.getIntParameter(request, "cid");

        // get collection
        Collection myCollection = Collection.find(context, myID);

        // authorize check
        AuthorizeManager.authorizeAction(context, myCollection, Constants.ADMIN);

        return myCollection;

    }//end processPost
}
