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
        <title>${currentItem.getMetadata("dc.title")[0].value}</title>
        <meta name="DC.title" content="${currentItem.getMetadata("dc.title")[0].value}" />
    </head>
    <body>
        <h1 class="ds-div-head">${currentItem.getMetadata("dc.title")[0].value}</h1>
        <div id="aspect_artifactbrowser_ItemViewer_div_item-view" class="ds-static-div primary">
            <div class="item-summary-view-metadata">
                <div class="simple-item-view-authors">
                    <span>${currentItem.getMetadata("dc.contributor.author")[0].value}</span>
                </div>
                <div class="simple-item-view-other">
                    <span class="bold">URI:</span>
                        <a href="${currentItem.getMetadata("dc.identifier.uri")[0].value}">${currentItem.getMetadata("dc.identifier.uri")[0].value}</a>
                    </span>
                </div>
                <div class="simple-item-view-other">
                    <span class="bold">Date:</span>
                        ${currentItem.getMetadata("dc.date.issued")[0].value}
                    </span>
                </div>
                <div class="simple-item-view-description">
                    <h3 class="bold">Abstract:</h3>
                    <div>${currentItem.getMetadata("dc.description.abstract")[0].value}</div>
                </div>
                <p class="ds-paragraph item-view-toggle item-view-toggle-bottom">
                    <a href="?show=full">Show full item record</a>
                </p>
            </div>
            <h2>Files in this item</h2>
            <p>There are no files associated with this item.</p>
            <h2 class="ds-list-head">This item appears in the following Collection(s)</h2>
            <ul class="ds-referenceSet-list">
                <li>
                    <a href="/xmlui/handle/10673/2">Collection of Sample Items</a>
                    <br />This collection contains sample items. ******************
                </li>
            </ul>
        </div>
    </body>
</html>