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
    <#assign linkBase="/handle/${searchInfo.searchContainer.handle}/simple-search" />
<#else>
    <#assign linkBase="/simple-search" />
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
                <#-- include "pagination.ftl" / -->
            </div>
            <div class="ds-static-div primary">
                <#if searchInfo.communityResults??>
                    <ul class="ds-artifact-list">
                        <#list searchInfo.communityResults as currentCommunity>
                            <#assign trCss = (currentCommunity_index % 2 == 0)?string("even","odd") />
                            <li class="ds-artifact-item ${trCss}">
                                <#include "/viewers/itemListEntry.ftl" />
                            </li>
                        </#list>
                    </ul>
                </#if>
                <#if searchInfo.collectionResults??>
                    <ul class="ds-artifact-list">
                        <#list searchInfo.collectionResults as currentCollection>
                            <#assign trCss = (currentCollection_index % 2 == 0)?string("even","odd") />
                            <li class="ds-artifact-item ${trCss}">
                                <#include "/viewers/itemListEntry.ftl" />
                            </li>
                        </#list>
                    </ul>
                </#if>
                <#if searchInfo.itemResults??>
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
                <#-- include "pagination.ftl" / -->
            </div>
        </div>
    </body>
</html>
