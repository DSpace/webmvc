<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- Context box of performable tasks for current location, resides in sidebar
 #
 # -- Home, Communities & Collections => Create Community (Top Level)
 # -- Community => Edit, Export Container, Export Metadata, Create Coll, Create Sub-com
 # -- Collection => Edit, Item Mapper, Export container, Export metadata
 # -- Item =>  Edit, Export Item, Export Metadata
 # -- other pages get nothing (this whole thing gets skipped)

 Also, some features are available for admin, coll-admin, comm-admin, so will need to verify and perform authz check.
 -->
<#if context.currentUser??> <#-- @TODO Change this to check if the user is admin -->
    <div class="ds-option-set-wrapper">
        <h3 class="ds-option-set-head"><@dspace.message "ui.navigation.context.header" /></h3>
        <div id="Navigation_context" class="ds-option-set">
            <#if navigation.collectionContainer?has_content>
                <#assign container=navigation.getCollectionContainer() />
                <ul class="ds-simple-list">
                    <li>
                        <a href="<@dspace.url "/admin/collection?collectionID=${container.getID()}" />" class=""><@dspace.message "ui.navigation.context.collection.edit" /></a>
                    </li>
                    <li>
                        <a href="<@dspace.url "/admin/mapper?collectionID=${container.getID()}" />" class=""><@dspace.message "ui.navigation.context.itemmapper" /></a>
                    </li>
                    <li>
                        <a href="<@dspace.url "/admin/export?collectionID=${container.getID()}" />" class=""><@dspace.message "ui.navigation.context.collection.export" /></a>
                    </li>
                    <li>
                        <a href="<@dspace.url "/csv/handle/${container.handle}" />" class=""><@dspace.message "ui.navigation.context.metadata.export" /></a>
                    </li>
                </ul>
            <#elseif navigation.communityContainer?has_content>
                <#assign container=navigation.getCommunityContainer() />
                <ul class="ds-simple-list">
                    <li>
                        <a href="<@dspace.url "/admin/community?communityID=${container.getID()}" />" class=""><@dspace.message "ui.navigation.context.community.edit" /></a>
                    </li>
                    <li>
                        <a href="<@dspace.url "/admin/export?communityID=${container.getID()}" />" class=""><@dspace.message "ui.navigation.context.community.export" /></a>
                    </li>
                    <li>
                        <a href="<@dspace.url "/csv/handle/${container.handle}" />" class=""><@dspace.message "ui.navigation.context.metadata.export" /></a>
                    </li>
                    <li>
                        <a href="<@dspace.url "/admin/collection?createNew&communityID=${container.getID()}" />" class=""><@dspace.message "ui.navigation.context.collection.create" /></a>
                    </li>
                    <li>
                        <a href="<@dspace.url "/admin/community?createNew&communityID=${container.getID()}" />" class=""><@dspace.message "ui.navigation.context.subcommunity.create" /></a>
                    </li>
                </ul>
            <#elseif navigation.itemContainer?has_content>
                <#assign container=navigation.getItemContainer() />
                <ul class="ds-simple-list">
                    <li>
                        <a href="<@dspace.url "/admin/item/${container.getID()}" />" class=""><@dspace.message "ui.navigation.context.item.edit" /></a>
                    </li>
                    <li>
                        <a href="<@dspace.url "/admin/export?itemID=${container.getID()}" />" class=""><@dspace.message "ui.navigation.context.item.export" /></a>
                    </li>
                    <li>
                        <a href="<@dspace.url "/csv/handle/${container.handle}" />" class=""><@dspace.message "ui.navigation.context.metadata.export" /></a>
                    </li>
                </ul>
            <#else>
                <#-- @TODO Change this to only show the Create Community link when on the Home or Community&Collection list page -->
                <ul class="ds-simple-list">
                    <li>
                        <a href="<@dspace.url "/admin/community?createNew" />" class=""><@dspace.message "ui.navigation.context.community.create" /></a>
                    </li>
                </ul>
            </#if>
        </div>
    </div>
</#if>