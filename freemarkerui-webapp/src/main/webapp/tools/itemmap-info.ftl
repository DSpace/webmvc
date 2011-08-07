<#import "/includes/dspace.ftl" as dspace />

<h2><@dspace.message "ui.tools.itemmap-info.heading" /></h2>

<p>
<#if message?? && processedItems??>
    <#if message="none-selected">
    <@dspace.message "ui.tools.itemmap-info.msg.none-selected" />
        <#elseif message="none-removed">
        <@dspace.message "ui.tools.itemmap-info.msg.none-removed" />
        <#elseif message="added">
            <#list processedItems as pi>
                <#assign showingArgs=[pi]>
            <@dspace.messageArgs "ui.tools.itemmap-info.msg.added", showingArgs /><br/>
            </#list>
        <#elseif message="remove">
            <#list processedItems as pi>
                <#assign showingArgs=[pi]>
            <@dspace.messageArgs "ui.tools.itemmap-info.msg.remove", showingArgs /><br/>
            </#list>
        <#else>
    </#if>
</#if>
</p>

    <form method="post" action="<@dspace.url "/tools/itemmap"/>">
        <input type="hidden" name="cid" value="<#if collection??>${collection.getID()}</#if>"/>
        <input type="submit" name="item_mapper" value="<@dspace.message "ui.tools.itemmap-info.button.continue" />"/>
    </form>
