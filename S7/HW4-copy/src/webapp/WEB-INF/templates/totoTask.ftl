<#macro totoElement todoTask>
    <div class="todo-element">
        <label>
            <input type="checkbox"/>
            ${todoTask.name}
        </label>
        ${todoTask.description}
    </div>
</#macro>