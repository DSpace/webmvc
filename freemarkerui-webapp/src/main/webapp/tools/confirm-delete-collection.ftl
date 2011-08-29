<#import "/includes/dspace.ftl" as dspace />

<#if collection??><#assign showingArgs=[collection.getID()]>
<h1><@dspace.messageArgs "ui.tools.confirm-delete-collection.heading", showingArgs /></h1></#if>

<#if collection??><#assign showingArgs=[collection.getMetadata("name")]>
<h1><@dspace.messageArgs "ui.tools.confirm-delete-collection.confirm", showingArgs /></h1></#if>

    <ul>
        <li><@dspace.message "ui.tools.confirm-delete-collection.info1" /></li>
        <li><@dspace.message "ui.tools.confirm-delete-collection.info2" /></li>
        <li><@dspace.message "ui.tools.confirm-delete-collection.info3" /></li>
    </ul>

    <form method="post" action="<@dspace.url "/admin/editcommunities"/>">
        <#if collection??><input type="hidden" name="collection_id" value="${collection.getID()}" /></#if>
        <#if community??><input type="hidden" name="community_id" value="${community.getID()}" /></#if>

        <div align="center">
            <table width="70%">
                <tr>
                    <td align="left">
                        <input type="submit" name="submit_confirm_delete_collection" value="<@dspace.message "ui.tools.general.delete" />"/>
                    </td>
                    <td align="right">
                        <input type="submit" name="submit_cancel" value="<@dspace.message "ui.tools.general.cancel" />"/>
                    </td>
                </tr>
            </table>
        </div>
    </form>

