<#import "/includes/dspace.ftl" as dspace />

<head>
    <script type="text/javascript" src="<@dspace.url "/utils.js" />"></script>
</head>

<#if collection??>
    <#assign showingArgs=[collection.getHandle()]>
<h1><@dspace.messageArgs "ui.tools.edit-collection.heading2", showingArgs /></h1>
    <#else>
    <@dspace.message "ui.tools.edit-collection.heading1" />
</#if>

<div align="center">
    <table width="70%">
        <tr>
            <td class="standard">
            <#if delete_button??>
                <form method="post" action="<@dspace.url "/admin/editcommunities"/>">
                    <#if community??><input type="hidden" name="community_id" value="${community.getID()}"/></#if>
                    <#if collection??><input type="hidden" name="collection_id" value="${collection.getID()}"/></#if>
                    <input type="submit" name="submit_delete_collection"
                           value="<@dspace.message "ui.tools.edit-collection.button.delete" />"/>
                </form>
                <#else>
                    &nbsp;
            </#if>
            </td>
            <td align="right" class="standard">
                <a href="<@dspace.url "/help/site-admin.html#editcollection" />"
                   onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
            </td>
        </tr>
    </table>
</div>

<form method="post" action="<@dspace.url "/admin/editcommunities"/>">
<#if community??><input type="hidden" name="community_id" value="${community.getID()}"/></#if>
<#if collection??><input type="hidden" name="collection_id" value="${collection.getID()}"/></#if>
<table>
<%-- ===========================================================
Basic metadata
=========================================================== --%>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label1" /></td>
    <td><input type="text" name="name" value="<#if name??>${name}</#if>" size="50"/></td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label2" /></td>
    <td>
        <input type="text" name="short_description" value="<#if shortDesc??>${shortDesc}</#if>" size="50"/>
    </td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label3" /></td>
    <td>
        <textarea name="introductory_text" rows="6" cols="50"><#if intro??>${intro}</#if></textarea>
    </td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label4" /></td>
    <td>
        <textarea name="copyright_text" rows="6" cols="50"><#if copy??>${copy}</#if></textarea>
    </td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label5" /></td>
    <td>
        <textarea name="side_bar_text" rows="6" cols="50"><#if side??>${side}</#if></textarea>
    </td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label6" /></td>
    <td>
        <textarea name="license" rows="6" cols="50"><#if license??>${license}</#if></textarea>
    </td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label7" /></td>
    <td>
        <textarea name="provenance_description" rows="6" cols="50"><#if provenance??>${provenance}</#if></textarea>
    </td>
</tr>
<%-- ===========================================================
Logo
=========================================================== --%>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label8" /></td>
    <td>
    <#if logo??>
        <table>
            <tr>
                <td>
                    <img src="<@dspace.url "/retrieve/${logo.getID()}"/>" alt="collection logo"/>
                </td>
                <td>
                    <input type="submit" name="submit_collection_set_logo"
                           value="<@dspace.message "ui.tools.edit-collection.form.button.add-logo" />"/><br/><br/>
                    <input type="submit" name="submit_collection_delete_logo"
                           value="<@dspace.message "ui.tools.edit-collection.form.button.delete-logo" />"/>
                </td>
            </tr>
        </table>
        <#else>

            <input type="submit" name="submit_collection_set_logo"
                   value="<@dspace.message "ui.tools.edit-collection.form.button.set-logo" />"/>
    </#if>
    </td>
</tr>

<tr>
    <td>&nbsp;</td>
</tr>
<#if (submitters_button?? && submitters_button==true)|| (workflows_button?? && workflows_button==true) || (admin_create_button?? && admin_create_button==true)||(admins?? && admin_remove_button?? && admin_remove_button==true)>
<tr>
    <td colspan="2">
        <div align="center"><h3><@dspace.message "ui.tools.edit-collection.form.label9" /></h3></div>
    </td>
</tr>
</#if>

<#if submitters_button?? && submitters_button==true>
<%-- ===========================================================
Collection Submitters
=========================================================== --%>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label10" /></td>
    <td>
        <#if submitters??>
            <a href="<@dspace.url "/admin/editcommunities/colsubedit/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.edit" /></a>
            <a href="<@dspace.url "/admin/editcommunities/colsubdelete/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.delete" /></a>
            <#else>
                <a href="<@dspace.url "/admin/editcommunities/colsubcreate/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.create" /></a>
        </#if>
    </td>
</tr>
</#if>

<#if workflows_button??>
<%-- ===========================================================
Workflow groups
=========================================================== --%>

    <#assign roleTexts = ["Accept/Reject", "Accept/Reject/Edit Metadata", "Edit Metadata"]>
    <#list roleTexts as rT>
    <tr>


        <td class="submitFormLabel"><em>${rT}</em><br/><@dspace.message "ui.tools.edit-collection.form.label11" /></td>
        <td>
            <#if wfGroups[rT_index]??>
                <a href="<@dspace.url "/admin/editcommunities/wfgroupsedit/${rT_index+1}/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.edit" /></a>
                <a href="<@dspace.url "/admin/editcommunities/wfgroupsdelete/${rT_index+1}/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.delete" /></a>
                <#else>
                    <a href="<@dspace.url "/admin/editcommunities/wfgroupscreate/${rT_index+1}/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.create" /></a>
            </#if>
        </td>
    </tr>
    </#list>
</#if>

<tr>
    <td>&nbsp;</td>
</tr>

