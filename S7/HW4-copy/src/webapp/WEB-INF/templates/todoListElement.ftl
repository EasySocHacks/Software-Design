<#-- @ftlvariable name="todoTaskList" type="easy.soc.hacks.hw4.model.TodoTaskList" -->

<#import "totoTask.ftl" as element>

<#macro todoListElement todoTaskList>
    <div class="todo-list-element">
        <h5>${todoTaskList.name}</h5>
        <#list todoTaskList.todoTasks as todoTask>
            <@element.totoElement todoTask=todoTask/>
        </#list>
    </div>
</#macro>