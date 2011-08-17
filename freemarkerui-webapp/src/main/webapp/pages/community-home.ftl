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
<head>
    <script type="text/javascript" src="<@dspace.url "/utils.js" />"></script>
</head>
<#assign currentCommunity=community />
<html>
<head>
    <title>${community.getName()!"Untitled"}</title>
</head>
<body>
<!--<#include "/viewers/community.ftl" />-->
<table border="0" cellpadding="5" width="100%">
    <tr>
        <td width="100%">
            <h1>
            <#if name??>${name}</#if>
        <#if strengthsShow?? && ic?? && community?? && strengthsShow==true>
            ${ic.getCount(community)}
            </#if>
            </h1>

            <h3><@dspace.message "ui.community-home.heading1" /></h3>
        </td>
        <td valign="top">
        <#if logo??><img alt="Logo" src="<@dspace.url relativeUrl="/retrieve/${logo.getID()}"/>"/></#if></td>
    </tr>
</table>
<%-- Search/Browse --%>

<table class="miscTable" align="center"
       summary="This table allows you to search through all communities held in the repository">
    <tr>
        <td class="evenRowEvenCol" colspan="2">
            <form method="get" action="">
                <table>
                    <tr>
                        <td class="standard" align="center">
                            <small><label
                                    for="tlocation"><strong><@dspace.message "ui.general.location" /></strong></label>
                            </small>
                            &nbsp;<select name="location" id="tlocation">
                            <option value="/"><@dspace.message "ui.general.genericScope" /></option>
                            <option selected="selected"
                                    value="<#if community??>${community.getHandle()}</#if>"><#if name??>${name}</#if></option>
                        <#if collections??>
                            <#list collections as col>
                                <option value="${col.getHandle()}">${col.getMetadata("name")}</option>
                            </#list>
                        </#if>
                        <#if subcommunities??>
                            <#list subcommunities as sub>
                                <option value="${sub.getHandle()}">${sub.getMetadata("name")}</option>
                            </#list>
                        </#if></select></td>
                    </tr>
                    <tr>
                        <td class="standard" align="center">
                            <small><label for="tquery"><strong><@dspace.message "ui.general.searchfor" />&nbsp;</strong></label>
                            </small>
                            <input type="text" name="query" id="tquery"/>&nbsp;<input type="submit" name="submit_search"
                                                                                      value="<@dspace.message "ui.general.go" />"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
    <tr>
        <td align="center" class="standard" valign="middle">
            <small><@dspace.message "ui.general.orbrowse" />&nbsp;</small>
        </td>
    </tr>
<#if bis??>
    <tr>
        <#list bis as bi>
            <#assign key = "browse.menu." + bi.getName()>
            <form method="get" action="<@dspace.url relativeUrl="/handle/${community.getHandle()}/browse"/>">
                <input type="hidden" name="type" value="${bi.getName()}"/>
                <td><input type="submit" name="submit_browse" value="<@dspace.message "${key}" />"/></td>
            </form>
        </#list>
    </tr>
</#if>


</table>
<#if intro??>
${intro}
</#if>
<#if collections?? && collections?size!=0>
<h2><@dspace.message "ui.community-home.heading2" /></h2>
<ul class="collectionListItem">
    <#list collections as col>
        <li>
            <table>
                <tr>
                    <td>
                        <a href="<@dspace.url "/handle/${col.getHandle()}"/>">${col.getMetadata("name")}</a>
                        <#if strengthsShow?? && strengthsShow==true && ic??>[${ic.getCount(col)}]</#if></td>
                    <#if remove_button?? && remove_button==true>
                        <td>
                            <form method="post" action="<@dspace.url "/admin/editcommunities"/>">
                                <input type="hidden" name="parent_community_id" value="<#if community??>${community.getID()}</#if>"/>
                                <input type="hidden" name="community_id" value="<#if community??>${community.getID()}</#if>"/>
                                <input type="hidden" name="collection_id" value="${col.getID()}"/>
                                <input type="image" src="<@dspace.url "/image/remove.gif"/>"/>
                            </form>
                        </td>
                    </#if>
                </tr>
            </table>
            <p class="collectionDescription">${col.getMetadata("short_description")}</p>
        </li>
    </#list></ul>
</#if>
<#if subcommunities?? && subcommunities?size !=0>
<h2><@dspace.message "ui.community-home.heading3" /></h2>

<ul class="collectionListItem">
    <#list subcommunities as sub>
        <li>
            <table>
                <tr>
                    <td>
                        <a href="<@dspace.url "/handle/${sub.getHandle()}/browse"/>">${sub.getMetadata("name")}</a>
                        <#if strengthsShow?? && strengthsShow==true && ic??>[${ic.getCount(sub)}]</#if></td>
                    <#if remove_button && remove_button==true>
                        <td>
                            <form method="post" action="<@dspace.url "/admin/editcommunities"/>">
                                <input type="hidden" name="parent_community_id"
                                       value="<#if community??>${community.getID()}</#if>"/>
                                <input type="hidden" name="community_id" value="${sub.getID()}"/>
                                <input type="image" src="<@dspace.url "/image/remove.gif"/>"/>
                            </form>
                        </td></#if>
                </tr>
            </table>
            <p class="collectionDescription">${sub.getMetadata("short_description")}</p>
        </li>
    </#list>
