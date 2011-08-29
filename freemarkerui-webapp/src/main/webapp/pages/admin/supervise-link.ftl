<#import "/includes/dspace.ftl" as dspace />


<h1><@dspace.message "ui.dspace-admin.supervise-link.heading" /></h1>

<h3><@dspace.message "ui.dspace-admin.supervise-link.choose" /></h3>

<form method="post" action="<@dspace.url "/admin/supervise" />">

    <table>

        <tr>
            <td>
                 <b><@dspace.message "ui.dspace-admin.supervise-link.group" /></b>
                 <select name="TargetGroup">
                  <#if groups??>
                  <#list groups as grp>
                    <option value="${grp.getID()}">${grp.getName()}</option>
                  </#list>
                  </#if>
                  </select>
                  <br/><br/>
            </td>
        </tr>

        <tr>
        <td>
            <b><@dspace.message "ui.dspace-admin.supervise-link.policy" /></b>
            <select name="PolicyType">
                <#if policynone?? && policyeditor?? && policyobserver??>
                <option value="${policynone}" selected="selected"><@dspace.message "ui.dspace-admin.supervise-link.policynone" /></option>
                <option value="${policyeditor}"><@dspace.message "ui.dspace-admin.supervise-link.policyeditor" /></option>
                <option value="${policyobserver}"><@dspace.message "ui.dspace-admin.supervise-link.policyobserver" /></option>
                </#if>
             </select>
            <br/><br/>
        </td>
        </tr>

        <tr>
            <td>
                <b><@dspace.message "ui.dspace-admin.supervise-link.workspace" /></b>
                <br/><br/>
                <div align="left">
                <table class="miscTable">
                <tr>
                    <th class="odRowOddCol"><@dspace.message "ui.dspace-admin.supervise-link.id" /></th>
                    <th class="oddRowEvenCol"><@dspace.message "ui.dspace-admin.supervise-link.submittedby" /></th>
                    <th class="oddRowOddCol"><@dspace.message "ui.dspace-admin.supervise-link.title" /></th>
                    <th class="oddRowEvenCol"><@dspace.message "ui.dspace-admin.supervise-link.submittedto" /></th>
                    <th class="oddRowOddCol"><@dspace.message "ui.dspace-admin.supervise-link.select" /></th>
                </tr>

                <#assign row = "even">
                <#if wsItems??>
                <#list wsItems as wsItem>
                <#if itemany??>
                <#assign titleArray = wsItem.getItem().getDC("title", null, itemany)>
                </#if>
                <#assign submitter = wsItem.getItem().getSubmitter()>
                <tr>
                    <td class="${row}RowOddCol">
                        ${wsItem.getID()}
                    </td>
                    <td class="${row}RowEvenCol">
                        <a href="mailto:${submitter.getEmail()}">${submitter.getFullName()}</a>
                    </td>
                    <td class="${row}RowOddCol">
                        <#if titleArray?size &gt; 0>
                         ${titleArray[0].value}
                         <#else>
                         <@dspace.message "ui.general.untitled" />
                        </#if>
                    </td>
                    <td class="${row}RowEvenCol">
                        ${wsItem.getCollection().getMetadata("name")}
                    </td>
                    <td class="${row}RowOddCol" align="center">
                        <input type="radio" name="TargetWSItem" value="${wsItem.getID()}"/>
                    </td>
                </tr>
                <#if row = "even">
                <#assign row = "odd">
                <#else>
                <#assign row = "even">
                </#if>
                </#list>
                </#if>
                </table>
                </div>
            <br/><br/>
            </td>
        </tr>

        <tr>
        <td>
            <input type="submit" name="submit_link" value="<@dspace.message "ui.dspace-admin.supervise-link.submit.button" />"/>
            <input type="submit" name="submit_base" value="<@dspace.message "ui.dspace-admin.general.cancel" />"/>
        </td>
        </tr>

    </table>

</form>