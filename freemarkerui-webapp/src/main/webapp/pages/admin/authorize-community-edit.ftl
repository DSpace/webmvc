<#import "/includes/dspace.ftl" as dspace />
<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>
<table width="95%">
    <tr>
      <td align="left">
    <#if community??><#assign showingArgs=[community.getMetadata("name"), community.getHandle(), community.getID()]>
    <h1><@dspace.messageArgs "ui.dspace-admin.authorize-community-edit.policies", showingArgs /></h1></#if>
      </td>
      <td align="right" class="standard">
        <a href="<@dspace.url "/help/site-admin.html#communitypolicies" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
      </td>
    </tr>
  </table>

  <form action="<@dspace.url "/admin/authorize" />" method="post">
    <p align="center">
            <input type="hidden" name="community_id" value="<#if community??>${community.getID()}</#if>" />
            <input type="submit" name="submit_community_add_policy" value="<@dspace.message "ui.dspace-admin.general.addpolicy" />" />
    </p>
  </form>

<table class="miscTable" align="center" summary="Community Policy Edit Form">
        <tr>

            <th id="t1" class="oddRowOddCol"><strong><@dspace.message "ui.general.id" /></strong></th>
            <th id="t2" class="oddRowEvenCol"><strong><@dspace.message "ui.dspace-admin.general.action" /></strong></th>
            <th id="t3" class="oddRowOddCol"><strong><@dspace.message "ui.dspace-admin.general.group" /></strong></th>
            <th id="t4" class="oddRowEvenCol">&nbsp;</th>
            <th id="t5" class="oddRowOddCol">&nbsp;</th>
        </tr>

    <#assign row = "even">
    <#if policies??>
        <#list policies as rp>
            <tr>
            <td headers="t1" class="${row}RowOddCol">${rp.getID()}</td>
            <td headers="t2" class="${row}RowEvenCol">
                    ${rp.getActionText()}
            </td>
            <td headers="t3" class="${row}RowOddCol"><#if rp.getGroup()??>${rp.getGroup().getName()}<#else>${"..."}</#if></td>
            <td headers="t4" class="${row}RowEvenCol">
                <form action="<@dspace.url "/admin/authorize" />" method="post">
                    <input type="hidden" name="policy_id" value="${rp.getID()}" />
                    <input type="hidden" name="community_id" value="<#if community??>${community.getID()}</#if>" />
                    <input type="submit" name="submit_community_edit_policy" value="<@dspace.message "ui.dspace-admin.general.edit" />" />
                </form>
             </td>
             <td headers="t5" class="${row}RowOddCol">
                <form action="<@dspace.url "/admin/authorize" />" method="post">
                    <input type="hidden" name="policy_id" value="${rp.getID()}" />
                    <input type="hidden" name="community_id" value="<#if community??>${community.getID()}</#if>" />
                    <input type="submit" name="submit_community_delete_policy" value="<@dspace.message "ui.dspace-admin.general.delete" />" />
                </form>
             </td>
         </tr>
        <#if row == "even">
        <#assign row = "odd">
        <#else>
        <#assign row = "even">
        </#if>
        </#list>
    </#if>
    </table>