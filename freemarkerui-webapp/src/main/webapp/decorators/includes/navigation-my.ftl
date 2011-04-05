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
<div class="ds-option-set-wrapper">
    <h3 class="ds-option-set-head">My Account</h3>
    <div id="Navigation_list_account" class="ds-option-set">
        <#if context.currentUser??>
            <ul class="ds-simple-list">
                <li>
                    <a href="<@dspace.url "/logout" />" class="">Logout</a>
                </li>
                <li>
                    <a href="<@dspace.url "/profile" />" class="">Profile</a>
                </li>
                <li>
                    <a href="<@dspace.url "/submission" />" class="">Submissions</a>
                </li>
            </ul>
        <#else>
            <ul class="ds-simple-list">
                <li>
                    <a href="<@dspace.url "/login" />" class="">Login</a>
                </li>
                <li>
                    <a href="<@dspace.url "/register" />" class="">Register</a>
                </li>
            </ul>
        </#if>
    </div>
</div>