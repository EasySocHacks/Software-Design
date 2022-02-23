package easy.soc.hacks.hw2.service

import easy.soc.hacks.hw2.domain.Product
import easy.soc.hacks.hw2.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService {
    @Autowired
    private lateinit var productRepository: ProductRepository

    fun getAllProducts() = productRepository.findAll()

    fun addProduct(product: Product) = productRepository.save(product)
}