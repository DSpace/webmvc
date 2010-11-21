<#ftl strip_whitespace=true>
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
 * This is a collection of common utility macros that will be widely used.
 * Primarily, these pass through to other macro files (ie Spring), or tag libraries
 * By providing an access through a common file, we reduce the amount of includes required
 * in most user's templates, and provide a means to swap out some of the functionality if necessary.
-->
<#--
 * The assingment below would allow us to use a JSP Tag Library,
 * And the macro provides an example of using JSTL for internationalized messages
 * <#-- assign fmt=JspTaglibs["http://java.sun.com/jsp/jstl/fmt"] / -->
 * <#-- macro message key><@fmt.message key="${key}" /></#macro -->
-->
<#--
 * Spring FreeMarker macros - first import the file so we can reference it as part of a namespace
 * secondly, import it, so that all macros defined within it, become visible to anyone importing *this* file
-->
<#import  "/spring.ftl" as spring />
<#include "/spring.ftl" />

<#assign themeName><#attempt><@theme "theme.name" /><#recover></#attempt></#assign>
<#assign themeTemplatePath><#attempt><@theme "theme.template.path" /><#recover></#attempt></#assign>

<#--
 * Override macros that we included above, where we need to alter the operation
-->
<#--
 * Spring will throw an Exception if the message is missing, with unsightly rendering the result
 * Using the attempt directive, we can catch these problems and replicate the behaviour of the JSTL tags
-->
<#macro message code><#attempt><@spring.message code="${code}" /><#recover>???${code}???</#attempt></#macro>
<#macro messageArgs code, args><#attempt><@spring.messageArgs code="${code}" args="${args}" /><#recover>???${code}???</#attempt></#macro>

<#--
 * Combines the functionality of the spring.url and spring.theme macros, allowing the retrieval of a url from the
 * theme properties, but including the current request context.
-->
<#macro themeUrl code>
    <@spring.url relativeUrl="${themeTemplatePath}${code}" />
</#macro>

<#macro themeUrlKey code>
    <#local codeUrl><@spring.theme code="${code}" /></#local>
    <@spring.url relativeUrl="${codeUrl}" />
</#macro>

<#macro themeStyleSheet sheet, media="screen">
    <link type="text/css" rel="stylesheet" media="${media}" href="<@spring.url relativeUrl="${themeTemplatePath}${sheet}" />" />
</#macro>

<#macro themeIcon icon>
    <link rel="shortcut icon" href="<@spring.url relativeUrl="${themeTemplatePath}${icon}" />" />
</#macro>

<#macro messageOrString code="" str="">
    <#if str != "">
        ${str}
    <#elseif code != "">
        <@message "${code}" />
    <#else>
        <span class="error">messageOrString: no key or string supplied</span>
    </#if>
</#macro>

<#macro processMetadata item, field>
    <#local dcvalues=item.getMetadata(field) />
    <#if dcalues?? || dcvalues?size == 0>
    <#else>
        <#nested dcvalues />
    </#if>
</#macro>
