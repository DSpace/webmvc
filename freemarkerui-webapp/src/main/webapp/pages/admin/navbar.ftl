<#import "/includes/dspace.ftl" as dspace />

<%-- <h1>Administration Tools</h1> --%>
    <h1><@dspace.message "ui.dspace-admin.index.heading" /></h1>

    <%-- <p>Please select an operation from the navigation bar on the left.</p> --%>
    <p><@dspace.message "ui.dspace-admin.index.text" /></p>

<div id="Navigation_list_account" class="ds-option-set">

    <ul class="ds-simple-list">

         <li> <a href="<@dspace.url "/admin/eperson/edit-epeople" />"><@dspace.message "ui.layout.navbar-admin.epeople" /></li>

    </ul>

</div>