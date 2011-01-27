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
        <title><@dspace.message "ui.browse.heading.${browseInfo.browseIndex.name}" /></title>
    </head>
    <body>
        <#assign sharedLink="browse?type=${browseInfo.getBrowseIndex().getName()}&amp;order=${browseInfo.isAscending()?string('ASC','DESC')}&amp;rpp=${browseInfo.getResultsPerPage()}" />
        <h1 class="ds-div-head"><@dspace.message "ui.browse.heading.${browseInfo.browseIndex.name}" /></h1>
        <div class="ds-static-div primary">
            <ul xmlns:oreatom="http://www.openarchives.org/ore/atom/" xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:atom="http://www.w3.org/2005/Atom" class="ds-artifact-list">
                <#list browseInfo.getStringResults() as currentEntry>
                    <#assign trCss = (currentEntry_index % 2 == 0)?string("even","odd") />
                    <li class="${trCss}">
                        <div class="artifact-info">
                            <#if currentEntry[1]??>
                                <a href="<@dspace.url relativeUrl="${sharedLink}&amp;authority=${currentEntry[1]}" />">${currentEntry[0]}</a>
                            <#else>
                                <a href="<@dspace.url relativeUrl="${sharedLink}&amp;value=${currentEntry[0]}" />">${currentEntry[0]}</a>
                            </#if>
                        </div>
                    </li>
                </#list>
            </ul>
        </div>
    </body>
</html>
