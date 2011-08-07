<#import "/includes/dspace.ftl" as dspace />


	<h1><@dspace.message "ui.mydspace.reject-reason.title" /></h1>


	<p><@dspace.message "ui.mydspace.reject-reason.text1" /></p>

    <#if workflowitem??>
    <form action="<@dspace.url "/mydspace"/>" method="post">
        <input type="hidden" name="workflow_id" value="${workflowitem.getID()}"/>
        <input type="hidden" name="step" value="4"/>
        <center>
            <textarea rows="6" cols="50" name="reason"></textarea>
        </center>

        <table border="0" width="90%" cellpadding="10" align="center">
            <tr>
                <td align="left">

					 <input type="submit" name="submit_send" value="<@dspace.message "ui.mydspace.reject-reason.reject.button" />" />
                </td>
                <td align="right">

					<input type="submit" name="submit_cancel" value="<@dspace.message "ui.mydspace.reject-reason.cancel.button" />" />
                </td>
            </tr>
        </table>
    </form></#if>