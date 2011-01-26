<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- Internationalization can also be achieved via multiple templates - ie. home.ftl, home_en_US.ftl, etc. -->
<#import "/includes/dspace.ftl" as dspace />
<h1 class="ds-div-head">${community.getName()!"Untitled"}</h1>
<div id="CommunityViewer_div_community-home" class="ds-static-div primary repository community">
    <div id="CommunityViewer_div_community-search-browse" class="ds-static-div secondary search-browse">
        <div id="CommunityViewer_div_community-browse" class="ds-static-div secondary browse">
            <h2 class="ds-head"><@dspace.message "ui.navigation.browse.header" /></h2>
            <ul id="CommunityViewer_list_community-browse" class="ds-simple-list community-browse">
            <#list navigation.browseIndices as browseIndex>
                <li>
                    <a href="<@dspace.url relativeUrl="/handle/${community.getHandle()}/browse?type=${browseIndex.getName()}" />" class=""><@dspace.message "ui.navigation.browse.${browseIndex.getName()}" /></a>
                </li>
            </#list>
            </ul>
        </div>
        <form id="CommunityViewer_div_community-search" class="ds-interactive-div secondary search" method="post" action="">

        </form>
    </div>
    <div id="CommunityViewer_div_community-view" class="ds-static-div secondary">
        <div xmlns:oreatom="http://www.openarchives.org/ore/atom/" xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:atom="http://www.w3.org/2005/Atom" class="detail-view">
            <#if community.getLogo()??>
                <div class="ds-logo-wrapper">
                    <img alt="Logo" src="<@dspace.url relativeUrl="/retrieve/${community.getLogo().getID()}" />" />
                </div>
            </#if>
            <#if community.getMetadata("introductory_text")??>
                <p class="intro-text">
                    ${community.getMetadata("introductory_text")}
                </p>
            </#if>
        </div>
        <h2 class="ds-list-head"><@dspace.message "ui.community.collections" /></h2>
        <#-- Add Collection list -->
    </div>
    <h2 class="ds-div-head"><@dspace.message "ui.common.recent.items" /></h2>
    <div id="CommunityViewer_div_community-recent-submission" class="ds-static-div secondary recent-submission">
        <#-- Add article list -->
    </div>
</div>
