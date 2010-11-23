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
<div class="item-summary-view-metadata">

    <@dspace.processMetadata item=currentItem field="dc.identifier.author" ; dcvalues>
        <div class="simple-item-view-authors">
            <#list dcvalues as dcvalue>
                <span>${dcvalue.value}</span>
                <#if dcvalue_has_next>; </#if>
            </#list>
        </div>
    </@dspace.processMetadata>

    <@dspace.processMetadata item=currentItem field="dc.identifier.uri" ; dcvalues>
        <div class="simple-item-view-other">
            <span class="bold"><@dspace.message "ui.item.dc.identifier.uri" /></span>
            <span>
                <#list dcvalues as dcvalue>
                    <a href="${dcvalue.value}">${dcvalue.value}</a>
                    <#if dcvalue_has_next>
                        <br />
                    </#if>
                </#list>
            </span>
        </div>
    </@dspace.processMetadata>

    <@dspace.processMetadata item=currentItem field="dc.date.issued" ; dcvalues>
        <div class="simple-item-view-other">
            <span class="bold"><@dspace.message "ui.item.dc.date.issued" /></span>
            <span>
                <#list dcvalues as dcvalue>
                    ${dcvalue.value}
                    <#if dcvalue_has_next>
                        <br />
                    </#if>
                </#list>
            </span>
        </div>
    </@dspace.processMetadata>

    <@dspace.processMetadata item=currentItem field="dc.description.abstract" ; dcvalues>
        <div class="simple-item-view-description">
            <h3 class="bold"><@dspace.message "ui.item.dc.description.abstract" /></h3>
            <div>
                <#list dcvalues as dcvalue>
                    ${dcvalue.value}
                    <#if dcvalue_has_next>
                        <br />
                    </#if>
                </#list>
            </div>
        </div>
    </@dspace.processMetadata>

    <p class="ds-paragraph item-view-toggle item-view-toggle-bottom">
        <a href="?show=full"><@dspace.message "ui.item.show.full" /></a>
    </p>
</div>
