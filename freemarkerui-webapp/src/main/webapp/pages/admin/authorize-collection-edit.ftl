<#import "/includes/dspace.ftl" as dspace />

 <table width="95%">
    <tr>
      <td align="left">
          <#if collection??><#assign showingArgs=[collection.getMetadata("name"), collection.getHandle(), collection.getID()]>
          <h1><@dspace.messageArgs "ui.dspace-admin.authorize-collection-edit.policies", showingArgs /></h1></#if>
      </td>
      <td align="right" class="standard">
         <a href="<@dspace.url "/help/site-admin.html#collectionpolicies" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
      </td>
    </tr>
 </table>

 <form action="<@dspace.url "/admin/authorize" />" method="post">
    <p align="center">
            <input type="hidden" name="collection_id" value="<#if collection??>${collection.getID()}</#if>" />
            <input type="submit" name="submit_collection_add_policy" value="<@dspace.message "ui.dspace-admin.general.addpolicy" />" />
    </p>
 </form>

<#assign row = "even">
<#if policies??>
        <#list policies as rp>
        <form action="<@dspace.url "/admin/authorize" />" method="post">
        <table class="miscTable" align="center" summary="Collection Policy Edit Form">
            <tr>
               <th class="oddRowOddCol"><strong><@dspace.message "ui.general.id" /></strong></th>
               <th class="oddRowEvenCol"><strong><@dspace.message "ui.dspace-admin.general.action" /></strong></th>
               <th class="oddRowOddCol"><strong><@dspace.message "ui.dspace-admin.general.group" /></strong></th>
               <th class="oddRowEvenCol">&nbsp;</th>
               <th class="oddRowOddCol">&nbsp;</th>
            </tr>

            <tr>
               <td class="${row}RowOddCol">${rp.getID()}</td>
               <td class="${row}RowEvenCol">
                    ${rp.getActionText()}
               </td>
               <td class="${row}RowOddCol">
                    <#if rp.getGroup()??>${rp.getGroup().getName()}<#else>${"..."}</#if>
               </td>
               <td class="${row}RowEvenCol">
                    <input type="hidden" name="policy_id" value="${rp.getID()}" />
                    <input type="hidden" name="collection_id" value="<#if collection??>${collection.getID()}</#if>" />
                    <input type="submit" name="submit_collection_edit_policy" value="<@dspace.message "ui.dspace-admin.general.edit" />" />
               </td>
               <td class="<%= row %>RowOddCol">
                    <input type="submit" name="submit_collection_delete_policy" value="<@dspace.message "ui.dspace-admin.general.delete" />" />
               </td>
            </tr>
       </table>
     </form>
        <#if row == "even">
        <#assign row = "odd">
        <#else>
        <#assign row = "even">
        </#if>
        </#list>
</#if>