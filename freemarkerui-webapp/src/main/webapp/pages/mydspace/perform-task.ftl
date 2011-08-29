<#import "/includes/dspace.ftl" as dspace />

<h1><@dspace.message "ui.mydspace.perform-task.title" /></h1>

<#if workflowitem?? && WFSTATE_STEP1?? && workflowitem.getState()==WFSTATE_STEP1>
    <#if collection??>
        <#assign showingArgs=[collection.getMetadata("name")]>
    <@dspace.messageArgs "ui.mydspace.perform-task.text1", showingArgs /></#if>
</#if>


<#if workflowitem?? && WFSTATE_STEP2?? && workflowitem.getState()==WFSTATE_STEP2>
    <#if collection??>
        <#assign showingArgs=[collection.getMetadata("name")]>
    <@dspace.messageArgs "ui.mydspace.perform-task.text3", showingArgs /></#if>
</#if>


<#if workflowitem?? && WFSTATE_STEP3?? && workflowitem.getState()==WFSTATE_STEP3>
    <#if collection??>
        <#assign showingArgs=[collection.getMetadata("name")]>
    <@dspace.messageArgs "ui.mydspace.perform-task.text4", showingArgs /></#if>
</#if>

<#if workflowitem??>
    <#assign item = workflowitem.getItem()>
    <#include "/pages/item.ftl" />

<p>&nbsp;</p>

<form action="<@dspace.url "/mydspace"/>" method="post">
    <input type="hidden" name="workflow_id" value="${workflowitem.getID()}"/>
    <input type="hidden" name="step" value="3"/>
<table class="miscTable" width="80%">

    <#assign row = "odd">
    <#if WFSTATE_STEP1??&&WFSTATE_STEP2??&&(workflowitem.getState()==WFSTATE_STEP1||workflowitem.getState()==WFSTATE_STEP2)>
        <tr>
            <td class="${row}RowOddCol">
            <@dspace.message "ui.mydspace.perform-task.instruct1" />
            </td>
            <td class="${row}RowEvenCol" valign="middle">
                <input type="submit" name="submit_approve" value="<@dspace.message "ui.mydspace.general.approve" />"/>
            </td>
        </tr>
        <#else>
            <tr>
                <td class="${row}RowOddCol">
                <@dspace.message "ui.mydspace.perform-task.instruct2" />
                </td>
                <td class="${row}RowEvenCol" valign="middle">
                    <input type="submit" name="submit_approve"
                           value="<@dspace.message "ui.mydspace.perform-task.commit.button" />"/>
                </td>
            </tr>
    </#if>
    <#assign row = "even">
    <#if WFSTATE_STEP1??&&WFSTATE_STEP2??&&(workflowitem.getState()==WFSTATE_STEP1||workflowitem.getState()==WFSTATE_STEP2)>
        <tr>
            <td class="${row}RowOddCol">
            <@dspace.message "ui.mydspace.perform-task.instruct3"/>
            </td>
            <td class="${row}RowEvenCol" valign="middle">
                <input type="submit" name="submit_reject" value="<@dspace.message "ui.mydspace.general.reject"/>"/>
            </td>
        </tr>
        <#if row = "odd">
            <#assign row = "even">
            <#else>
                <#assign row = "odd">
        </#if>
    </#if>

    <tr>
        <td class="${row}RowOddCol">
        <@dspace.message "ui.mydspace.perform-task.instruct5"/>
        </td>
        <td class="${row}RowEvenCol" valign="middle">
            <input type="submit" name="submit_cancel"
                   value="<@dspace.message "ui.mydspace.perform-task.later.button"/>"/>
        </td>
    </tr>

    <#if row = "odd">
        <#assign row = "even">
        <#else>
            <#assign row = "odd">
    </#if>

    <tr>
                <td class="${row}RowOddCol">
                    <@dspace.message "ui.mydspace.perform-task.instruct6"/>
				</td>
                <td class="${row}RowEvenCol" valign="middle">
			<input type="submit" name="submit_pool" value="<@dspace.message "ui.mydspace.perform-task.return.button"/>" />
                </td>
            </tr>
        </table>
    </form>
</#if>









