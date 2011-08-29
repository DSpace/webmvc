<#import "/includes/dspace.ftl" as dspace />

<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>

<h1><@dspace.message "ui.dspace-admin.authorize-advanced.advanced" /></h1>

<div><@dspace.message "ui.dspace-admin.authorize-advanced.text" /><a href="<@dspace.url "/help/site-admin.html#advancedpolicies" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.morehelp" /></a></div>

<form method="post" action="<@dspace.url "/admin/authorize" />">

    <table class="miscTable" align="center" summary="Advanced policy manager">
        <tr>
            <%-- <td>Collection:</td> --%>
            <th id="t1"><label for ="tcollection"><@dspace.message "ui.dspace-admin.authorize-advanced.col" /></label></th>
            <td headers="t1">
                <select size="10" name="collection_id" id="tcollection">
                    <#if collections??>
                    <#list collections as c>
                       <option value="${c.getID()}"> ${c.getMetadata("name")}</option>
                    </#list>
                    </#if>
                </select>
            </td>
        </tr>

        <tr>
            <%-- <td>Content Type:</td> --%>
            <th id="t2"><label for="tresource_type"><@dspace.message "ui.dspace-admin.authorize-advanced.type" /></label></th>
            <td headers="t2">
                <select name="resource_type" id="tresource_type">
                    <#if item?? && bitstream??>
                    <option value="${item}%>"><@dspace.message "ui.dspace-admin.authorize-advanced.type1" /></option>
                    <option value="${bitstream}"><@dspace.message "ui.dspace-admin.authorize-advanced.type2 " /></option>
                    </#if>
                </select>
            </td>
        </tr>

        <tr>
            <%-- <td>Group:</td> --%>
            <th id="t3"><@dspace.message "ui.dspace-admin.general.group-colon" /></th>
            <td headers="t3">
                <select size="10" name="group_id" id="tgroup_id">
                    <#if groups??>
                        <#list groups as grp>
                        <option value="${grp.getID()}"> ${grp.getName()}</option>
                        </#list>
                    </#if>
                </select>
            </td>
        </tr>

        <tr>
            <%-- <tr><td>Action:</td> --%>
            <th id="t4"><label for="taction_id"><@dspace.message "ui.dspace-admin.general.action-colon" /></label></th>
            <td headers="t4">
                <select name="action_id" id="taction_id">
                    <#if actiontext??>
                        <#list actiontext as at>
                    <option value="${at_index}">${at}</option>
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
                    <%-- <input type="submit" name="submit_advanced_add" value="Add Policy"> --%>
                    <input type="submit" name="submit_advanced_add" value="<@dspace.message "ui.dspace-admin.authorize-advanced.add" />" />
                </td>
                <td align="right">
                    <%-- <input type="submit" name="submit_advanced_clear" value="Clear Policies"> (warning: clears all policies for a given set of objects) --%>
                    <input type="submit" name="submit_advanced_clear" value="<@dspace.message "ui.dspace-admin.authorize-advanced.clear" />"/></td> <td><@dspace.message "ui.dspace-admin.authorize-advanced.warning" />
                </td>
            </tr>
        </table>

    </div>
</form>