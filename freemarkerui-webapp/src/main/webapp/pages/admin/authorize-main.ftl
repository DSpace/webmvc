<#import "/includes/dspace.ftl" as dspace />
<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>
<table width="95%">
    <tr>
        <td align="left">
            <h3><@dspace.message "ui.dspace-admin.authorize-main.choose" /></h3>
        </td>
        <td align="right" class="standard">
            <a href="<@dspace.url "/help/site-admin.html#authorize" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
        </td>
    </tr>
</table>

<form method="post" action="<@dspace.url "/admin/authorize" />">
    <div align="center">
        <table width="70%">
            <tr>
                <td align="center">
                    <input type="submit" name="submit_community" value="<@dspace.message "ui.dspace-admin.authorize-main.manage1" />" />
                </td>
            </tr>
            <tr>
                <td align="center">
                    <input type="submit" name="submit_collection" value="<@dspace.message "ui.dspace-admin.authorize-main.manage2" />" />
                </td>
            </tr>
            <tr>
                <td align="center">
                    <input type="submit" name="submit_item" value="<@dspace.message "ui.dspace-admin.authorize-main.manage3" />" />
                </td>
            </tr>
            <tr>
                <td align="center">
                    <input type="submit" name="submit_advanced" value="<@dspace.message "ui.dspace-admin.authorize-main.advanced" />" />
                </td>
            </tr>
        </table>
    </div>
</form>