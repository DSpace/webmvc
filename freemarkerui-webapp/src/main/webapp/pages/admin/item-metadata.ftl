<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#--
    View and edit Item Metadata
-->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>${item.getMetadata("dc.title")[0].value!"Untitled"}</title>
    </head>
    <body>
        <ul>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/status"/>">Item Status</a></li>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/bitstreams"/>">Item Bitstreams</a></li>
            <li><a href="<@dspace.url "/admin/item/${item.getID()}/metadata"/>">Item Metadata</a></li>
            <li><a href="<@dspace.url "/handle/${item.getHandle()}"/>">View Item</a></li>
        </ul>
        <h1>Metadata: ${item.getMetadata("dc.title")[0].value!"Untitled"}</h1>
        <form method="post">
            <table>
                <thead>
                    <tr>
                        <th>Schema Element Qualifier</th>
                        <th>Value</th>
                        <th>Language</th>
                        <th></th>
                    </tr>
                </thead>
                <#assign row = 0>
                <tbody>
                    <#list values as value>
                        <tr id="metadata_${row}">
                            <td><input type="hidden" name="name_${row}" value="${value.schema!""}_${value.element!""}_${value.qualifier!""}">${value.schema!""}.${value.element!""}.${value.qualifier!""}</td>
                            <td><textarea rows="4" cols="35" name="value_${row}">${value.value!""}</textarea></td>
                            <td><input type="text" size="6" name="language_${row}" value="${value.language!""}"></td>
                            <#--<td><a href="#" onclick="$('#metadata_${row}').remove()">Remove</a> </td>-->
                            <td><a href="#" onclick="document.getElementById('metadata_${row}').innerHTML=''">Remove</a> </td>
                        </tr>
                        <#assign row = row +1>
                    </#list>
                </tbody>
                <tfoot>
                    <tr><th colspan="3">Add New Metadata</th></tr>
                    <tr>
                        <td>
                            <select id="field_name" class="ds-select-field" name="name_${row}">
                                <#-- TODO Fetch fields from database instead of hard coded. -->
                                <option value="">Select a Metadata Field</option>
                                <option value="dc_contributor_advisor">dc.contributor.advisor</option>
                                <option value="dc_contributor_author">dc.contributor.author</option>
                                <option value="dc_contributor_editor">dc.contributor.editor</option>
                                <option value="dc_contributor_illustrator">dc.contributor.illustrator</option>
                                <option value="dc_contributor_other">dc.contributor.other</option>
                                <option value="dc_contributor">dc.contributor</option>
                                <option value="dc_coverage_spatial">dc.coverage.spatial</option>
                                <option value="dc_coverage_temporal">dc.coverage.temporal</option>
                                <option value="dc_creator">dc.creator</option>
                                <option value="dc_date_accessioned">dc.date.accessioned</option>
                                <option value="dc_date_available">dc.date.available</option>
                                <option value="dc_date_copyright">dc.date.copyright</option>
                                <option value="dc_date_created">dc.date.created</option>
                                <option value="dc_date_issued">dc.date.issued</option>
                                <option value="dc_date_submitted">dc.date.submitted</option>
                                <option value="dc_date">dc.date</option>
                                <option value="dc_description_abstract">dc.description.abstract</option>
                                <option value="dc_description_embargo">dc.description.embargo</option>
                                <option value="dc_description_provenance">dc.description.provenance</option>
                                <option value="dc_description_sponsorship">dc.description.sponsorship</option>
                                <option value="dc_description_statementofresponsibility">dc.description.statementofresponsibility</option>
                                <option value="dc_description_tableofcontents">dc.description.tableofcontents</option>
                                <option value="dc_description_uri">dc.description.uri</option>
                                <option value="dc_description">dc.description</option>
                                <option value="dc_format_extent">dc.format.extent</option>
                                <option value="dc_format_medium">dc.format.medium</option>
                                <option value="dc_format_mimetype">dc.format.mimetype</option>
                                <option value="dc_format">dc.format</option>
                                <option value="dc_identifier_citation">dc.identifier.citation</option>
                                <option value="dc_identifier_doi">dc.identifier.doi</option>
                                <option value="dc_identifier_govdoc">dc.identifier.govdoc</option>
                                <option value="dc_identifier_isbn">dc.identifier.isbn</option>
                                <option value="dc_identifier_ismn">dc.identifier.ismn</option>
                                <option value="dc_identifier_issn">dc.identifier.issn</option>
                                <option value="dc_identifier_osuauthor">dc.identifier.osuauthor</option>
                                <option value="dc_identifier_other">dc.identifier.other</option>
                                <option value="dc_identifier_sici">dc.identifier.sici</option>
                                <option value="dc_identifier_tgn">dc.identifier.tgn</option>
                                <option value="dc_identifier_uri">dc.identifier.uri</option>
                                <option value="dc_identifier">dc.identifier</option>
                                <option value="dc_language_iso">dc.language.iso</option>
                                <option value="dc_language">dc.language</option>
                                <option value="dc_publisher">dc.publisher</option>
                                <option value="dc_relation_haspart">dc.relation.haspart</option>
                                <option value="dc_relation_hasversion">dc.relation.hasversion</option>
                                <option value="dc_relation_isbasedon">dc.relation.isbasedon</option>
                                <option value="dc_relation_isformatof">dc.relation.isformatof</option>
                                <option value="dc_relation_ispartof">dc.relation.ispartof</option>
                                <option value="dc_relation_ispartofseries">dc.relation.ispartofseries</option>
                                <option value="dc_relation_isreferencedby">dc.relation.isreferencedby</option>
                                <option value="dc_relation_isreplacedby">dc.relation.isreplacedby</option>
                                <option value="dc_relation_isversionof">dc.relation.isversionof</option>
                                <option value="dc_relation_replaces">dc.relation.replaces</option>
                                <option value="dc_relation_requires">dc.relation.requires</option>
                                <option value="dc_relation_uri">dc.relation.uri</option>
                                <option value="dc_relation">dc.relation</option>
                                <option value="dc_rights_uri">dc.rights.uri</option>
                                <option value="dc_rights">dc.rights</option>
                                <option value="dc_source_image">dc.source.image</option>
                                <option value="dc_source_uri">dc.source.uri</option>
                                <option value="dc_source">dc.source</option>
                                <option value="dc_subject_classification">dc.subject.classification</option>
                                <option value="dc_subject_ddc">dc.subject.ddc</option>
                                <option value="dc_subject_lcc">dc.subject.lcc</option>
                                <option value="dc_subject_lcsh">dc.subject.lcsh</option>
                                <option value="dc_subject_mesh">dc.subject.mesh</option>
                                <option value="dc_subject_other">dc.subject.other</option>
                                <option value="dc_subject">dc.subject</option>
                                <option value="dc_title_alternative">dc.title.alternative</option>
                                <option value="dc_title">dc.title</option>
                                <option value="dc_type">dc.type</option>
                            </select>
                        </td>
                        <td><textarea class="ds-textarea-field" name="value_${row}" cols="35" rows="4"></textarea></td>
                        <td><input id="field_language" class="ds-text-field" name="language_${row}" type="text" value="" size="6"></td>
                        <td></td>
                    </tr>
                </tfoot>
            </table>
            <hr/>

            <div class="ds-form-content">
                <input class="ds-button-field" name="update" type="submit" value="Update">
                <input class="ds-button-field" name="cancel" type="submit" value="Return">
            </div>
        </form>
    </body>
</html>