<#import "/includes/dspace.ftl" as dspace />
<#if !dspace.themeTemplatePath?has_content>
<div is="ds-site-warning=">
    <p>The theme '${dspace.themeName}' has no template directory set</p>
</div>
</#if>
<div id="ds-header">
    <a href="<@dspace.url "/" />"><span id="ds-header-logo"></span></a>
    <h1 class="pagetitle">${title}</h1>
    <h2 class="static-pagetitle">DSpace/FreeMarker Repository</h2>
    <ul id="ds-trail">
        <li class="ds-trail-link first-link last-link">
            <a href="<@dspace.url "/" />">DSpace Home</a>
        </li>
    </ul>
    <div id="ds-user-box">
        <p>
            <a href="<@dspace.url "/login" />">Login</a>
        </p>
    </div>
</div>
