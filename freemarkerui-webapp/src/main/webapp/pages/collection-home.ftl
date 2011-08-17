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
<#assign currentCollection=collection />
<html>
<head>
    <title>${collection.getName()!"Untitled"}</title>
</head>
<body>
<!--<#include "/viewers/collection.ftl" />-->
<table border="0" cellpadding="5" width="100%">
    <tr>
        <td width="100%">
            <h1>
            <#if name??>${name}</#if>
        <#if strengthsShow?? && ic?? && collection?? && strengthsShow==true>
            ${ic.getCount(collection)}
            </#if>
            </h1>

            <h3><@dspace.message "ui.collection-home.heading1" /></h3>
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
                                    value="<#if community??>${community.getHandle()}</#if>"><#if communityName??>${communityName}</#if></option>
                            <option selected="selected"
                                    value="<#if collection??>${collection.getHandle()}</#if>"><#if name??>${name}</#if></option>
                        </select></td>
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
            <form method="get" action="<@dspace.url relativeUrl="/handle/${collection.getHandle()}/browse"/>">
                <input type="hidden" name="type" value="${bi.getName()}"/>
                <td><input type="submit" name="submit_browse" value="<@dspace.message "${key}" />"/></td>
            </form>
        </#list>
    </tr>
</#if>
</table>

<table width="100%" align="center" cellspacing="10">
    <tr>
        <td>
        <#if can_submit_button?? && can_submit_button==true>
            <center>
                <form action="<@dspace.url "/submit"/>" method="post">
                    <input type="hidden" name="collection" value="<#if collection??>${collection.getID()}</#if>"/>
                    <input type="submit" name="submit" value="<@dspace.message "ui.collection-home.submit.button" />"/>
                </form>
            </center>
        </#if>
        </td>
        <td class="oddRowEvenCol">
            <form method="get" action="<@dspace.url "/subscribe"/>">
                <table>
                    <tr>
                    <td class="standard">
                    <#if loggedin?? && subscribed?? && loggedin==true && subscribed==true>
                        <small>
                        <@dspace.message "ui.collection-home.subscribed" />
                            <a href="<@dspace.url "/subscribe"/>">
                            <@dspace.message "ui.collection-home.info" />
                            </a></small>
                    </td>
                    <td class="standard">
                        <input type="submit" name="submit_unsubscribe"
                               value="<@dspace.message "ui.collection-home.unsub" />"/>
                        <#else>
                            <small>
                            <@dspace.message "ui.collection-home.subscribe.msg" />
                            </small>
                        </td>
                        <td class="standard">
                            <input type="submit" name="submit_subscribe"
                                   value="<@dspace.message "ui.collection-home.subscribe" />"/>
                    </#if>
                    </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>

<div align="center">
    <form method="get" action="<@dspace.url "/displaystats" />">
        <input type="hidden" name="handle" value="<#if collection??>${collection.getHandle()}</#if>"/>
        <input type="submit" name="submit_simple" value="<@dspace.message "ui.collection-home.display-statistics" />"/>
    </form>
</div>
<#if intro??>${intro}</#if>
<p class="copyrightText">
<#if copyright??>${copyright}</#if></p>

<#if admin_button?? && editor_button?? && (admin_button==true || editor_button==true )>
<table class="miscTable" align="center">
    <tr>
        <td class="evenRowEvenCol" colspan="2">
            <table>
                <tr>
                    <th id="t1" class="standard">
                        <strong><@dspace.message "ui.admintools" /></strong>
                    </th>
                </tr>

                <#if editor_button?? && editor_button==true>
                    <tr>
                        <td headers="t1" class="standard" align="center">
                            <form method="post" action="<@dspace.url "/admin/editcommunities"/>">
                                <input type="hidden" name="collection_id"
                                       value="<#if collection??>${collection.getID()}</#if>"/>
                                <input type="hidden" name="community_id"
                                       value="<#if community??>${community.getID()}</#if>"/>
                                <input type="submit" name="submit_edit_collection"
                                       value="<@dspace.message "ui.general.edit.button" />"/>
                            </form>
                        </td>
                    </tr>
                </#if>

                <#if admin_button?? &&  admin_button==true>
                    <tr>
                        <td headers="t1" class="standard" align="center">
                            <form method="post" action="<@dspace.url "/tools/itemmap"/>">
                                <input type="hidden" name="cid" value="<#if collection??>${collection.getID()}</#if>"/>
                                <input type="submit" name="item_mapper" value="<@dspace.message "ui.collection-home.item.button" />"/>
                            </form>
                        </td>
                    </tr>
                    <#if submitters??>
                        <tr>
                            <td headers="t1" class="standard" align="center">
                                <form method="get" action="<@dspace.url "/tools/group-edit"/>">
                                    <input type="hidden" name="group_id"
                                           value="<#if submitters??>${submitters.getID()}</#if>"/>
                                    <input type="submit" name="submit_edit"
                                           value="<@dspace.message "ui.collection-home.editsub.button" />"/>
                                </form>
                            </td>
                        </tr>
                    </#if>
                    <#if admin_button?? && editor_button?? && (admin_button==true || editor_button==true )>
                        <tr>
                            <td headers="t1" class="standard" align="center">
                                <form method="post" action="<@dspace.url "/mydspace"/>">
                                    <input type="hidden" name="collection_id"
                                           value="<#if collection??>${collection.getID()}</#if>"/>
                                    <input type="hidden" name="step" value="5" />
                                    <input type="submit" value="<@dspace.message "ui.mydspace.request.export.collection" />"/>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td headers="t1" class="standard" align="center">
                                <form method="post" action="<@dspace.url "/mydspace"/>">
                                    <input type="hidden" name="collection_id"
                                           value="<#if collection??>${collection.getID()}</#if>"/>
                                    <input type="hidden" name="step" value="6" />
                                    <input type="submit" value="<@dspace.message "ui.mydspace.request.export.migratecollection" />"/>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td headers="t1" class="standard" align="center">
                                <form method="post" action="<@dspace.url "/dspace-admin/metadataexport"/>">
                                    <input type="hidden" name="handle"
                                           value="<#if collection??>${collection.getHandle()}</#if>"/>
                                    <input type="submit"
                                           value="<@dspace.message "ui.general.metadataexport.button" />"/>
                                </form>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td headers="t1" class="standard" align="center">
                            <a href="<@dspace.url "/help.collection-admin.html" />"
                               onClick="javascript: popup_window(this, 'group_popup'); return false;"><@dspace.message "ui.adminhelp" /></a>
                        </td>
                    </tr>
                </#if>
            </table>
        </td>
    </tr>
</table>
</#if>

<h3><@dspace.message "ui.collection-home.recentsub" /></h3>

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
    <h4><@dspace.message "ui.collection-home.feeds" /></h4>
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
            <#if collection??>
            <a href="<@dspace.url "/feed/${fmt}/${collection.getHandle()}" />"><img
                    src="<@dspace.url "/image/${icon}" />" alt="RSS Feed" width="${width}" height="15" vspace="3"
                    border="0"/></a></#if>
        </#list>
    </#if>
</div>
</#if>

<#if sidebar??>${sidebar}</#if>


</body>
</html>
