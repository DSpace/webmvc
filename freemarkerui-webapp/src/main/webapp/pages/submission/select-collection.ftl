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
    Select a collection for the submission
-->
<#import "/includes/dspace.ftl" as dspace />
<html>
    <head>
        <title>Submission: Select a Collection</title>
    </head>
    <body>
        <h1>Submission: Select a Collection</h1>

        <p>Select the collection you wish to submit an item to from the list below, then click "Next".</p>

        <form method="post">
            <table>
                <tr>
                    <td>
                        Collection
                    </td>
                    <td>
                        <select name="collectionHandle" title="Select the collection you wish to submit an item to.">
                            <option value="">Select a collection...</option>
                            <#list collections as collection>
                                <option value="${collection.getHandle()}">${collection.getName()}</option>
                            </#list>
                        </select>
                        <span class="field-help">Select the collection you wish to submit an item to.</span>
                    </td>
                </tr>
            </table>
            <p>
                <input class="ds-button-field" name="submit" type="submit" value="Next">
            </p>
        </form>


    </body>
</html>