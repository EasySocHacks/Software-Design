package easy.soc.hacks.hw2.repository

import easy.soc.hacks.hw2.domain.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : ReactiveMongoRepository<User, String> {
    fun getUserById(id: Long): Mono<User>

    fun getUserByLoginAndPassword(login: String, password: String): Mono<User>
}