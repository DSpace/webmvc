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
        <form method="post">
            <table>
                <tr>
                    <th>Schema Element Qualifier</th>
                    <th>Value</th>
                    <th>Language</th>
                    
                </tr>
                <#assign row = 0>
                <#list values as value>
                    <tr>
                        <td><input type="hidden" name="name_${row}" value="${value.schema!""}_${value.element!""}_${value.qualifier!""}">${value.schema!""}.${value.element!""}.${value.qualifier!""}</td>
                        <td><textarea rows="4" cols="35" name="value_${row}">${value.value!""}</textarea></td>
                        <td><input type="text" size="6" name="language_${row}" value="${value.language!""}"></td>
                    </tr>
                    <#assign row = row +1>
                </#list>
            </table>
            <p>
                <input class="ds-button-field" name="update" type="submit" value="Update">
                <input class="ds-button-field" name="cancel" type="submit" value="Return">
            </p>
        </form>
    </body>
</html>