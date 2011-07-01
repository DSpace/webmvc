<#import "/includes/dspace.ftl" as dspace />

<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>

  <table width="95%">
    <tr>
      <td align="left">
          <#if edit_title??><#assign showingArgs=["${edit_title}"]>
          <h1><@dspace.messageArgs "ui.dspace-admin.authorize-policy-edit.heading", showingArgs /></h1></#if>
      </td>
      <td align="right" class="standard">
        <a href="<@dspace.url "/help/site-admin.html#authorize" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
      </td>
    </tr>
  </table>

<form action="<@dspace.url "/admin/authorize" />" method="post">

    <table class="miscTable" align="center" summary="Edit Policy Form">
        <tr>
            <%-- <td>Group:</td> --%>
            <th id="t1"><label for="tgroup_id"><@dspace.message "ui.dspace-admin.general.group-colon" /></label></th>
            <td headers="t1">
                <select size="15" name="group_id" id="tgroup_id">
                    <#if groups?? && policy??>
                    <#list groups as grp>
                    <option value="${grp.getID()}" <#if grp.getID()== policy.getGroupID()>${"selected=\"selected\""}<#else>${""}</#if>>
                    ${grp.getName()}
                    </option>
                    </#list>
                    </#if>
                </select>
            </td>
        </tr>
        <%-- <tr><td>Action:</td> --%>
        <tr>
          <th id="t2"><label for="taction_id"><@dspace.message "ui.dspace-admin.general.action-colon" /></label></th>
            <td headers="t2">
                <#if id_name?? && id??><input type="hidden" name="${id_name}" value="${id}" /></#if>
                <#if policy??><input type="hidden" name="policy_id" value="${policy.getID()}" /></#if>
                <select name="action_id" id="taction_id">
                    <#if actionText?? && policy??>
                        <#list actionText as at>
                        <option value="${at_index}" <#if policy.getAction() == at_index>${"selected=\"selected\""}<#else>${""}</#if>>${at}</option>
                        </#list>
                    </#if>
                </select>
            </td>
        </tr>
    </table>

    <#if newpolicy??><input name="newpolicy" type="hidden" value="${newpolicy}"/></#if>

    <div align="center">
        <table width="70%">
            <tr>
                <td align="left">
                    <%-- <input type="submit" name="submit_save_policy" value="Save Policy"> --%>
                    <input type="submit" name="submit_save_policy" value="<@dspace.message "ui.dspace-admin.general.save" />" />
                </td>
                <td align="right">
                    <%-- <input type="submit" name="submit_cancel_policy" value="Cancel"> --%>
                    <input type="submit" name="submit_cancel_policy" value="<@dspace.message "ui.dspace-admin.general.cancel" />" />
                </td>
            </tr>
        </table>
    </div>
    </form>





