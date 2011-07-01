<#import "/includes/dspace.ftl" as dspace />

<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>

<h1><@dspace.message "ui.dspace-admin.item-select.heading" /></h1>

<#if invalidid??>
    <p>
    <#assign showingArgs = "/dspace-freemarkerui-webapp-1.7.0-SNAPSHOT/admin/edit-communities">
    <@dspace.messageArgs "ui.dspace-admin.item-select.text", showingArgs />
    </p>
</#if>

<%-- <p>Enter the Handle or internal item ID of the item you wish to select. --%>
    <div><@dspace.message "ui.dspace-admin.item-select.enter" />
    <a href="<@dspace.url "/help/site-admin.html#itempolicies" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.morehelp" /></a>
    </div>

    <form method="post" action="<@dspace.url "/admin/authorize" />">
        <div align="center">
            <table class="miscTable">
                <tr class="oddRowEvenCol">
                    <%-- <td class="submitFormLabel">Handle:</td> --%>
                    <td class="submitFormLabel"><label for="thandle"><@dspace.message "ui.dspace-admin.item-select.handle" /></label></td>
                    <td>
                            <input type="text" name="handle" id="thandle" value="<#if handleprefix??>${handleprefix}</#if>/" size=12>
                            <input type="submit" name="submit_item_select" value="<@dspace.message "ui.dspace-admin.item-select.find" />" />
                    </td>
                </tr>
                <tr><td></td></tr>
                <tr class="oddRowEvenCol">
                    <td class="submitFormLabel"><label for="titem_id"><@dspace.message "ui.dspace-admin.item-select.id" /></label></td>
                    <td>
                            <input type="text" name="item_id" size="12">
                            <input type="submit" name="submit_item_select" value="<@dspace.message "ui.dspace-admin.item-select.find" />" />
                    </td>
                </tr>
            </table>
        </div>
    </form>

