<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- Internationalization can also be achieved via multiple templates - ie. home.ftl, home_en_US.ftl, etc. -->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>${currentItem.getMetadata("dc.title")[0].value!"Untitled"}</title>
        <meta name="DC.title" content="${currentItem.getMetadata("dc.title")[0].value}" />
    </head>
    <body>
        <h1 class="ds-div-head">${currentItem.getMetadata("dc.title")[0].value!"Untitled"}</h1>
        <div id="aspect_artifactbrowser_ItemViewer_div_item-view" class="ds-static-div primary">
            <div class="item-summary-view-metadata">

                <@dspace.processMetadata item=currentItem field="dc.identifier.author" ; dcvalues>
                    <div class="simple-item-view-authors">
                        <#list dcvalues as dcvalue>
                            <span>${dcvalue.value}</span>
                            <#if dcvalue_has_next>; </#if>
                        </#list>
                    </div>
                </@dspace.processMetadata>

                <@dspace.processMetadata item=currentItem field="dc.identifier.uri" ; dcvalues>
                    <div class="simple-item-view-other">
                        <span class="bold"><@dspace.message "ui.item.dc.identifier.uri" /></span>
                        <span>
                            <#list dcvalues as dcvalue>
                                <a href="${dcvalue.value}">${dcvalue.value}</a>
                                <#if dcvalue_has_next>
                                    <br />
                                </#if>
                            </#list>
                        </span>
                    </div>
                </@dspace.processMetadata>

                <@dspace.processMetadata item=currentItem field="dc.date.issued" ; dcvalues>
                    <div class="simple-item-view-other">
                        <span class="bold"><@dspace.message "ui.item.dc.date.issued" /></span>
                        <span>
                            <#list dcvalues as dcvalue>
                                ${dcvalue.value}
                                <#if dcvalue_has_next>
                                    <br />
                                </#if>
                            </#list>
                        </span>
                    </div>
                </@dspace.processMetadata>

                <@dspace.processMetadata item=currentItem field="dc.description.abstract" ; dcvalues>
                    <div class="simple-item-view-description">
                        <h3 class="bold"><@dspace.message "ui.item.dc.description.abstract" /></h3>
                        <div>
                            <#list dcvalues as dcvalue>
                                ${dcvalue.value}
                                <#if dcvalue_has_next>
                                    <br />
                                </#if>
                            </#list>
                        </div>
                    </div>
                </@dspace.processMetadata>

                <p class="ds-paragraph item-view-toggle item-view-toggle-bottom">
                    <a href="?show=full"><@dspace.message "ui.item.show.full" /></a>
                </p>
            </div>
            <h2><@dspace.message "ui.item.files.heading" /></h2>
            <p><@dspace.message "ui.item.files.none" /></p>
            <h2 class="ds-list-head"><@dspace.message "ui.item.collections.heading" /></h2>
            <ul class="ds-referenceSet-list">
                <#list currentItem.getCollections() as collection>
                    <li>
                        <a href="<@dspace.url relativeUrl="/handle/${collection.getHandle()}" />">${collection.getName()}</a><br/>
                        ${collection.getMetadata("short_description")}
                    </li>
                </#list>
            </ul>
        </div>
    </body>
</html>
