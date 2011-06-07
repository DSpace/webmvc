<#import "/includes/dspace.ftl" as dspace />
<html>
   <head>
       <title>Welcome!</title>
   </head>
    <body>
        <h1>Welcome new User!</h1>
         <form id="PasswordLogin_div_login" class="ds-interactive-dib primary" action="register/email" method="post">

            <fieldset id="PasswordLogin_list_password-login" class="ds-form-list">
                
                <ol>
                    <li class="ds-form-item">
                        <label for="PasswordLogin_field_login_email" class="ds-form-label"><@dspace.message "ui.login.email" /></label>
                        <div class="ds-form-content">
                            <input id="PasswordLogin_field_login_email" class="ds-text-field" name="email" type="text" value="" />
                            
                        </div>
                    </li>
                   
                    <li id="PasswordLogin_item_login-in" class="ds-form-item last">
                        <label for="PasswordLogin_field_submit" class="ds-form-label"></label>
                        <div class="ds-form-content">
                            <input id="PasswordLogin_field_submit" class="ds-button-field" name="submit" type="submit" value="<@dspace.message "ui.register.new-user.register.button" />" />
                        </div>
                    </li>
                </ol>
            </fieldset>
        </form>
        
    </body>
</html>  
