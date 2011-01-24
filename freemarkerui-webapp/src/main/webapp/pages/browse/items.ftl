<#--
 $Id: $
 $URL: $
 *************************************************************************
 Copyright (c) 2002-2009, DuraSpace.  All rights reserved
 Licensed under the DuraSpace License.

 A copy of the DuraSpace License has been included in this
 distribution and is available at: http://scm.dspace.org/svn/repo/licenses/LICENSE.txt
-->
<#-- Internationalization can also be achieved via multiple templates - ie. home.ftl, home_en_US.ftl, etc. -->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title><@dspace.message "ui.browse.heading.${browseInfo.browseIndex.name}" /></title>
    </head>
    <body>
        <h1 class="ds-div-head"><@dspace.message "ui.browse.heading.${browseInfo.browseIndex.name}" /></h1>
        <div class="ds-static-div primary">
            <ul xmlns:oreatom="http://www.openarchives.org/ore/atom/" xmlns:ore="http://www.openarchives.org/ore/terms/" xmlns:atom="http://www.w3.org/2005/Atom" class="ds-artifact-list">
                <#list browseInfo.results as currentItem>
                    <li class="ds-artifact-item odd">
                        <@dspace.processMetadata item=currentItem field="dc.title" ; dcvalues>
                            <div class="artifact-title">
                                <#list dcvalues as dcvalue>
                                    <a href="/"><span class="z3988" title="">${dcvalue.value}</span></a>
                                    <#if dcvalue_has_next><br/></#if>
                                </#list>
                            </div>
                        </@dspace.processMetadata>
                        <div class="artifact-info">
                            <@dspace.processMetadata item=currentItem field="dc.identifier.author" ; dcvalues>
                                <span class="author">
                                    <#list dcvalues as dcvalue>
                                        <span>${dcvalue.value}</span>
                                        <#if dcvalue_has_next>; </#if>
                                    </#list>
                                </span>
                            </@dspace.processMetadata>
                            <@dspace.processMetadata item=currentItem field="dc.date.published" ; dcvalues>
                                <span class="publisher-date">
                                    <#list dcvalues as dcvalue>
                                        (<span class="date">${dcvalue.value}</span>)
                                    </#list>
                                </span>
                            </@dspace.processMetadata>
                        </div>
                        <@dspace.processMetadata item=currentItem field="dc.description.abstract" ; dcvalues>
                            <#list dcvalues as dcvalue>
                                <div class="artifact-abstract">${dcvalue.value}</div>
                                <#if dcvalue_has_next><br/></#if>
                            </#list>
                        </@dspace.processMetadata>
                    </li>
                </#list>
            </ul>
        </div>
    </body>
</html>
