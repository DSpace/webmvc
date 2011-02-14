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
<#if browseInfo.inCommunity()||browseInfo.inCollection()>
    <#assign linkBase="/handle/${browseInfo.browseContainer.handle}/browse" />
<#else>
    <#assign linkBase="/browse" />
</#if>
<html>
    <head>
        <title><@dspace.message "ui.browse.heading.${browseInfo.browseIndex.name}" /></title>
    </head>
    <body>
        <h1 class="ds-div-head"><@dspace.message "ui.browse.heading.${browseInfo.browseIndex.name}" /></h1>
        <div class="ds-static-div primary">
            <#assign showSortByOptions=true />
            <#include "/includes/browse/navigation.ftl" />
            <#include "/includes/browse/controls.ftl" />
            <div class="pagination clearfix top">
                <#include "/includes/browse/pagination.ftl" />
            </div>
            <div class="ds-static-div primary">
                <ul xmlns:oreatom="http://www.openarchives.org/ore/atom/" xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:atom="http://www.w3.org/2005/Atom" class="ds-artifact-list">
                    <#list browseInfo.results as currentItem>
                        <#assign trCss = (currentItem_index % 2 == 0)?string("even","odd") />
                        <li class="ds-artifact-item ${trCss}">
                            <#include "/viewers/itemListEntry.ftl" />
                        </li>
                    </#list>
                </ul>
            </div>
            <div class="pagination clearfix bottom">
                <#include "/includes/browse/pagination.ftl" />
            </div>
        </div>
    </body>
</html>
