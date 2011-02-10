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

<p id="AdvancedSearch_p_hidden-fields" class="ds-paragraph hidden">
    <input id="AdvancedSearch_field_num_search_field" class="ds-hidden-field" name="num_search_field" type="hidden" value="3" />
    <input id="AdvancedSearch_field_results_per_page" class="ds-hidden-field" name="results_per_page" type="hidden" value="10" />
</p>
<fieldset id="AdvancedSearch_list_search-query" class="ds-form-list">
    <ol>
        <li class="ds-form-item">
            <label class="ds-form-label"><@dspace.message "ui.search.scope.label"/></label>
            <div class="ds-form-content">
                <select id="SimpleSearch_field_scope" class="ds-select-field" name="scope">
                    <#-- correctly determine scope -->
                    <option value="/" selected="selected"><@dspace.message "ui.search.scope.all" /></option>
                    <option value="123456789/1">Imports</option>
                </select>
                <span class="field-help">Limit your search to a community or collection.</span>
            </div>
        </li>
    </ol>
    <table id="AdvancedSearch_table_search-query" class="ds-table">
        <tr class="ds-table-header-row">
            <th class="ds-table-header-cell odd">Conjunction</th>
            <th class="ds-table-header-cell even">Search type</th>
            <th class="ds-table-header-cell odd">Search for</th>
        </tr>
        <#list 1..3 as row>
            <#assign trCss = (row_index % 2 == 0)?string("even","odd") />
            <tr class="ds-table-row ${trCss}">
                <#if row=1>
                    <td class="ds-table-cell odd" />
                <#else>
                    <td class="ds-table-cell odd">
                        <select id="AdvancedSearch_field_conjunction${row}" class="ds-select-field" name="conjunction${row}">
                            <option value="AND" selected="selected">AND</option>
                            <option value="OR">OR</option>
                            <option value="NOT">NOT</option>
                        </select>
                    </td>
                </#if>
                <td class="ds-table-cell even">
                    <select id="AdvancedSearch_field_field${row}" class="ds-select-field" name="field${row}">
                        <option value="ANY" selected="selected">Full Text</option>
                        <option value="abstract">Abstract</option>
                        <option value="series">Series</option>
                        <option value="author">Author</option>
                        <option value="title">Title</option>
                        <option value="keyword">Keyword</option>
                        <option value="language">Language (ISO)</option>
                        <option value="mime">Mime-Type</option>
                        <option value="sponsor">Sponsor</option>
                        <option value="identifier">Identifier</option>
                    </select>
                </td>
                <td class="ds-table-cell odd">
                    <input id="AdvancedSearch_field_query${row}" class="ds-text-field" name="query${row}" type="text" value="" />
                </td>
            </tr>
        </#list>
    </table>
</fieldset>
