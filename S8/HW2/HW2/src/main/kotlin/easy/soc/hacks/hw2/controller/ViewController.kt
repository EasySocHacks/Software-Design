package easy.soc.hacks.hw2.controller

import easy.soc.hacks.hw2.domain.*
import easy.soc.hacks.hw2.domain.Currency.Companion.DEFAULT_CURRENCY
import easy.soc.hacks.hw2.domain.Currency.Companion.exchangeRate
import easy.soc.hacks.hw2.service.ProductService
import easy.soc.hacks.hw2.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.server.WebSession
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Flux

@Controller
class ViewController {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var productService: ProductService

    @GetMapping("/")
    fun index(webSession: WebSession): String {
        return "index"
    }

    @GetMapping("/register")
    fun registerGet(): String {
        return "register"
    }

    @PostMapping("/register")
    fun registerPost(@ModelAttribute formUser: FormUser, webSession: WebSession, model: Model): String {
        try {
            val user = userService.registerUser(
                User().apply {
                    login = formUser.login!!
                    password = formUser.password!!
                    currency = formUser.currency!!
                }).share()

            webSession.attributes["userMono"] = user
        } catch (e: Exception) {
            model["error"] = "Smth went wrong"

            return "redirect:/register"
        }

        return "redirect:/"
    }

    @GetMapping("/login")
    fun loginGet(): String {
        return "login"
    }

    @PostMapping("/login")
    fun loginPost(@ModelAttribute formUser: FormUser, webSession: WebSession, model: Model): String {
        try {
            val user = userService.loginUser(formUser.login!!, formUser.password!!).share()

            webSession.attributes["userMono"] = user
        } catch (e: Exception) {
            model["error"] = "Smth went wrong"

            return "redirect:/login"
        }

        return "redirect:/"
    }

    @GetMapping("/logout")
    fun logout(webSession: WebSession): String {
        webSession.attributes.remove("user")

        return "redirect:/"
    }

    @GetMapping("/products")
    fun products(model: Model, webSession: WebSession): String {
        val user = webSession.getAttribute<User>("user")
        if (user != null) {
            val products: IReactiveDataDriverContextVariable =
                ReactiveDataDriverContextVariable(
                    productService.getAllProducts()
                        .flatMap {
                            Flux.just<Product>(Product().apply {
                                id = it.id
                                name = it.name
                                description = it.description
                                price = it.price * exchangeRate(DEFAULT_CURRENCY, user.currency)
                            })
                        }
                        .share(),
                    100
                )
            model.addAttribute("products", products)
        } else {
            model["error"] = "You have to be signed in to view this page"
        }

        return "products"
    }

    @GetMapping("/addProduct")
    fun addProductGet(model: Model, webSession: WebSession): String {
        if (webSession.getAttribute<User>("user") == null) {
            model["error"] = "You have to be signed in to view this page"
        }

        return "addProduct"
    }

    @PostMapping("/addProduct")
    fun addProductPost(@ModelAttribute formProduct: FormProduct, webSession: WebSession, model: Model): String {
        val user = webSession.getAttribute<User>("user")
        if (user != null) {
            try {
                productService.addProduct(Product().apply {
                    name = formProduct.name!!
                    description = formProduct.description ?: ""
                    price = formProduct.price!! * Currency.exchangeRate(user.currency, DEFAULT_CURRENCY)
                }).subscribe {}
            } catch (e: Exception) {
                model["error"] = "Smth went wrong"

                return "redirect:/addProduct"
            }
        }

        return "redirect:/products"
    }
}