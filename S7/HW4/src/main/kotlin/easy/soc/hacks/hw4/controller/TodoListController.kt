package easy.soc.hacks.hw4.controller

import easy.soc.hacks.hw4.model.TodoTask
import easy.soc.hacks.hw4.model.TodoTaskList
import easy.soc.hacks.hw4.utils.DatabaseUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletRequest

@Controller
class TodoListController {
    @GetMapping("/", "")
    fun todos(request: HttpServletRequest, view: MutableMap<String, List<TodoTaskList>>): String {

        val todoTaskListDbo = DatabaseUtils.statement.executeQuery(
            "select * from TodoTaskList"
        )
        val todoList = mutableListOf<TodoTaskList>()

        while (todoTaskListDbo.next()) {
            todoList.add(
                TodoTaskList(
                    todoTaskListDbo.getInt("id"),
                    todoTaskListDbo.getString("name"),
                    mutableListOf()
                )
            )
        }

        for (todoTaskList in todoList) {
            val todoTaskListTodoTaskJoinTableDbo = DatabaseUtils.statement.executeQuery(
                "select * from TodoTaskList_TodoTask where todo_task_list_id = ${todoTaskList.id};"
            )

            val relatedTodoTaskIds = mutableListOf<Int>()

            while (todoTaskListTodoTaskJoinTableDbo.next()) {
                relatedTodoTaskIds.add(todoTaskListTodoTaskJoinTableDbo.getInt("todo_task_id"))
            }

            for (todoTaskId in relatedTodoTaskIds) {
                val todoTaskDbo = DatabaseUtils.statement.executeQuery(
                    "select * from TodoTask where id = ${todoTaskId};"
                )

                if (todoTaskDbo.next()) {
                    val todoTaskName = todoTaskDbo.getString("name")
                    val todoTaskDescription = todoTaskDbo.getString("description")
                    val todoTaskDone = todoTaskDbo.getBoolean("done")

                    todoTaskList.todoTasks.add(
                        TodoTask(
                            todoTaskId,
                            todoTaskName,
                            todoTaskDescription,
                            todoTaskDone
                        )
                    )
                }
            }
        }

        view["todos"] = todoList
        return "todo";
    }
}