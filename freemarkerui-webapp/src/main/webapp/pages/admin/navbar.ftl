<#import "/includes/dspace.ftl" as dspace />

<%-- <h1>Administration Tools</h1> --%>
    <h1><@dspace.message "ui.dspace-admin.index.heading" /></h1>

    <%-- <p>Please select an operation from the navigation bar on the left.</p> --%>
    <p><@dspace.message "ui.dspace-admin.index.text" /></p>

<div id="Navigation_list_account" class="ds-option-set">

    <ul class="ds-simple-list">

         <li> <a href="<@dspace.url "/admin/editcommunities" />"><@dspace.message "ui.layout.navbar-admin.communities-collections" /></a></li>
         <li> <a href="<@dspace.url "/admin/eperson/edit-epeople" />"><@dspace.message "ui.layout.navbar-admin.epeople" /></a></li>
         <li> <a href="<@dspace.url "/admin/group/main" />"><@dspace.message "ui.layout.navbar-admin.groups" /></a></li>
         <li> <a href="<@dspace.url "/admin/supervise/main" />"><@dspace.message "ui.layout.navbar-admin.supervisors" /></a></li>
         <li> <a href="<@dspace.url "/admin/authorize/main" />"><@dspace.message "ui.layout.navbar-admin.authorization" /></a></li>
    </ul>

</div>