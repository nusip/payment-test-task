package kz.maks.paymenttesttask.service

import kz.maks.paymenttesttask.repository.CouponRepository
import kz.maks.paymenttesttask.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PriceCalculationService @Autowired constructor(
    private val productRepository: ProductRepository,
    private val couponRepository: CouponRepository
) {
    fun calculatePrice(productId: Long, taxNumber: String, couponCode: String?): Double {
        val product = productRepository.findById(productId)
            .orElseThrow { IllegalArgumentException("Product not found") }

        var price = product.price

        couponCode?.let {
            val coupon = couponRepository.findByCode(it)
                ?: throw IllegalArgumentException("Invalid coupon code")
            price = if (coupon.isPercentage) {
                price - price * (coupon.discount / 100)
            } else {
                price - coupon.discount
            }
        }

        val taxRate = getTaxRate(taxNumber)
        return price + price * taxRate
    }

    private fun getTaxRate(taxNumber: String): Double {
        return when {
            taxNumber.startsWith("DE") -> 0.19
            taxNumber.startsWith("IT") -> 0.22
            taxNumber.startsWith("FR") -> 0.20
            taxNumber.startsWith("GR") -> 0.24
            else -> throw IllegalArgumentException("Invalid tax number format")
        }
    }
}