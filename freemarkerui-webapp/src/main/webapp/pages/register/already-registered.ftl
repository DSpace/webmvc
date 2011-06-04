<#import "/includes/dspace.ftl" as dspace />
<html>
   <head>
       <title>Welcome!</title>
   </head>
   <body>
       <h1><@dspace.message "ui.register.already-registered.title" /></h1>
       <p><@dspace.message "ui.register.already-registered.info1" />
       <p><@dspace.message "ui.register.already-registered.info2" />
       <a href="<@dspace.url "/forgot?newpassword=true" />"><@dspace.message "ui.register.forgot-password.title" /></a>
       <p><@dspace.message "ui.register.already-registered.info4" />
    
   </body>
</html>  
