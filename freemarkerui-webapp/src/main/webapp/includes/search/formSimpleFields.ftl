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
            </div>
        </li>
        <li class="ds-form-item">
            <label id="Search_field_label" class="ds-form-label"><@dspace.message "ui.search.simple.query.label"/></label>
            <div class="ds-form-content">
                <#-- add search form query -->
                <input id="Search_field_query" class="ds-text-field" name="query" type="text" value="${(searchForm.query)!""}" />
            </div>
        </li>
    </ol>
</fieldset>
