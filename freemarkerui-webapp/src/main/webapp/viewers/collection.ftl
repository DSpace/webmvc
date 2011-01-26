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
<h1 class="ds-div-head">${collection.getName()!"Untitled"}</h1>
<div id="CollectionViewer_div_collection-home" class="ds-static-div primary repository collection">
    <div id="CollectionViewer_div_collection-search-browse" class="ds-static-div secondary search-browse">
        <div id="CollectionViewer_div_collection-browse" class="ds-static-div secondary browse">
            <h2 class="ds-head"><@dspace.message "ui.navigation.browse.header" /></h2>
            <ul id="CollectionViewer_list_collection-browse" class="ds-simple-list collection-browse">
            <#list navigation.browseIndices as browseIndex>
                <li>
                    <a href="<@dspace.url relativeUrl="/handle/${collection.getHandle()}/browse?type=${browseIndex.getName()}" />" class=""><@dspace.message "ui.navigation.browse.${browseIndex.getName()}" /></a>
                </li>
            </#list>
            </ul>
        </div>
        <form id="CollectionViewer_div_collection-search" class="ds-interactive-div secondary search" method="post" action="">

        </form>
    </div>
    <div id="CollectionViewer_div_collection-view" class="ds-static-div secondary">
        <div xmlns:oreatom="http://www.openarchives.org/ore/atom/" xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:atom="http://www.w3.org/2005/Atom" class="detail-view">
            <#if collection.getLogo()??>
                <div class="ds-logo-wrapper">
                    <img alt="Logo" src="<@dspace.url relativeUrl="/retrieve/${collection.getLogo().getID()}" />
                </div>
            </#if>
            <#if collection.getMetadata("introductory_text")??>
                <p class="intro-text">
                    ${collection.getMetadata("introductory_text")}
                </p>
                <#-- Add Collection description introductory_text, short_description, copyright_text, side_bar_text -->
            </#if>
        </div>
    </div>
    <h2 class="ds-div-head"><@dspace.message "ui.common.recent.items" /></h2>
    <div id="CollectionViewer_div_collection-recent-submission" class="ds-static-div secondary recent-submission">
        <#-- Add article list -->
    </div>
</div>
