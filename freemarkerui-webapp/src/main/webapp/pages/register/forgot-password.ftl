<#import "/includes/dspace.ftl" as dspace />    
 
	<h1><@dspace.message "ui.register.forgot-password.title" /></h1>

    <#if retry == true> <%-- model.addAttribute("retry", true) --%>
	<p><strong><@dspace.message "ui.register.forgot-password.info1" /></strong></p>
    </#if>

	<p><@dspace.message "ui.register.forgot-password.info2" /></p>
    
    <form action="<@dspace.url "/forgot"/>" method="post">
        <input type="hidden" name="step" value="1"/>

        <center>
            <table class="miscTable">
                <tr>
                    <td class="oddRowEvenCol">
                        <table border="0" cellpadding="5">
                            <tr>
                                <%-- <td class="standard"><strong>E-mail Address:</strong></td> --%>
								<td class="standard"><strong><label for="temail">
								<@dspace.message "ui.register.forgot-password.email.field" />
								</strong></label></td>
                                <td class="standard"><input type="text" name="email" id="temail" /></td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2">

									<input type="submit" name="submit" value="<@dspace.message "ui.register.forgot-password.forgot.button" />" />
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </center>
    </form>
