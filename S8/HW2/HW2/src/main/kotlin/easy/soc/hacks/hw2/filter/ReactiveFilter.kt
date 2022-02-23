package easy.soc.hacks.hw2.filter

import easy.soc.hacks.hw2.domain.User
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class ReactiveFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        exchange.session.subscribe { webSession ->
            val user = webSession.getAttribute<Mono<User>>("userMono")?.block()
            if (user != null) {
                webSession.attributes["user"] = user
            }

            webSession.attributes.remove("userMono")

            if (webSession.getAttribute<User>("user") == null)
                exchange.attributes.remove("user")
            else
                exchange.attributes["user"] = webSession.getAttribute<User>("user")
        }

        return chain.filter(exchange)
    }
}