<#import "/includes/dspace.ftl" as dspace />

<head>
    <script type="text/javascript" src="<@dspace.url "/utils.js" />"></script>
</head>

<#if sysadmin_button??>
    <#else>
        <#assign sysadmin_button = false>
</#if>

<#if admin_create_button??>
    <#else>
        <#assign admin_create_button = false>
</#if>

<#if workflows_button??>
    <#else>
        <#assign workflows_button = false>
</#if>

<#if submitters_button??>
    <#else>
        <#assign submitters_button = false>
</#if>

<#if template_button??>
    <#else>
        <#assign template_button = false>
</#if>

<h1><@dspace.message "ui.dspace-admin.wizard-questions.title" /></h1>

<form action="<@dspace.url "/tools/collection-wizard"/>" method="post">

    <div><@dspace.message "ui.dspace-admin.wizard-questions.text" />
        <a href="<@dspace.url "/help/site-admin.html#createcollection" />"
           onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.morehelp" /></a>
    </div>
    <center>
        <table class="miscTable">
            <tr class="oddRowOddCol">
                <td class="oddRowOddCol" align="left">
                    <table border="0">
                        <tr>
                            <td valign="top">
                            <#if sysadmin_button?? && sysadmin_button==false>
                                <input type="hidden" name="public_read" value="true"/>
                                <input type="checkbox" name="public_read" value="true" disabled="disabled"
                                       checked="checked"/>
                                <#else>
                                    <input type="checkbox" name="public_read" value="true" checked="checked"/>
                            </#if>
                            </td>
                            <td class="submitFormLabel" nowrap="nowrap">
                            <@dspace.message "ui.dspace-admin.wizard-questions.check1" />
                            <#if sysadmin_button?? && sysadmin_button==false>
                                <@dspace.message "ui.dspace-admin.wizard-questions.check1-disabled" />
                                </#if>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="evenRowOddCol">
                <td class="evenRowOddCol" align="left">
                    <table border="0">
                        <tr>
                            <td valign="top">
                            <#if submitters_button?? && submitters_button==false>
                                <input type="hidden" name="submitters" value="false"/>
                                <input type="checkbox" name="submitters" value="true" disabled="disabled"/>
                                <#else>
                                    <input type="checkbox" name="submitters" value="true" checked="checked"/>
                            </#if>
                            </td>
                            <td class="submitFormLabel" nowrap="nowrap">
                            <@dspace.message "ui.dspace-admin.wizard-questions.check2" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="oddRowOddCol">
                <td class="oddRowOddCol" align="left">
                    <table border="0">
                        <tr>
                            <td valign="top">
                            <#if workflows_button?? && workflows_button==false>
                                <input type="hidden" name="workflow1" value="false"/>
                                <input type="checkbox" name="workflow1" value="true" disabled="disabled"/>
                                <#else>
                                    <input type="checkbox" name="workflow1" value="true"/>
                            </#if>
                            </td>
                            <td class="submitFormLabel" nowrap="nowrap">
                            <@dspace.message "ui.dspace-admin.wizard-questions.check3" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="evenRowOddCol">
                <td class="evenRowOddCol" align="left">
                    <table border="0">
                        <tr>
                            <td valign="top">
                            <#if workflows_button?? && workflows_button==false><input type="hidden" name="workflow2"
                                                                                      value="false"/>
                                <input type="checkbox" name="workflow2" value="true" disabled="disabled"/>
                                <#else>
                                    <input type="checkbox" name="workflow2" value="true"/>
                            </#if>
                            </td>
                            <td class="submitFormLabel" nowrap="nowrap">
                            <@dspace.message "ui.dspace-admin.wizard-questions.check4" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="oddRowOddCol">
                <td class="oddRowOddCol" align="left">
                    <table border="0">
                        <tr>
                            <td valign="top">
                            <#if workflows_button?? && workflows_button==false><input type="hidden" name="workflow3"
                                                                                      value="false"/>
                                <input type="checkbox" name="workflow3" value="true" disabled="disabled"/>
                                <#else>
                                    <input type="checkbox" name="workflow3" value="true"/>
                            </#if>
                            </td>
                            <td class="submitFormLabel" nowrap="nowrap">
                            <@dspace.message "ui.dspace-admin.wizard-questions.check5" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="evenRowOddCol">
                <td class="evenRowOddCol" align="left">
                    <table border="0">
                        <tr>
                            <td valign="top">
                            <#if admin_create_button?? && admin_create_button==false>
                                <input type="hidden" name="admins" value="false"/>
                                <input type="checkbox" name="admins" value="true" disabled="disabled"/>
                                <#else>
                                    <input type="checkbox" name="admins" value="true"/>
                            </#if>
                            </td>
                            <td class="submitFormLabel" nowrap="nowrap">
                            <@dspace.message "ui.dspace-admin.wizard-questions.check6" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr class="oddRowOddCol">
                <td class="oddRowOddCol" align="left">
                    <table border="0">
                        <tr>
                        <td valign="top">
                        <#if template_button?? && template_button==false>
                            <input type="hidden" name="default.item" value="false"/>
                            <input type="checkbox" name="default.item" value="true" disabled="disabled"/>
                            <#else>
                                <input type="checkbox" name="default.item" value="true"/></td>
                        </#if>
                            <td class="submitFormLabel" nowrap="nowrap">
                            <@dspace.message "ui.dspace-admin.wizard-questions.check7" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </center>

    <p>&nbsp;</p>

    <input type="hidden" name="collection_id" value="<#if collection??>${collection.getID()}</#if>"/>
    <input type="hidden" name="stage" value="1"/>

    <center>
        <table border="0" width="80%">
            <tr>
                <td width="100%">&nbsp;

                </td>
                <td>
                    <input type="submit" name="submit_next" value="<@dspace.message "ui.dspace-admin.general.next.button" />"/>
                </td>
            </tr>
        </table>
    </center>
</form>