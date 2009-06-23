<#import "/includes/dspace.ftl" as dspace />
<div id="ds-footer">
    <a href="http://dspace.org" id="ds-logo-link">
        <span id="ds-footer-logo"> </span>
    </a>
    <p><@dspace.message "ui.layout.footer-default.text" /></p>
    <div id="ds-footer-links">
        <a href="<@dspace.url "/contact" />"><@dspace.message "ui.layout.footer-default.contact" /></a> | <a href="<@dspace.url "/feedback" />"><@dspace.message "ui.layout.footer-default.feedback" /></a>
    </div>
</div>
