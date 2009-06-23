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