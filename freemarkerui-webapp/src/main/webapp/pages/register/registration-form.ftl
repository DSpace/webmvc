<#import "/includes/dspace.ftl" as dspace />

<html>
   <head>
       <title>Welcome!</title>
   </head>
   <body>
       <h1>
           <@dspace.message "ui.register.registration-form.title" />
       </h1>

       <#if missingfields??>

                 <p><strong><@dspace.message "ui.register.registration-form.instruct1" /></strong></p>
       </#if>

              <#if passwordproblem??>

                  <p><strong><@dspace.message "ui.register.registration-form.instruct2" /></strong></p>

              </#if>

           <%-- <p>Please enter the following information.  The fields marked with a * are
           required.</p> --%>
           <p><@dspace.message "ui.register.registration-form.instruct3" /></p>
           <form action="register" method="post">
              <#if netid??><input type="hidden" name="netid" value="${netid}" /></#if>
              <#if email??><input type="hidden" name="email" value="${email}" /></#if>
              <#include "profile-form.ftl" />
                  <#if setpassword??>

              <%-- <p>Please choose a password and enter it into the box below, and confirm it by typing it
               again into the second box.  It should be at least six characters long.</p> --%>
               <p> <@dspace.message "ui.register.registration-form.instruct4" /></p>

               <table class="misc" align="center">
                   <tr>
                       <td class="oddRowEvenCol">
                           <table border="0" cellpadding="5">
                               <tr>
                                   <%-- <td align="right" class="standard"><strong>Password:</strong></td> --%>
                                   <td align="right" class="standard"><label for="tpassword"><strong><@dspace.message "ui.register.registration-form.pswd.field" /></strong></label></td>
                                   <td class="standard"><input type="password" name="password" id="tpassword" /></td>
                               </tr>
                               <tr>
                                   <%-- <td align="right" class="standard"><strong>Again to Confirm:</strong></td> --%>
                                   <td align="right" class="standard"><label for="tpassword_confirm"><strong><@dspace.message "ui.register.registration-form.confirm.field" /></strong></label></td>
                                   <td class="standard"><input type="password" name="password_confirm" id="tpassword_confirm" /></td>
                               </tr>
                           </table>
                       </td>
                   </tr>
               </table>

              </#if>
            <input type="hidden" name="step" value="2"/>
               <#if token??><input type="hidden" name="token" value="${token}"/><#else><input type="hidden" name="token" value="null"/></#if>
               <%-- <p align="center"><input type="submit" name="submit" value="Complete Registration"></p> --%>
               <p align="center"><input type="submit" name="submit" value="<@dspace.message "ui.register.registration-form.complete.button"/>"</p>
           </form>

           </body>

</html>  
