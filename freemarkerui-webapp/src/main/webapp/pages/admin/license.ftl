<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title><@dspace.message "ui.admin.license.edit.heading" /></title>
    </head>
    <body>
        <h1 class="ds-div-head"><@dspace.message "ui.admin.license.edit.heading" /></h1>
        <div class="ds-static-div primary">
            <p class="ds-paragraph"><@dspace.message "ui.admin.license.edit.description" /></p>
            <form id="LicenseEdit_div_general-query" class="ds-interactive-div secondary licenseEdit" action="" method="post">
                <textarea rows="20" cols="80" name="licenseText">${licenseForm.licenseText?html}</textarea>
                <p class="ds-paragraph button-list">
                    <input id="LicenseEdit_field_submit" class="ds-button-field" name="submit" type="submit" value="<@dspace.message "ui.form.controls.save" />">
                </p>
            </form>
        </div>
    </body>
</html>
