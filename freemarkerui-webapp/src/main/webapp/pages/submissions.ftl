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
                        <input id="return_tasks" class="ds-button-field" name="submit_return_tasks" type="submit" value="Return selected tasks to the pool">
                    </td>
                </tr>
            </table>
        <#else>
            <p>No owned items</p>
        </#if>
        <br/>

        <h3>Tasks in the pool</h3>
        <#if pooledItems??  && pooledItems?size &gt; 0>
            <table class="submissions">
                <tr>
                    <td></td>
                    <th>Status</th>
                    <th>Item</th>
                    <th>Collection</th>
                    <th>Submitter</th>
                </tr>
            <#list pooledItems as pooledItem>
                <tr class="item">
                    <td>
                        <input name="workflowID" type="checkbox" value="${pooledItem.getID()}" title="Select for return to task pool">
                    </td>
                    <td class="task">${pooledItem.getState()}<!-- @TODO Get message for task status --></td>
                    <td class="title">
                        <a href="<@dspace.url "/handle/${pooledItem.getCollection().getHandle()}/workflow?workflowID=${pooledItem.getID()}" />" title="Work on this item">
                            ${pooledItem.getItem().getMetadata("dc.title")[0].value!"untitled"}
                        </a>
                    </td>
                    <td class="collection">
                        <a href="<@dspace.url "/handle/${pooledItem.getCollection().getHandle()}"/>" title="Go to the parent Collection">
                            ${pooledItem.getCollection().getMetadata("name")!"untitled"}
                        </a>
                    </td>
                    <td class="submitter">${pooledItem.getSubmitter().getFullName()!"unknown"}</td>
                </tr>
            </#list>
                <tr>
                    <td colspan="5">
                        <input id="take_tasks" class="ds-button-field" name="submit_return_tasks" type="submit" value="Take task from pool">
                    </td>
                </tr>
            </table>
        <#else>
            <p>No tasks in the pool</p>
        </#if>
        <hr/>

        <h2>Unfinished submissions</h2>
        <p>These are incomplete item submissions. You may also <a href="<@dspace.url "/submit"/>">start another submission</a>.</p>
        <#if unfinishedItems??  && unfinishedItems?size &gt; 0>
            <table class="submissions">
                <tr>
                    <td></td>
                    <th>Item</th>
                    <th>Collection</th>
                    <th>Submitter</th>
                </tr>
            <#list unfinishedItems as unfinishedItem>
                <tr class="item">
                    <td>
                        <input name="workflowID" type="checkbox" value="${unfinishedItem.getID()}" title="Remove selected submission">
                    </td>
                    <td class="title">
                        <a href="<@dspace.url "/submit?workspaceID=${unfinishedItem.getID()}" />" title="Work on this item">
                            <#assign title = unfinishedItem.getItem().getMetadata("dc.title")>
                            <#if title?? && title?size &gt; 0>
                                ${title[0].value!"untitled"}
                            <#else>
                                untitled
                            </#if>
                        </a>
                    </td>
                    <td class="collection">
                        <a href="<@dspace.url "/handle/${unfinishedItem.getCollection().getHandle()}"/>" title="Go to the parent Collection">
                            ${unfinishedItem.getCollection().getMetadata("name")!"untitled"}
                        </a>
                    </td>
                    <td class="submitter">${unfinishedItem.getSubmitter().getFullName()!"unknown"}</td>
                </tr>
            </#list>
                <tr>
                    <td colspan="5">
                        <input id="remove-submissions" class="ds-button-field" name="submit_return_tasks" type="submit" value="Removed selected submissions">
                    </td>
                </tr>
            </table>
        <#else>
            <p>No unfinished submissions</p>
        </#if>
        <br/>

        <h2>Supervised Items</h2>
        <#if supervisedItems??  && supervisedItems?size &gt; 0>
            <table class="submissions">
                <tr>
                    <td></td>
                    <th>Item</th>
                    <th>Collection</th>
                    <th>Submitter</th>
                </tr>
            <#list supervisedItems as supervisedItem>
                <tr class="item">
                    <td>
                        <input name="workflowID" type="checkbox" value="${supervisedItem.getID()}" title="Remove selected submission">
                    </td>
                    <td class="title">
                        <a href="<@dspace.url "/submit?workspaceID=${supervisedItem.getID()}" />" title="Work on this item">
                            ${supervisedItem.getItem().getMetadata("dc.title")[0].value!"untitled"}
                        </a>
                    </td>
                    <td class="collection">
                        <a href="<@dspace.url "/handle/${supervisedItem.getCollection().getHandle()}"/>" title="Go to the parent Collection">
                            ${supervisedItem.getCollection().getMetadata("name")!"untitled"}
                        </a>
                    </td>
                    <td class="submitter">${supervisedItem.getSubmitter().getFullName()!"unknown"}</td>
                </tr>
            </#list>
                <tr>
                    <td colspan="5">
                        <input id="remove-supervised-item" class="ds-button-field" name="submit_return_tasks" type="submit" value="Removed selected submissions">
                    </td>
                </tr>
            </table>
        <#else>
            <p>No supervised submissions</p>
        </#if>
        <br/>

        <h2>Submissions being reviewed</h2>
        <#if inprogressItems??  && inprogressItems?size &gt; 0>
            <table class="submissions">
                <tr>
                    <th>Item</th>
                    <th>Collection</th>
                    <th>Submitter</th>
                </tr>
            <#list inprogressItems as inprogressItem>
                <tr class="item">
                    <td class="title">
                        <a href="<@dspace.url "/submit?workspaceID=${inprogressItem.getID()}" />" title="Look at this item">
                            ${inprogressItem.getItem().getMetadata("dc.title")[0].value!"untitled"}
                        </a>
                    </td>
                    <td class="collection">
                        <a href="<@dspace.url "/handle/${inprogressItem.getCollection().getHandle()}"/>" title="Go to the parent Collection">
                            ${inprogressItem.getCollection().getMetadata("name")!"untitled"}
                        </a>
                    </td>
                    <td class="submitter">${inprogressItem.getSubmitter().getFullName()!"unknown"}</td>
                </tr>
            </#list>
            </table>
        <#else>
            <p>No submissions being reviewed</p>
        </#if>
        <br/>

        <h2>Accepted submissions</h2>
        <#if submittedItems??  && submittedItems?size &gt; 0>
            <table class="submissions">
                <tr>
                    <th>Item</th>
                    <th>Collection</th>
                    <th>Date Accessioned</th>
                </tr>
            <#list submittedItems as submittedItem>
                <tr class="item">
                    <td class="title">
                        <a href="<@dspace.url "/handle/${submittedItem.getHandle()}" />" title="View this item">
                            ${submittedItem.getMetadata("dc.title")[0].value!"untitled"}
                        </a>
                    </td>
                    <td class="collection">
                        <a href="<@dspace.url "/handle/${submittedItem.getCollections()[0].getHandle()}"/>" title="Go to the parent Collection">
                            ${submittedItem.getCollections()[0].getMetadata("name")!"untitled"}
                        </a>
                    </td>
                    <td class="dc.date.accessioned">${submittedItem.getMetadata("dc.date.accessioned")[0].value!"unknown"}</td>
                </tr>
            </#list>
            </table>
        <#else>
            <p>No items in the archive</p>
        </#if>
        <br/>


    </body>
</html>