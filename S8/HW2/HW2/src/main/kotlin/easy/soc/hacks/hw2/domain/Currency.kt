package easy.soc.hacks.hw2.domain

enum class Currency(
    val proportion: Double
) {
    RUB(0.013),
    USD(1.0),
    EUR(1.13);

    companion object {
        val DEFAULT_CURRENCY = USD

        fun exchangeRate(from: Currency, to: Currency) = from.proportion / to.proportion
    }
}
