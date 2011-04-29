<h1>Upload Bitstream</h1>

<form id="add-bitstream" class="ds-interactive-div primary administrative item" method="post"
      enctype="multipart/form-data">
    <fieldset id="submit-upload-new" class="ds-form-list thick">
        <legend>Upload a new bitstream</legend>
        <ol>
            <li class="ds-form-item even">
                <label class="ds-form-label" for="bundle">Bundle:</label>

                <div class="ds-form-content">
                    <select id="bundle" class="ds-select-field"
                            name="bundle">
                        <option value="ORIGINAL" selected="selected">Content Files (default)</option>
                        <option value="METADATA">Metadata Files</option>
                        <option value="THUMBNAIL">Thumbnails</option>
                        <option value="LICENSE">Licenses</option>
                        <option value="CC_LICENSE">Creative Commons Licenses</option>
                    </select>
                </div>
            </li>
            <li class="ds-form-item odd">
                <label class="ds-form-label"
                       for="aspect_administrative_item_AddBitstreamForm_field_file">File:</label>

                <div class="ds-form-content">
                    <input id="aspect_administrative_item_AddBitstreamForm_field_file" class="ds-file-field"
                           name="file" type="file" value=""
                           title="Please enter the name of the file on your computer corresponding to your item. If you click &quot;Browse...&quot;, a new window will appear in which you can locate and select the file from your computer."/>
                    <span class="field-help">Please enter the name of the file on your computer corresponding to your item. If you click "Browse...", a new window will appear in which you can locate and select the file from your computer.</span>
                </div>
            </li>
            <li class="ds-form-item even">
                <label class="ds-form-label" for="aspect_administrative_item_AddBitstreamForm_field_description">Description:</label>

                <div class="ds-form-content">
                    <input id="aspect_administrative_item_AddBitstreamForm_field_description" class="ds-text-field"
                           name="description" type="text" value=""
                           title="Optionally, provide a brief description of the file, for example &quot;Main article&quot;, or &quot;Experiment data readings&quot;."/>
                    <span class="field-help">Optionally, provide a brief description of the file, for example "<i>Main
                        article</i>", or "<i>Experiment data readings</i>".</span>
                </div>
            </li>
            <li class="ds-form-item odd last">
                <div class="ds-form-content">
                    <input id="submit_upload" class="ds-button-field" name="create" type="submit" value="Upload"/>
                    <input id="submit_cancel" class="ds-button-field" name="submit_cancel" type="submit"
                           value="Cancel"/>
                </div>
            </li>
        </ol>
    </fieldset>
</form>