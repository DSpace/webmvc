<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<div class="ds-option-set-wrapper">
    <h3 class="ds-option-set-head"><@dspace.message "ui.navigation.browse.header" /></h3>
    <div id="Navigation_list_browse" class="ds-option-set">
        <ul class="ds-option-list">
            <li>
                <h4 class="ds-sublist-head"><@dspace.message "ui.navigation.browse.allheader" /></h4>
                <ul class="ds-simple-list">
                    <li>
                        <a href="<@dspace.url relativeUrl="/community-list"/>" class=""><@dspace.message "ui.navigation.browse.communitylist" /></a>
                    </li>
                    <#list navigation.browseIndices as browseIndex>
                        <li>
                            <a href="<@dspace.url relativeUrl="/browse?type=${browseIndex.getName()}" />" class=""><@dspace.message "ui.navigation.browse.${browseIndex.getName()}" /></a>
                        </li>
                    </#list>
                </ul>
            </li>
        </ul>
    </div>
</div>