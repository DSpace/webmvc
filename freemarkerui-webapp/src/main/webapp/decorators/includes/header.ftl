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
<div id="ds-header-wrapper">
    <div id="ds-header" class="clearfix">
        <a id="ds-header-logo-link" href="<@dspace.url "/" />">
            <span id="ds-header-logo"></span>
        </a>
        <h1 class="pagetitle">${title}</h1>
        <h2 class="static-pagetitle">DSpace/FreeMarker Repository</h2>
        <div id="ds-user-box">
            <#if context.currentUser??>
                <p>
                    <a href="<@dspace.url "/profile" />">${context.getCurrentUser().getFirstName()} ${context.getCurrentUser().getLastName()}</a> |
                    <a href="<@dspace.url "/logout" />">Logout</a>
                </p>
            <#else>
                <p>
                    <a href="<@dspace.url "/login" />">Login</a>
                </p>
            </#if>
        </div>
    </div>
</div>
<div id="ds-trail-wrapper">
    <ul id="ds-trail">
        <li class="ds-trail-link first-link">
            <a href="<@dspace.url "/" />">DSpace Home</a>
        </li>
        <#if trailList??>
            <#list trailList as trailEntry>
                <li class="ds-trail-arrow">&#x21d2;</li>
                <li class="ds-trail-link">
                    <#if trailEntry_has_next>
                        <@dspace.trailLink trailEntry />
                    <#else>
                        <@dspace.trailLabel trailEntry />
                    </#if>
                </li>
            </#list>
        </#if>
    </ul>
</div>
