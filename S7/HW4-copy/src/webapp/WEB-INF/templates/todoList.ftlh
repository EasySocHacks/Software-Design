<#-- @ftlvariable name="todos" type="easy.soc.hacks.hw4.model.TodoTaskList[]" -->

<#import "common.ftl" as common>
<#import "todoListElement.ftl" as element>

<@common.page>
    <div class="todo-list">
        <#list todos as todoTaskList>
            <@element.todoListElement todoTaskList=todoTaskList/>
        </#list>
    </div>
</@common.page>