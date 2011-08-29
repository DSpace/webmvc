<#import "/includes/dspace.ftl" as dspace />

<#if community??><#assign showingArgs=[community.getID()]>
          <h1><@dspace.messageArgs "ui.tools.confirm-delete-community.heading", showingArgs /></h1></#if>

<#if community??><#assign showingArgs=[community.getMetadata("name")]>
          <h1><@dspace.messageArgs "ui.tools.confirm-delete-community.confirm", showingArgs /></h1></#if>

    <ul>
        <li><@dspace.message "ui.tools.confirm-delete-community.info1" /></li>
        <li><@dspace.message "ui.tools.confirm-delete-community.info2" /></li>
        <li><@dspace.message "ui.tools.confirm-delete-community.info3" /></li>
        <li><@dspace.message "ui.tools.confirm-delete-community.info4" /></li>
    </ul>

    <form method="post" action="<@dspace.url "/admin/editcommunities"/>">
        <#if community??><input type="hidden" name="community_id" value="${community.getID()}" /></#if>
        <div align="center">
            <table width="70%">
                <tr>
                    <td align="left">
                        <input type="submit" name="submit_confirm_delete_community" value="<@dspace.message "ui.tools.general.delete" />"/>
                    </td>
                    <td align="right">
                        <input type="submit" name="submit_cancel" value="<@dspace.message "ui.tools.general.cancel" />"/>
                    </td>
                </tr>
            </table>
        </div>
    </form>