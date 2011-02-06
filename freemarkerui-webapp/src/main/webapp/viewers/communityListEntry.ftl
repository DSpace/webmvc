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
    Viewer for communities in browse / search lists.
    Expects including template to have set a value for 'currentCommunity', which is the community to render.
-->
<#import "/includes/dspace.ftl" as dspace />
<div class="artifact-title">
    <a href="<@dspace.url relativeUrl="/handle/${currentCommunity.getHandle()}" />"><span class="z3988" title="">${currentCommunity.getName()!"Untitled"}</span></a>
</div>
<#if currentCommunity.getMetadata("short_description")??>
    <div class="artifact-info">
        <span class="short-description">${currentCommunity.getMetadata("short_description")}</span>
    </div>
</#if>
