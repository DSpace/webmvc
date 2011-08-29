<#import "/includes/dspace.ftl" as dspace />


<h2><@dspace.message "ui.mydspace.own-submissions.title" /></h2>

<#if items?? && items?size==0>
<p><@dspace.message "ui.mydspace.own-submissions.text1" /></p>
    <#else>

    <p><@dspace.message "ui.mydspace.own-submissions.text2" /></p>

        <#if items?? && items?size==1>
        <p><@dspace.message "ui.mydspace.own-submissions.text3" /></p>
            <#else>
            <p> <#assign showingArgs=items?size>
        <@dspace.messageArgs "ui.mydspace.own-submissions.text4", showingArgs /></p>
        </#if>
    <#list items as item>
        <#include "/pages/item.ftl" />
    </#list>
</#if>

<p align="center"><a href="<@dspace.url "/mydspace"/>"><@dspace.message "ui.mydspace.general.backto-mydspace" /></a></p>