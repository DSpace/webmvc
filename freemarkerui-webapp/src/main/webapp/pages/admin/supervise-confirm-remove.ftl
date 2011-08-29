<#import "/includes/dspace.ftl" as dspace />

<h1><@dspace.message "ui.dspace-admin.supervise-confirm-remove.heading" /></h1>

<h3><@dspace.message "ui.dspace-admin.supervise-confirm-remove.subheading" /></h3>

<br/><br/>

<div align="center"/>

    <#if wsItem?? && itemany??><#assign titleArray = wsItem.getItem().getDC("title", null, itemany)></#if>
    <#if wsItem??><#assign submitter = wsItem.getItem().getSubmitter()></#if>

<strong><@dspace.message "ui.dspace-admin.supervise-confirm-remove.titleheader" /></strong>:
<br/>

<#if titleArray?size &gt; 0>
 ${titleArray[0].value}
<#else>
  <@dspace.message "ui.general.untitled" />
</#if>

<br/><br/>
<strong><@dspace.message "ui.dspace-admin.supervise-confirm-remove.authorheader" /></strong>:
<br/>
<a href="mailto:${submitter.getEmail()}">${submitter.getFullName()}</a>
<br/><br/>
<strong><@dspace.message "ui.dspace-admin.supervise-confirm-remove.supervisorgroupheader" /></strong>:
<br/>
<#if group??>
${group.getName()}
</#if>
<br/><br/>

<@dspace.message "ui.dspace-admin.supervise-confirm-remove.confirm" />

<%-- form to request removal of supervisory linking --%>
<form method="post" action="<@dspace.url "/admin/supervise" />">
    <#if group??><input type="hidden" name="gID" value="${group.getID()}"/></#if>
    <#if wsItem??><input type="hidden" name="siID" value="${wsItem.getID()}"/></#if>
    <input type="submit" name="submit_doremove" value="<@dspace.message "ui.dspace-admin.general.remove" />"/>
    <input type="submit" name="submit_base" value="<@dspace.message "ui.dspace-admin.general.cancel" />"/>
</form>