<#if (admin_create_button?? && admin_create_button==true)|| (admins?? && admin_remove_button?? && admin_remove_button==true)>
<%-- ===========================================================
Collection Administrators
=========================================================== --%>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label12" /></td>
    <td>
        <#if admins??>
            <#if admin_create_button?? && admin_create_button==true>
                <a href="<@dspace.url "/admin/editcommunities/admincoledit/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.edit" /></a>
            </#if>
            <#if admin_remove_button?? && admin_remove_button==true>
                <a href="<@dspace.url "/admin/editcommunities/admincoldelete/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.delete" /></a>
            </#if>
            <#else>
                <#if admin_create_button?? && admin_create_button==true>
                <a href="<@dspace.url "/admin/editcommunities/admincolcreate/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.create" /></a>
                </#if>
        </#if>
    </td>
</tr>
</#if>

<#if template_button?? && template_button==true>
<%-- ===========================================================
Item template
=========================================================== --%>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label13" /></td>
    <td>
        <#if template??>
            <a href="<@dspace.url "/admin/editcommunities/edittemplate/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.edit" /></a>
            <a href="<@dspace.url "/admin/editcommunities/deletetemplate/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.delete" /></a>
            <#else>
                <a href="<@dspace.url "/admin/editcommunities/createtemplate/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.create" /></a>
        </#if></td>
</tr></#if>

<#if policy_button?? && policy_button==true>
<%-- ===========================================================
Edit collection's policies
=========================================================== --%>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label14" /></td>
    <td>
        <a href="<@dspace.url "/admin/editcommunities/editauthorization/${community.getID()}/${collection.getID()}" />"><@dspace.message "ui.tools.edit-collection.form.button.edit" /></a>
        </td>
</tr>
</#if>


<#if admin_collection?? && admin_collection==true>

<%-- ===========================================================
Harvesting Settings
=========================================================== --%>

<tr>
    <td>&nbsp;</td>
</tr>
<tr>
    <td colspan="2">
        <div align="center"><h3><@dspace.message "ui.tools.edit-collection.form.label15" /></h3></div>
    </td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label16" /></td>
    <td>
        <input type="radio" value="source_normal"
               <#if harvestLevelValue?? && harvestLevelValue=0>checked="checked"</#if>
               name="source"/><@dspace.message "ui.tools.edit-collection.form.label17" /><br/>
        <input type="radio" value="source_harvested"
               <#if harvestLevelValue?? && harvestLevelValue &gt;0>checked="checked"</#if>
               name="source"/><@dspace.message "ui.tools.edit-collection.form.label18" /><br/>
    </td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label19" /></td>
    <td><input type="text" name="oai_provider" value="<#if oaiProviderValue??>${oaiProviderValue}</#if>" size="50"/>
    </td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label20" /></td>
    <td><input type="text" name="oai_setid" value="<#if oaiSetIdValue??>${oaiSetIdValue}</#if>" size="50"/></td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label21" /></td>
    <td>
        <select name="metadata_format">
            <#assign metaString = "harvester.oai.metadataformats.">
            <#if pe??>
                <#list pe as key>
                    <#if key?? && key.startsWith(metaString)>
                        <#assign metadataKey = key?substring(metaString?length)>
                        <#assign label = "ui.tools.edit-collection.form.label21.select." + metadataKey>
                        <option value="${metadataKey}"
                            <#if metadataFormatValue?? && (metadataKey?upper_case=metadataFormatValue?upper_case)>
                                selected="selected" </#if>>
                        <@dspace.message "${label}" /></option>


                    </#if>
                </#list>
            </#if>
        </select>
    </td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label22" /></td>
    <td>
        <input type="radio" value="1" <#if harvestLevelValue?? && harvestLevelValue!=2>checked="checked"</#if>
               name="harvest_level"/><@dspace.message "ui.tools.edit-collection.form.label23" /><br/>
        <input type="radio" value="2" <#if harvestLevelValue?? && harvestLevelValue==2>checked="checked"</#if>
               name="harvest_level"/><@dspace.message "ui.tools.edit-collection.form.label24" /><br/>
        <input type="radio" value="3" <#if harvestLevelValue?? && harvestLevelValue==3>checked="checked"</#if>
               name="harvest_level"/><@dspace.message "ui.tools.edit-collection.form.label25" /><br/>
    </td>
</tr>
<tr>
    <td class="submitFormLabel"><@dspace.message "ui.tools.edit-collection.form.label26" /></td>
    <td><#if lastHarvestMsg??>${lastHarvestMsg}</#if></td>
</tr>

</#if>

</table>

<p>&nbsp;</p>

<div align="center">
    <table width="70%">
        <tr>
            <td class="standard">

            <#if collection??>
                <input type="hidden" name="create" value="false"/>
                <input type="submit" name="submit_confirm_edit_collection"
                       value="<@dspace.message "ui.tools.edit-collection.form.button.update" />"/>

                <#else>

                    <input type="hidden" name="community_id" value="<#if community??>${community.getID()}</#if>"/>
                    <input type="hidden" name="create" value="true"/>
                    <input type="submit" name="submit_confirm_edit_collection"
                           value="<@dspace.message "ui.tools.edit-collection.form.button.create2" />"/>

            </#if>

            </td>
            <td>
                <input type="hidden" name="community_id" value="<#if community??>${community.getID()}</#if>"/>
                <input type="submit" name="submit_cancel"
                       value="<@dspace.message "ui.tools.edit-collection.form.button.cancel" />"/>
            </td>
        </tr>
    </table>
</div>
</form>

