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
<table id="SimpleSearch_table_search-controls" class="ds-table">
    <tbody>
        <tr class="ds-table-row odd">
            <td class="ds-table-cell odd">
                <@dspace.message "ui.list.controls.results" />
                <select id="Search_field_rpp" class="ds-select-field" name="rpp">
                    <option value="5" <#if searchForm.resultsPerPage=5>selected</#if>>5</option>
                    <option value="10" <#if searchForm.resultsPerPage=10>selected</#if>>10</option>
                    <option value="20" <#if searchForm.resultsPerPage=20>selected</#if>>20</option>
                    <option value="40" <#if searchForm.resultsPerPage=40>selected</#if>>40</option>
                    <option value="60" <#if searchForm.resultsPerPage=60>selected</#if>>60</option>
                    <option value="80" <#if searchForm.resultsPerPage=80>selected</#if>>80</option>
                    <option value="100" <#if searchForm.resultsPerPage=100>selected</#if>>100</option>
                </select>
            </td>
            <td class="ds-table-cell even">
                <@dspace.message "ui.list.controls.sort" />
                <select id="Search_field_sort_by" class="ds-select-field" name="sort_by">
                    <option value="0" <#if (searchForm.sortOption.number)!0=0>selected</#if>><@dspace.message "ui.list.controls.sort.relevance" /></option>
                    <#list dspaceHelper.sortOptions as sortOption>
                        <#if sortOption.isVisible()>
                            <option value="${sortOption.number}" <#if (searchForm.sortOption.number)!0=sortOption.number>selected</#if>><@dspace.message "ui.list.controls.sort.${sortOption.name}" /></option>
                        </#if>
                    </#list>
                </select>
            </td>
            <td class="ds-table-cell odd">
                <@dspace.message "ui.list.controls.order" />
                <select id="Search_field_order" class="ds-select-field" name="order">
                    <option value="ASC" <#if searchForm.isAscending()>selected</#if>><@dspace.message "ui.list.controls.order.ascending" /></option>
                    <option value="DESC" <#if searchForm.isAscending()><#else>selected</#if>><@dspace.message "ui.list.controls.order.descending" /></option>
                </select>
            </td>
        </tr>
    </tbody>
</table>
