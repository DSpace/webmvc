<#import "/includes/dspace.ftl" as dspace />
<head>
    <script type="text/javascript" src="<@dspace.url "/utils.js" />"></script>
</head>

<table width="100%" border="0">
    <tr>
        <td align="left">

            <h1><@dspace.message "ui.mydspace.subscriptions.title" /></h1>
        </td>
        <td align="right" class="standard">
            <a href="<@dspace.url "/help/index.html#subscribe" />"
               onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
        </td>
    </tr>
</table>

<#if updated?? && updated=true>
<p><strong>
    <@dspace.message "ui.mydspace.subscriptions.info1" />
</strong></p>
</#if>
<p>
    <@dspace.message "ui.mydspace.subscriptions.info2" />
</p>
<#if subscriptions?? && subscriptions?size&gt;0>
<p>
    <@dspace.message "ui.mydspace.subscriptions.info2" />
</p>

<center>
    <table class="miscTable" summary="Table displaying your subscriptions">

        <#assign  row = "odd">
        <#list subscriptions as s>
        <tr>
        <td class="${row}RowOddCol">
                <a href="<@dspace.url "/handle/${s.getHandle()}" />">${s.getMetadata("name")}</a>
            </td>
            <td class="${row}RowEvenCol">
                <form method="post" action="<@dspace.url "/subscribe" />">
                    <input type="hidden" name="collection" value="${s.getID()}"/>
                    <input type="submit" name="submit_unsubscribe" value="<@dspace.message "ui.mydspace.subscriptions.unsub.button" />" />
                </form>
            </td>
        </tr>
        <#if row = "even">
            <#assign row = "odd">
        <#else>
            <#assign row = "even">
        </#if>
        </#list>
    </table>
</center>

<br/>

<center>
    <form method="post" action="<@dspace.url "/subscribe" />">
        <input type="submit" name="submit_clear" value="<@dspace.message "ui.mydspace.subscriptions.remove.button" />"/>
    </form>
</center>
<#else>
<p>
    <@dspace.message "ui.mydspace.subscriptions.info4" />
</p>
</#if>
<p align="center"><a href="<@dspace.url "/mydspace"/>"><@dspace.message "ui.mydspace.general.goto-mydspace" /></a></p>