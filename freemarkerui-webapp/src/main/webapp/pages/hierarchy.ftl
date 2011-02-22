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
<html>
    <head>
        <title><@dspace.message "ui.community-list.heading" /></title>
    </head>
    <body>
        <h1 class="ds-div-head"><@dspace.message "ui.community-list.heading" /></h1>
        <div id="CommunityBrowser_div_comunity-browser" class="ds-static-div primary">
            <ul>
                <#-- Iterate the list of top level communities -->
                <#list topLevelCommunities as tlComm>
                    <#assign tlcCss = (tlComm_index % 2 == 0)?string("odd","even") />
                    <li class="ds-artifact-item community ${tlcCss}">
                        <@processCommunity tlComm />
                    </li>
                </#list>
            </ul>
        </div>
    </body>
</html>
<#macro processCommunity comm>
    <div class="artifact-description">
        <div class="artifact-title">
            <span class="Z3988"><a href="<@dspace.url relativeUrl="/handle/${comm.getHandle()}" />">${comm.getName()}</a></span>
        </div>
        <#if comm.getMetadata("short_description")??>
            <div class="article-info">
                <span class="short-description">
                    ${comm.getMetadata("short_description")}
                </span>
            </div>
        </#if>
    </div>

    <#-- Iterate the collections within this community -->
    <#if collectionMap(comm.getID())??>
        <ul>
            <#list collectionMap(comm.getID()) as coll>
                <#assign colCss = (coll_index % 2 == 0)?string("odd","even") />
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

    <#-- Iterate the collections within this community -->
    <#if communityMap(comm.getID())??>
        <ul>
            <#list communityMap(comm.getID()) as subComm>
                <#assign subComCss = (subComm_index % 2 == 0)?string("odd","even") />
                <li class="ds-artifact-item collection ${subComCss}">
                    <@processCommunity subComm />
                </li>
            </#list>
        </ul>
    </#if>
</#macro>
