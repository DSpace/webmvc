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
<div id="ds-footer-wrapper">
    <div id="ds-footer">
        <div id="ds-footer-left">
            <p><@dspace.message "ui.layout.footer-default.text" /></p>
        </div>
        <div id="ds-footer-right">
            <span class="theme-by">Theme by&nbsp;</span>
            <a id="ds-footer-logo-link" href="http://www.openrepository.com" target="_blank" title="Open Repository">
                <span id="ds-footer-logo">&nbsp;</span>
            </a>
        </div>
        <div id="ds-footer-links">
            <a href="<@dspace.url "/contact" />"><@dspace.message "ui.layout.footer-default.contact" /></a> | <a href="<@dspace.url "/feedback" />"><@dspace.message "ui.layout.footer-default.feedback" /></a>
        </div>
    </div>
</div>
