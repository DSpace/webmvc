<#import "/includes/dspace.ftl" as dspace />

<html>
 <head>
        <%-- <title>Select Groups</title> --%>
        <title><@dspace.message "ui.tools.group-select-list.title" /></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<script type="text/javascript">
<!-- Begin

// Add the selected items to main group list by calling method of parent
function addGroup(id, name)
{
	self.opener.addGroup(id, name);
}

// Clear selected items from main group list
function clearGroups()
{
	var list = self.opener.document.epersongroup.group_ids;
	while (list.options.length > 0)
	{
		list.options[0] = null;
	}
}

// End -->
</script></head>

<body class="pageContents">

       <#if first?? && last?? && groups??>
       <#assign showingArgs=["${first+1}","${last+1}","${groups?size}"]>
       <h3><@dspace.messageArgs "ui.tools.group-select-list.heading", showingArgs/></h3>
       </#if>

       <#if multiple?? && multiple==true>
          <p class="submitFormHelp"><@dspace.message "ui.tools.group-select-list.info1" /></p>
       </#if>

<%-- Controls for jumping around list--%>
	<table width="99%">
		<tr>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}0"><@dspace.message "ui.tools.group-select-list.jump.first" /></a></strong></small></td>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpFiveBack}"><@dspace.message "ui.tools.group-select-list.jump.five-back" /></a></strong></small></td>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpOneBack}"><@dspace.message "ui.tools.group-select-list.jump.one-back" /></a></strong></small></td>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpOneForward}"><@dspace.message "ui.tools.group-select-list.jump.one-forward" /></a></strong></small></td>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpFiveForward}"><@dspace.message "ui.tools.group-select-list.jump.five-forward" /></a></strong></small></td>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpEnd}"><@dspace.message "ui.tools.group-select-list.jump.last" /></a></strong></small></td>
		</tr>
	</table>
<br/>

<form method="get" action="">

    <table class="miscTable" align="center" summary="Group list">

        <tr>
            <th id="t1" class="oddRowOddCol">&nbsp;</th>
            <th id="t2" class="oddRowEvenCol">
               <#if sortBy?? && group?? && sortBy==group.ID>
                <strong><@dspace.message "ui.tools.group-select-list.th.id.sortedby" /></strong>
                <#else>
                <strong><a href="${sortLink}id"><@dspace.message "ui.tools.group-select-list.th.id" /></a></strong>
               </#if>
               </th>
               <th id="t3" class="oddRowOddCol">
               <#if sortBy?? && group?? && sortBy == group.NAME>
                <strong><@dspace.message "ui.tools.group-select-list.th.name.sortedby" /></strong>
                <#else>
                    <a href="${sortLink}name"><@dspace.message "ui.tools.group-select-list.th.name" /></a>
               </#if>
               </th>
        </tr>

        <#assign row = "even">
        <#if multiple?? && multiple == true>
        <#assign clearList = "">
        <#assign closeWindow = "">
        <#else>
        <#assign clearList = "clearGroups();">
        <#assign closeWindow = "window.close();">
        </#if>

        <#if groups??>
        <#list groups as gm>

        <#assign fullname = "${gm.getName()?replace('\"','')}">

        <tr>
			<td headers="t1" class="${row}RowOddCol">
            <input type="button" value="<#if multiple?? && multiple==true>
            <@dspace.message "ui.tools.general.add" />
            <#else>
            <@dspace.message "ui.tools.general.select" />
            </#if>" onclick="javascript:${clearList}addGroup('${gm.getID()}', '${fullname}');${closeWindow}"/>
            </td>
            <td headers="t2" class="${row}RowEvenCol">${gm.getID()}</td>
			<td headers="t3" class="${row}RowOddCol"> ${fullname}</td>
        </tr>
        <#if row == "odd">
        <#assign row = "even">
        <#else>
        <#assign row = "odd"></#if>
        </#list>
        </#if>

    </table>
    <table width="99%">
		<tr>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}0"><@dspace.message "ui.tools.group-select-list.jump.first" /></a></strong></small></td>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpFiveBack}"><@dspace.message "ui.tools.group-select-list.jump.five-back" /></a></strong></small></td>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpOneBack}"><@dspace.message "ui.tools.group-select-list.jump.one-back" /></a></strong></small></td>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpOneForward}"><@dspace.message "ui.tools.group-select-list.jump.one-forward" /></a></strong></small></td>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpFiveForward}"><@dspace.message "ui.tools.group-select-list.jump.five-forward" /></a></strong></small></td>
			<td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpEnd}"><@dspace.message "ui.tools.group-select-list.jump.last" /></a></strong></small></td>
		</tr>
	</table>
    <p align="center"><input type="button" value="<@dspace.message "ui.tools.group-select-list.close.button" />" onclick="window.close();"/></p>

</form>

</body>
</html>