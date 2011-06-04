<#import "/includes/dspace.ftl" as dspace />
<html>
   <head>
       <title>Welcome!</title>
   </head>
   <body>
       <h1>
       <@dspace.message "ui.register.inactive-account.title" />
       </h1>
       <p><@dspace.message "ui.register.inactive-account.info" />
           <a href="<@dspace.url "/register" />"><@dspace.message "ui.login.register.link" /></a></p>
   </body>
</html>  
