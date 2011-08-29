<#import "/includes/dspace.ftl" as dspace />

<h1><@dspace.message "ui.dspace-admin.eperson-deletion-error.heading" /></h1>

<#if eperson??>
<#assign showingArgs=["${eperson.getFullName()}"]>
<#else>
<#assign showingArgs=[""]>
</#if>

<p><@dspace.messageArgs "ui.dspace-admin.eperson-deletion-error.errormsg", showingArgs/></p>

<ul>
    <#if tableList??>
    <#list tableList as tL>
    <li>${tL.next()}</li>
    </#list>
    </#if>
</ul>

    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>

<p align="center">
        <a href="<@dspace.url "/admin/eperson/edit-epeople" />"><@dspace.message "ui.dspace-admin.confirm-delete-format.returntoedit" /></a>
 </p>