<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- FreeMarker decorator template -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<#import "/includes/dspace.ftl" as dspace />
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" xmlns:vml="urn:schemas-microsoft-com:vml">
    <#include "includes/head.ftl" />
    <body onload="${page.properties["body.onload"]?default("")}" id="${page.properties["body.id"]?default("")}">
        <div id="ds-main">
            <#include "includes/header.ftl" />
            <div id="ds-content-wrapper">
                <div id="ds-content" class="clearfix">
                    <div id="ds-body">
                        ${body}
                    </div>
                    <div id="ds-options-wrapper">
                        <div id="ds-options">
                            <#include "includes/search.ftl" />
                            <#include "includes/navigation-browse.ftl" />
                            <#include "includes/navigation-my.ftl" />
                            <#include "includes/navigation-context.ftl" />
                        </div>
                    </div>
                </div>
            </div>
            <#include "includes/footer.ftl" />
        </div>
    </body>
</html>
