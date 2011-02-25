<form id="ConfigurableBrowse_div_browse-controls" class="ds-interactive-div browse controls" method="post" action="<@dspace.url "${linkBase}" />">
    <p id=ConfigurableBrowse_p_hidden-fields">
        <input id="ConfigurableBrowse_field_type" class="ds-hidden-field" type="hidden" name="type" value="${browseInfo.browseIndex.name}" />
        <#if browseInfo.hasValue()>
            <input id="ConfigurableBrowse_field_type" class="ds-hidden-field" type="hidden" name="value" value="${browseInfo.value}" />
        </#if>
    </p>
    <p class="ds-paragraph">
        <#if showSortByOptions??>
            <@dspace.message "ui.list.controls.sort" />
            <select id="ConfigurableBrowse_field_sort_by" class="ds-select-field" name="sort_by">
                <#list dspaceHelper.sortOptions as sortOption>
                    <#if sortOption.isVisible()>
                        <option value="${sortOption.number}" <#if browseInfo.sortOption.number=sortOption.number>selected</#if>><@dspace.message "ui.list.controls.sort.${sortOption.name}" /></option>
                    </#if>
                </#list>
            </select>
        </#if>
        <@dspace.message "ui.list.controls.order" />
        <select id="ConfigurableBrowse_field_order" class="ds-select-field" name="order">
            <option value="ASC" <#if browseInfo.isAscending()>selected</#if>><@dspace.message "ui.list.controls.order.ascending" /></option>
            <option value="DESC" <#if browseInfo.isAscending()><#else>selected</#if>><@dspace.message "ui.list.controls.order.descending" /></option>
        </select>
        <@dspace.message "ui.list.controls.results" />
            <select id="ConfigurableBrowse_field_rpp" class="ds-select-field" name="rpp">
                <option value="5" <#if browseInfo.resultsPerPage=5>selected</#if>>5</option>
                <option value="10" <#if browseInfo.resultsPerPage=10>selected</#if>>10</option>
                <option value="20" <#if browseInfo.resultsPerPage=20>selected</#if>>20</option>
                <option value="40" <#if browseInfo.resultsPerPage=40>selected</#if>>40</option>
                <option value="60" <#if browseInfo.resultsPerPage=60>selected</#if>>60</option>
                <option value="80" <#if browseInfo.resultsPerPage=80>selected</#if>>80</option>
                <option value="100" <#if browseInfo.resultsPerPage=100>selected</#if>>100</option>
            </select>
        <input id="ConfigurableBrowse_field_update" class="ds-button-field" name="update" type="submit" value="<@dspace.message "ui.list.controls.update" />" />
    </p>
</form>
