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
    Viewer for items in full metadata format.
    Expects including template to have set a value for 'currentItem', which is the item to render.
-->
<#import "/includes/dspace.ftl" as dspace />
<p class="ds-paragraph item-view-toggle item-view-toggle-bottom">
    <a href="?show=simple"><@dspace.message "ui.item.show.simple" /></a>
</p>
<table class="ds-includeSet-table detailtable">
    <#list currentItem.getMetadata("*","*","*","*") as dcvalue>
        <#assign trCss = (dcvalue_index % 2 == 0)?string("even","odd") />

        <tr class="ds-table-row ${trCss} ">
            <#if dcvalue.qualifier??>
                <td class="label-cell">${dcvalue.schema}.${dcvalue.element}.${dcvalue.qualifier}</td>
            <#else>
                <td class="label-cell">${dcvalue.schema}.${dcvalue.element}</td>
            </#if>
            <td>${dcvalue.value}</td>
        </tr>
    </#list>
</table>