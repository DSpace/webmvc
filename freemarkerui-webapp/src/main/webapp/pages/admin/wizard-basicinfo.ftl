<#import "/includes/dspace.ftl" as dspace />

<head>
    <script type="text/javascript" src="<@dspace.url "/utils.js" />"></script>
</head>

<table width="95%">
    <tr>
      <td>
        <h1><@dspace.message "ui.dspace-admin.wizard-basicinfo.title" /></h1>
      </td>
      <td class="standard" align="right">
        <a href="<@dspace.url "/help/site-admin.html#wizard_description" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
      </td>
    </tr>
  </table>

    <form action="<@dspace.url "/tools/collection-wizard"/>" method="post" enctype="multipart/form-data">
        <table summary="Describe the Collection table">
            <tr>
            	<td><p class="submitFormLabel">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.name" /></p></td>
               <td><input type="text" name="name" size="50" id="tname" /></td>
            </tr>

            <tr>
            	<td width="40%">&nbsp;</td>
				<td>&nbsp;</td>
				<td width="40%">&nbsp;</td>
            </tr>
            <tr>
				<td colspan="3" class="submitFormHelp">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.shown" />
                </td>
            </tr>
            <tr>
                <td><p class="submitFormLabel"><@dspace.message "ui.dspace-admin.wizard-basicinfo.description" /></p></td>
                <td><input type="text" name="short_description" size="50"/></td>
           </tr>

            <tr><td>&nbsp;</td></tr>

            <tr>
                <td colspan="3" class="submitFormHelp">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.html1" />
                </td>
            </tr>
            <tr>
                <td><p class="submitFormLabel">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.intro" /></p></td>
                <td><textarea name="introductory_text" rows="4" cols="50"></textarea></td>
            </tr>

            <tr><td>&nbsp;</td></tr>

            <tr>
                <td colspan="3" class="submitFormHelp">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.plain" />
                </td>
            </tr>
            <tr>
                <td><p class="submitFormLabel">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.copyright" /></p></td>
                <td><textarea name="copyright_text" rows="3" cols="50"></textarea></td>
            </tr>

            <tr><td>&nbsp;</td></tr>

            <tr>
                <td colspan="3" class="submitFormHelp">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.html2" />
                </td>
            </tr>
            <tr>
                <td><p class="submitFormLabel">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.side" /></p></td>
                <td><textarea name="side_bar_text" rows="4" cols="50"></textarea></td>
            </tr>

            <tr><td>&nbsp;</td></tr>

            <tr>
                <td colspan="32" class="submitFormHelp">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.license1" />
                </td>
            </tr>
            <tr>

                <td><p class="submitFormLabel">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.license2" /></p></td>
                <td><textarea name="license" rows="4" cols="50"></textarea></td>
            </tr>

            <tr><td>&nbsp;</td></tr>

            <tr>
                <td colspan="3" class="submitFormHelp">
	                <@dspace.message "ui.dspace-admin.wizard-basicinfo.plain2" />
                </td>
            </tr>
            <tr>

                <%-- <td><p class="submitFormLabel">Provenance:</p></td> --%>
                <td><p class="submitFormLabel">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.provenance" /></p></td>
                <td><textarea name="provenance_description" rows="4" cols="50"></textarea></td>
            </tr>

            <tr><td>&nbsp;</td></tr>

             <tr>
                <td colspan="3" class="submitFormHelp">
	                <@dspace.message "ui.dspace-admin.wizard-basicinfo.choose" />
                </td>
            </tr>
            <tr>
                <td><p class="submitFormLabel">
                    <@dspace.message "ui.dspace-admin.wizard-basicinfo.logo" /></p></td>
                <td><input type="file" size="40" name="file"/></td>
            </tr>
        </table>

        <p>&nbsp;</p>

        <input type="hidden" name="collection_id" value="<#if collection??>${collection.getID()}</#if>" />
        <input type="hidden" name="stage" value="2" />

        <center>
            <table border="0" width="80%">
                <tr>
                    <td width="100%">&nbsp;
                    </td>
                    <td>

                        <input type="submit" name="submit_next_logo" value="<@dspace.message "ui.dspace-admin.general.next.button" />" />
                   </td>
                </tr>
            </table>
        </center>
    </form>