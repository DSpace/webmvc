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

<p id="Search_p_hidden-fields" class="ds-paragraph hidden">
    <input id="Search_field_num_search_field" class="ds-hidden-field" name="num_search_field" type="hidden" value="3" />
    <input class="ds-hidden-field" name="advanced" type="hidden" value="true" />
</p>
<fieldset id="Search_list_search-query" class="ds-form-list">
    <ol>
        <li class="ds-form-item">
            <label id="Search_label_scope" class="ds-form-label"><@dspace.message "ui.search.scope.label"/></label>
            <div class="ds-form-content">
                <select id="Search_field_scope" class="ds-select-field" name="scope">
                    <option value="/" selected="selected"><@dspace.message "ui.search.scope.all" /></option>
                    <#list dspaceHelper.topLevelCommunities as community>
                        <option value="${community.handle}">${community.name!"Untitled"}</option>
                    </#list>
                </select>
                <span class="field-help">Limit your search to a community or collection.</span>
            </div>
        </li>
    </ol>
    <table id="Search_table_search-query" class="ds-table">
        <tr class="ds-table-header-row">
            <th class="ds-table-header-cell odd">Conjunction</th>
            <th class="ds-table-header-cell even">Search field</th>
            <th class="ds-table-header-cell odd">Search for</th>
        </tr>
        <#list 1..3 as row>
            <#assign trCss = (row_index % 2 == 0)?string("odd","even") />
            <tr class="ds-table-row ${trCss}">
                <#if row=1>
                    <td class="ds-table-cell odd" />
                <#else>
                    <td class="ds-table-cell odd">
                        <select id="Search_field_conjunction${row}" class="ds-select-field" name="conjunction${row}">
                            <#assign currentConjunction = (searchForm.advancedFields[row-1].conjunction)!"AND" />
                            <option value="AND" <#if currentConjunction="AND">selected</#if>>AND</option>
                            <option value="OR"  <#if currentConjunction="OR">selected</#if>>OR</option>
                            <option value="NOT" <#if currentConjunction="NOT">selected</#if>>NOT</option>
                        </select>
                    </td>
                </#if>
                <td class="ds-table-cell even">
                    <select id="Search_field_field${row}" class="ds-select-field" name="field${row}">
                        <#assign currentField = (searchForm.advancedFields[row-1].field)!"ANY" />
                        <option value="ANY"        <#if currentField="ANY">selected</#if>>Full Text</option>
                        <option value="abstract"   <#if currentField="abstract">selected</#if>>Abstract</option>
                        <option value="series"     <#if currentField="series">selected</#if>>Series</option>
                        <option value="author"     <#if currentField="author">selected</#if>>Author</option>
                        <option value="title"      <#if currentField="title">selected</#if>>Title</option>
                        <option value="keyword"    <#if currentField="keyword">selected</#if>>Keyword</option>
                        <option value="language"   <#if currentField="language">selected</#if>>Language (ISO)</option>
                        <option value="mime"       <#if currentField="mime">selected</#if>>Mime-Type</option>
                        <option value="sponsor"    <#if currentField="sponsor">selected</#if>>Sponsor</option>
                        <option value="identifier" <#if currentField="identifier">selected</#if>>Identifier</option>
                    </select>
                </td>
                <td class="ds-table-cell odd">
                    <input id="Search_field_query${row}" class="ds-text-field" name="query${row}" type="text" value="${(searchForm.advancedFields[row-1].query)!""}" />
                </td>
            </tr>
        </#list>
    </table>
</fieldset>
