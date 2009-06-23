<#import "/includes/dspace.ftl" as dspace />
<#macro box class id titleKey="" titleStr="">
    <div class="${class}">
        <h2><@dspace.messageOrString code="${titleKey}" str="${titleStr}" /></h2>
        <div id="${id}" class="boxContent">
            <i>Demonstrating import overrides in a jar/theme</i><br/><br/>
            <#nested>
        </div>
    </div>
</#macro>