</ul>
</#if>
<p class="copyrightText">
<#if copyright??>${copyright}</#if></p>
<#if editor_button?? && add_button && (editor_button==true||add_button==true)>
<table class="miscTable" align="center">
    <tr>
        <td class="evenRowEvenCol" colspan="2">
            <table>
                <tr>
                    <th id="t1" class="standard">
                        <strong><@dspace.message "ui.admintools" /></strong>
                    </th>
                </tr>
                <tr>
                    <td headers="t1" class="standard" align="center">
                        <#if editor_button==true>
                            <form method="post" action="<@dspace.url "/admin/editcommunities"/>">
                                <input type="hidden" name="community_id"
                                       value="<#if community??>${community.getID()}</#if>"/>
                                <input type="submit" name="submit_edit_community" value="<@dspace.message "ui.general.edit.button" />"/>
                            </form>
                        </#if>
                        <#if add_button==true>
                            <form method="post" action="<@dspace.url "/tools/collection-wizard"/>">
                                <input type="hidden" name="community_id" value="<#if community??>${community.getID()}</#if>"/>
                                <input type="submit" value="<@dspace.message "ui.community-home.create1.button" />"/>
                            </form>

                            <form method="post" action="<@dspace.url "/admin/editcommunities"/>">
                                <input type="hidden" name="parent_community_id"
                                       value="<#if community??>${community.getID()}</#if>"/>
                                <input type="submit" name="submit_create_community"
                                       value="<@dspace.message "ui.community-home.create2.button" />"/>
                            </form>
                        </#if></td>
                </tr>
                <#if editor_button==true>
                    <tr>
                        <td headers="t1" class="standard" align="center">
                            <form method="post" action="<@dspace.url "/mydspace"/>">
                                <input type="hidden" name="community_id"
                                       value="<#if community??>${community.getID()}</#if>"/>
                                <input type="hidden" name="step" value="5" />
                                <input type="submit" value="<@dspace.message "ui.mydspace.request.export.community" />"/>
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td headers="t1" class="standard" align="center">
                            <form method="post" action="<@dspace.url "/mydspace"/>">
                                <input type="hidden" name="community_id" value="<#if community??>${community.getID()}</#if>"/>
                                <input type="hidden" name="step" value="6" />
                                <input type="submit" value="<@dspace.message "ui.mydspace.request.export.migratecommunity" />"/>
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td headers="t1" class="standard" align="center">
                            <form method="post" action="<@dspace.url "/dspace-admin/metadataexport"/>">
                                <input type="hidden" name="handle"
                                       value="<#if community??>${community.getHandle()}</#if>"/>
                                <input type="submit" value="<@dspace.message "ui.general.metadataexport.button" />"/>
                            </form>
                        </td>
                    </tr>
                </#if>
                <tr>
                    <td headers="t1" class="standard" align="center">
                    <@dspace.message "ui.dspace-admin.authorize-advanced.text" /><a
                            href="<@dspace.url "/help/site-admin.html" />"
                            onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.adminhelp" /></a>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

</#if>
<h3><@dspace.message "ui.community-home.recentsub" /></h3>
<#if recentlysubmitted??>
    <#assign items = recentlysubmitted.getRecentSubmissions()>
    <#list items as item>
    <p class="recentItem"><a
            href="<@dspace.url relativeUrl="/handle/${item.getHandle()}" />">${item.getMetadata("dc.title")[0].value!"Untitled"}</a>
    </p>
    </#list>
</#if>
<p>&nbsp;</p>

<#if feedEnabled??>

<div align="center">
    <h4><@dspace.message "ui.community-home.feeds" /></h4>
    <#if feedData??>
        <#assign fmts = feedData.substring(5).split(",")>
        <#list fmts as fmt>
            <#if "rss_1.0" == fmt>
                <#assign icon = "rss1.gif">
                <#assign width = 80>
                <#elseif "rss_2.0" == fmt>
                    <#assign icon = "rss2.gif">
                    <#assign width = 80>
                <#else>
                    <#assign icon = "rss.gif">
                    <#assign width = 36>
            </#if>
            <a href="<@dspace.url "/feed/${fmt}/${community.getHandle()}" />"><img
                    src="<@dspace.url "/image/${icon}" />" alt="RSS Feed" width="${width}" height="15" vspace="3"
                    border="0"/></a>
        </#list>
    </#if>
</div>
</#if>
<#if sidebar??>${sidebar}</#if>
<div align="center">
    <form method="get" action="<@dspace.url "/displaystats" />">
        <input type="hidden" name="handle" value="<#if community??>${community.getHandle()}</#if>"/>
        <input type="submit" name="submit_simple" value="<@dspace.message "ui.community-home.display-statistics" />"/>
    </form>
</div>
</body>
</html>
