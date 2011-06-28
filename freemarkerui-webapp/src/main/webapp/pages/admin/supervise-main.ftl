<#import "/includes/dspace.ftl" as dspace />

<h1><@dspace.message "ui.dspace-admin.supervise-main.heading" /></h1>

<h3><@dspace.message "ui.dspace-admin.supervise-main.subheading" /></h3>

<br/>

<div align="center">
<%-- form to navigate to any of the three options available --%>
<form method="post" action="<@dspace.url "/admin/supervise" />">
    <input type="submit" name="submit_add" value="<@dspace.message "ui.dspace-admin.supervise-main.add.button" />"/>
    <br/><br/>
    <input type="submit" name="submit_view" value="<@dspace.message "ui.dspace-admin.supervise-main.view.button" />"/>
    <br/><br/>
    <input type="submit" name="submit_clean" value="<@dspace.message "ui.dspace-admin.supervise-main.clean.button" />"/>
</form>
</div>
