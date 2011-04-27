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
    View and edit Item Bitstreams
-->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>Item Bitstreams: ${item.getMetadata("dc.title")[0].value!"Untitled"}</title>
    </head>
    <body>
        <ul>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/status"/>">Item Status</a></li>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/bitstreams"/>">Item Bitstreams</a></li>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/metadata"/>">Item Metadata</a></li>
            <li><a href="<@dspace.url "/handle/${item.getHandle()}"/>">View Item</a></li>
        </ul>

        <h1>Bitstreams: ${item.getMetadata("dc.title")[0].value!"Untitled"}</h1>
        <table width="100%">
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Format</th>
                <th>View</th>
            </tr>
            <#list item.getBundles() as bundle>
                <tr>
                    <td colspan="4">Bundle: ${bundle.getName()}</td>
                </tr>
                <#list bundle.getBitstreams() as bitstream>
                    <tr>
                        <td>${bitstream.getName()!""}</td>
                        <td>${bitstream.getDescription()!""}</td>
                        <td>${bitstream.getFormatDescription()!""}</td>
                        <td>View</td>
                    </tr>
                </#list>
                <tr><td colspan="4">&nbsp;</td></tr>
            </#list>
        </table>
    </body>
</html>