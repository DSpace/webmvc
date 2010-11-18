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
<#macro box class id titleKey="" titleStr="">
    <div class="${class}">
        <h2><@dspace.messageOrString code="${titleKey}" str="${titleStr}" /></h2>
        <div id="${id}" class="boxContent">
            <i>Demonstrating import overrides in a jar/theme with parent</i><br/><br/>
            <#nested>
        </div>
    </div>
</#macro>
