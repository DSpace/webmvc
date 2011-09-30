<#import "/includes/dspace.ftl" as dspace />

<h1><@dspace.message "ui.dspace-admin.community-select.com" /></h1>

<form method="post" action="<@dspace.url "/admin/authorize" />">
    <table class="miscTable" align="center" summary="Community selection table">
        <tr>
            <td>
                <select size="12" name="community_id">
                    <#if communities??>
                        <#list communities as c>
                            <option value="${c.getID()}">
                                ${c.getMetadata("name")}
                            </option>
                        </#list>
                    </#if>
                </select>
            </td>
        </tr>
    </table>

    <div align="center">
        <table width="70%">
            <tr>
                <td align="left">
                    <input type="submit" name="submit_community_select" value="<@dspace.message "ui.dspace-admin.general.editpolicy" />" />
                </td>
                <td align="right">
                    <input type="submit" name="submit_community_select_cancel" value="<@dspace.message "ui.dspace-admin.general.cancel" />" />
                </td>
            </tr>
        </table>
    </div>
</form>