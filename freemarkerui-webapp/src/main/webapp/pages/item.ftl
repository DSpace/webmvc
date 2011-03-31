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
<#assign currentItem=item />
<html>
    <head>
        <title>${currentItem.getMetadata("dc.title")[0].value!"Untitled"}</title>
        <meta name="DC.title" content="${currentItem.getMetadata("dc.title")[0].value}" />
    </head>
    <body>
        <h1 class="ds-div-head">${currentItem.getMetadata("dc.title")[0].value!"Untitled"}</h1>
        <div id="ItemViewer_div_item-view" class="ds-static-div primary">
            <#if RequestParameters["show"]?? && RequestParameters["show"]="full">
                <#include "/viewers/itemFull.ftl" />
            <#else>
                <#include "/viewers/itemSummary.ftl" />
            </#if>
            <h2><@dspace.message "ui.item.files.heading" /></h2>
            <#assign baseURL = "http://localhost:8080/jspui" />
            <#assign nonInternalBitstreams = currentItem.getNonInternalBitstreams() />
            <#if nonInternalBitstreams??  && nonInternalBitstreams?size &gt; 0>
                <#list nonInternalBitstreams as bitstream>
                    <p><a href="<@dspace.url "/bitstream/${currentItem.getHandle()}/${bitstream.getSequenceID()}/${bitstream.getName()}" />">${bitstream.getName()}</a></p>
                </#list>
            <#else>
                <p><@dspace.message "ui.item.files.none" /></p>
            </#if>
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
