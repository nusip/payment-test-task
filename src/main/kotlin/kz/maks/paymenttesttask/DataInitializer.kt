package kz.maks.paymenttesttask

import kz.maks.paymenttesttask.model.Coupon
import kz.maks.paymenttesttask.model.Product
import kz.maks.paymenttesttask.model.User
import kz.maks.paymenttesttask.repository.CouponRepository
import kz.maks.paymenttesttask.repository.ProductRepository
import kz.maks.paymenttesttask.repository.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val productRepository: ProductRepository,
    private val couponRepository: CouponRepository,
    private val userRepository: UserRepository
) {

    @Bean
    fun initProducts(): ApplicationRunner {
        return ApplicationRunner {
            createUsers()
            createProducts()
            createCoupons()
        }
    }

    private fun createUsers() {
        if (!userRepository.existsById(1L)) {
            userRepository.save(User(id = 1L, username = "john"))
        }
    }

    private fun createProducts() {
        if (!productRepository.existsById(1L)) {
            productRepository.save(Product(id = 1L, name = "Iphone", price = 100.0))
        }
        if (!productRepository.existsById(2L)) {
            productRepository.save(Product(id = 2L, name = "Наушники", price = 20.0))
        }
        if (!productRepository.existsById(3L)) {
            productRepository.save(Product(id = 3L, name = "Чехол", price = 10.0))
        }
    }

    private fun createCoupons() {
        if (couponRepository.findByCode("P15") == null) {
            couponRepository.save(Coupon(code = "P15", isPercentage = true, discount = 15.0))
        }
        if (couponRepository.findByCode("P50") == null) {
            couponRepository.save(Coupon(code = "P50", isPercentage = true, discount = 50.0))
        }
        if (couponRepository.findByCode("F10") == null) {
            couponRepository.save(Coupon(code = "F10", isPercentage = false, discount = 10.0))
        }
    }
}