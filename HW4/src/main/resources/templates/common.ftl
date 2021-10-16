<#import "/spring.ftl" as spring/>

<#macro header>
    <div id="header">
        Header
    </div>
</#macro>

<#macro footer>
    <div id="footer">
        Footer
    </div>
</#macro>

<#macro page>
    <!DOCTYPE html>
    <html lang="en">
        <head>
            <link rel="stylesheet" type="text/css" href="common.css">
            <link rel="stylesheet" type="text/css" href="todo.css">
            <link rel="stylesheet" type="text/css" href="todoList.css">
            <link rel="stylesheet" type="text/css" href="todoTask.css">
            <meta charset="UTF-8">
            <title>Todo List</title>
        </head>
        <body>
        <@header/>
        <div class="middle">
            <main id="main">
                <#nested/>
            </main>
        </div>
        <@footer/>
        </body>
    </html>
</#macro>