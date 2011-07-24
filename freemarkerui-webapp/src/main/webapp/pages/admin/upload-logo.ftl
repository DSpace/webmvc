<#import "/includes/dspace.ftl" as dspace />

<h1><@dspace.message "ui.dspace-admin.upload-logo.title" /></h1>

     <#if collection??>
      <#assign showingArgs=[collection.getMetadata("name")]>
      <h1><@dspace.messageArgs "ui.dspace-admin.upload-logo.select.col", showingArgs /></h1>
     <#else>
      <#if community??>
      <#assign showingArgs=[community.getMetadata("name")]>
      <h1><@dspace.messageArgs "ui.dspace-admin.upload-logo.select.com", showingArgs /></h1></#if>
     </#if>

<form method="post" enctype="multipart/form-data" action="<@dspace.url "/admin/editcommunities"/>">
        <p align="center">
            <input type="file" size="40" name="file"/>
        </p>

 <#if community??><input type="hidden" name="community_id" value="${community.getID()}" /></#if>
 <#if collection??><input type="hidden" name="collection_id" value="${collection.getID()}" /></#if>

        <%-- <p align="center"><input type="submit" name="submit" value="Upload"/></p> --%>
        <p align="center"><input type="submit" name="submit_upload" value="<@dspace.message "ui.dspace-admin.general.upload" />" /></p>
    </form>