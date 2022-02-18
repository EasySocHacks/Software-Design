package easy.soc.hacks.hw4.controller

import easy.soc.hacks.hw4.model.TodoTask
import easy.soc.hacks.hw4.model.TodoTaskList
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletRequest

@Controller
class TodoListController {
    @GetMapping("/", "")
    fun todoList(request: HttpServletRequest, view: MutableMap<String, List<TodoTaskList>>): String {
        view["todos"] = listOf(
            TodoTaskList("TODO 1",
                listOf(
                    TodoTask("name1", "desc1")
                )),
            TodoTaskList("TODO 1",
                listOf(
                    TodoTask("name2", "desc2"),
                    TodoTask("name3", "desc3")
                ))
        )
        return "todoList";
    }
}