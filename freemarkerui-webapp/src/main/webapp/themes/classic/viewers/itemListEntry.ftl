<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#--
    Viewer for items in browse / search lists.
    Expects including template to have set a value for 'currentItem', which is the item to render.
-->
<#import "/includes/dspace.ftl" as dspace />
<@dspace.processMetadata item=currentItem field="dc.title" ; dcvalues>
    <div class="artifact-title">
        <#list dcvalues as dcvalue>
            <a href="<@dspace.url relativeUrl="/handle/${currentItem.getHandle()}" />"><span class="z3988" title="">${dcvalue.value!"Untitled"}</span></a>
            <#if dcvalue_has_next><br/></#if>
        </#list>
    </div>
</@dspace.processMetadata>
<div class="artifact-info">
    <@dspace.processMetadata item=currentItem field="dc.contributor.author" ; dcvalues>
        <span class="author">
            <#list dcvalues as dcvalue>
                <span>${dcvalue.value}</span>
                <#if dcvalue_has_next>; </#if>
            </#list>
        </span>
    </@dspace.processMetadata>
    <@dspace.processMetadata item=currentItem field="dc.date.published" ; dcvalues>
        <span class="publisher-date">
            <#list dcvalues as dcvalue>
                (<span class="date">${dcvalue.value}</span>)
            </#list>
        </span>
    </@dspace.processMetadata>
</div>
