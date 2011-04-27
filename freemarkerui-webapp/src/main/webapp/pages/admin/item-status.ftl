<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#--
    Item Status.
-->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>Item Status: ${item.getMetadata("dc.title")[0].value!"Untitled"}</title>
    </head>
    <body>
        <ul>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/status"/>">Item Status</a></li>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/bitstreams"/>">Item Bitstreams</a></li>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/metadata"/>">Item Metadata</a></li>
            <li><a href="<@dspace.url "/handle/${item.getHandle()}"/>">View Item</a></li>
        </ul>
        <h1>Item Status: ${item.getMetadata("dc.title")[0].value!"Untitled"}</h1>
        <p>Welcome to the item management page. From here you can withdraw, reinstate, move or delete the item. You may also update or add new metadata / bitstreams on the other tabs.</p>

        <table id="aspect_administrative_item_EditItemStatusForm_list_item-info" class="ds-gloss-list">
            <tr class="ds-table-row odd ">
                <td>
                    <span class="ds-gloss-list-label ">Item Internal ID:</span>
                </td>
                <td>${item.getID()!"ERROR"}</td>
            </tr>
            <tr class="ds-table-row even ">
                <td>
                    <span class="ds-gloss-list-label ">Handle:</span>
                </td>
                <td>${item.getHandle()!"None"}</td>
            </tr>
            <tr class="ds-table-row odd ">
                <td>
                    <span class="ds-gloss-list-label ">Last Modified:</span>
                </td>
                <td>${item.getLastModified().toString()!""}</td>
            </tr>
            <tr class="ds-table-row even ">
                <td>
                    <span class="ds-gloss-list-label ">Item Page:</span>
                </td>
                <td>
                    <a href="<@dspace.url "/handle/${item.getHandle()}" />"><@dspace.url "/handle/${item.getHandle()}" /></a>
                </td>
            </tr>
            <tr class="ds-table-row odd ">
                <td>
                    <span class="ds-gloss-list-label ">Edit item's authorization policies:</span>
                </td>
                <td>
                    Authorizations...
                </td>
            </tr>
            <tr class="ds-table-row even ">
                <td>
                    <span class="ds-gloss-list-label ">Withdraw item from the repository:</span>
                </td>
                <td>
                    Withdraw...
                </td>
            </tr>
            <tr class="ds-table-row odd ">
                <td>
                    <span class="ds-gloss-list-label ">Move item to another collection:</span>
                </td>
                <td>
                    Move...
                </td>
            </tr>
            <tr class="ds-table-row even ">
                <td>
                    <span class="ds-gloss-list-label ">Completely expunge item:</span>
                </td>
                <td>
                    Permanently delete
                </td>
            </tr>
        </table>


    </body>
</html>