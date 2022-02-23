package easy.soc.hacks.hw2.service

import easy.soc.hacks.hw2.domain.User
import easy.soc.hacks.hw2.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun getAllUsers() = userRepository.findAll()

    fun getUserById(id: Long) = userRepository.getUserById(id)

    fun loginUser(login: String, password: String) =
        userRepository.getUserByLoginAndPassword(login, password)

    fun registerUser(user: User) = userRepository.save(user)
}