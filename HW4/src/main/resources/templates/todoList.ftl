<#-- @ftlvariable name="todoTaskList" type="easy.soc.hacks.hw4.model.TodoTaskList" -->

<#import "totoTask.ftl" as element>

<#macro todoListElement todoTaskList>
    <#assign done=true/>
    <#list todoTaskList.todoTasks as todoTask>
        <#if !todoTask.done>
            <#assign done=false/>
        </#if>
    </#list>

    <#if todoTaskList.todoTasks?size == 0>
        <#assign done=false/>
    </#if>

    <#if done>
        <div class="todo-list-element-done">
            <div class="todo-list-element-name">
                <h4 class="todo-list-element-name-title">${todoTaskList.name}</h4>
                <form class="delete-todo-task-list" method="get" name="delete-todo-task-list" action="/deleteTodoList">
                    <input type="submit" value="Delete">
                    <input type="hidden" name="id" value="${todoTaskList.id}">
                </form>
            </div>
            <#list todoTaskList.todoTasks as todoTask>
                <@element.todoTask todoTask=todoTask/>
            </#list>
            <div>
                <b><p>Add task</p></b>
                <form method="get" name="addTask" autocomplete="off" class="add-todo-task" action="/addTask">
                    <div class="add-task-info">
                        <input type="text" placeholder="Name" name="name">
                        <textarea rows="7" class="add-task-info-description" placeholder="Description" name="description"></textarea>
                    </div>
                    <input type="submit" value="Add task" class="add-todo-submit">
                    <input type="hidden" name="id" value="${todoTaskList.id}">
                </form>
            </div>
        </div>
    <#else>
        <div class="todo-list-element">
            <div class="todo-list-element-name">
                <h4 class="todo-list-element-name-title">${todoTaskList.name}</h4>
                <form class="delete-todo-task-list" method="get" name="delete-todo-task-list" action="/deleteTodoList">
                    <input type="submit" value="Delete">
                    <input type="hidden" name="id" value="${todoTaskList.id}">
                </form>
            </div>
            <#list todoTaskList.todoTasks as todoTask>
                <@element.todoTask todoTask=todoTask/>
            </#list>
            <div>
                <b><p>Add task</p></b>
                <form method="get" name="addTask" autocomplete="off" class="add-todo-task" action="/addTask">
                    <div class="add-task-info">
                        <input type="text" placeholder="Name" name="name">
                        <textarea rows="7" class="add-task-info-description" placeholder="Description" name="description"></textarea>
                    </div>
                    <input type="submit" value="Add task" class="add-todo-submit">
                    <input type="hidden" name="id" value="${todoTaskList.id}">
                </form>
            </div>
        </div>
    </#if>
</#macro>