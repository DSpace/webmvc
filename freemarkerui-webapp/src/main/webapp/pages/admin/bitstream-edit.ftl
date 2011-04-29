<h1 class="ds-div-head">Edit Bitstream</h1>
<form class="ds-interactive-div" method="post" enctype="multipart/form-data">
    <fieldset class="ds-form-list thick">
        <ol>
            <li class="ds-form-item even">
                <span class="ds-form-label">File:</span>

                <div class="ds-form-content">
                    <a href="TODO-file-bitstream-retrieve">TODO-bitstream-file-name</a>
                </div>
            </li>
            <li class="ds-form-item odd">
                <label class="ds-form-label" for="aspect_administrative_item_EditBitstreamForm_field_primary">Primary
                    bitstream:</label>

                <div class="ds-form-content">
                    <select id="aspect_administrative_item_EditBitstreamForm_field_primary" class="ds-select-field"
                            name="primary">
                        <option value="yes">yes</option>
                        <option value="no" selected="selected">no</option>
                    </select>
                </div>
            </li>
            <li class="ds-form-item even">
                <label class="ds-form-label" for="aspect_administrative_item_EditBitstreamForm_field_description">Description:</label>

                <div class="ds-form-content">
                    <input id="aspect_administrative_item_EditBitstreamForm_field_description" class="ds-text-field"
                           name="description" type="text" value="Extracted text"
                           title="Optionally, provide a brief description of the file, for example &quot;Main article&quot;, or &quot;Experiment data readings&quot;.&quot;"/>
                    <span class="field-help">Optionally, provide a brief description of the file, for example "<i>Main
                        article</i>", or "<i>Experiment data readings</i>"."</span>
                </div>
            </li>
            <li class="ds-form-item odd">
                <div class="ds-form-content">Select the format of the file from the list below, for example "<i>Adobe
                    PDF</i>" or "<i>Microsoft Word</i>", <b>OR</b> if the format is not in the list, please describe it
                    in the box below.
                </div>
            </li>
            <li class="ds-form-item even">
                <label class="ds-form-label" for="aspect_administrative_item_EditBitstreamForm_field_formatID">Selected
                    Format:</label>

                <div class="ds-form-content">
                    <select id="aspect_administrative_item_EditBitstreamForm_field_formatID" class="ds-select-field"
                            name="formatID">
                        <option value="-1">Format not in list</option>
                        <option value="2">License (known) (Internal)</option>
                        <option value="3">Adobe PDF (Supported)</option>
                        <option value="4">XML (Supported)</option>
                        <option value="5" selected="selected">Text (Supported)</option>
                        <option value="6">HTML (Supported)</option>
                        <option value="7">Microsoft Word (known)</option>
                        <option value="8">Microsoft PowerPoint (known)</option>
                        <option value="9">Microsoft Excel (known)</option>
                        <option value="10">MARC (Supported)</option>
                        <option value="11">JPEG (Supported)</option>
                        <option value="12">GIF (Supported)</option>
                        <option value="13">image/png (Supported)</option>
                        <option value="14">TIFF (Supported)</option>
                        <option value="15">AIFF (Supported)</option>
                        <option value="16">audio/basic (known)</option>
                        <option value="17">WAV (known)</option>
                        <option value="18">MPEG (known)</option>
                        <option value="19">RTF (Supported)</option>
                        <option value="20">Microsoft Visio (known)</option>
                        <option value="21">FMP3 (known)</option>
                        <option value="22">BMP (known)</option>
                        <option value="23">Photoshop (known)</option>
                        <option value="24">Postscript (Supported)</option>
                        <option value="25">Video Quicktime (known)</option>
                        <option value="26">MPEG Audio (known)</option>
                        <option value="27">Microsoft Project (known)</option>
                        <option value="28">Mathematica (known)</option>
                        <option value="29">LateX (known)</option>
                        <option value="30">TeX (known)</option>
                        <option value="31">TeX dvi (known)</option>
                        <option value="32">SGML (known)</option>
                        <option value="33">WordPerfect (known)</option>
                        <option value="34">RealAudio (known)</option>
                        <option value="35">Photo CD (known)</option>
                        <option value="36">OGG Media Type (known)</option>
                        <option value="37">CSV (Supported)</option>
                        <option value="38">Microsoft Word XML (known)</option>
                        <option value="39">Microsoft PowerPoint XML (known)</option>
                        <option value="40">Microsoft Excel XML (known)</option>
                        <option value="41">CSS File (known)</option>
                        <option value="42">CC License (known) (Internal)</option>
                        <option value="43">RDF XML (known)</option>
                    </select>
                </div>
            </li>
            <li class="ds-form-item odd">
                <div class="ds-form-content">If the format is not in the above list, <strong>select "format not in list"
                    above</strong> and describe it in the field below.
                </div>
            </li>
            <li class="ds-form-item even">
                <label class="ds-form-label" for="aspect_administrative_item_EditBitstreamForm_field_user_format">Other
                    Format:</label>

                <div class="ds-form-content">
                    <input id="aspect_administrative_item_EditBitstreamForm_field_user_format" class="ds-text-field"
                           name="user_format" type="text" value=""
                           title="The application you used to create the file, and the version number (for example, &quot;ACMESoft SuperApp version 1.5&quot;)."/>
                    <span class="field-help">The application you used to create the file, and the version number (for example, "<i>ACMESoft
                        SuperApp version 1.5</i>").</span>
                </div>
            </li>
            <li class="ds-form-item odd last">
                <div class="ds-form-content">
                    <input id="aspect_administrative_item_EditBitstreamForm_field_submit_save" class="ds-button-field" name="submit_save" type="submit" value="Save"/>
                    <input id="aspect_administrative_item_EditBitstreamForm_field_submit_cancel" class="ds-button-field" name="submit_cancel" type="submit" value="Cancel"/>
                </div>
            </li>
        </ol>
    </fieldset>
</form>