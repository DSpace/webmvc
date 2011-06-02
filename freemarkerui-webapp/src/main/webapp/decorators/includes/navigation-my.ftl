<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- My Account box for sidebar -->
<div class="ds-option-set-wrapper">
    <h3 class="ds-option-set-head"><@dspace.message "ui.navigation.my.account" /></h3>
    <div id="Navigation_list_account" class="ds-option-set">
        <#if context.currentUser??>
            <ul class="ds-simple-list">
                <li>
                    <a href="<@dspace.url "/logout" />" class=""><@dspace.message "ui.navigation.logout" /></a>
                </li>
                <li>
                    <a href="<@dspace.url "/profile" />" class=""><@dspace.message "ui.navigation.profile" /></a>
                </li>
                <li>
                    <a href="<@dspace.url "/submissions" />" class=""><@dspace.message "ui.navigation.submissions" /></a>
                </li>
            </ul>
        <#else>
            <ul class="ds-simple-list">
                <li>
                    <a href="<@dspace.url "/login" />" class=""><@dspace.message "ui.navigation.login" /></a>
                </li>
                <li>
                    <a href="<@dspace.url "/register" />" class=""><@dspace.message "ui.navigation.register" /></a>
                </li>
            </ul>
        </#if>
    </div>
</div>