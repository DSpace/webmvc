<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- This is a placeholder template to show how the content should be formed -->
<h3 class="ds-option-set-head" id="ds-search-option-head">Search DSpace</h3>
<div class="ds-option-set" id="ds-search-option">
    <form method="post" id="ds-search-form" action="<@dspace.url "/search" />">
        <fieldset>
            <input type="text" class="ds-text-field " name="query" />
            <input value="Go" type="submit" name="submit" class="ds-button-field" />
        </fieldset>
    </form>
    <a href="<@dspace.url "/advanced-search" />">Advanced Search</a>
</div>
