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
                <#list topLevelCommunities as tlComm>
                    <#assign trCss = (tlComm_index % 2 == 0)?string("even","odd") />
                    <li class="ds-artifact-item community ${trCss}">
                        <div class="artifact-description">
                            <div class="artifact-title">
                                <span class="Z3988"><a href="<@dspace.url relativeUrl="/handle/${tlComm.getHandle()}" />">${tlComm.getName()}</a></span>
                            </div>
                            <#if tlComm.getMetadata("short_description")??>
                                <div class="article-info">
                                    <span class="short-description">
                                        ${tlComm.getMetadata("short_description")}
                                    </span>
                                </div>
                            </#if>
                        </div>
                    </li>
                </#list>
            </ul>
        </div>
    </body>
</html>
