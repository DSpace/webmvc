<#import "/includes/dspace.ftl" as dspace />

<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>

  <table width="95%">
    <tr>
      <td align="left">
        <#if item??>
            <#if item.getHandle()??>
            <#assign showingArgs=[item.getHandle(), item.getID()]>
            <#else>
            <#assign showingArgs=["null", item.getID()]>
            </#if>
          <h1><@dspace.messageArgs "ui.dspace-admin.authorize-item-edit.policies", showingArgs /></h1></#if>
      </td>
      <td align="right" class="standard">
        <a href="<@dspace.url "/help/site-admin.html#itempolicies" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
      </td>
    </tr>
  </table>

  <p><@dspace.message "ui.dspace-admin.authorize-item-edit.text1" /></p>
  <p><@dspace.message "ui.dspace-admin.authorize-item-edit.text2" /></p>

<h3><@dspace.message "ui.dspace-admin.authorize-item-edit.item" /></h3>

    <form method="post" action="<@dspace.url "/admin/authorize" />">
      <p align="center">
          <input type="hidden" name="item_id" value="<#if item??>${item.getID()}</#if>" />
          <input type="submit" name="submit_item_add_policy" value="<@dspace.message "ui.dspace-admin.general.addpolicy" />" />
      </p>
    </form>

    <table class="miscTable" align="center" summary="Item Policy Edit Form">
        <tr>
            <th class="oddRowOddCol"><strong><@dspace.message "ui.general.id" /></strong></th>
            <th class="oddRowEvenCol"><strong><@dspace.message "ui.dspace-admin.general.action" /></strong></th>
            <th class="oddRowOddCol"><strong><@dspace.message "ui.dspace-admin.authorize-item-edit.eperson" /></strong></th>
            <th class="oddRowEvenCol"><strong><@dspace.message "ui.dspace-admin.general.group" /></strong></th>
            <th class="oddRowOddCol">&nbsp;</th>
        </tr>

    <#assign row = "even">
    <#if item_policies??>
    <#list item_policies as rp>

    <tr>
            <td class="${row}RowOddCol">${rp.getID()}</td>
            <td class="${row}RowEvenCol">
                    ${rp.getActionText()}
            </td>
            <td class="${row}RowOddCol">
                <#if rp.getEPerson()??>
                    ${rp.getEPerson().getEmail()}
                <#else>
                    ${"..."}
                </#if>
            </td>
            <td class="${row}RowEvenCol">
                <#if rp.getGroup()??>
                    ${rp.getGroup().getName()}
                <#else>
                    ${"..."}
                </#if>
            </td>
            <td class="${row}RowOddCol">
                 <form method="post" action="<@dspace.url "/admin/authorize" />">
                     <input type="hidden" name="policy_id" value="${rp.getID()}" />
                     <input type="hidden" name="item_id" value="<#if item??>${item.getID()}</#if>" />
                     <input type="submit" name="submit_item_edit_policy" value="<@dspace.message "ui.dspace-admin.general.edit" />" />
                     <input type="submit" name="submit_item_delete_policy" value="<@dspace.message "ui.dspace-admin.general.delete" />" />
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

    <#if bundles??>
    <#list bundles as myBun>
    <#if bundle_policies??>
    <#assign myPolicies = bundle_policies.get(new Integer(myBun.getID()))>
    <#assign showingArgs=[myBun.getName(), myBun.getID()]>
    <h1><@dspace.messageArgs "ui.dspace-admin.authorize-item-edit.bundle", showingArgs /></h1>

    <form method="post" action="<@dspace.url "/admin/authorize" />">

        <p align="center">
            <input type="hidden" name="item_id" value="<#if item??>${item.getID()}</#if>" />
            <input type="hidden" name="bundle_id" value="${myBun.getID()}" />
            <input type="submit" name="submit_bundle_add_policy" value="<@dspace.message "ui.dspace-admin.general.addpolicy" />" />
        </p>
    </form>

    <table class="miscTable" align="center" summary="Bundle Policy Edit Form">
        <tr>
            <th class="oddRowOddCol"><strong><@dspace.message "ui.general.id" /></strong></th>
            <th class="oddRowEvenCol"><strong><@dspace.message "ui.dspace-admin.general.action" /></strong></th>
            <th class="oddRowOddCol"><strong><@dspace.message "ui.dspace-admin.general.eperson" /></strong></th>
            <th class="oddRowEvenCol"><strong><@dspace.message "ui.dspace-admin.general.group" /></strong></th>
            <th class="oddRowOddCol">&nbsp;</th>
        </tr>

    <#assign row = "even">
    <#list myPolicies as rp>

    <tr>
            <td class="${row}RowOddCol">${rp.getID()}</td>
            <td class="${row}RowEvenCol">
                    ${rp.getActionText()}
            </td>
            <td class="${row}RowOddCol">
                    <#if rp.getEPerson()??>
                        ${rp.getEPerson().getEmail()}
                    </#if>
            </td>
            <td class="${row}RowEvenCol">
                    <#if rp.getGroup()??>
                        ${rp.getGroup().getName()}
                    </#if>
            </td>
            <td class="${row}RowOddCol">
                <form method="post" action="<@dspace.url "/admin/authorize" />">
                    <input type="hidden" name="policy_id" value="${rp.getID()}" />
                    <input type="hidden" name="item_id" value="<#if item??>${item.getID()}</#if>" />
                    <input type="hidden" name="bundle_id" value="${myBun.getID()}" />
                    <input type="submit" name="submit_item_edit_policy" value="<@dspace.message "ui.dspace-admin.general.edit" />" />
                    <input type="submit" name="submit_item_delete_policy" value="<@dspace.message "ui.dspace-admin.general.delete" />" />
                </form>
            </td>
    </tr>
    <#if row == "even">
        <#assign row = "odd">
    <#else>
        <#assign row = "even">
    </#if>
    </#list>
    </table>

    <#assign bitstreams = myBun.getBitstreams()>
    <#list bitstreams as myBits>
    <#if bitstream_policies??>
    <#assign myPolicies = bitstream_policies.get(new Integer(myBits.getID()))>
    </#if>
    <#assign showingArgs=[myBits.getID(), myBits.getName()]>
    <p><@dspace.messageArgs "ui.dspace-admin.authorize-item-edit.bundle", showingArgs /></p>
    <form method="post" action="<@dspace.url "/admin/authorize" />">
        <p align="center">
           <input type="hidden" name="item_id" value="<#if item??>${item.getID()}</#if>" />
           <input type="hidden" name="bitstream_id" value="${myBits.getID()}" />
           <input type="submit" name="submit_bitstream_add_policy" value="<@dspace.message "ui.dspace-admin.general.addpolicy" />" />
        </p>
    </form>
    <table class="miscTable" align="center" summary="This table displays the bitstream data">
    <tr>
        <th class="oddRowOddCol"><strong><@dspace.message "ui.general.id" /></strong></th>
        <th class="oddRowEvenCol"><strong><@dspace.message "ui.dspace-admin.general.action" /></strong></th>
        <th class="oddRowOddCol"><strong><@dspace.message "ui.dspace-admin.authorize-item-edit.eperson" /></strong></th>
        <th class="oddRowEvenCol"><strong><@dspace.message "ui.dspace-admin.general.group" /></strong></th>
        <th class="oddRowOddCol">&nbsp;</th>
    </tr>
    <#assign row = "even">
    <#list myPolicies as rp>

    <tr>
            <td class="${row}RowOddCol">${rp.getID()}</td>
            <td class="${row}RowEvenCol">
                    ${rp.getActionText()}
            </td>
            <td class="${row}RowOddCol">
                <#if rp.getEPerson()??>
                    ${rp.getEPerson().getEmail()}
                <#else>
                    ${"..."}
                </#if>
            </td>
            <td class="${row}RowEvenCol">
                <#if rp.getGroup()??>
                    ${rp.getGroup().getName()}
                <#else>
                    ${"..."}
                </#if>
            </td>
            <td class="${row}RowOddCol">
                <form method="post" action="">
                    <input type="hidden" name="policy_id" value="${rp.getID()}" />
                    <input type="hidden" name="item_id" value="<#if item??>${item.getID()}</#if>" />
                    <input type="hidden" name="bitstream_id" value="${myBits.getID()}" />
                    <input type="submit" name="submit_item_edit_policy" value="<@dspace.message "ui.dspace-admin.general.edit" />" />
                    <input type="submit" name="submit_item_delete_policy" value="<@dspace.message "ui.dspace-admin.general.delete" />" />
                 </form>
            </td>
      </tr>
    <#if row == "even">
        <#assign row = "odd">
    <#else>
        <#assign row = "even">
    </#if>
    </#list>
    </table>
    </#list>
    </#if>
    </#list>
    </#if>










