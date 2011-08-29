<#import "/includes/dspace.ftl" as dspace />

<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>

<table width="95%">
    <tr>
      <td align="left">
      <#if community??>
      <#assign showingArgs=[community.getHandle()]>
      <h1><@dspace.messageArgs "ui.tools.edit-community.heading2", showingArgs /></h1>

      <#if delete_button?? && delete_button==true>
      <div align="center">
        <table width="70%">
          <tr>
            <td class="standard">
              <form method="post" action="">
                <input type="hidden" name="community_id" value="${community.getID()}" />
                <input type="submit" name="submit_delete_community" value="<@dspace.message "ui.tools.edit-community.button.delete" />" />
              </form>
            </td>
          </tr>
        </table>
      </div>
    </#if>
      <#else>
      <h1><@dspace.message "ui.tools.edit-community.heading1" /></h1>
      </#if>
      </td>
      <td align="right" class="standard">
        <a href="<@dspace.url "/help/site-admin.html#editcommunity" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
      </td>
    </tr>
  </table>

    <form method="post" action="<@dspace.url "/admin/editcommunities"/>">
        <table>
<%-- ===========================================================
     Basic metadata
     =========================================================== --%>
            <tr>
                <td class="submitFormLabel"><@dspace.message "ui.tools.edit-community.form.label1" /></td>
                <td><input type="text" name="name" value="<#if name??>${name}</#if>" size="50" /></td>
            </tr>
            <tr>
                <td class="submitFormLabel"><@dspace.message "ui.tools.edit-community.form.label2" /></td>
                <td>
                    <input type="text" name="short_description" value="<#if shortDesc??>${shortDesc}</#if>" size="50" />
                </td>
            </tr>
            <tr>
                <td class="submitFormLabel"><@dspace.message "ui.tools.edit-community.form.label3" /></td>
                <td>
                    <textarea name="introductory_text" rows="6" cols="50"><#if intro??>${intro}</#if></textarea>
                </td>
            </tr>
            <tr>
                <td class="submitFormLabel"><@dspace.message "ui.tools.edit-community.form.label4" /></td>
                <td>
                    <textarea name="copyright_text" rows="6" cols="50"><#if copy??>${copy}</#if></textarea>
                </td>
            </tr>
            <tr>
                <td class="submitFormLabel"><@dspace.message "ui.tools.edit-community.form.label5" /></td>
                <td>
                    <textarea name="side_bar_text" rows="6" cols="50"><#if side??>${side}</#if></textarea>
                </td>
            </tr>
<%-- ===========================================================
     Logo
     =========================================================== --%>
            <tr>
                <td class="submitFormLabel"><@dspace.message "ui.tools.edit-community.form.label6" /></td>
                <td>
<#if logo??>
                    <table>
                        <tr>
                            <td>
                                <img src="<@dspace.url "/retrieve/${logo.getID()}"/>" alt="logo" />
                            </td>
                            <td>
                                <input type="submit" name="submit_community_set_logo" value="<@dspace.message "ui.tools.edit-community.form.button.add-logo" />" /><br/><br/>
                                <input type="submit" name="submit_community_delete_logo" value="<@dspace.message "ui.tools.edit-community.form.button.delete-logo" />" />
                            </td>
                        </tr>
                    </table>
<#else>
                    <input type="submit" name="submit_community_set_logo" value="<@dspace.message "ui.tools.edit-community.form.button.set-logo" />" />
</#if>
                </td>
            </tr>
    <#if (admin_create_button?? && admin_create_button==true)||(admins?? && admin_remove_button?? && admin_remove_button==true)>
 <%-- ===========================================================
     Community Administrators
     =========================================================== --%>
            <tr>
                <td class="submitFormLabel"><@dspace.message "ui.tools.edit-community.form.label8" /></td>
                <td>
                    <#if admins??>
                    <#if admin_create_button?? && admin_create_button==true>
                    <input type="submit" name="submit_admins_community_edit" value="<@dspace.message "ui.tools.edit-community.form.button.edit" />" />
                    </#if>
                    <#if admin_remove_button?? && admin_remove_button==true>
                    <input type="submit" name="submit_admins_community_remove" value="<@dspace.message "ui.tools.edit-community.form.button.remove" />" />
                    </#if>
                    <#else>
                    <input type="submit" name="submit_admins_community_create" value="<@dspace.message "ui.tools.edit-community.form.button.create" />" />
                    </#if>
                </td>
            </tr>
    </#if>
    <#if policy_button?? && policy_button==true>
    <%-- ===========================================================
     Edit community's policies
     =========================================================== --%>
    <tr>
         <td class="submitFormLabel"><@dspace.message "ui.tools.edit-community.form.label7" /></td>
         <td>
         <input type="submit" name="submit_community_authorization_edit" value="<@dspace.message "ui.tools.edit-community.form.button.edit" />" />
         </td>
    </tr>
    </#if>
    </table>

        <p>&nbsp;</p>

        <div align="left">
            <table width="70%">
                <tr>
                    <td class="standard">

<#if community??>
                       <input type="hidden" name="community_id" value="${community.getID()}" />
                       <input type="hidden" name="create" value="false" />
                       <input type="submit" name="submit_confirm_edit_community" value="<@dspace.message "ui.tools.edit-community.form.button.update" />" />
                    </td>
                    <td>
                       <input type="hidden" name="community_id" value="${community.getID()}" />
                       <input type="submit" name="submit_cancel" value="<@dspace.message "ui.tools.edit-community.form.button.cancel" />" />

<#else>
                    <input type="hidden" name="parent_community_id" value="<#if parentID??>${parentID}</#if>" />
                    <input type="hidden" name="create" value="true" />
                    <input type="submit" name="submit_confirm_edit_community" value="<@dspace.message "ui.tools.edit-community.form.button.create" />" />
                    </td>
                    <td>
                    <input type="hidden" name="parent_community_id" value="<#if parentID??>${parentID}</#if>" />
                    <input type="submit" name="submit_cancel" value="<@dspace.message "ui.tools.edit-community.form.button.cancel" />" />

</#if>
                    </td>
                </tr>
            </table>
        </div>
    </form>

