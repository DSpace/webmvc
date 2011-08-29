<#import "/includes/dspace.ftl" as dspace />

<h2>
<#assign showcollection = false>
<#if browsetype?? && browsetext??>
    <#if browsetype = "Add">
        <#assign showingArgs=[browsetext]>
    <@dspace.messageArgs "ui.tools.itemmap-browse.heading-authors", showingArgs />
        <#elseif browsetype = "Remove">
            <#if collection??>
                <#assign showingArgs=[browsetext, collection.getName()]>
            <@dspace.messageArgs "ui.tools.itemmap-browse.heading-authors", showingArgs /></#if>
    </#if>
</#if>
</h2>

<#if browsetype?? && browsetype = "Add">
    <#assign showingArgs=[collection.getName()]><@dspace.messageArgs "ui.tools.itemmap-browse.add", showingArgs />
</#if>

<#if browsetype?? && browsetype = "Remove">
    <#assign showingArgs=[collection.getName()]><@dspace.messageArgs "ui.tools.itemmap-browse.remove", showingArgs />
</#if>

<form method="post" action="<@dspace.url "/tools/itemmap"/>">
    <input type="hidden" name="cid" value="<#if collection??>${collection.getID()}</#if>"/>

    <table>
        <tr>
            <td>
            <#if browsetype??>
                <#if browsetype = "Add">
                    <input type="submit" name="add" value="<@dspace.message "ui.tools.general.add" />"/>
                    <#elseif browsetype = "Remove">
                        <input type="submit" name="remove" value="<@dspace.message "ui.tools.general.remove" />"/>
                </#if></#if>
            </td>
            <td><input type="submit" name="cancel" value="<@dspace.message "ui.tools.general.cancel" />" /></td>
        </tr>
    </table>


    <table class="miscTable" align="center">
        <tr>
            <th class="oddRowOddCol"><strong>
            <@dspace.message "ui.tools.itemmap-browse.th.date" />
            </strong></th>
            <th class="oddRowEvenCol"><strong>
            <@dspace.message "ui.tools.itemmap-browse.th.author" />
            </strong></th>
            <th class="oddRowOddCol"><strong>
            <@dspace.message "ui.tools.itemmap-browse.th.title" />
            </strong></th>
        <#if showcollection==true>
            <th class="oddRowEvenCol"><strong>
            <@dspace.message "ui.tools.itemmap-browse.th.action" />
            </strong></th>
            <th class="oddRowOddCol">
            <@dspace.message "ui.tools.itemmap-browse.th.remove" />
            </th>
            <#else>
                <th class="oddRowEvenCol">
                    <#if browsetype??>
                        <#if browsetype = "Add">
                            <input type="submit" name="add" value="<@dspace.message "ui.tools.general.add" />"/>
                            <#elseif browsetype = "Remove">
                                <input type="submit" name="remove" value="<@dspace.message "ui.tools.general.remove" />"/>
                        </#if></#if>
                </th>
        </#if>
        </tr>

    <#assign row="even">
    <#if itemList??>
        <#list itemList as item>
            <#assign dates = item.getMetadata("dc","date","issued","")>
            <tr>
                <td class="${row}RowOddCol">
                ${item.getMetadata("dc.date.issued")[0].value!"Untitled"}
                </td>
                <td class="${row}RowEvenCol">
                ${item.getMetadata("dc.contributor")[0].value!"Untitled"}
                </td>
                <td class="${row}RowOddCol">
                ${item.getMetadata("dc.title")[0].value!"Untitled"}</td>

                <#if showcollection==true>
                    <td class="${row}RowEvenCol"><#if collection??>${collection.getID()}</#if></td>
                    <td class="${row}RowOddCol">${item.getMetadata("dc.title")[0].value!"Untitled"}</td>
                    <#else>
                        <td class="${row}RowEvenCol"><input type="checkbox" name="item_ids" value="${item.getID()}"/>
                        </td>

                </#if>
                <#if row = "odd">
                    <#assign row = "even">
                    <#else>
                        <#assign row = "odd">
                </#if>
            </tr>
        </#list>
    </#if>

    </table>

    <table>
        <tr>
            <td>
                <#if browsetype??>

                <#if browsetype = "Add">
                <input type="submit" name="add" value="<@dspace.message "ui.tools.general.add" />" />
                <#elseif browsetype="Remove">
                <input type="submit" name="remove" value="<@dspace.message "ui.tools.general.remove" />" />
                </#if></#if>
            </td>
            <td><input type="submit" name="cancel" value="<@dspace.message "ui.tools.general.cancel" />" /></td>
        </tr>
    </table>

</form>

