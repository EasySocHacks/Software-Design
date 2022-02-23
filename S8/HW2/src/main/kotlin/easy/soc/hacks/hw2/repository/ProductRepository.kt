package easy.soc.hacks.hw2.repository

import easy.soc.hacks.hw2.domain.Product
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: ReactiveMongoRepository<Product, String>