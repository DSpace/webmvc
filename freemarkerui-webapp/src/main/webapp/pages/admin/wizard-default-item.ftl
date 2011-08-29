<#import "/includes/dspace.ftl" as dspace />

<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>

<table width="95%">
    <tr>
      <td>
	<h1><@dspace.message "ui.dspace-admin.wizard-default-item.enter" /></h1>
      </td>
      <td class="standard" align="right">
        <a href="<@dspace.url "/help/site-admin.html#wizard_default" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
      </td>
    </tr>
  </table>

	<p><@dspace.message "ui.dspace-admin.wizard-default-item.text1" /></p>

	<p><@dspace.message "ui.dspace-admin.wizard-default-item.text2" /></p>

    <form method="post" action="<@dspace.url "/tools/collection-wizard"/>">
        <div align="center"><table class="miscTable" summary="Enter default metadata table">
            <tr>
                <th id="t1" class="oddRowOddCol"><strong><@dspace.message "ui.dspace-admin.wizard-default-item.dcore" /></strong></th>
                <th id="t2" class="oddRowEvenCol"><strong><@dspace.message "ui.dspace-admin.wizard-default-item.value" /></strong></th>
                <th id="t3" class="oddRowOddCol"><strong><@dspace.message "ui.dspace-admin.wizard-default-item.language" /></strong></th>
            </tr>

    <#assign row = "even">
    <#assign x=10>
    <#list 1..x as i>

			<tr>
			    <td headers="t1" class="<%= row %>RowOddCol"><select name="dctype_${i}">
			    <option value="-1"><@dspace.message "ui.dspace-admin.wizard-default-item.select" /></option>
    <#if dctypes??>
        <#list 1..dctypes?size as dc>
        <option value="${dcTypes[dc].getFieldID()}">
            <#if metaDataValues??>${metaDataValues.get(dc)}</#if></option>
        </#list>
    </#if>
    </select></td>
				<td headers="t2" class="${row}RowEvenCol">
					<input type="text" name="value_${i}" size="40" />
				</td>
				<td headers="t3" class="${row}RowEvenCol">
					<input type="text" name="lang_${i}" size="5" maxlength="5" />
				</td>
			</tr>

    </#list>

		</table>
	</div>
       <p>&nbsp;</p>

        <input type="hidden" name="collection_id" value="<#if collection??>${collection.getID()}</#if>" />
        <input type="hidden" name="stage" value="4" />

        <div align="center">
            <table border="0" width="80%">
                <tr>
                    <td width="100%">&nbsp;</td>
                    <td>
                        <input type="submit" name="submit_next" value="<@dspace.message "ui.dspace-admin.general.next.button" />" />
                    </td>
                </tr>
            </table>
        </div>
    </form>