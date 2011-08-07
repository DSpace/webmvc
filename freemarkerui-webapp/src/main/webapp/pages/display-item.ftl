<#import "/includes/dspace.ftl" as dspace />


<#if displayall??>
    <#assign displayAll=displayall.booleanValue()>
    <#else>
        <#assign displayAll=false>
</#if>

<#if suggestenable??>
    <#assign suggestLink=suggestenable.booleanValue()>
    <#else>
        <#assign suggestLink=false>
</#if>

<#if admin_button??>
    <#assign adminbutton=admin_button.booleanValue()>
    <#else>
        <#assign adminbutton=false>
</#if>

<#if item??>
    <#assign handle=item.getHandle()>
    <#else>
        <#assign handle="">
</#if>


<#if item??><#assign currentItem=item></#if>
<table align="center" class="miscTable">
    <tr>
        <td class="evenRowEvenCol" align="center">

        <@dspace.processMetadata item=currentItem field="dc.identifier.uri" ; dcvalues>

            <span class="bold"><@dspace.message "ui.display-item.identifier" /></span>
            <span>
                <#list dcvalues as dcvalue>
                    <a href="${dcvalue.value}">${dcvalue.value}</a>
                    <#if dcvalue_has_next>
                        <br/>
                    </#if>
                </#list>
            </span>
        </@dspace.processMetadata>
        </td>

    <#if item??&& adminbutton==true>
        <td class="evenRowEvenCol" align="center">
            <form method="post" action="<@dspace.url "/mydspace"/>">
                <input type="hidden" name="item_id" value="${item.getID()}"/>
                <input type="hidden" name="step" value="5"/>
                <input type="submit" name="submit" value="<@dspace.message "ui.mydspace.request.export.item" />"/>
            </form>
            <form method="post" action="<@dspace.url "/mydspace"/>">
                <input type="hidden" name="item_id" value="${item.getID()}"/>
                <input type="hidden" name="step" value="6"/>
                <input type="submit" name="submit"
                       value="<@dspace.message "ui.mydspace.request.export.migrateitem" />"/>
            </form>
            <form method="post" action="<@dspace.url "/dspace-admin/metadataexport"/>">
                <input type="hidden" name="handle" value="${item.getHandle()}"/>
                <input type="submit" name="submit" value="<@dspace.message "ui.general.metadataexport.button" />"/>
            </form>

        </td>
        <td class="evenRowEvenCol" align="center">
            <form method="get" action="<@dspace.url "/admin/item/${item.getID()}/edit"/>">
                <input type="hidden" name="item_id" value="${item.getID()}"/>
                <input type="submit" name="edit" value="<@dspace.message "ui.general.edit.button" />"/>
            </form>
        </td>
    </#if>
    </tr>
</table>
<br/>

<#if displayAll==true>
    <#assign displayStyle="full">
    <#else>
        <#assign displayStyle="">
</#if>

<table>
    <thead>
    <tr>
        <th>Schema Element Qualifier</th>
        <th>Value</th>
        <th>Language</th>
        <th></th>
    </tr>
    </thead>
<#assign row = 0>
    <tbody>
    <#if values??>
        <#list values as value>
        <tr id="metadata_${row}">
            <td><input type="hidden" name="name_${row}"
                       value="${value.schema!""}_${value.element!""}_${value.qualifier!""}">${value.schema!""}
                .${value.element!""}.${value.qualifier!""}</td>
            <td>${value.value!""}</td>
            <td>${value.language!""}</td>
        </tr>
            <#assign row = row +1>
        </#list></#if>
    </tbody>
</table>



<#if displayAll==true>

<div align="center">

    <#if workspace_id??>

        <form method="post" action="<@dspace.url "/submit"/>">
            <input type="hidden" name="workspace_id" value="${workspace_id.intValue()?string}"/>
            <input type="submit" name="submit_simple" value="<@dspace.message "ui.display-item.text1" />"/>
        </form>

        <#else>
            <form method="get" action="<@dspace.url "/handle/${handle}"/>">
                <input type="hidden" name="mode" value="simple"/>
                <input type="submit" name="submit_simple" value="<@dspace.message "ui.display-item.text1" />"/>
            </form>

    </#if>
</div>
    <#else>
    <div align="center">

        <#if workspace_id??>
            <form method="post" action="<@dspace.url "/submit"/>">
                <input type="hidden" name="workspace_id" value="${workspace_id.intValue()?string}"/>
                <input type="submit" name="submit_full" value="<@dspace.message "ui.display-item.text1" />"" />
            </form>
            <#else>
                <form method="get" action="<@dspace.url "/handle/${handle}"/>">
                    <input type="submit" name="submit_simple" value="<@dspace.message "ui.display-item.text1" />"/>
                </form>
        </#if>

        <#if suggestLink==true>
            <a href="<@dspace.url "/suggest/${handle}"/>" target="new_window">
            <@dspace.message "ui.display-item.suggest" /></a>
        </#if>
    </div>

</#if>

<div align="center">
    <form method="get" action="<@dspace.url "/displaystats"/>">
        <input type="hidden" name="handle" value="${handle}"/>
        <input type="submit" name="submit_simple" value="<@dspace.message "ui.display-item.display-statistics" />"/>
    </form>
</div>

<#if workspace_id??>
<div align="center">
    <form method="post" action="<@dspace.url "/workspace"/>">
        <input type="hidden" name="workspace_id" value="${workspace_id.intValue()?string}"/>
        <input type="submit" name="submit_open" value="<@dspace.message "ui.display-item.back_to_workspace" />"/>
    </form>
</div>


    <#else>
        <#assign currentItem=item>
        <#include "/viewers/itemSummary.ftl" />
</#if>

