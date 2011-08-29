<#import "/includes/dspace.ftl" as dspace />

<#if workflowitem?? && WFSTATE_STEP1POOL?? && workflowitem.getState()==WFSTATE_STEP1POOL>
    <#if collection??>
        <#assign showingArgs=[collection.getMetadata("name")]>
    <@dspace.messageArgs "ui.mydspace.perform-task.text1", showingArgs /></#if>
    <#elseif workflowitem?? && WFSTATE_STEP2POOL?? && workflowitem.getState()==WFSTATE_STEP2POOL>
        <#if collection??>
            <#assign showingArgs=[collection.getMetadata("name")]>
        <@dspace.messageArgs "ui.mydspace.perform-task.text3", showingArgs /></#if>
    <#elseif workflowitem?? && WFSTATE_STEP3POOL?? && workflowitem.getState()==WFSTATE_STEP3POOL>
        <#if collection??>
            <#assign showingArgs=[collection.getMetadata("name")]>
        <@dspace.messageArgs "ui.mydspace.perform-task.text4", showingArgs /></#if>
</#if>

<#if workflowitem??>
    <#assign item = workflowitem.getItem()>
    <#include "/pages/item.ftl" />

<form action="<@dspace.url "/mydspace"/>" method="post">
    <input type="hidden" name="workflow_id" value="${workflowitem.getID()}"/>
    <input type="hidden" name="step" value="2"/>
    <table border="0" width="90%" cellpadding="10" align="center">
        <tr>
            <td align="left">
                <input type="submit" name="submit_start" value="<@dspace.message "ui.mydspace.preview-task.accept.button" />"/>
            </td>
            <td align="right">
                <input type="submit" name="submit_cancel" value="<@dspace.message "ui.mydspace.general.cancel" />"/>
            </td>
        </tr>
    </table>
</form>
</#if>




