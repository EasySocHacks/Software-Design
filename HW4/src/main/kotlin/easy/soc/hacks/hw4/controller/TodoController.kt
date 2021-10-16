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
}