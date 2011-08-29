<#import "/includes/dspace.ftl" as dspace />

<head>
    <script type="text/javascript" src="<@dspace.url "/utils.js" />"></script>
</head>

<#if permission??>
    <#if permission = 10>
    <h1><@dspace.message "ui.dspace-admin.wizard-permissions.heading1" /></h1>
    <p><@dspace.message "ui.dspace-admin.wizard-permissions.text1" /></p>
        <#elseif permission = 11>
        <h1><@dspace.message "ui.dspace-admin.wizard-permissions.heading2" /></h1>
        <p><@dspace.message "ui.dspace-admin.wizard-permissions.text2" /></p>
        <#elseif permission = 11>
        <h1><@dspace.message "ui.dspace-admin.wizard-permissions.heading2" /></h1>
        <p><@dspace.message "ui.dspace-admin.wizard-permissions.text2" /></p>
        <#elseif permission = 12>
        <h1><@dspace.message "ui.dspace-admin.wizard-permissions.heading3" /></h1>
        <p><@dspace.message "ui.dspace-admin.wizard-permissions.text3" /></p>
        <#elseif permission = 13>
        <h1><@dspace.message "ui.dspace-admin.wizard-permissions.heading4" /></h1>
        <p><@dspace.message "ui.dspace-admin.wizard-permissions.text4" /></p>
        <#elseif permission = 14>
        <h1><@dspace.message "ui.dspace-admin.wizard-permissions.heading5" /></h1>
        <p><@dspace.message "ui.dspace-admin.wizard-permissions.text5" /></p>
        <#elseif permission = 15>
        <h1><@dspace.message "ui.dspace-admin.wizard-permissions.heading6" /></h1>
        <p><@dspace.message "ui.dspace-admin.wizard-permissions.text6" /></p>
    </#if>
</#if>

<div>
    <a href="<@dspace.url "/help/site-admin.html#wizard_permissions" />"
       onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.morehelp" /></a>
</div>

<p><@dspace.message "ui.dspace-admin.wizard-permissions.change" /></p>

<form name="epersongroup" action="<@dspace.url "/tools/collection-wizard"/>" method="post">
    <center>
        <table>
        <#if mitgroup?? && permission?? && mitgroup==true && (permission==10||permission==11)>
            <tr>
                <td></td>
                <td><input type="checkbox" name="mitgroup" value="true"/>&nbsp;<span class="submitFormLabel">
                <@dspace.message "ui.dspace-admin.wizard-permissions.mit" /></span>
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <%--
                <td colspan="2" class="submitFormHelp"><strong>OR</strong></td>
                --%>
                <td colspan="2" class="submitFormHelp"><strong>
                <@dspace.message "ui.dspace-admin.wizard-permissions.or" />
                </strong></td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
        </#if>
            <tr>
                <td colspan="2">
                    <table align="center" width="80%">
                        <tr>
                            <td class="submitFormHelp">
                            <@dspace.message "ui.dspace-admin.wizard-permissions.click" />
                                <br/>
                            <#assign multiple = true>
                                <table>
                                <tr>
                                    <td colspan="2" align="center"><select multiple="multiple" name="eperson_id"
                                                                           <#if multiple?? && multiple==true>size="10"
                                                                           <#else>size="1"</#if>></select></td>

                                <#if multiple?? && multiple==true>
                                </tr>
                                <tr>
                                    <td width="50%" align="center">
                                        <input type="button"
                                               value="<@dspace.message "ui.dspace.app.webui.SelectEPersonTag.selectPeople" />"
                                               onclick="javascript:popup_window('<@dspace.url "/admin/eperson/browse-epeople?multiple=true" />', 'eperson_popup');"/>
                                    </td>

                                <td width="50%" align="center">
                                    <input type="button"
                                           value="<@dspace.message "ui.dspace.app.webui.SelectEPersonTag.removeSelected" />"
                                           onclick="javascript:removeSelected(window.document.epersongroup.eperson_id);"/>
                                    <#else>
                                    <td><input type="button"
                                               value="<@dspace.message "ui.dspace.app.webui.SelectEPersonTag.selectPerson" />"
                                               onclick="javascript:popup_window('<@dspace.url "/admin/eperson/browse-epeople?multiple=false" />', 'eperson_popup');"/>
                                </#if>
                                </td></tr>
                                </table>
                            </td>
                            <td>&nbsp;</td>
                            <td class="submitFormHelp">
                            <@dspace.message "ui.dspace-admin.wizard-permissions.click2" />
                                <br/>
                            <#assign multiple = true>
                                <table>
                                <tr>
                                    <td colspan="2" align="center"><select multiple="multiple" name="group_ids"
                                                                           <#if multiple?? && multiple==true>size="10"
                                                                           <#else>size="1"</#if>>
                                    </select></td>

                                <#if multiple?? && multiple==true>
                                </tr>
                                <tr>
                                    <td width="50%" align="center">
                                        <input type="button"
                                               value="<@dspace.message "ui.dspace.app.webui.SelectGroupTag.selectGroups" />"
                                               onclick="javascript:popup_window('<@dspace.url "/admin/group/group-select-list?multiple=true" />', 'group_popup');"/>
                                    </td>

                                <td width="50%" align="center">
                                    <input type="button"
                                           value="<@dspace.message "ui.dspace.app.webui.SelectGroupTag.removeSelected" />"
                                           onclick="javascript:removeSelected(window.document.epersongroup.group_ids);"/>
                                    <#else>
                                    <td><input type="button"
                                               value="<@dspace.message "ui.dspace.app.webui.SelectGroupTag.selectGroup" />"
                                               onclick="javascript:popup_window('<@dspace.url "/admin/group/group-select?multiple=false" />', 'group_popup');"/>
                                </#if>
                                </td></tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </center>

    <input type="hidden" name="collection_id" value="<#if collection??>${collection.getID()}</#if>" />
        <input type="hidden" name="stage" value="3" />
        <input type="hidden" name="permission" value="<#if permission??>${permission}</#if>" />

        <center>
            <table border="0" width="80%">
                <tr>
                    <td width="100%">&nbsp;

                    </td>
                    <td>
                        <input type="submit" name="submit_next" value="<@dspace.message "ui.dspace-admin.general.next.button" />" onclick="javascript:finishEPerson();finishGroups();"/>
                    </td>
                </tr>
            </table>
        </center>
    </form>






