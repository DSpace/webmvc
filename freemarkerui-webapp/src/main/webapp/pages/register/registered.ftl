<#import "/includes/dspace.ftl" as dspace />

<html>
   <head>
       <title>Welcome!</title>
   </head>
   <body>

	<h1><@dspace.message "ui.register.registered.title" /></h1>

	<p><@dspace.message "ui.register.registered.thank" /> ${epersonName}</p>

	<p><@dspace.message "ui.register.registered.info" /></p>

	<p><a href="<@dspace.url "/"/>"><@dspace.message "ui.register.general.return-home" /></a></p>
   </body>
</html>  
