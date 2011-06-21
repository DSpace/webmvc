<#import "/includes/dspace.ftl" as dspace />


<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>

<h1><@dspace.message "ui.dspace-admin.eperson-main.heading" /></h1>
  <table width="95%">
    <tr>
      <td align="left">
        <%-- <h3>Choose an action:</h3> --%>
        <h3><@dspace.message "ui.dspace-admin.eperson-main.choose" /></h3>
      </td>
    </tr>
  </table>

<#if no_eperson_selected??>
    <p><strong><@dspace.message "ui.dspace-admin.eperson-main.noepersonselected" /></strong></p>
</#if>

    <form name="epersongroup" method="post" action="<@dspace.url "/admin/eperson" />">
      <table width="90%">

          <tr>
                <td colspan="3" align="center">
                    <%-- <input type="submit" name="submit_add" value="Add EPerson..."> --%>
                    <input type="submit" name="submit_add" value="<@dspace.message "ui.dspace-admin.eperson-main.add" />" />
                </td>
          </tr>
          <tr>
            	<%-- <td colspan="3" align="center"><strong>OR</strong></td> --%>
            	<td colspan="3"><strong><@dspace.message "ui.dspace-admin.eperson-main.or" /></strong></td>
          </tr>
          <tr>
                <td>
                <table><tr><td colspan="2" align="center"><select multiple="multiple" name="eperson_id" <#if multiple?? && multiple==true>size="10"<#else>size="1"</#if>>

                   <#if epeople?? && epeople?size&gt;0>
                        <#list epeople as e>
                             <option value="${e.getID()}"> ${e.getFullName()}${"("}${e.getEmail()}${")"}</option>
                        </#list>
                   <#else>
                   <option value="">&nbsp;</option>
                   </#if>
                </select></td>
                    <#assign multiple = false>
                    <#if multiple?? && multiple==true>
                    </tr>
                       <tr>
                       <td width="50%" align="center">
                       <input type="button" value="<@dspace.message "ui.dspace.app.webui.SelectEPersonTag.selectPeople" />" onclick="javascript:popup_window('<@dspace.url "/admin/eperson/browse-epeople?multiple=true" />', 'eperson_popup');" />
                       </td>

                       <td width="50%" align="center">
                       <input type="button" value="<@dspace.message "ui.dspace.app.webui.SelectEPersonTag.removeSelected" />" onclick="javascript:removeSelected(window.document.epersongroup.eperson_id);"/>
                    <#else>
                    <td><input type="button" value="<@dspace.message "ui.dspace.app.webui.SelectEPersonTag.selectPerson" />" onclick="javascript:popup_window('<@dspace.url "/admin/eperson/browse-epeople?multiple=false" />', 'eperson_popup');" />
                    </#if>
                    </td></tr></table>
                </td>

          <td>
              <@dspace.message "ui.dspace-admin.eperson-main.then" />&nbsp;<input type="submit" name="submit_edit" value="<@dspace.message "ui.dspace-admin.general.edit" />" onclick="javascript:finishEPerson();"/>
          </td>
          <td>
              <input type="submit" name="submit_delete" value="<@dspace.message "ui.dspace-admin.general.delete-w-confirm" />" onclick="javascript:finishEPerson();"/>
          </td>
          </tr>
      </table>
    </form>
