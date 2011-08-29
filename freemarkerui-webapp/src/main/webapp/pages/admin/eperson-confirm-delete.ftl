<#import "/includes/dspace.ftl" as dspace />

<h1><@dspace.message "ui.dspace-admin.eperson-confirm-delete.heading" />
<#if eperson??>${eperson.getFullName()}${eperson.getEmail()}
</h1>

<p><@dspace.message "ui.dspace-admin.eperson-confirm-delete.confirm" /></p>

  <form method="post" action="<@dspace.url "/admin/eperson" />">
        <input type="hidden" name="eperson_id" value="${eperson.getID()}"/>
            <table width="70%">
                <tr>
                    <td align="left">
                        <%-- <input type="submit" name="submit_confirm_delete" value="Delete"> --%>
                        <input type="submit" name="submit_confirm_delete" value="<@dspace.message "ui.dspace-admin.general.delete" />" />
                    </td>
                    <td align="right">
                        <%-- <input type="submit" name="submit_cancel" value="Cancel"> --%>
                        <input type="submit" name="submit_cancel" value="<@dspace.message "ui.dspace-admin.general.cancel" />" />
                    </td>
                </tr>
            </table>

    </form>
</#if>