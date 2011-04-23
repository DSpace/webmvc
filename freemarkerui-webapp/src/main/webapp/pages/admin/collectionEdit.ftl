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
    Metadata edit for collections.
-->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>Editing Collection: ${collectionMetadataForm.getName()!"Untitled"}</title>
    </head>
    <body>
        <h1>Editing Collection: ${collectionMetadataForm.getName()!"Untitled"}</h1>
        <form id="Collection_div_edit" class="ds-interactive-div primary administrative collection" method="post">
            <fieldset id="Collection_list_edit" class="ds-form-list thick">
                <#-- @TODO ADD: Top Tabs: Edit Metadata, Assign Roles, Curate-->
                <@dspace.showErrorsFor "collectionMetadataForm" />
                <ol>
                    <li class="ds-form-item even">
                        <label class="ds-form-label" for="name">Name:</label>
                        <div class="ds-form-content">
                            <input id="name" class="ds-text-field" name="name" type="text" value="${collectionMetadataForm.getName()!""}" size="40">
                            <@dspace.showErrorsFor "collectionMetadataForm.name" />
                        </div>
                    </li>
                    <li class="ds-form-item odd">
                        <label class="ds-form-label" for="short_description">Short Description:</label>

                        <div class="ds-form-content">
                            <input id="short_description" class="ds-text-field" name="short_description" type="text" value="${collectionMetadataForm.getShort_description()!""}" size="40">
                            <@dspace.showErrorsFor "collectionMetadataForm.short_description" />
                        </div>
                    </li>
                    <li class="ds-form-item even">
                        <label class="ds-form-label" for="introductory_text">Introductory text (HTML):</label>

                        <div class="ds-form-content">
                            <textarea id="introductory_text" class="ds-textarea-field" name="introductory_text" onfocus="javascript:tFocus(this);" cols="40" rows="6">${collectionMetadataForm.getIntroductory_text()!""}</textarea>
                            <@dspace.showErrorsFor "collectionMetadataForm.introductory_text" />
                        </div>
                    </li>
                    <li class="ds-form-item odd">
                        <label class="ds-form-label" for="copyright_text">Copyright text (HTML):</label>

                        <div class="ds-form-content">
                            <textarea id="copyright_text" class="ds-textarea-field" name="copyright_text" onfocus="javascript:tFocus(this);" cols="40" rows="6">${collectionMetadataForm.getCopyright_text()!""}</textarea>
                            <@dspace.showErrorsFor "collectionMetadataForm.copyright_text" />
                        </div>
                    </li>
                    <li class="ds-form-item even">
                        <label class="ds-form-label" for="side_bar_text">News (HTML):</label>

                        <div class="ds-form-content">
                            <textarea id="side_bar_text" class="ds-textarea-field" name="side_bar_text" onfocus="javascript:tFocus(this);" cols="40" rows="6">${collectionMetadataForm.getSide_bar_text()!""}</textarea>
                            <@dspace.showErrorsFor "collectionMetadataForm.side_bar_text" />
                        </div>
                    </li>
                    <li class="ds-form-item even">
                        <label class="ds-form-label" for="license">Default License:</label>

                        <div class="ds-form-content">
                            <textarea id="license" class="ds-textarea-field" name="license" onfocus="javascript:tFocus(this);" cols="40" rows="6">${collectionMetadataForm.getLicense()!""}</textarea>
                            <@dspace.showErrorsFor "collectionMetadataForm.license" />
                        </div>
                    </li>
                    <li class="ds-form-item even">
                        <label class="ds-form-label" for="provenance_description">Provenance Description:</label>

                        <div class="ds-form-content">
                            <textarea id="provenance_description" class="ds-textarea-field" name="provenance_description" onfocus="javascript:tFocus(this);" cols="40" rows="6">${collectionMetadataForm.getProvenance_description()!""}</textarea>
                            <@dspace.showErrorsFor "collectionMetadataForm.provenance_description" />
                        </div>
                    </li>
                </ol>
            </fieldset>

            <p class="ds-paragraph">
                <input id="update" class="ds-button-field" name="update" type="submit" value="Update">
                <input id="delete" class="ds-button-field" name="delete" type="submit" value="Delete Collection">
            </p>
        </form>
    </body>
</html>