<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#--
    Metadata edit for communities.
    Expects including template to have set a value for 'currentCommunity', which is the community to render/edit.
-->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>Editing Community: ${communityMetadataForm.getName()!"Untitled"}</title>
    </head>
    <body>
        <h1>Editing Community: ${communityMetadataForm.getName()!"Untitled"}</h1>
        <form id="Community_div_edit" class="ds-interactive-div primary administrative community" method="post" enctype="multipart/form-data">
            <fieldset id="Community_list_edit" class="ds-form-list thick">
                <#-- @TODO ADD: Top Tabs: Edit Metadata, Assign Roles, Curate-->
                <@dspace.showErrorsFor "communityMetadataForm" />
                <ol>
                    <li class="ds-form-item even">
                        <label class="ds-form-label" for="name">Name:</label>
                        <div class="ds-form-content">
                            <input id="name" class="ds-text-field" name="name" type="text" value="${communityMetadataForm.getName()!""}" size="40">
                            <@dspace.showErrorsFor "communityMetadataForm.name" />
                        </div>
                    </li>
                    <li class="ds-form-item odd">
                        <label class="ds-form-label" for="short_description">Short Description:</label>

                        <div class="ds-form-content">
                            <input id="short_description" class="ds-text-field" name="short_description" type="text" value="${communityMetadataForm.getShort_description()!""}" size="40">
                            <@dspace.showErrorsFor "communityMetadataForm.short_description" />
                        </div>
                    </li>
                    <li class="ds-form-item even">
                        <label class="ds-form-label" for="introductory_text">Introductory text (HTML):</label>

                        <div class="ds-form-content">
                            <textarea id="introductory_text" class="ds-textarea-field" name="introductory_text" onfocus="javascript:tFocus(this);" cols="40" rows="6">${communityMetadataForm.getIntroductory_text()!""}</textarea>
                            <@dspace.showErrorsFor "communityMetadataForm.introductory_text" />
                        </div>
                    </li>
                    <li class="ds-form-item odd">
                        <label class="ds-form-label" for="copyright_text">Copyright text (HTML):</label>

                        <div class="ds-form-content">
                            <textarea id="copyright_text" class="ds-textarea-field" name="copyright_text" onfocus="javascript:tFocus(this);" cols="40" rows="6">${communityMetadataForm.getCopyright_text()!""}</textarea>
                            <@dspace.showErrorsFor "communityMetadataForm.copyright_text" />
                        </div>
                    </li>
                    <li class="ds-form-item even">
                        <label class="ds-form-label" for="side_bar_text">News (HTML):</label>

                        <div class="ds-form-content">
                            <textarea id="side_bar_text" class="ds-textarea-field" name="side_bar_text" onfocus="javascript:tFocus(this);" cols="40" rows="6">${communityMetadataForm.getSide_bar_text()!""}</textarea>
                            <@dspace.showErrorsFor "communityMetadataForm.side_bar_text" />
                        </div>
                    </li>
                    <li class="ds-form-item odd">
                        <label class="ds-form-label" for="logo">Logo:</label>

                        <div class="ds-form-content">
                            <input id="logo" class="ds-textarea-field" name="logo" type="file">
                            <@dspace.showErrorsFor "communityMetadataForm.logo" />
                        </div>
                    </li>

                </ol>
            </fieldset>

            <p class="ds-paragraph">
                <input id="update" class="ds-button-field" name="update" type="submit" value="Update">
                <input id="delete" class="ds-button-field" name="delete" type="submit" value="Delete Community">
            </p>
        </form>
    </body>
</html>