<#import "/includes/dspace.ftl" as dspace />
<html>
   <head>
       <title>Welcome!</title>
   </head>
   <body>
       <h1>
            <@dspace.message "ui.register.new-password.title" />
       </h1>
       <p><@dspace.message "ui.register.new-password.hello" /> <#if eperson??>${eperson.getFirstName()}</#if></p>

    <#if passwordproblem??>
     <p><strong><@dspace.message "ui.register.new-password.info1"/></strong></p>
    </#if>

   <p><@dspace.message "ui.register.new-password.info2"/></p>


       <form action="forgot/password" method="post">
        <table class="misc" align="center">
            <tr>
                <td class="oddRowEvenCol">
                    <table border="0" cellpadding="5">
                        <tr>
                            <%-- <td align="right" class="standard"><strong>New Password:</strong></td> --%>
							<td align="right" class="standard"><label for="tpassword"><strong>
                                <@dspace.message "ui.register.new-password.pswd.field"/>
                                </strong></label></td>
                            <td class="standard"><input type="password" name="password" id="tpassword" /></td>
                        </tr>
                        <tr>
                            <%-- <td align="right" class="standard"><strong>Again to Confirm:</strong></td> --%>
							<td align="right" class="standard"><label for="tpassword_confirm"><strong><@dspace.message "ui.register.new-password.confirm.field"/></strong></label></td>
                            <td class="standard"><input type="password" name="password_confirm" id="tpassword_confirm" /></td>
                        </tr>
                        <tr>
                            <td align="center" colspan="2">
                                <%-- <input type="submit" name="submit" value="Set New Password"> --%>
								<input type="submit" name="submit" value="<@dspace.message "ui.register.new-password.set.button"/>" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>

        <#if token??><input type="hidden" name="token" value="${token}"/><#else><input type="hidden" name="token" value="null"/></#if>
    </form>


   </body>
</html>  
