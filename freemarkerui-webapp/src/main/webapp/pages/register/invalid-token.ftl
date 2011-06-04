<#import "/includes/dspace.ftl" as dspace />
<html>
   <head>
       <title>Welcome!</title>
   </head>
   <body>
 <%-- <h1>Invalid Token</h1> --%>
	<h1><@dspace.message "ui.register.invalid-token.title" /></h1>

    <%-- <p>The registration or forgotten password "token" in the URL is invalid.
    This may be because of one of the following reason:</p> --%>
	<p><@dspace.message "ui.register.invalid-token.info1" /></p>

    <ul>
        <%--  <li>The token might be incorrectly copied into the URL.  Some e-mail
        programs will "wrap" long lines of text in an email, so maybe it split
        your special URL up into two lines, like this: --%>
		<li><@dspace.message "ui.register.invalid-token.info2" />
        <pre>
         <@dspace.url "/register?token=ABCDEFGHIJKLMNOP"/>
        </pre>

        <%-- If it has, you should copy and paste the first line into your browser's
        address bar, then copy the second line, and paste into the address bar
        just on the end of the first line, making sure there are no spaces.  The
        address bar should then contain something like: --%>
		<li><@dspace.message "ui.register.invalid-token.info3" />

        <pre>
            <@dspace.url "/register?token=ABCDEFGHIJKLMNOP"/>

        </pre>

        <%-- Then press return in the address bar, and the URL should work fine.</li> --%>
		<@dspace.message "ui.register.invalid-token.info4" /></li>
    </ul>

    <%-- <p>If you're still having trouble, please contact us.</p> --%>
	<p><@dspace.message "ui.register.invalid-token.info5" /></p>
   </body>
</html>  
