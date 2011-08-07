<#import "/includes/dspace.ftl" as dspace />

<head>
    <script type="text/javascript" src="<@dspace.url "/utils.js" />"></script>
</head>

<#if displaygroupmemberships??>
    <#assign displayGroupMembership = displaygroupmemberships.booleanValue()>
<#else>
<#assign displayGroupMembership = false>
</#if>


<table width="100%" border="0">
    <tr>
        <td align="left">
            <h1>
            <@dspace.message "ui.mydspace" />: <#if mydspaceuser??>${mydspaceuser.getFullName()}</#if>
            </h1>
        </td>
        <td align="right" class="standard">
            <a href="<@dspace.url "/help/index.html#mydspace" />"
               onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
        </td>
    </tr>
</table>


<#if workflowowned?? && workflowowned?size&gt;0>
<h2><@dspace.message "ui.mydspace.main.heading2" /></h2>

<p class="submitFormHelp">
<@dspace.message "ui.mydspace.main.text1" />
</p>

<table class="miscTable" align="center" summary="Table listing owned tasks">
    <tr>
        <th id="t1" class="oddRowOddCol"><@dspace.message "ui.mydspace.main.task" /></th>
        <th id="t2" class="oddRowOddCol"><@dspace.message "ui.mydspace.main.item" /></th>
        <th id="t3" class="oddRowOddCol"><@dspace.message "ui.mydspace.main.subto" /></th>
        <th id="t4" class="oddRowEvenCol"><@dspace.message "ui.mydspace.main.subby" /></th>
        <th id="t5" class="oddRowEvenCol">&nbsp;</th>
    </tr>


    <#assign row = "even">
    <#list workflowowned as owned>
        <tr>
            <td headers="t1" class="${row}RowOddCol">
                <#if WFSTATE_STEP1?? && owned.getState()=WFSTATE_STEP1>
                <@dspace.message "ui.mydspace.main.sub1" />
                    <#elseif WFSTATE_STEP2?? && owned.getState()=WFSTATE_STEP2>
                <@dspace.message "ui.mydspace.main.sub2" />
                    <#elseif WFSTATE_STEP3?? && owned.getState()=WFSTATE_STEP3>
                    <@dspace.message "ui.mydspace.main.sub3" />
                </#if></td>
            <#assign submitter = owned.getItem().getSubmitter()>
            <td headers="t2"
                class="${row}RowEvenCol">${owned.getItem().getMetadata("dc.title")[0].value!"Untitled"}</td>
            <td headers="t3" class="${row}RowOddCol">${owned.getCollection().getMetadata("name")}</td>
            <td headers="t4" class="${row}RowEvenCol"><a
                    href="mailto:${submitter.getEmail()}">${submitter.getFullName()}</a></td>
            <!-- <td headers="t5" class="<%= row %>RowOddCol"></td> -->
            <td headers="t5" class="${row}RowEvenCol">
                <form action="<@dspace.url "/mydspace"/>" method="post">
                    <input type="hidden" name="step" value="0"/>
                    <input type="hidden" name="workflow_id" value="${owned.getID()}"/>
                    <input type="submit" name="submit_perform"
                           value="<@dspace.message "ui.mydspace.main.perform.button" />"/>
                    <input type="submit" name="submit_return"
                           value="<@dspace.message "ui.mydspace.main.return.button" />"/>
                </form>
            </td>
        </tr>
        <#if row = "even">
            <#assign row = "odd">
            <#else>
                <#assign row = "even">
        </#if>

    </#list>
</table>

</#if>


<#if workflowpooled?? && workflowpooled?size&gt;0>
<h2><@dspace.message "ui.mydspace.main.heading3" /></h2>

<p class="submitFormHelp">
<@dspace.message "ui.mydspace.main.text2" />
</p>

