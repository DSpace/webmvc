<#import "/includes/dspace.ftl" as dspace />

<h1>
<#if group??><#assign showingArgs = "${group.getName()}"></#if>
<@dspace.messageArgs "ui.dspace-admin.group-confirm-delete.heading", showingArgs />
</h1>

    <p><@dspace.message "ui.dspace-admin.group-confirm-delete.confirm" /></p>
    <table width="70%">
                <tr>
                    <td align="left">
                    <form method="post" action="<@dspace.url "/admin/group" />">
                        <#if group??>
                        <input type="hidden" name="group_id" value="${group.getID()}"/>
                        </#if>
                        <input type="submit" name="submit_confirm_delete" value="<@dspace.message "ui.dspace-admin.general.delete" />" />
                    </form>
                    </td>
                    <td align="right">
                    <form method="post" action="">
                        <input type="submit" name="submit_cancel_delete" value="<@dspace.message "ui.dspace-admin.general.cancel" />" />
                    </form>
                    </td>
                </tr>
     </table>