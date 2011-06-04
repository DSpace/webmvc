<html>
   <head>
       <title>Welcome!</title>
   </head>
   <body>
       <h1>
            Welcome ${name}<#if name = "Big Joe">, our beloved leader</#if>!
       </h1>
       <p>URL: <a href="${url}">${url}</a>

        <table border=1>
                <tr><th>Chosen Products<th></tr>
                <#list types as being>
                    <tr><td>${being}</td></tr>
                </#list>
        </table> 
   </body>
</html>  
