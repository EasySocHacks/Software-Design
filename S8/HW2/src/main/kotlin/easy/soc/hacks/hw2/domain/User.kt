package easy.soc.hacks.hw2.domain

import lombok.Getter
import lombok.Setter
import javax.persistence.*

@Entity
@Table
@Getter
@Setter
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    lateinit var id: String

    @Column(unique = true)
    lateinit var login: String

    @Column
    lateinit var password: String

    @Column
    lateinit var currency: Currency
}

data class FormUser(
    var id: String?,
    var login: String?,
    var password: String?,
    var currency: Currency?
)