<#import "/includes/dspace.ftl" as dspace />

<html>
   <head>
       <title>Welcome!</title>
   </head>
   <body>
       <h1>
            <@dspace.message "ui.register.password-changed.title" />
       </h1>
       <%-- <p>Thank you, your new password has been set and is active immediately.</p> --%>
           <p><@dspace.message "ui.register.password-changed.info" /></p>

           <%-- <p><a href="<@dspace.url "/"/>">Go to DSpace Home</a></p> --%>
           <p>

               <a href="<@dspace.url "/"/>"><@dspace.message "ui.register.password-changed.link" /></a></p>

   </body>
</html>  
