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
    Viewer for communities.
    Expects including template to have set a value for 'currentCommunity', which is the community to render.
-->
<#import "/includes/dspace.ftl" as dspace />
<h1 class="ds-div-head">${currentCommunity.getName()!"Untitled"}</h1>
<div id="CommunityViewer_div_community-home" class="ds-static-div primary repository community">
    <div id="CommunityViewer_div_community-search-browse" class="ds-static-div secondary search-browse">
        <div id="CommunityViewer_div_community-browse" class="ds-static-div secondary browse">
            <h2 class="ds-head"><@dspace.message "ui.navigation.browse.header" /></h2>
            <ul id="CommunityViewer_list_community-browse" class="ds-simple-list community-browse">
            <#list navigation.browseIndices as browseIndex>
                <li>
                    <a href="<@dspace.url relativeUrl="/handle/${currentCommunity.getHandle()}/browse?type=${browseIndex.getName()}" />" class=""><@dspace.message "ui.navigation.browse.${browseIndex.getName()}" /></a>
                </li>
            </#list>
            </ul>
        </div>
        <form id="CommunityViewer_div_community-search" class="ds-interactive-div secondary search" method="post" action="">
            <p id="CommunitySearch_p_search-query" class="ds-paragraph">
                <@dspace.message "ui.community.search"/>
                <input id="CommunitySearch_field_query" class="ds-text-field" name="query" type="text" value="">
                <input id="CommunitySearch_field_submit" class="ds-button-field" name="submit" type="submit" value="Go">
            </p>
        </form>
    </div>
    <div id="CommunityViewer_div_community-view" class="ds-static-div secondary">
        <div xmlns:oreatom="http://www.openarchives.org/ore/atom/" xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:atom="http://www.w3.org/2005/Atom" class="detail-view">
            <#if currentCommunity.getLogo()??>
                <div class="ds-logo-wrapper">
                    <img alt="Logo" src="<@dspace.url relativeUrl="/retrieve/${currentCommunity.getLogo().getID()}" />" />
                </div>
            </#if>
            <#if currentCommunity.getMetadata("introductory_text")??>
                <p class="intro-text">
                    ${currentCommunity.getMetadata("introductory_text")}
                </p>
            </#if>
        </div>

        <#assign subCommunities=currentCommunity.getSubcommunities() />
        <#if subCommunities?has_content>
            <h2 class="ds-list-head"><@dspace.message "ui.community.subcommunities" /></h2>
            <ul>
                <#list subCommunities as subComm>
                    <#assign subCommCss = (subComm_index % 2 == 0)?string("even","odd") />
                    <li class="ds-artifact-item collection ${subCommCss}">
                        <div class="artifact-description">
                            <div class="artifact-title">
                                <span class="Z3988"><a href="<@dspace.url relativeUrl="/handle/${subComm.getHandle()}" />">${subComm.getName()}</a></span>
                            </div>
                            <#if subComm.getMetadata("short_description")??>
                                <div class="article-info">
                                    <span class="short-description">
                                        ${subComm.getMetadata("short_description")}
                                    </span>
                                </div>
                            </#if>
                        </div>
                    </li>
                </#list>
            </ul>
        </#if>

        <#assign collections=currentCommunity.getCollections() />
        <#if collections?has_content>
            <h2 class="ds-list-head"><@dspace.message "ui.community.collections" /></h2>
            <ul>
                <#list collections as coll>
                    <#assign colCss = (coll_index % 2 == 0)?string("even","odd") />
                    <li class="ds-artifact-item collection ${colCss}">
                        <div class="artifact-description">
                            <div class="artifact-title">
                                <span class="Z3988"><a href="<@dspace.url relativeUrl="/handle/${coll.getHandle()}" />">${coll.getName()}</a></span>
                            </div>
                            <#if coll.getMetadata("short_description")??>
                                <div class="article-info">
                                    <span class="short-description">
                                        ${coll.getMetadata("short_description")}
                                    </span>
                                </div>
                            </#if>
                        </div>
                    </li>
                </#list>
            </ul>
        </#if>
    </div>
    <h2 class="ds-div-head"><@dspace.message "ui.common.recent.items" /></h2>
    <div id="CommunityViewer_div_community-recent-submission" class="ds-static-div secondary recent-submission">
        <#assign recentSubmissions=dspaceHelper.recentSubmissions.within(currentCommunity) />
        <#if recentSubmissions?has_content>
            <ul class="ds-artifact-list">
                <#list recentSubmissions as currentItem>
                    <#assign trCss = (currentItem_index % 2 == 0)?string("even","odd") />
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
