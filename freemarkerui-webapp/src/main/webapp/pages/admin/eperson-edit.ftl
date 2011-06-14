<#import "/includes/dspace.ftl" as dspace />

<head><script type="text/javascript" src="<@dspace.url "/utils.js" />"></script></head>
<table width="95%">
    <tr>
      <td align="left">
          <#if eperson??>
          <#assign showingArgs=["${eperson.getEmail()}"]>
          <h1><@dspace.messageArgs "ui.dspace-admin.eperson-edit.heading", showingArgs /></h1></#if>
      </td>
      <td align="right" class="standard">
       <a href="<@dspace.url "/help/site-admin.html#epeople" />" target="_blank"><@dspace.message "ui.help" /></a>
          <!--<a href="<@dspace.url "/help/site-admin.html#epeople" />" onClick="javascript: popup(this, 'eperson_popup');"><@dspace.message "ui.help" /></a>-->
      </td>
    </tr>
  </table>

<#if email_exists?? && email_exists==true>
<p><strong><@dspace.message "ui.dspace-admin.eperson-edit.emailexists" /></strong></p>
</#if>

<form method="post" action="<@dspace.url "/admin/eperson" />">

    <table class="miscTable" align="center">
          <tr>
              <td><label for="temail"><@dspace.message "ui.dspace-admin.eperson-edit.email" /></label></td>
              <td>
                <#if eperson??>
                <input type="hidden" name="eperson_id" value="${eperson.getID()}"/>
                </#if>
                <#if email??>
                <input name="email" id="temail" size="24" value="${email}"/>
                <#else>
                <input name="email" id="temail" size="24" value=""/>
                </#if>
          </tr>

        <tr>

            <td><label for="tlastname"><@dspace.message "ui.dspace-admin.eperson.general.lastname" /></label></td>
            <td>
                <#if lastName??>
                   <input name="lastname" id="tlastname" size="24" value="${lastName}"/>
                <#else>
                   <input name="lastname" id="tlastname" size="24" value=""/>
                </#if>
            </td>
        </tr>

        <tr>
            <%-- <td>First Name:</td> --%>
            <td><label for="tfirstname"><@dspace.message "ui.dspace-admin.eperson.general.firstname" /></label></td>
            <td>
                <#if firstName??>
                 <input name="firstname" id="tfirstname" size="24" value="${firstName}"/>
                <#else>
                 <input name="firstname" id="tfirstname" size="24" value=""/>
                </#if>
            </td>
        </tr>

        <#if ldap_enabled?? && ldap_enabled==true>
        <tr>
            <td>LDAP NetID:</td>
            <td>
                <#if netid??>
                <input name="netid" size="24" value="${netid}" />
                <#else>
                <input name="netid" size="24" value="" />
                </#if>
            </td>
        </tr>
        </#if>

        <tr>
            <td><label for="tphone"><@dspace.message "ui.dspace-admin.eperson-edit.phone" /></label></td>
            <td>
                <#if phone??>
                <input name="phone" id="tphone" size="24" value="${phone}"/>
                <#else>
                <input name="phone" id="tphone" size="24" value=""/>
                </#if>
            </td>
        </tr>

             <tr>
            <td><label for="tlanguage"><@dspace.message "ui.register.profile-form.language.field" /></label></td>
            <td class="standard">
        	<select name="language" id="tlanguage">

               <#if valueOne??>
               <#if supportedLocales??>
                <#list supportedLocales as sL>
                <#if (language?? && language=="")||language??==false>
                <#if localeCompare?? && sL.toString()==localeCompare>
                <#assign selected = "selected=\"selected\"">
                </#if>
                <#elseif sL.toString()==language>
                <#assign selected = "selected=\"selected\"">
                </#if>
                <option ${selected} value="${sL.toString()}">${sL.getDisplayName(valueOne)}</option>
                </#list>
               </#if>
               </#if>

            </select>
   		    </td>
        </tr>

        <tr>

            <td><label for="tcan_log_in"><@dspace.message "ui.dspace-admin.eperson-edit.can" /></label></td>
            <td>
                <#if eperson?? && eperson.canLogIn()==true>
                <input type="checkbox" name="can_log_in" id="tcan_log_in" value="true"${"checked=\"checked\""}/>
                <#else>
                <input type="checkbox" name="can_log_in" id="tcan_log_in" value="true"${""}/>
                </#if>
            </td>
        </tr>

        <tr>

            <td><label for="trequire_certificate"><@dspace.message "ui.dspace-admin.eperson-edit.require" /></label></td>
            <td>
                <#if eperson?? && eperson.getRequireCertificate()>
                <input type="checkbox" name="require_certificate" id="trequire_certificate" value="true"${"checked=\"checked\""}/>
                <#else>
                <input type="checkbox" name="require_certificate" id="trequire_certificate" value="true"${""} />
                </#if>
            </td>
        </tr>

     </table>

    <center>
       <table width="70%">
            <tr>
                <td align="left">

                    <input type="submit" name="submit_save" value="<@dspace.message "ui.dspace-admin.general.save" />" />
                </td>
                <td align="right">

                    <input type="submit" name="submit_delete" value="<@dspace.message "ui.dspace-admin.general.delete" />" />
                </td>
            </tr>
        </table>
    </center>

</form>

    <#if groupMemberships?? && groupMemberships?size&gt;0>
    <h3><@dspace.message "ui.dspace-admin.eperson-edit.groups" /></h3>
     <#list groupMemberships as gM>
      <#assign args = "submit_edit&amp;group_id=" + "${gM.getID()}">
     <li><a href="<@dspace.url "/admin/eperson/group-edit?${args}" />">${gM.getName()}</a> </li>
     </#list>
    </#if>
