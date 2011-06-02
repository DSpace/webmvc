<#import "/includes/dspace.ftl" as dspace />
<html>
   <head>
       <title>Welcome!</title>
   </head>
   <body>


       <h1>
           <@dspace.message "ui.register.new-ldap-user.heading" />

       </h1>

       <#if retry??>
           <p><strong><@dspace.message "ui.register.new-ldap-user.info1"/></strong></p>
       </#if>

       <p><@dspace.message "ui.register.new-ldap-user.info2"/></p>

       <form action="register" method="post"/>
       <input type="hidden" name="step" value="1"/>

       <table class="miscTable" align="center">
            <tr>
                <td class="oddRowEvenCol">
                    <table border="0" cellpadding="5">
                    	<tr>
                            <td class="standard"><strong><@dspace.message "ui.register.new-ldap-user.label.username"/></strong></td>
                            <td class="standard"><input type="text" name="netid"></td>
                        </tr>
                        <tr>
                            <td class="standard"><strong><@dspace.message "ui.register.new-ldap-user.label.password"/></strong></td>
                            <td class="standard"><input type="password" name="password"></td>
                        </tr>
                        <tr>
                            <td class="standard"><strong><@dspace.message "ui.register.new-ldap-user.label.email"/></strong></td>
                            <td class="standard"><input type="text" name="email"></td>
                        </tr>
                        <tr>
                            <td align="center" colspan="2">
                                <input type="submit" name="submit" value="<@dspace.message "ui.register.new-ldap-user.button.register"/>"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
       </form>

   <p><@dspace.message "ui.register.new-ldap-user.info3"/></p>
   </body>
</html>  
