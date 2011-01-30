<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#import "/includes/dspace.ftl" as dspace />
<form id="ConfigurableBrowse_div_browse-navigation" class="ds-interactive-div browse controls" method="post" action="<@dspace.url "${linkBase}" />">
    <p id=ConfigurableBrowse_navigation_p_hidden-fields">
        <input id="ConfigurableBrowse_navigation_field_order"   class="ds-hidden-field" type="hidden" name="order"   value="${browseInfo.isAscending()?string('ASC','DESC')}" />
        <input id="ConfigurableBrowse_navigation_field_rpp"     class="ds-hidden-field" type="hidden" name="rpp"     value="${browseInfo.resultsPerPage}" />
        <input id="ConfigurableBrowse_navigation_field_sort_by" class="ds-hidden-field" type="hidden" name="sort_by" value="${browseInfo.sortOption.number}" />
        <input id="ConfigurableBrowse_navigation_field_etal"    class="ds-hidden-field" type="hidden" name="etal"    value="${browseInfo.etAl}" />
        <input id="ConfigurableBrowse_navigation_field_type"    class="ds-hidden-field" type="hidden" name="type"    value="${browseInfo.browseIndex.name}" />
    </p>
    <#if browseInfo.sortOption.isDate() || (browseInfo.browseIndex.isDate() && browseInfo.sortOption.isDefault())>
        <p class="ds-paragraph"><@dspace.message "ui.list.controls.jump-to" />
            <select id="ConfigurableBrowse_navigation_field_month" class="ds-select-field" name="month">
                <option value="-1"><@dspace.message "ui.list.controls.jump-to.month" /></option>
                <option value="1"><@dspace.message "ui.list.controls.jump-to.month.jan" /></option>
                <option value="2"><@dspace.message "ui.list.controls.jump-to.month.feb" /></option>
                <option value="3"><@dspace.message "ui.list.controls.jump-to.month.mar" /></option>
                <option value="4"><@dspace.message "ui.list.controls.jump-to.month.apr" /></option>
                <option value="5"><@dspace.message "ui.list.controls.jump-to.month.may" /></option>
                <option value="6"><@dspace.message "ui.list.controls.jump-to.month.jun" /></option>
                <option value="7"><@dspace.message "ui.list.controls.jump-to.month.jul" /></option>
                <option value="8"><@dspace.message "ui.list.controls.jump-to.month.aug" /></option>
                <option value="9"><@dspace.message "ui.list.controls.jump-to.month.sep" /></option>
                <option value="10"><@dspace.message "ui.list.controls.jump-to.month.oct" /></option>
                <option value="11"><@dspace.message "ui.list.controls.jump-to.month.nov" /></option>
                <option value="12"><@dspace.message "ui.list.controls.jump-to.month.dec" /></option>
            </select>
            <select id="aspect_artifactbrowser_ConfigurableBrowse_field_year" class="ds-select-field" name="year">
                <option value="-1"><@dspace.message "ui.list.controls.jump-to.year" /></option>
                <option value="2011">2011</option>
                <option value="2010">2010</option>
                <option value="2009">2009</option>
                <option value="2008">2008</option>
                <option value="2007">2007</option>
                <option value="2006">2006</option>
                <option value="2005">2005</option>
                <option value="2004">2004</option>
                <option value="2003">2003</option>
                <option value="2002">2002</option>
                <option value="2001">2001</option>
                <option value="2000">2000</option>
                <option value="1995">1995</option>
                <option value="1990">1990</option>
                <option value="1985">1985</option>
                <option value="1980">1980</option>
                <option value="1970">1970</option>
                <option value="1960">1960</option>
                <option value="1950">1950</option>
                <option value="1940">1940</option>
                <option value="1930">1930</option>
                <option value="1920">1920</option>
            </select>
        </p>
        <p class="ds-paragraph">
            <@dspace.message "ui.list.controls.start-year" />
            <input id="ConfigurableBrowse_navigation_field_starts_with" class="ds-text-field" name="starts_with" type="text" value="" title="Browse for items that are from the given year.">
            <input id="ConfigurableBrowse_navigation_field_submit" class="ds-button-field" name="submit" type="submit" value="<@dspace.message "ui.list.controls.go" />">
        </p>
        <#else>
        <ul id="ConfigurableBrowse_list_jump-list" class="ds-simple-list alphabet">
            <#assign jumpLink="${linkBase}?type=${browseInfo.browseIndex.name}&amp;order=${browseInfo.isAscending()?string('ASC','DESC')}&amp;rpp=${browseInfo.resultsPerPage}&amp;etal=${browseInfo.etAl}" />
            <#list "0-9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z"?split(",") as letter>
                <li><a href="${jumpLink}&amp;starts_with=${letter?substring(0,1)}">${letter}</a></li>
            </#list>
        </ul>
        <p class="ds-paragraph">
            <@dspace.message "ui.list.controls.starts-with" />
            <input id="ConfigurableBrowse_navigation_field_starts_with" class="ds-text-field" name="starts_with" type="text" value="" title="Browse for items that begin with these letters" />
            <input id="ConfigurableBrowse_navigation_field_submit"      class="ds-button-field" name="submit" type="submit" value="<@dspace.message "ui.list.controls.go" />">
        </p>
    </#if>
</form>