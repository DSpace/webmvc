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
<fieldset id="SimpleSearch_list_search-query" class="ds-form-list">
    <ol>
        <li class="ds-form-item">
            <label class="ds-form-label"><@dspace.message "ui.search.scope.label"/></label>
            <div class="ds-form-content">
                <select id="SimpleSearch_field_scope" class="ds-select-field" name="scope">
                    <#-- correctly determine scope -->
                    <option value="/" selected="selected"><@dspace.message "ui.search.scope.all" /></option>
                    <option value="123456789/1">Imports</option>
                </select>
            </div>
        </li>
        <li class="ds-form-item">
            <label class="ds-form-label"><@dspace.message "ui.search.simple.query.label"/></label>
            <div class="ds-form-content">
                <#-- add search form query -->
                <input id="aspect_artifactbrowser_SimpleSearch_field_query" class="ds-text-field" name="query" type="text" value="" />
            </div>
        </li>
    </ol>
</fieldset>
