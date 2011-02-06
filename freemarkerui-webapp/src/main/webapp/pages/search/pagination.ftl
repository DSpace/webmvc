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

<#assign paginationLink="${linkBase}?query=${searchInfo.query?url}&amp;order=${searchInfo.isAscending()?string('ASC','DESC')}&amp;rpp=${searchInfo.resultsPerPage}&amp;etal=${searchInfo.etAl}" />
<#assign showingArgs=["${searchInfo.overallPosition+1}", "${searchInfo.overallPosition+searchInfo.resultCount}", "${searchInfo.total}"] />
<p class="pagination-info"><@dspace.messageArgs "ui.list.showing", showingArgs /></p>
<ul class="pagination-links">
    <li>
        <#if searchInfo.isFirst()>
        <#else>
            <a href="<@dspace.url relativeUrl="${paginationLink}&amp;start=${searchInfo.prevOffset}" />"><@dspace.message "ui.list.prevPage" /></a>
        </#if>
    </li>
    <li>
        <#if searchInfo.isLast()>
        <#else>
            <a href="<@dspace.url relativeUrl="${paginationLink}&amp;start=${searchInfo.nextOffset}" />"><@dspace.message "ui.list.nextPage" /></a>
        </#if>
    </li>
</ul>
