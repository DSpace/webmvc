<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- Internationalization can also be achieved via multiple templates - ie. home.ftl, home_en_US.ftl, etc. -->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title><@dspace.message "ui.login.heading" /></title>
    </head>
    <body>
        <h1><@dspace.message "ui.login.heading" /></h1>
        <form id="PasswordLogin_div_login" class="ds-interactive-dib primary" action="login" method="post">
            <fieldset id="PasswordLogin_list_password-login" class="ds-form-list">
                <input type="hidden" name="url" value="${loginForm.url!""}" />
                <@dspace.showErrorsFor "loginForm" />
                <ol>
                    <li class="ds-form-item">
                        <label for="PasswordLogin_field_login_email" class="ds-form-label"><@dspace.message "ui.login.email" /></label>
                        <div class="ds-form-content">
                            <input id="PasswordLogin_field_login_email" class="ds-text-field" name="email" type="text" value="${loginForm.email!""}" />
                            <@dspace.showErrorsFor "loginForm.email" />
                        </div>
                    </li>
                    <li class="ds-form-item">
                        <label for="PasswordLogin_field_login_password" class="ds-form-label"><@dspace.message "ui.login.password" /></label>
                        <div class="ds-form-content">
                           <input id="PasswordLogin_field_login_password" class="ds-password-field" name="password" type="password" value="" />
                            <@dspace.showErrorsFor "loginForm.password" />
                            <a href="<@dspace.url "/forgot" />"><@dspace.message "ui.login.forgot" /></a>
                        </div>
                    </li>
                    <li id="PasswordLogin_item_login-in" class="ds-form-item last">
                        <label for="PasswordLogin_field_submit" class="ds-form-label"></label>
                        <div class="ds-form-content">
                            <input id="PasswordLogin_field_submit" class="ds-button-field" name="submit" type="submit" value="<@dspace.message "ui.login.submit" />" />
                        </div>
                    </li>
                </ol>
            </fieldset>
        </form>
        <h2 class="ds-div-head"><@dspace.message "ui.login.register.heading" /></h2>
        <div id="PasswordLogin_div_register" class="ds-static-div">
            <p class="ds-paragraph"><@dspace.message "ui.login.register.description" /></p>
            <p class="ds-paragraph"><a href="<@dspace.url "/register"/>"><@dspace.message "ui.login.register.link" /></a></p>
        </div>
    </body>
</html>
