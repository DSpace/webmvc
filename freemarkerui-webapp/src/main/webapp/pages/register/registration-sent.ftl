<#import "/includes/dspace.ftl" as dspace />

<html>
   <head>
       <title>Welcome!</title>
   </head>
   <body>
     <%-- <h1>Registration E-mail Sent</h1> --%>
	 <h1><@dspace.message "ui.register.registration-sent.title" /></h1>

    <%-- <p>You have been sent an e-mail containing a special URL, or "token".  When
    you visit this URL, you will need to fill out some simple information.
    After that,	you'll be ready to log into DSpace!</p> --%>
	<p> <@dspace.message "ui.register.registration-sent.info" /></p>
   </body>
</html>  
