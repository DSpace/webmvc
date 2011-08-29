<#import "/includes/dspace.ftl" as dspace />

<h2><@dspace.message "ui.tools.itemmap-main.heading" /></h2>

<p><#if collection??><#assign showingArgs=[collection.getMetadata("name")]>
<@dspace.messageArgs "ui.tools.itemmap-main.collection", showingArgs /></#if></p>

<p><#if count_native??&&count_import??><#assign showingArgs=["${count_native}","${count_import}"]>
<@dspace.messageArgs "ui.tools.itemmap-main.info1", showingArgs /></#if></p>

<h3><@dspace.message "ui.tools.itemmap-main.info4" /></h3>
<@dspace.message "ui.tools.itemmap-main.info5" /><br/>

<form method="post" action="<@dspace.url "/tools/itemmap"/>">
    <input type="hidden" name="cid" value="<#if collection??>${collection.getID()}</#if>"/>
    <input name="namepart"/>
    <input type="submit" name="search_authors" value="<@dspace.message "ui.tools.itemmap-main.search.button" />"/>
    <br/>
</form>

	<h3><@dspace.message "ui.tools.itemmap-main.info6" /></h3>
	<p><@dspace.message "ui.tools.itemmap-main.info7" /></p>

<#assign row = "even">

<#if colKeys?? && colKeys.hasNext()==false>
    <p><@dspace.message "ui.tools.itemmap-main.info8" /></p>
</#if>
<#if colKeys?? && collection_counts??>
<#list colKeys as ck>
    <#assign myCollection = [collections.get(ck.next())]>
    <#assign myTitle = [myCollection.getMetadata("name")]>
    <#if collection??><#assign cid = [collection.getID()]></#if>
    <#assign myID = [myCollection.getID()]>
    <#assign myTitle = [myCollection.getMetadata("name")]>
    <#assign myCount = [collection_counts.get(new Integer(myID)).intValue()]>

    <p align="center">
        <a href= "<@dspace.url "/tools/itemmap/browse/${cid}/${myID}"/>">${myTitle}<#if myCount??>(${myCount})</#if></a>
    </p>
</#list></#if>


