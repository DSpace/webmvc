<#-- FreeMarker decorator template -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<#import "/includes/dspace.ftl" as dspace />
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" xmlns:vml="urn:schemas-microsoft-com:vml">
    <#include "includes/head.ftl" />
    <body onload="${page.properties["body.onload"]?default("")}" id="${page.properties["body.id"]?default("")}">
        <div id="ds-main">
            <#include "includes/header.ftl" />
            <div id="ds-body-container">
                <div id="ds-body">
                    ${body}
                </div>
                <div id="ds-options">
                    <#include "includes/search.ftl" />
                    <#include "includes/navigation-browse.ftl" />
                    <#include "includes/navigation-my.ftl" />
                </div>
            </div>
            <#include "includes/footer.ftl" />
        </div>
    </body>
</html>