<table class="miscTable" align="center" summary="Table listing the tasks in the pool">
    <tr>
        <th id="t6" class="oddRowOddCol"><@dspace.message "ui.mydspace.main.task" /></th>
        <th id="t7" class="oddRowEvenCol"><@dspace.message "ui.mydspace.main.item" /></th>
        <th id="t8" class="oddRowOddCol"><@dspace.message "ui.mydspace.main.subto" /></th>
        <th id="t9" class="oddRowEvenCol"><@dspace.message "ui.mydspace.main.subby" /></th>
    </tr>


    <#assign row = "even">
    <#list workflowpooled as pooled>
        <tr>
            <td headers="t1" class="${row}RowOddCol">
                <#if WFSTATE_STEP1POOL?? && pooled.getState()=WFSTATE_STEP1POOL>
                <@dspace.message "ui.mydspace.main.sub1" />
                    <#elseif WFSTATE_STEP2POOL?? && pooled.getState()=WFSTATE_STEP2POOL>
                <@dspace.message "ui.mydspace.main.sub2" />
                    <#elseif WFSTATE_STEP3POOL?? && pooled.getState()=WFSTATE_STEP3POOL>
                    <@dspace.message "ui.mydspace.main.sub3" />
                </#if></td>
            <#assign submitter = pooled.getItem().getSubmitter()>
            <td headers="t7"
                class="${row}RowEvenCol">${pooled.getItem().getMetadata("dc.title")[0].value!"Untitled"}</td>
            <td headers="t8" class="${row}RowOddCol">${pooled.getCollection().getMetadata("name")}</td>
            <td headers="t9" class="${row}RowEvenCol"><a
                    href="mailto:${submitter.getEmail()}">${submitter.getFullName()}</a></td>
            <!-- <td headers="t5" class="<%= row %>RowOddCol"></td> -->
            <td class="${row}RowOddCol">
                <form action="<@dspace.url "/mydspace"/>" method="post">
                    <input type="hidden" name="step" value="0"/>
                    <input type="hidden" name="workflow_id" value="${pooled.getID()}"/>
                    <input type="submit" name="submit_claim"
                           value="<@dspace.message "ui.mydspace.main.take.button" />"/>
                </form>
            </td>
        </tr>
        <#if row = "even">
            <#assign row = "odd">
            <#else>
                <#assign row = "even">
        </#if>

    </#list>
</table>
</#if>


<form action="<@dspace.url "/mydspace"/>" method="post">
    <input type="hidden" name="step" value="0"/>
    <center>
        <table border="0" width="70%">
            <tr>
                <td align="left">
                    <input type="submit" name="submit_new" value="<@dspace.message "ui.mydspace.main.start.button" />"
                            />
                </td>
                <td align="right">
                    <input type="submit" name="submit_own" value="<@dspace.message "ui.mydspace.main.view.button" />"/>
                </td>
            </tr>
        </table>
    </center>
</form>

<p align="center"><a href="<@dspace.url "/subscribe"/>">
<@dspace.message "ui.mydspace.main.link" />
</a></p>


<#if workspaceitems?? && superviseditems?? && (workspaceitems?size&gt;0||superviseditems?size&gt;0)>
    <#assign row = "even">
<h2>
<@dspace.message "ui.mydspace.main.heading4" />
</h2>

<p>
<@dspace.message "ui.mydspace.main.text4" />
</p>

