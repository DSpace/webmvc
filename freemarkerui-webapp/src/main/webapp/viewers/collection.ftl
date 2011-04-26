<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#--
    Viewer for collections.
    Expects including template to have set a value for 'currentCollection', which is the collection to render.
-->
<#import "/includes/dspace.ftl" as dspace />
<h1 class="ds-div-head">${currentCollection.getName()!"Untitled"}</h1>
<div id="CollectionViewer_div_collection-home" class="ds-static-div primary repository collection">
    <div id="CollectionViewer_div_collection-search-browse" class="ds-static-div secondary search-browse">
        <div id="CollectionViewer_div_collection-browse" class="ds-static-div secondary browse">
            <h2 class="ds-head"><@dspace.message "ui.navigation.browse.header" /></h2>
            <ul id="CollectionViewer_list_collection-browse" class="ds-simple-list collection-browse">
                <#list navigation.browseIndices as browseIndex>
                    <li>
                        <a href="<@dspace.url relativeUrl="/handle/${currentCollection.getHandle()}/browse?type=${browseIndex.getName()}" />" class=""><@dspace.message "ui.navigation.browse.${browseIndex.getName()}" /></a>
                    </li>
                </#list>
            </ul>
        </div>
        <form id="CollectionViewer_div_collection-search" class="ds-interactive-div secondary search" method="post" action="">
            <p id="CollectionSearch_p_search-query" class="ds-paragraph">
                <@dspace.message "ui.collection.search"/>
                <input id="CollectionSearch_field_query" class="ds-text-field" name="query" type="text" value="">
                <input id="CollectionSearch_field_submit" class="ds-button-field" name="submit" type="submit" value="Go">
            </p>
        </form>
    </div>
    <div id="CollectionViewer_div_collection-view" class="ds-static-div secondary">
        <div xmlns:oreatom="http://www.openarchives.org/ore/atom/" xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:atom="http://www.w3.org/2005/Atom" class="detail-view">
            <#if currentCollection.getLogo()??>
                <div class="ds-logo-wrapper">
                    <img alt="Logo" src="<@dspace.url relativeUrl="/retrieve/${currentCollection.getLogo().getID()}" />"/>
                </div>
            </#if>
            <#if currentCollection.getMetadata("introductory_text")??>
                <p class="intro-text">
                    ${currentCollection.getMetadata("introductory_text")}
                </p>
                <#-- Add Collection description introductory_text, short_description, copyright_text, side_bar_text -->
            </#if>
        </div>
    </div>
    <h2 class="ds-div-head"><@dspace.message "ui.common.recent.items" /></h2>
    <div id="CollectionViewer_div_collection-recent-submission" class="ds-static-div secondary recent-submission">
        <#assign recentSubmissions=dspaceHelper.recentSubmissions.within(currentCollection) />
        <#if recentSubmissions??>
            <ul class="ds-artifact-list">
                <#list recentSubmissions as currentItem>
                    <#assign trCss = (currentItem_index % 2 == 0)?string("odd","even") />
                    <li class="ds-artifact-item ${trCss}">
                        <div class="artifact-description">
                            <#include "/viewers/itemRecentEntry.ftl" />
                        </div>
                    </li>
                </#list>
            </ul>
        <#else>
        </#if>
    </div>
</div>
