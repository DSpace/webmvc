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
<#if searchForm.isAdvancedForm()>
    <#assign paginationLink="${linkBase}?order=${searchForm.isAscending()?string('ASC','DESC')}&amp;rpp=${searchForm.resultsPerPage}&amp;etal=${searchForm.etAl}&amp;num_search_field=${searchForm.numAdvancedFields}&amp;advanced=true" />
    <#list searchForm.advancedFields as currentField>
        <#assign paginationLink>${paginationLink}&amp;query${currentField_index + 1}=${currentField.query!""}&amp;field${currentField_index + 1}=${currentField.field!"ANY"}&amp;conjunction${currentField_index + 1}=${currentField.conjunction!"AND"}</#assign>
    </#list>
<#else>
    <#assign paginationLink="${linkBase}?query=${searchForm.query?url}&amp;order=${searchForm.isAscending()?string('ASC','DESC')}&amp;rpp=${searchForm.resultsPerPage}&amp;etal=${searchForm.etAl}" />
</#if>
<#assign paginationLink>${paginationLink}&amp;submit=true</#assign>
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
