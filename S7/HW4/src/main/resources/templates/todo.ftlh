<#-- @ftlvariable name="todos" type="easy.soc.hacks.hw4.model.TodoTaskList[]" -->

<#import "common.ftl" as common>
<#import "todoList.ftl" as element>

<@common.page>
    <div class="todo-list">
        <#list todos as todoTaskList>
            <@element.todoListElement todoTaskList=todoTaskList/>
        </#list>
        <div class="add-todo-list">
            <b><p>Add task list</p></b>
            <form method="get" name="add-todo-list" action="/addTodoList" autocomplete="off" class="add-todo-list-form">
                <input class="add-todo-list-info" type="text" name="name" placeholder="Name">
                <input class="add-todo-list-submit" type="submit" value="Add">
            </form>
        </div>
    </div>
</@common.page>