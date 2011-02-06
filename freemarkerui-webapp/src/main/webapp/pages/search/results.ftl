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
<#if searchInfo.inCommunity()||searchInfo.inCollection()>
    <#assign linkBase="/handle/${searchInfo.searchContainer.handle}/search" />
<#else>
    <#assign linkBase="/search" />
</#if>
<html>
    <head>
        <title><@dspace.message "ui.search.heading.results" /></title>
    </head>
    <body>
        <h1 class="ds-div-head"><@dspace.message "ui.search.heading.results" /></h1>
        <div class="ds-static-div primary">
            <#assign showSortByOptions=true />
            <#-- include "navigation.ftl" / -->
            <#-- include "controls.ftl" / -->
            <div class="pagination clearfix top">
                <#include "pagination.ftl" />
            </div>
            <div class="ds-static-div primary">
                <#if searchInfo.communityResults?has_content>
                    <h3 class="ds-list-head">Communities matching your query</h3>
                    <ul class="ds-artifact-list">
                        <#list searchInfo.communityResults as currentCommunity>
                            <#assign trCss = (currentCommunity_index % 2 == 0)?string("even","odd") />
                            <li class="ds-artifact-item ${trCss}">
                                <#include "/viewers/communityListEntry.ftl" />
                            </li>
                        </#list>
                    </ul>
                </#if>
                <#if searchInfo.collectionResults?has_content>
                    <h3 class="ds-list-head">Collections matching your query</h3>
                    <ul class="ds-artifact-list">
                        <#list searchInfo.collectionResults as currentCollection>
                            <#assign trCss = (currentCollection_index % 2 == 0)?string("even","odd") />
                            <li class="ds-artifact-item ${trCss}">
                                <#include "/viewers/collectionListEntry.ftl" />
                            </li>
                        </#list>
                    </ul>
                </#if>
                <#if searchInfo.itemResults?has_content>
                    <#if searchInfo.communityResults?has_content || searchInfo.collectionResults?has_content>
                        <h3 class="ds-list-head">Items matching your query</h3>
                    </#if>
                    <ul class="ds-artifact-list">
                        <#list searchInfo.itemResults as currentItem>
                            <#assign trCss = (currentItem_index % 2 == 0)?string("even","odd") />
                            <li class="ds-artifact-item ${trCss}">
                                <#include "/viewers/itemListEntry.ftl" />
                            </li>
                        </#list>
                    </ul>
                </#if>
            </div>
            <div class="pagination clearfix bottom">
                <#include "pagination.ftl" />
            </div>
        </div>
    </body>
</html>
