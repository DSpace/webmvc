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
    View and edit Item Metadata
-->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>${item.getMetadata("dc.title")[0].value!"Untitled"}</title>
    </head>
    <body>
        <ul>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/status"/>">Item Status</a></li>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/bitstreams"/>">Item Bitstreams</a></li>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/metadata"/>">Item Metadata</a></li>
            <li><a href="<@dspace.url "/handle/${item.getHandle()}"/>">View Item</a></li>
        </ul>
        <h1>Metadata: ${item.getMetadata("dc.title")[0].value!"Untitled"}</h1>
        <h2>Add new metadata</h2>
        <p>Not yet implemented</p>
        <hr/>
        <h2>Current Metadata</h2>
        <p>Not yet implemented</p>


    </body>
</html>