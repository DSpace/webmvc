<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- Requires linkBase to be set -->
<#import "/includes/dspace.ftl" as dspace />

<#assign paginationLink="${linkBase}?type=${browseInfo.browseIndex.name}&amp;order=${browseInfo.isAscending()?string('ASC','DESC')}&amp;rpp=${browseInfo.resultsPerPage}&amp;etal=${browseInfo.etAl}" />
<#if browseInfo.hasAuthority()>
    <#assign paginationLink="${paginationLink}&amp;authority=${browseInfo.authority?url}" />
<#elseif browseInfo.hasValue()>
    <#assign paginationLink="${paginationLink}&amp;value=${browseInfo.value?url}" />
</#if>
<#assign showingArgs=["${browseInfo.overallPosition+1}", "${browseInfo.overallPosition+browseInfo.resultCount}", "${browseInfo.total}"] />
<p class="pagination-info"><@dspace.messageArgs "ui.list.showing", showingArgs /></p>
<ul class="pagination-links">
    <li>
        <#if browseInfo.isFirst()>
        <#else>
            <a href="<@dspace.url relativeUrl="${paginationLink}&amp;offset=${browseInfo.prevOffset}" />"><@dspace.message "ui.list.prevPage" /></a>
        </#if>
    </li>
    <li>
        <#if browseInfo.isLast()>
        <#else>
            <a href="<@dspace.url relativeUrl="${paginationLink}&amp;offset=${browseInfo.nextOffset}" />"><@dspace.message "ui.list.nextPage" /></a>
        </#if>
    </li>
</ul>
