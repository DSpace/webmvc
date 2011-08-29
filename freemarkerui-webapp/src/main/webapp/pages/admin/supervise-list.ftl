<#import "/includes/dspace.ftl" as dspace />

<h1><@dspace.message "ui.dspace-admin.supervise-list.heading" /></h1>

<h3><@dspace.message "ui.dspace-admin.supervise-list.subheading" /></h3>

<br/><br/>

<div align="center" />
<%-- form to navigate to the "add supervisory settings" page --%>
<form method="post" action="<@dspace.url "/admin/supervise" />">
    <input type="submit" name="submit_add" value="<@dspace.message "ui.dspace-admin.supervise-list.add.button" />"/>
    <input type="submit" name="submit_base" value="<@dspace.message "ui.dspace-admin.supervise-list.back.button" />"/>
</form>

<table class="miscTable">
    <tr>
        <th class="oddRowOddCol">
            &nbsp;
        </th>
        <th class="oddRowEvenCol">
            <@dspace.message "ui.dspace-admin.supervise-list.group" />
        </th>
        <th class="oddRowOddCol">
            <@dspace.message "ui.dspace-admin.supervise-list.author" />
        </th>
        <th class="oddRowEvenCol">
            <@dspace.message "ui.dspace-admin.supervise-list.title" />
        </th>
        <th class="oddRowOddCol">
            &nbsp;
        </th>
    </tr>

    <#assign row = "even">
    <#if supervised??>
    <#list supervised as sp>
        <#if itemany??>
        <#assign titleArray = sp.getItem().getDC("title", null, itemany)>
        </#if>
        <#assign submitter = sp.getItem().getSubmitter()>
        <#assign supervisors = sp.getSupervisorGroups()>
        <#list supervisors as spv>
        <tr>
            <td class="${row}RowOddCol">
            <%-- form to navigate to the item policies --%>
            <form action="<@dspace.url "/admin/authorize" />" method="post">
                <input type="hidden" name="item_id" value="${sp.getItem().getID()}"/>
                <input type="submit" name="submit_item_select" value="<@dspace.message "ui.dspace-admin.supervise-list.policies.button" />"/>
            </form>
            </td>
            <td class="${row}RowEvenCol">
              ${spv.getName()}
            </td>
            <td class="${row}RowOddCol">
            <a href="mailto:${submitter.getEmail()}">${submitter.getFullName()}</a>
            </td>
            <td class="${row}RowEvenCol">
                <#if titleArray?size &gt; 0>
                    ${titleArray[0].value}
                <#else>
                    <@dspace.message "ui.general.untitled" />
                </#if>
            </td>
            <td class="<%= row %>RowOddCol">
            <%-- form to request removal of supervisory linking --%>
            <form method="post" action="<@dspace.url "/admin/supervise" />">
            <input type="hidden" name="gID" value="${spv.getID()}"/>
            <input type="hidden" name="siID" value="${sp.getID()}"/>
            <input type="submit" name="submit_remove" value="<@dspace.message "ui.dspace-admin.general.remove" />"/>
            </form>
            </td>
        </tr>
            <#if row = "even">
            <#assign row = "odd">
            <#else>
            <#assign row = "even">
            </#if>
        </#list>
    </#list>
    </#if>
</table>