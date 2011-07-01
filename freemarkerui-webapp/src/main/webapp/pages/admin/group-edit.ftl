<#import "/includes/dspace.ftl" as dspace />

<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>

<table width="95%">
    <tr>
      <td align="left">
    <#if group??><h1><@dspace.message "ui.tools.group-edit.title " /> : ${group.getName()} (id: ${group.getID()})</h1></#if>
      </td>
      <td align="right" class="standard">
          <a href="<@dspace.url "/help/site-admin.html#groups" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
      </td>
    </tr>
  </table>

<form name="epersongroup" method="post" action="<@dspace.url "/admin/group" />">

    <p><#if utilsGrpName??><label for="tgroup_name"><@dspace.message "ui.tools.group-edit.name" /></label><input name="group_name" id="tgroup_name" value="${utilsGrpName}"/></#if></p>
    <h3><@dspace.message "ui.tools.group-edit.heading" /></h3>

     <#if group??><input type="hidden" name="group_id" value="${group.getID()}"/></#if>
     <table>
          <tr>
            <td align="center"><strong><@dspace.message "ui.tools.group-edit.eperson" /></strong><br/>
                <#assign multiple = true>
                <table><tr><td colspan="2" align="center"><select multiple="multiple" name="eperson_id" <#if multiple?? && multiple==true>size="10"<#else>size="1"</#if>>

                   <#if members?? && members?size&gt;0>
                        <#list members as m>
                             <option value="${m.getID()}"> ${m.getFullName()}${"("}${m.getEmail()}${")"}</option>
                        </#list>
                   <#else>
                   <option value="">&nbsp;</option>
                   </#if>
                </select></td>

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


            <td align="center"><strong><@dspace.message "ui.tools.group-edit.group" /></strong><br/>
           <#assign multiple = true>
           <table><tr><td colspan="2" align="center"><select multiple="multiple" name="group_ids" <#if multiple?? && multiple==true>size="10"<#else>size="1"</#if>>

                   <#if membergroups?? && membergroups?size&gt;0>
                        <#list membergroups as mg>
                             <option value="${mg.getID()}"> ${mg.getName()}${"("}${mg.getID()}${")"}</option>
                        </#list>
                   <#else>
                   <option value="">&nbsp;</option>
                   </#if>
                </select></td>

                    <#if multiple?? && multiple==true>
                    </tr>
                       <tr>
                       <td width="50%" align="center">
                       <input type="button" value="<@dspace.message "ui.dspace.app.webui.SelectGroupTag.selectGroups" />" onclick="javascript:popup_window('<@dspace.url "/admin/group/group-select-list?multiple=true" />', 'group_popup');" />
                       </td>

                       <td width="50%" align="center">
                       <input type="button" value="<@dspace.message "ui.dspace.app.webui.SelectGroupTag.removeSelected" />" onclick="javascript:removeSelected(window.document.epersongroup.group_ids);"/>
                    <#else>
                    <td><input type="button" value="<@dspace.message "ui.dspace.app.webui.SelectGroupTag.selectGroup" />" onclick="javascript:popup_window('<@dspace.url "/admin/group/group-select?multiple=false" />', 'group_popup');" />
                    </#if>
                    </td></tr></table>

            </td>
		  </tr>
     </table>

        <br/>
     <p><input type="submit" name="submit_group_update" value="<@dspace.message "ui.tools.group-edit.update.button" />" onclick="javascript:finishEPerson();finishGroups();"/></p>
</form>