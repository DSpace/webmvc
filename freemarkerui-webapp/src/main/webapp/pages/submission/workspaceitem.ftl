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
    Debugging: Display the current workspace item.
-->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>Submission: Workspace Item ${workspaceitem.getID()}</title>
    </head>
    <body>
        <h1>Submission: Workspace Item</h1>

        <table>
            <tr>
                <td>Collection:</td>
                <td>${workspaceitem.getCollection().getName()}</td>
            </tr>
        </table>

    </body>
</html>