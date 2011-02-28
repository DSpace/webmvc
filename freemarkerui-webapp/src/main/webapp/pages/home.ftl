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
        <title><@dspace.message "ui.home.heading" /></title>
    </head>
    <body>
        <#include "/news/home.ftl" />
        <h1 class="ds-div-head"><@dspace.message "ui.common.recent.items" /></h1>
        <div id="SiteRecentSubmissions_div_site-recent-submission" class="ds-static-div secondary recent-submission">
            <#assign recentSubmissions=dspaceHelper.recentSubmissions.allRepository() />
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
    </body>
</html>