<#import "/includes/dspace.ftl" as dspace />

    <h1><@dspace.message "ui.mydspace.in-archive.heading1" /></h1>

	<p><@dspace.message "ui.mydspace.in-archive.text1" /></p>
    <#if handle??><p><a href="${handle}">${handle}</a></p></#if>

	<p><@dspace.message "ui.mydspace.in-archive.text2" /></p>

    <p align="center">
        <a href="<@dspace.url "/mydspace" />">
            <@dspace.message "ui.mydspace.in-archive.return.link" />
        </a>
    </p>
