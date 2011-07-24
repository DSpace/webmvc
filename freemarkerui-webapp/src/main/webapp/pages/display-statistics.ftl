<#import "/includes/dspace.ftl" as dspace />

<h1><@dspace.message "ui.statistics.title" /></h1>
<h2><@dspace.message "ui.statistics.heading.visits" /></h2>

<table class="statsTable">
    <tr>
        <th><!-- spacer cell --></th>
        <th><@dspace.message "ui.statistics.heading.views" /></th>
    </tr>
<#if statsVisits?? && statsVisitsMatrix??>
    <#list statsVisitsMatrix as row>

        <#list row as cell>
            <#if cell_index % 2 == 0>
                <#assign rowClass = "evenRowOddCol">
                <#else>
                    <#assign rowClass = "oddRowOddCol">
            </#if>
            <tr class="${rowClass}">
                <td>${statsVisits.colLabels[row_index]}</td>
                <td>${cell}</td>
            </tr>
        </#list>
    </#list>
</#if>
</table>


<h2><@dspace.message "ui.statistics.heading.monthlyvisits" /></h2>
<table class="statsTable">
<tr>
    <th><!-- spacer cell --></th>
<#if statsMonthlyVisits?? && statsMonthlyVisitsCol??>
    <#list statsMonthlyVisitsCol as headerlabel>
        <th>${headerlabel}</th>
    </#list>
</tr>
    <#list statsMonthlyVisitsMatrix as row>
        <#if row_index % 2 == 0>
            <#assign rowClass = "evenRowOddCol">
            <#else>
                <#assign rowClass = "oddRowOddCol">
        </#if>
        <tr class="${rowClass}">
            <td>${statsMonthlyVisits.rowLabels[row_index]}</td>
            <#list row as cell>
                <td>${cell}</td>
            </#list>
        </tr>

    </#list>
</#if></table>


<#if isItem??>

<h2><@dspace.message "ui.statistics.heading.filedownloads" /></h2>
<table class="statsTable">
    <tr>
        <th><!-- spacer cell --></th>
        <th><@dspace.message "ui.statistics.heading.views" /></th>
    </tr>
    <#if statsFileDownloads?? && statsFileDownloadsMatrix??>
        <#list statsFileDownloadsMatrix as row>

            <#list row as cell>
                <#if cell_index % 2 == 0>
                    <#assign rowClass = "evenRowOddCol">
                    <#else>
                        <#assign rowClass = "oddRowOddCol">
                </#if>
            <tr class="${rowClass}">
                <td>${statsFileDownloads.colLabels[cell_index]}</td>
                <td>${cell}</td>
            </#list>
           </tr>
        </#list>
    </#if>
    </table>

</#if>

<h2><@dspace.message "ui.statistics.heading.countryvisits" /></h2>
<table class="statsTable">
    <tr>
        <th><!-- spacer cell --></th>
        <th><@dspace.message "ui.statistics.heading.views" /></th>
    </tr>
<#if statsCountryVisits?? && statsCountryVisitsMatrix??>
    <#list statsCountryVisitsMatrix as row>
        <#list row as cell>
            <#if cell_index % 2 == 0>
                <#assign rowClass = "evenRowOddCol">
                <#else>
                    <#assign rowClass = "oddRowOddCol">
            </#if>
            <tr class="${rowClass}">
                <td>${statsCountryVisits.colLabels[cell_index]}</td>
                <td>${cell}</td>
            </tr>
        </#list>
    </#list>
</#if>
</table>


<h2>
<@dspace.message "ui.statistics.heading.cityvisits" />
</h2>
<table class="statsTable">
    <tr>
        <th><!-- spacer cell --></th>
        <th>
        <@dspace.message "ui.statistics.heading.views" />
        </th>
    </tr>
<#if statsCityVisits?? && statsCityVisitsMatrix??>
    <#list statsCityVisitsMatrix as row>
        <#list row as cell>
            <#if cell_index % 2 == 0>
                <#assign rowClass = "evenRowOddCol">
                <#else>
                    <#assign rowClass = "oddRowOddCol">
            </#if>
            <tr class="${rowClass}">
                <td>${statsCityVisits.colLabels[cell_index]}</td>
                <td>${cell}</td>
            </tr>
        </#list>
    </#list>
</#if>
</table>














