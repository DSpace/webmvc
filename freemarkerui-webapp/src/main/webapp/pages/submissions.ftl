<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- Internationalization can also be achieved via multiple templates - ie. home.ftl, home_en_US.ftl, etc. -->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>Submissions</title>
    </head>
    <body>
        <h1 class="ds-div-head">Submissions & Workflow Tasks</h1>
        <h2>Workflow Tasks</h2>
        <p>These tasks are items that are awaiting approval before they are added to the repository. There are two task queues, one for tasks which you have chosen to accept and another for tasks which have not been taken by anyone yet.</p>

        <h3>Tasks you own</h3>
        <#if ownedItems??  && ownedItems?size &gt; 0>
            <table class="submissions">
                <tr>
                    <td></td>
                    <th>Status</th>
                    <th>Item</th>
                    <th>Collection</th>
                    <th>Submitter</th>
                </tr>
            <#list ownedItems as ownedItem>
                <tr class="item">
                    <td>
                        <input name="workflowID" type="checkbox" value="${ownedItem.getID()}" title="Select for return to task pool">
                    </td>
                    <td class="task">${ownedItem.getState()}<!-- @TODO Get message for task status --></td>
                    <td class="title">
                        <a href="<@dspace.url "/handle/${ownedItem.getCollection().getHandle()}/workflow?workflowID=${ownedItem.getID()}" />" title="Work on this item">
                            ${ownedItem.getItem().getMetadata("dc.title")[0].value!"untitled"}
                        </a>
                    </td>
                    <td class="collection">
                        <a href="<@dspace.url "/handle/${ownedItem.getCollection().getHandle()}"/>" title="Go to the parent Collection">
                            ${ownedItem.getCollection().getMetadata("name")!"untitled"}
                        </a>
                    </td>
                    <td class="submitter">${ownedItem.getSubmitter().getFullName()!"unknown"}</td>
                </tr>
            </#list>
                <tr>
                    <td colspan="5">
                        <input id="aspect_submission_Submissions_field_submit_return_tasks" class="ds-button-field" name="submit_return_tasks" type="submit" value="Return selected tasks to the pool">
                    </td>
                </tr>
            </table>
        <#else>
            <p>No owned items</p>
        </#if>

        <h3>Tasks in the pool</h3>
        <p>Not yet implemented</p>

        <h2>Unfinished submissions</h2>
        <p>These are incomplete item submissions. You may also <a href="<@dspace.url "/submit"/>">start another submission</a>.</p>
        <p>Not yet implemented</p>

        <h2>Accepted submissions</h2>
        <p>Not yet implemented</p>


    </body>
</html>