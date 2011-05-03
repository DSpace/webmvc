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
    View and edit Item Bitstreams
-->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>Bitstream Edit: ${bitstreamForm.getName()!"Untitled"}</title>
    </head>
    <body>
        <h1 class="ds-div-head">Edit Bitstream</h1>
        <form class="ds-interactive-div" method="post" enctype="multipart/form-data">
            <fieldset class="ds-form-list thick">
                <ol>
                    <li class="ds-form-item even">
                        <span class="ds-form-label">File:</span>

                        <div class="ds-form-content">
                            <a href="<@dspace.url "/retrieve/${bitstreamForm.getBitstreamID()}"/>">${bitstreamForm.getName()}</a>
                        </div>
                    </li>
                    <li class="ds-form-item odd">
                        <label class="ds-form-label" for="field_primary">Primary bitstream:</label>

                        <div class="ds-form-content">
                            <select id="field_primary" class="ds-select-field" name="primary">
                                <#if bitstreamForm.getPrimary() == "yes" >
                                    <option value="yes" selected="selected" >yes</option>
                                    <option value="no">no</option>
                                <#else>
                                    <option value="yes" >yes</option>
                                    <option value="no" selected="selected">no</option>
                                </#if>

                            </select>
                        </div>
                    </li>
                    <li class="ds-form-item even">
                        <label class="ds-form-label" for="field_description">Description:</label>

                        <div class="ds-form-content">
                            <input id="field_description" class="ds-text-field"
                                   name="description" type="text" value="${bitstreamForm.getDescription()}"
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
                        <label class="ds-form-label" for="field_formatID">Selected Format:</label>

                        <div class="ds-form-content">
                            <select id="field_formatID" class="ds-select-field" name="formatID">
                                <option value="-1" <#if bitstreamForm.getFormatID() == -1>selected="selected"</#if>>Format not in list</option>
                                <option value="2" <#if bitstreamForm.getFormatID() == 2>selected="selected"</#if>>License (known) (Internal)</option>
                                <option value="3" <#if bitstreamForm.getFormatID() == 3>selected="selected"</#if>>Adobe PDF (Supported)</option>
                                <option value="4" <#if bitstreamForm.getFormatID() == 4>selected="selected"</#if>>XML (Supported)</option>
                                <option value="5" <#if bitstreamForm.getFormatID() == 5>selected="selected"</#if>>Text (Supported)</option>
                                <option value="6" <#if bitstreamForm.getFormatID() == 6>selected="selected"</#if>>HTML (Supported)</option>
                                <option value="7" <#if bitstreamForm.getFormatID() == 7>selected="selected"</#if>>Microsoft Word (known)</option>
                                <option value="8" <#if bitstreamForm.getFormatID() == 8>selected="selected"</#if>>Microsoft PowerPoint (known)</option>
                                <option value="9" <#if bitstreamForm.getFormatID() == 9>selected="selected"</#if>>Microsoft Excel (known)</option>
                                <option value="10" <#if bitstreamForm.getFormatID() == 10>selected="selected"</#if>>MARC (Supported)</option>
                                <option value="11" <#if bitstreamForm.getFormatID() == 11>selected="selected"</#if>>JPEG (Supported)</option>
                                <option value="12" <#if bitstreamForm.getFormatID() == 12>selected="selected"</#if>>GIF (Supported)</option>
                                <option value="13" <#if bitstreamForm.getFormatID() == 13>selected="selected"</#if>>image/png (Supported)</option>
                                <option value="14" <#if bitstreamForm.getFormatID() == 14>selected="selected"</#if>>TIFF (Supported)</option>
                                <option value="15" <#if bitstreamForm.getFormatID() == 15>selected="selected"</#if>>AIFF (Supported)</option>
                                <option value="16" <#if bitstreamForm.getFormatID() == 16>selected="selected"</#if>>audio/basic (known)</option>
                                <option value="17" <#if bitstreamForm.getFormatID() == 17>selected="selected"</#if>>WAV (known)</option>
                                <option value="18" <#if bitstreamForm.getFormatID() == 18>selected="selected"</#if>>MPEG (known)</option>
                                <option value="19" <#if bitstreamForm.getFormatID() == 19>selected="selected"</#if>>RTF (Supported)</option>
                                <option value="20" <#if bitstreamForm.getFormatID() == 20>selected="selected"</#if>>Microsoft Visio (known)</option>
                                <option value="21" <#if bitstreamForm.getFormatID() == 21>selected="selected"</#if>>FMP3 (known)</option>
                                <option value="22" <#if bitstreamForm.getFormatID() == 22>selected="selected"</#if>>BMP (known)</option>
                                <option value="23" <#if bitstreamForm.getFormatID() == 23>selected="selected"</#if>>Photoshop (known)</option>
                                <option value="24" <#if bitstreamForm.getFormatID() == 24>selected="selected"</#if>>Postscript (Supported)</option>
                                <option value="25" <#if bitstreamForm.getFormatID() == 25>selected="selected"</#if>>Video Quicktime (known)</option>
                                <option value="26" <#if bitstreamForm.getFormatID() == 26>selected="selected"</#if>>MPEG Audio (known)</option>
                                <option value="27" <#if bitstreamForm.getFormatID() == 27>selected="selected"</#if>>Microsoft Project (known)</option>
                                <option value="28" <#if bitstreamForm.getFormatID() == 28>selected="selected"</#if>>Mathematica (known)</option>
                                <option value="29" <#if bitstreamForm.getFormatID() == 29>selected="selected"</#if>>LateX (known)</option>
                                <option value="30" <#if bitstreamForm.getFormatID() == 30>selected="selected"</#if>>TeX (known)</option>
                                <option value="31" <#if bitstreamForm.getFormatID() == 31>selected="selected"</#if>>TeX dvi (known)</option>
                                <option value="32" <#if bitstreamForm.getFormatID() == 32>selected="selected"</#if>>SGML (known)</option>
                                <option value="33" <#if bitstreamForm.getFormatID() == 33>selected="selected"</#if>>WordPerfect (known)</option>
                                <option value="34" <#if bitstreamForm.getFormatID() == 34>selected="selected"</#if>>RealAudio (known)</option>
                                <option value="35" <#if bitstreamForm.getFormatID() == 35>selected="selected"</#if>>Photo CD (known)</option>
                                <option value="36" <#if bitstreamForm.getFormatID() == 36>selected="selected"</#if>>OGG Media Type (known)</option>
                                <option value="37" <#if bitstreamForm.getFormatID() == 37>selected="selected"</#if>>CSV (Supported)</option>
                                <option value="38" <#if bitstreamForm.getFormatID() == 38>selected="selected"</#if>>Microsoft Word XML (known)</option>
                                <option value="39" <#if bitstreamForm.getFormatID() == 39>selected="selected"</#if>>Microsoft PowerPoint XML (known)</option>
                                <option value="40" <#if bitstreamForm.getFormatID() == 40>selected="selected"</#if>>Microsoft Excel XML (known)</option>
                                <option value="41" <#if bitstreamForm.getFormatID() == 41>selected="selected"</#if>>CSS File (known)</option>
                                <option value="42" <#if bitstreamForm.getFormatID() == 42>selected="selected"</#if>>CC License (known) (Internal)</option>
                                <option value="43" <#if bitstreamForm.getFormatID() == 43>selected="selected"</#if>>RDF XML (known)</option>
                            </select>
                        </div>
                    </li>
                    <li class="ds-form-item odd">
                        <div class="ds-form-content">If the format is not in the above list, <strong>select "format not in list"
                            above</strong> and describe it in the field below.
                        </div>
                    </li>
                    <li class="ds-form-item even">
                        <label class="ds-form-label" for="field_user_format">Other
                            Format:</label>

                        <div class="ds-form-content">
                            <input id="field_user_format" class="ds-text-field"
                                   name="user_format" type="text" value="${bitstreamForm.getUser_format()!""}"
                                   title="The application you used to create the file, and the version number (for example, &quot;ACMESoft SuperApp version 1.5&quot;)."/>
                            <span class="field-help">The application you used to create the file, and the version number (for example, "<i>ACMESoft
                                SuperApp version 1.5</i>").</span>
                        </div>
                    </li>
                    <li class="ds-form-item odd last">
                        <div class="ds-form-content">
                            <input id="submit_update" class="ds-button-field" name="update" type="submit" value="Save"/>
                            <input id="submit_cancel" class="ds-button-field" name="cancel" type="submit" value="Cancel"/>
                        </div>
                    </li>
                </ol>
            </fieldset>
        </form>
        </body>
</html>