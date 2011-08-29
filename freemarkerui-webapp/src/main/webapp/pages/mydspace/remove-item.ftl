<#import "/includes/dspace.ftl" as dspace />

<h1><@dspace.message "ui.mydspace.remove-item.title" /></h1>

    <p><@dspace.message "ui.mydspace.remove-item.confirmation" /></p>

    <#if workspaceitem??>
    <#assign item=workspaceitem.getItem()>
    <#include "/pages/item.ftl" />
    <form action="<@dspace.url "/mydspace"/>" method="post">
        <input type="hidden" name="workspace_id" value="${workspaceitem.getID()}"/>
        <input type="hidden" name="step" value="1"/>

        <table align="center" border="0" width="90%">
            <tr>
                <td align="left">
					<input type="submit" name="submit_delete" value="<@dspace.message "ui.mydspace.remove-item.remove.button" />" />
                </td>
                <td align="right">
					<input type="submit" name="submit_cancel" value="<@dspace.message "ui.mydspace.remove-item.cancel.button" />" />
                </td>
            </tr>
        </table>
    </form></#if>