<#macro todoTask todoTask>
    <#if todoTask.done>
        <div class="todo-task checked">
            <div class="task-info">
                <label>
                    <input type="checkbox" checked disabled/>
                    <s><b>${todoTask.name}</b></s>
                </label>
                <div class="todo-task-description">
                    ${todoTask.description}
                </div>
            </div>
            <div class="check-uncheck-delete-button">
                <div>
                    <form method="get" name="uncheck" action="/uncheck">
                        <input type="submit" value="Uncheck">
                        <input type="hidden" name="id" value="${todoTask.id}">
                    </form>
                </div>
                <div class="delete">
                    <form method="get" name="delete-todo-task" action="/deleteTask">
                        <input type="submit" value="Delete">
                        <input type="hidden" name="id" value="${todoTask.id}">
                    </form>
                </div>
            </div>
        </div>
    <#else>
        <div class="todo-task unchecked">
            <div class="task-info">
                <label>
                    <input type="checkbox" disabled/>
                    <b>${todoTask.name}</b>
                </label>
                <div class="todo-task-description">
                    ${todoTask.description}
                </div>
            </div>
            <div class="check-uncheck-delete-button">
                <div>
                    <form method="get" name="check" action="/check">
                        <input type="submit" value="Check">
                        <input type="hidden" name="id" value="${todoTask.id}">
                    </form>
                </div>
                <div class="delete">
                    <form method="get" name="delete-todo-task" action="/deleteTask">
                        <input type="submit" value="Delete">
                        <input type="hidden" name="id" value="${todoTask.id}">
                    </form>
                </div>
            </div>
        </div>
    </#if>
</#macro>