<table class="miscTable" align="center" summary="Table listing unfinished submissions">
    <tr>
        <th class="oddRowOddCol">&nbsp;</th>
        <th id="t10" class="oddRowEvenCol">
        <@dspace.message "ui.mydspace.main.subby" />
        </th>
        <th id="t11" class="oddRowOddCol">
        <@dspace.message "ui.mydspace.main.elem1" />
        </th>
        <th id="t12" class="oddRowEvenCol">
        <@dspace.message "ui.mydspace.main.elem2" />
        </th>
        <th id="t13" class="oddRowOddCol">&nbsp;</th>
    </tr>
    <#if workspaceitems?size&gt;0||superviseditems?size&gt;0>
        <tr>
            <th colspan="5">
            <@dspace.message "ui.mydspace.main.authoring" />
            </th>
        </tr>
    </#if>

    <#list workspaceitems as ws>
        <tr>
            <td class="${row}RowOddCol">
                <form action="<@dspace.url "/workspace"/>" method="post">
                    <input type="hidden" name="workspace_id" value="${ws.getID()}"/>
                    <input type="submit" name="submit_open" value="<@dspace.message "ui.mydspace.general.open" />"/>
                </form>
            </td>
            <#assign submitter = ws.getItem().getSubmitter()>
            <td headers="t10" class="${row}RowEvenCol">
                <a href="mailto:${submitter.getEmail()}">${submitter.getFullName()}</a>
            </td>
            <td headers="t11" class="${row}RowOddCol">${ws.getItem().getMetadata("dc.title")[0].value!"Untitled"}</td>
            <td headers="t12" class="${row}RowEvenCol">${ws.getCollection().getMetadata("name")}</td>
            <td headers="t13" class="${row}RowOddCol">
                <form action="<@dspace.url "/mydspace"/>e" method="post">
                    <input type="hidden" name="step" value="0"/>
                    <input type="hidden" name="workspace_id" value="${ws.getID()}"/>
                    <input type="submit" name="submit_delete" value="<@dspace.message "ui.mydspace.general.remove" />"/>
                </form>
            </td>
        </tr>
        <#if row = "even">
            <#assign row = "odd">
            <#else>
                <#assign row = "even">
        </#if>
    </#list>

    <#if superviseditems?size&gt;0>
        <tr>
            <th colspan="5">
            <@dspace.message "ui.mydspace.main.supervising" />
            </th>
        </tr>
    </#if>

    <#list superviseditems as si>
        <#assign submitter = si.getItem().getSubmitter()>
        <tr>
            <td class="${row}RowOddCol">
                <form action="<@dspace.url "/workspace"/>" method="post">
                    <input type="hidden" name="workspace_id" value="${si.getID()}"/>
                    <input type="submit" name="submit_open" value="<@dspace.message "ui.mydspace.general.open" />"/>
                </form>
            </td>
            <td class="${row}RowEvenCol">
                <a href="mailto:${submitter.getEmail()}">${submitter.getFullName()}</a>
            </td>
            <td class="${row}RowOddCol">${si.getItem().getMetadata("dc.title")[0].value!"Untitled"}</td>
            <td class="${row}RowEvenCol">${si.getCollection().getMetadata("name")}</td>
            <td class="${row}RowOddCol">
                <form action="<@dspace.url "/mydspace"/>" method="post">
                    <input type="hidden" name="step" value="0"/>
                    <input type="hidden" name="workspace_id" value="${si.getID()}"/>
                    <input type="submit" name="submit_delete" value="<@dspace.message "ui.mydspace.general.remove" />"/>
                </form>
            </td>
        </tr>
        <#if row = "even">
            <#assign row = "odd">
            <#else>
                <#assign row = "even">
        </#if>

    </#list>
</table>
</#if>


<#if workspaceitems??&&workspaceitems?size&gt;0>

    <#assign row = "even">
<h2>
<@dspace.message "ui.mydspace.main.heading5" />
</h2>

<table class="miscTable" align="center" summary="Table listing submissions in workflow process">
    <tr>
        <th id="t14" class="oddRowOddCol">
        <@dspace.message "ui.mydspace.main.elem1" />
        </th>
        <th id="t15" class="oddRowEvenCol">
        <@dspace.message "ui.mydspace.main.elem2" />
        </th>
    </tr>

    <#list workspaceitems as ws>
        <tr>
            <td headers="t14" class="${row}RowOddCol">${ws.getItem().getMetadata("dc.title")[0].value!"Untitled"}</td>
            <td headers="t15" class="${row}RowEvenCol">
                <form action="<@dspace.url "/mydspace"/>" method="post">
                ${ws.getCollection().getMetadata("name")}
                    <input type="hidden" name="step" value="0"/>
                    <input type="hidden" name="workflow_id" value="${ws.getID()}"/>
                </form>
            </td>
        </tr>
        <#if row = "even">
            <#assign row = "odd">
            <#else>
                <#assign row = "even">
        </#if>
    </#list>
</table>
</#if>



<#if displayGroupMembership==true && groupmemberships?? && groupmemberships?size&gt;0>
<h2>
    <@dspace.message "ui.mydspace.main.heading6" />
</h2>
<ul>
    <#list groupmemberships as gm>
    <li>${gm.getName()}</li>
    </#list>
</ul>
</#if>

<#if exportarchives?? && exportarchives?size&gt;0>
<h2>
    <@dspace.message "ui.mydspace.main.heading7" />
</h2>
<ol class="exportArchives">
    <%for(String fileName:exportsAvailable){%>
    <#list exportarchives as fileName>
    <#assign showingArgs=[fileName]>
    <li><a href="<@dspace.url "/exportdownload/${fileName}"/>" title="<@dspace.messageArgs "ui.mydspace.main.export.archive.title", showingArgs />">${fileName}</a></li>
    </#list>
</ol>
</#if>
