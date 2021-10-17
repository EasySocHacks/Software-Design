package easy.soc.hacks.hw4.controller

import easy.soc.hacks.hw4.utils.DatabaseUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TodoTaskController {
    @GetMapping("/check")
    fun checkTask(@RequestParam("id") id: Int): String {

        DatabaseUtils.statement.executeUpdate(
            "update TodoTask set done = true where id = ${id};"
        )

        return "redirect:";
    }

    @GetMapping("/uncheck")
    fun uncheckTask(@RequestParam("id") id: Int): String {

        DatabaseUtils.statement.executeUpdate(
            "update TodoTask set done = false where id = ${id};"
        )

        return "redirect:";
    }

    @GetMapping("/deleteTask")
    fun deleteTask(@RequestParam("id") id: Int): String {

        DatabaseUtils.statement.executeUpdate(
            "delete from TodoTaskList_TodoTask where todo_task_id = ${id};"
        )
        DatabaseUtils.statement.executeUpdate(
            "delete from TodoTask where id = ${id};"
        )

        return "redirect:";
    }

    @GetMapping("/addTask")
    fun addTask(
        @RequestParam("id") id: Int,
        @RequestParam("name") name: String,
        @RequestParam("description") description: String
    ): String {


        try {
            DatabaseUtils.statement.executeUpdate(
                "insert into TodoTask (name, description) values " +
                        "('${name}', '${description}');"
            )
        } catch (e: Exception) {
            return "redirect:"
        }

        val todoTaskDbo = DatabaseUtils.statement.executeQuery(
            "select * from TodoTask where name = '${name}';"
        )
        
        if (todoTaskDbo.next()) {
            val todoTaskId = todoTaskDbo.getInt("id")

            DatabaseUtils.statement.executeUpdate(
                "insert into TodoTaskList_TodoTask (todo_task_id, todo_task_list_id) values " +
                        "(${todoTaskId}, ${id});"
            )
        }

        return "redirect:";
    }
}