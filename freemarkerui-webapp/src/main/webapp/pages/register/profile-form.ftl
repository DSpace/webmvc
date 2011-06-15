
<table border="0" align="center" cellpadding="5">
 <tr>
 <%-- <td align="right" class="standard"><strong>First name*:</strong></td> --%>
 <td align="right" class="standard"><strong><@dspace.message "ui.register.profile-form.fname.field" /></strong></td>
 <td class="standard"><#if firstName??><input type="text" name="first_name" id="tfirst_name" size="40" value="${firstName}"/>
     <#else>
     <input type="text" name="first_name" id="tfirst_name" size="40" value=""/>
 </#if></td>
 </tr>
 <tr>
 <td align="right" class="standard"><label for="tlast_name"><strong><@dspace.message "ui.register.profile-form.lname.field" /></strong></label></td>
 <td class="standard"><#if lastName??><input type="text" name="last_name" id="tlast_name" size="40" value="${lastName}" />
     <#else><input type="text" name="last_name" id="tlast_name" size="40" value="" /></#if></td>
 </tr>
 <tr>
 <%-- <td align="right" class="standard"><strong>Contact telephone:</strong></td> --%>
 <td align="right" class="standard"><label for="tphone"><strong><@dspace.message "ui.register.profile-form.phone.field" /></strong></label></td>
 <td class="standard"><#if phone??><input type="text" name="phone" id="tphone" size="40" maxlength="32" value="${phone}"/><#else><input type="text" name="phone" id="tphone" size="40" maxlength="32" value=""/></#if></td>
 </tr>
 <tr>
  <td align="right" class="standard"><label for="tlanguage"><strong><@dspace.message "ui.register.profile-form.language.field" /></strong></label></td>
  <td class="standard">

 <select name="language" id="tlanguage">

     <#if supportedLocales??>
     <#list supportedLocales as supportedLocale>

     <option value="${supportedLocale.toString()}">

             <#if localeVariable??>${supportedLocale.getDisplayName(localeVariable)}
             <#else>
               ${supportedLocale.getDisplayName()}
             </#if>

     </option>
     </#list>

     </#if>
</select>

 </td>
  </tr>
 </table>

