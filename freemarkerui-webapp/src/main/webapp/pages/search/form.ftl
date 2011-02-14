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
<html>
    <head>
        <title><@dspace.message "ui.search.heading.advanced" /></title>
    </head>
    <body>
        <h1 class="ds-div-head"><@dspace.message "ui.search.heading.advanced" /></h1>
        <div class="ds-static-div primary">
            <form id="AdvancedSearch_div_general-query" class="ds-interactive-div secondary search" action="search" method="get" onsubmit="javascript:tSubmit(this);">
                <#include "/includes/search/formAdvancedFields.ftl" />
                <#include "/includes/search/controls.ftl" />
                <p class="ds-paragraph button-list">
                    <input id="AdvancedSearch_field_submit" class="ds-button-field" name="submit" type="submit" value="<@dspace.message "ui.list.controls.go" />">
                </p>
            </form>
        </div>
    </body>
</html>
