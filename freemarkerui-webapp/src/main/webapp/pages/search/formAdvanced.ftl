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

<#-- notes: simple search shows simple form on results - advanced search shows advanced form on results -->
<#-- Original XMLUI code -->
<h1 style="font-size: 244%;" class="ds-div-head">Advanced Search</h1>
<div id="aspect_artifactbrowser_AdvancedSearch_div_advanced-search" class="ds-static-div primary">
<form id="aspect_artifactbrowser_AdvancedSearch_div_search-query" class="ds-interactive-div secondary search" action="advanced-search" method="post" onsubmit="javascript:tSubmit(this);">
<p id="aspect_artifactbrowser_AdvancedSearch_p_hidden-fields" class="ds-paragraph hidden">
<input id="aspect_artifactbrowser_AdvancedSearch_field_num_search_field" class="ds-hidden-field" name="num_search_field" type="hidden" value="3" />
<input id="aspect_artifactbrowser_AdvancedSearch_field_results_per_page" class="ds-hidden-field" name="results_per_page" type="hidden" value="10" />
</p>
<fieldset id="aspect_artifactbrowser_AdvancedSearch_list_search-query" class="ds-form-list">
<ol>
<li class="ds-form-item">
<label class="ds-form-label" for="aspect_artifactbrowser_AdvancedSearch_field_scope">Search scope:</label>
<div class="ds-form-content">
<select id="aspect_artifactbrowser_AdvancedSearch_field_scope" class="ds-select-field" name="scope" title="Limit your search to a community or collection.">
<option value="/" selected="selected">All of DSpace</option>
<option value="123456789/1">Imports</option>
</select>
<span class="field-help">Limit your search to a community or collection.</span>
</div>
</li>
</ol>
</fieldset>
<table id="aspect_artifactbrowser_AdvancedSearch_table_search-query" class="ds-table">
<tr class="ds-table-header-row">
<th class="ds-table-header-cell odd">Conjunction</th>
<th class="ds-table-header-cell even">Search type</th>
<th class="ds-table-header-cell odd">Search for</th>
</tr>
<tr class="ds-table-row even">
<td class="ds-table-cell odd" />
<td class="ds-table-cell even">
<select id="aspect_artifactbrowser_AdvancedSearch_field_field1" class="ds-select-field" name="field1">
<option value="ANY" selected="selected">Full Text</option>
<option value="abstract">Abstract</option>
<option value="series">Series</option>
<option value="author">Author</option>
<option value="title">Title</option>
<option value="keyword">Keyword</option>
<option value="language">Language (ISO)</option>
<option value="mime">Mime-Type</option>
<option value="sponsor">Sponsor</option>
<option value="identifier">Identifier</option>
</select>
</td>
<td class="ds-table-cell odd">
<input id="aspect_artifactbrowser_AdvancedSearch_field_query1" class="ds-text-field" name="query1" type="text" value="" />
</td>
</tr>
<tr class="ds-table-row odd">
<td class="ds-table-cell odd">
<select id="aspect_artifactbrowser_AdvancedSearch_field_conjunction2" class="ds-select-field" name="conjunction2">
<option value="AND" selected="selected">AND</option>
<option value="OR">OR</option>
<option value="NOT">NOT</option>
</select>
</td>
<td class="ds-table-cell even">
<select id="aspect_artifactbrowser_AdvancedSearch_field_field2" class="ds-select-field" name="field2">
<option value="ANY" selected="selected">Full Text</option>
<option value="abstract">Abstract</option>
<option value="series">Series</option>
<option value="author">Author</option>
<option value="title">Title</option>
<option value="keyword">Keyword</option>
<option value="language">Language (ISO)</option>
<option value="mime">Mime-Type</option>
<option value="sponsor">Sponsor</option>
<option value="identifier">Identifier</option>
</select>
</td>
<td class="ds-table-cell odd">
<input id="aspect_artifactbrowser_AdvancedSearch_field_query2" class="ds-text-field" name="query2" type="text" value="" />
</td>
</tr>
<tr class="ds-table-row even">
<td class="ds-table-cell odd">
<select id="aspect_artifactbrowser_AdvancedSearch_field_conjunction3" class="ds-select-field" name="conjunction3">
<option value="AND" selected="selected">AND</option>
<option value="OR">OR</option>
<option value="NOT">NOT</option>
</select>
</td>
<td class="ds-table-cell even">
<select id="aspect_artifactbrowser_AdvancedSearch_field_field3" class="ds-select-field" name="field3">
<option value="ANY" selected="selected">Full Text</option>
<option value="abstract">Abstract</option>
<option value="series">Series</option>
<option value="author">Author</option>
<option value="title">Title</option>
<option value="keyword">Keyword</option>
<option value="language">Language (ISO)</option>
<option value="mime">Mime-Type</option>
<option value="sponsor">Sponsor</option>
<option value="identifier">Identifier</option>
</select>
</td>
<td class="ds-table-cell odd">
<input id="aspect_artifactbrowser_AdvancedSearch_field_query3" class="ds-text-field" name="query3" type="text" value="" />
</td>
</tr>
</table>
<table id="aspect_artifactbrowser_AdvancedSearch_table_search-controls" class="ds-table">
<tr class="ds-table-row odd">
<td class="ds-table-cell odd">Results/page<select id="aspect_artifactbrowser_AdvancedSearch_field_rpp" class="ds-select-field" name="rpp">
<option value="5">5</option>
<option value="10" selected="selected">10</option>
<option value="20">20</option>
<option value="40">40</option>
<option value="60">60</option>
<option value="80">80</option>
<option value="100">100</option>
</select>
</td>
<td class="ds-table-cell even">Sort items by<select id="aspect_artifactbrowser_AdvancedSearch_field_sort_by" class="ds-select-field" name="sort_by">
<option value="0">relevance</option>
<option value="3">submit date</option>
<option value="1">title</option>
<option value="2">issue date</option>
</select>
</td>
<td class="ds-table-cell odd">in order<select id="aspect_artifactbrowser_AdvancedSearch_field_order" class="ds-select-field" name="order">
<option value="ASC">ascending</option>
<option value="DESC" selected="selected">descending</option>
</select>
</td>
</tr>
</table>
<p class="ds-paragraph button-list">
<input id="aspect_artifactbrowser_AdvancedSearch_field_submit" class="ds-button-field" name="submit" type="submit" value="Go" />
</p>
</form>
</div>
</div>
<div id="ds-options">
<h3 class="ds-option-set-head" id="ds-search-option-head">Search DSpace</h3>
<div class="ds-option-set" id="ds-search-option">
<form method="post" id="ds-search-form" action="/xmlui/search">
<fieldset>
<input type="text" class="ds-text-field " name="query" />
<input value="Go" type="submit" name="submit" class="ds-button-field " onclick="&#10;                                    var radio = document.getElementById(&quot;ds-search-form-scope-container&quot;);&#10;                                    if (radio != undefined &amp;&amp; radio.checked)&#10;                                    {&#10;                                    var form = document.getElementById(&quot;ds-search-form&quot;);&#10;                                    form.action=&#10;                                &quot;/xmlui/handle/&quot; + radio.value + &quot;/&gt;search&quot; ; &#10;                                    }&#10;                                " />
</fieldset>
</form>