package easy.soc.hacks.hw4.controller

import easy.soc.hacks.hw4.utils.DatabaseUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TodoController {
    @GetMapping("/addTodoList")
    fun addTodoList(@RequestParam("name") name: String): String {
        DatabaseUtils.statement.executeUpdate(
            "insert into TodoTaskList (name) values ('${name}');"
        )

        return "redirect:"
    }

    @GetMapping("/deleteTodoList")
    fun deleteTodoList(@RequestParam("id") id: Int): String {
        DatabaseUtils.statement.executeUpdate(
            "delete from TodoTaskList_TodoTask where todo_task_list_id = ${id};"
        )
        DatabaseUtils.statement.executeUpdate(
            "delete from TodoTaskList where id = ${id};"
        )

        return "redirect:"
    }
}