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
<#import "/includes/test.ftl" as test />
<html>
    <head>
        <title>Home page</title>
        <meta name="test" content="Test value" />
    </head>
    <body>
        <@test.box class="boxStyle" id="mybox" titleKey="welcome">
            Testing DSpace home page layout<br />
            JSP Tag: <@dspace.message "welcome" /><br />
            <@dspace.url "/test" />
        </@test.box>
    </body>
</html>