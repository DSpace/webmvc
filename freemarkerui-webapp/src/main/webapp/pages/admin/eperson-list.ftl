<#import "/includes/dspace.ftl" as dspace />

<html>
   <head>
        <title><@dspace.message "ui.tools.eperson-list.title" /></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<script type="text/javascript">
<!-- Begin
// Add the selected items to main e-people list by calling method of parent
function addEPerson(id, email, name)
{
    self.opener.addEPerson(id, email, name);
}

// Clear selected items from main e-people list
function clearEPeople()
{

    var list = self.opener.document.epersongroup.eperson_id;
	while (list.options.length > 0)
	{
		list.options[0] = null;
	}
}
// End -->
</script>

   </head>
   <body class="pageContents">


       <#if search?? && search != "">
       <#if offset?? && last?? && epeoplelength??>
       <#assign showingArgs=["${offset+1}","${last+1}","${epeoplelength}"]>
       </#if>
       <#else>
       <#if first?? && last?? && epeoplelength??>
       <#assign showingArgs=["${first+1}","${last+1}","${epeoplelength}"]>
       </#if>
       </#if>
       <h3><@dspace.messageArgs "ui.tools.eperson-list.heading", showingArgs/></h3>



   <#if multiple??>
   <p class="submitFormHelp"><@dspace.message "ui.tools.eperson-list.info1" /></p>
   </#if>

   <form method="get">

       <#if first??><input type="hidden" name="first" value="${first}" /></#if>
       <#if sortBy??><input type="hidden" name="sortBy" value="${sortBy}" /></#if>
       <#if multiple??><input type="hidden" name="multiple" value="${multiple}" /></#if>
       <label for="search"><@dspace.message "ui.tools.eperson-list.search.query" /></label>
          <#if search??><input type="text" name="search" value="${search}"/></#if>
       <input type="submit" value="<@dspace.message "ui.tools.eperson-list.search.submit" />"/>

       <#if search?? && search != "">
       <br/>
       <#if multiple?? && sortByParam?? && first??>
          <a href="<@dspace.url "/admin/eperson/browse-epeople?multiple=${multiple}&sortby=${sortByParam}&first=${first}" />"><@dspace.message "ui.tools.eperson-list.search.return-browse" /></a>
       </#if>
       </#if>

   </form>

   <table width="99%">
      <tr>
       <td width="17%" align="center"><small><strong><a href="${jumpLink}0"><@dspace.message "ui.tools.eperson-list.jump.first" /></a></strong></small></td>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpFiveBack}"><@dspace.message "ui.tools.eperson-list.jump.five-back" /></a></strong></small></td>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpOneBack}"><@dspace.message "ui.tools.eperson-list.jump.one-back" /></a></strong></small></td>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpOneForward}"><@dspace.message "ui.tools.eperson-list.jump.one-forward" /></a></strong></small></td>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpFiveForward}"><@dspace.message "ui.tools.eperson-list.jump.five-forward" /></a></strong></small></td>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpEnd}"><@dspace.message "ui.tools.eperson-list.jump.last" /></a></strong></small></td>
      </tr>
   </table>

    <form method="get" action="<@dspace.url "/admin/eperson/browse-epeople" />">

        <table class="miscTable" align="center" summary="Epeople list">

           <#if search?? && search != "">
           <tr>
            <th class="oddRowOddCol">&nbsp;</th>
            <th class="oddRowEvenCol"><@dspace.message "ui.tools.eperson-list.th.id" /></th>
            <th class="oddRowOddCol"><@dspace.message "ui.tools.eperson-list.th.email" /></th>
            <th class="oddRowEvenCol"><@dspace.message "ui.tools.eperson-list.th.lastname" /></th>
            <th class="oddRowOddCol"><@dspace.message "ui.tools.eperson-list.th.lastname" /></th>
           </tr>
           <#else>
               <tr><th id="t1" class="oddRowOddCol">&nbsp;</th>
               <th id="t2" class="oddRowEvenCol">
               <#if sortBy?? && eperson?? && sortBy==eperson.ID>
                <strong><@dspace.message "ui.tools.eperson-list.th.id.sortedby" /></strong>
                <#else>
                <strong><a href="${sortLink}id"><@dspace.message "ui.tools.eperson-list.th.id" /></a></strong>
               </#if>
               </th>
               <th id="t3" class="oddRowOddCol">
               <#if sortBy?? && eperson?? && sortBy == eperson.EMAIL>
                <strong><@dspace.message "ui.tools.eperson-list.th.email.sortedby" /></strong>
                <#else>
                    <a href="${sortLink}email"><@dspace.message "ui.tools.eperson-list.th.email" /></a>
               </#if>
               </th>
               <th id="t4" class="oddRowEvenCol">
               <#if sortBy?? && eperson?? && sortBy == eperson.LASTNAME>
               <strong><@dspace.message "ui.tools.eperson-list.th.lastname.sortedby" /></strong>
               <#else>
                    <a href="${sortLink}lastname"><@dspace.message "ui.tools.eperson-list.th.lastname" /></a>
               </#if>
               </th>
               <th id="t5" class="oddRowOddCol">
               <@dspace.message "ui.tools.eperson-list.th.firstname" />
               </th>
               <th id="t6" class="oddRowEvenCol">
               <#if sortBy?? && eperson?? && sortBy == eperson.LANGUAGE>
                <strong><@dspace.message "ui.tools.eperson-list.th.language.sortedby" /></strong>
               <#else>
                <a href="${sortLink}language"><@dspace.message "ui.tools.eperson-list.th.language" /></a>
               </#if>
               </th>
               </tr>
           </#if>
                <#assign row = "even"/>
               <#if epersonList?? && clearList??>
                 <#list epersonList as e>
                 <tr>
                   <td headers="t1" class="${row}RowOddCol">
                   <#if multiple??>
                   <input type="button" value="<@dspace.message "ui.tools.general.add"/>" onclick="javascript:${clearList}addEPerson(${e.getID()}, '${e.getEmail()}', '${e.getFullName()}');${closeWindow}"/>
                   <#else>
                   <input type="button" value="<@dspace.message "ui.tools.general.select"/>" onclick="javascript:${clearList}addEPerson(${e.getID()}, '${e.getEmail()}', '${e.getFullName()}');${closeWindow}"/>
                   </#if>
                   </td>
                     <td headers="t2" class="${row}RowEvenCol">${e.getID()}</td>
                     <td headers="t3" class="${row}RowOddCol">${e.getEmail()}</td>
                     <td headers="t4" class="${row}RowEvenCol">
                     <#if e.getLastName()??>
                      ${e.getLastName()}
                      <#else>
                      ${""}
                     </#if>
                     </td>
                     <td headers="t5" class="${row}RowEvenCol">
                     <#if e.getFirstName()??>
                     ${e.getFirstName()}
                     <#else>
                     ${""}
                     </#if>
                     </td>
                     <td headers="t6" class="${row}RowOddCol">
                     <#if e.getLanguage()??>
                     ${e.getLanguage()}
                     <#else>
                    ${""}
                     </#if>
                    </td>

                 </tr>

                 <#if row == "odd">
                 <#assign row = "even">
                 <#else>
                 <#assign row = "odd">
                 </#if>

                 </#list>
               </#if>
        </table>

     <table width="99%">
		<tr>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}0"><@dspace.message "ui.tools.eperson-list.jump.first" /></a></strong></small></td>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpFiveBack}"><@dspace.message "ui.tools.eperson-list.jump.five-back" /></a></strong></small></td>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpOneBack}"><@dspace.message "ui.tools.eperson-list.jump.one-back" /></a></strong></small></td>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpOneForward}"><@dspace.message "ui.tools.eperson-list.jump.one-forward" /></a></strong></small></td>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpFiveForward}"><@dspace.message "ui.tools.eperson-list.jump.five-forward" /></a></strong></small></td>
	   <td width="17%" align="center"><small><strong><a href="${jumpLink}${jumpEnd}"><@dspace.message "ui.tools.eperson-list.jump.last" /></a></strong></small></td>
		</tr>
	</table>

	<%-- <p align="center"><input type="button" value="Close" onClick="window.close();"/></p> --%>
	<p align="center"><input type="button" value="<@dspace.message "ui.tools.eperson-list.close.button" />" onclick="window.close();"/></p>

   </form>
   </body>
</html>
