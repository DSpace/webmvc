<#import "/includes/dspace.ftl" as dspace />

<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>

<table width="95%">
    <tr>
      <td align="left">
    <%--  <h1>Group Editor</h1> --%>
    <h1><@dspace.message "ui.tools.group-list.title" /></h1>
      </td>
      <td align="right" class="standard">
        <a href="<@dspace.url "/help/site-admin.html#groups" />" onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.help" /></a>
      </td>
    </tr>
  </table>

	<p><@dspace.message "ui.tools.group-list.note1" /></p>
	<p><@dspace.message "ui.tools.group-list.note2" /></p>

   <form method="post" action="<@dspace.url "/admin/group" />">
        <p align="center">
	    <input type="submit" name="submit_add" value="<@dspace.message "ui.tools.group-list.create.button" />" />
        </p>
    </form>

<table class="miscTable" align="center" summary="Group data display table">

        <tr>
            <th class="oddRowOddCol"><strong><@dspace.message "ui.tools.group-list.id" /></strong></th>
			<th class="oddRowEvenCol"><strong><@dspace.message "ui.tools.group-list.name" /></strong></th>
            <th class="oddRowOddCol">&nbsp;</th>
            <th class="oddRowEvenCol">&nbsp;</th>
        </tr>

    <#assign row="even">

    <#if groups??>

    <#list groups as group>

    <tr>
        <td class="${row}RowOddCol">${group.getID()}</td>
        <td class="${row}RowEvenCol">${group.getName()}</td>
        <td class="${row}RowOddCol">
            <#if group.getID()&gt;0>
            <form method="post" action="<@dspace.url "/admin/group" />">
                <input type="hidden" name="group_id" value="${group.getID()}"/>
  		        <input type="submit" name="submit_edit" value="<@dspace.message "ui.tools.general.edit" />" />
            </form>
            </#if>
        </td>
        <td class="${row}RowEvenCol">
            <#if group.getID()&gt;1>
            <form method="post" action="<@dspace.url "/admin/group" />">
                <input type="hidden" name="group_id" value="${group.getID()}"/>
  		        <input type="submit" name="submit_group_delete" value="<@dspace.message "ui.tools.general.delete" />" />
            </form>
            </#if>
        </td>
    </tr>

    <#if row == "odd">
    <#assign row="even">
    <#else>
    <#assign row="odd">
    </#if>
    </#list>

    </#if>


</table>