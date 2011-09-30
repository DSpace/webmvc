<#import "/includes/dspace.ftl" as dspace />
<page align="center">
    <p><a href="<@dspace.url "/feedback" />">
        <#if dspacename??>
            <#assign showingArgs = [dspacename]>
            <h3><@dspace.messageArgs "ui.components.contact-info.details", showingArgs/></h3>
        </#if>
    </a></p>
</page>

