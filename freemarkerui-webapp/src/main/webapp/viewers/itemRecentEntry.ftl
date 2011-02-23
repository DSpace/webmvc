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
        <#if dcvalues?size &gt; 0>
            <a href="<@dspace.url relativeUrl="/handle/${currentItem.getHandle()}" />"><span class="z3988" title="">${dcvalues[0].value!"Untitled"}</span></a>
        <#else>
            <a href="<@dspace.url relativeUrl="/handle/${currentItem.getHandle()}" />"><span class="z3988" title="">Untitled</span></a>
        </#if>
    </div>
</@dspace.processMetadata>
<div class="artifact-info">
    <@dspace.processMetadata item=currentItem field="dc.contributor.author" ; dcvalues>
        <span class="author">
            <#list dcvalues as dcvalue>
                <#if dcvalue.value??>
                    <span>${dcvalue.value}</span>
                    <#if dcvalue_has_next>; </#if>
                </#if>
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
<@dspace.processMetadata item=currentItem field="dc.description.abstract" ; dcvalues>
    <#if dcvalues?size &gt; 0 && dcvalues[0].value??>
        <div class="artifact-abstract"><@dspace.truncate dcvalues[0].value, 100 /></div>
    </#if>
</@dspace.processMetadata>
