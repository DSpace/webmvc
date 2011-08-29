<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- Browse for sidebar -->
<div class="ds-option-set-wrapper">
    <h3 class="ds-option-set-head"><@dspace.message "ui.navigation.browse.header" /></h3>
    <div id="Navigation_list_browse" class="ds-option-set">
        <ul class="ds-option-list">
            <li>
                <h4 class="ds-sublist-head"><@dspace.message "ui.navigation.browse.subheader.all" /></h4>
                <ul class="ds-simple-list">
                    <li>
                        <a href="<@dspace.url relativeUrl="/admin/editcommunities"/>" class=""><@dspace.message "ui.navigation.browse.communitylist" /></a>
                    </li>
                    <#list navigation.browseIndices as browseIndex>
                        <li>
                            <a href="<@dspace.url relativeUrl="/browse?type=${browseIndex.getName()}" />" class=""><@dspace.message "ui.navigation.browse.${browseIndex.getName()}" /></a>
                        </li>
                    </#list>
                </ul>
                <#if navigation.collectionContainer?has_content>
                    <#assign container=navigation.getCollectionContainer() />
                    <h4 class="ds-sublist-head"><@dspace.message "ui.navigation.browse.subheader.collection" /></h4>
                <#elseif navigation.communityContainer?has_content>
                    <#assign container=navigation.getCommunityContainer() />
                    <h4 class="ds-sublist-head"><@dspace.message "ui.navigation.browse.subheader.community" /></h4>
                </#if>
                <#if container??>
                    <ul class="ds-simple-list">
                        <#list navigation.browseIndices as browseIndex>
                            <li>
                                <a href="<@dspace.url relativeUrl="/handle/${container.handle}/browse?type=${browseIndex.getName()}" />" class=""><@dspace.message "ui.navigation.browse.${browseIndex.getName()}" /></a>
                            </li>
                        </#list>
                    </ul>
                </#if>
            </li>
        </ul>
    </div>
</div>