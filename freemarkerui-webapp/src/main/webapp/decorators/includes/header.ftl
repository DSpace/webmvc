<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
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
