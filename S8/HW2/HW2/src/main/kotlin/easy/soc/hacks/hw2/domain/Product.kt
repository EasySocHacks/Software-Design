package easy.soc.hacks.hw2.domain

import lombok.Getter
import lombok.Setter
import javax.persistence.*

@Table
@Entity
@Getter
@Setter
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    lateinit var id: String

    @Column
    lateinit var name: String

    @Column
    lateinit var description: String

    /**
     * Note: price in USD currency
     */
    @Column
    var price: Double = 0.0
}

data class FormProduct(
    var id: String?,
    var name: String?,
    var description: String?,
    var price: Double?
)
