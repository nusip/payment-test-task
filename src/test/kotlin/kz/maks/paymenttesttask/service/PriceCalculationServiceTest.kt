package kz.maks.paymenttesttask.service

import kz.maks.paymenttesttask.model.Coupon
import kz.maks.paymenttesttask.model.Product
import kz.maks.paymenttesttask.repository.CouponRepository
import kz.maks.paymenttesttask.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class PriceCalculationServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var couponRepository: CouponRepository

    @InjectMocks
    private lateinit var priceCalculationService: PriceCalculationService

    @Test
    fun `should calculate price without coupon`() {
        val product = Product(1L, "Iphone", 100.0)
        `when`(productRepository.findById(1L)).thenReturn(Optional.of(product))

        val price = priceCalculationService.calculatePrice(1L, "DE123456789", null)

        assertEquals(119.0, price)
    }

    @Test
    fun `should calculate price with percentage coupon`() {
        val product = Product(1L, "Iphone", 100.0)
        val coupon = Coupon(1L, "P10", true, 10.0)
        `when`(productRepository.findById(1L)).thenReturn(Optional.of(product))
        `when`(couponRepository.findByCode("P10")).thenReturn(coupon)

        val price = priceCalculationService.calculatePrice(1L, "DE123456789", "P10")

        assertEquals(107.1, price)
    }

    @Test
    fun `should calculate price with fixed discount coupon`() {
        val product = Product(1L, "Iphone", 100.0)
        val coupon = Coupon(1L, "D15", false, 15.0)
        `when`(productRepository.findById(1L)).thenReturn(Optional.of(product))
        `when`(couponRepository.findByCode("D15")).thenReturn(coupon)

        val price = priceCalculationService.calculatePrice(1L, "DE123456789", "D15")

        assertEquals(101.15, price)
    }

    @Test
    fun `should throw exception for invalid product`() {
        `when`(productRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows(IllegalArgumentException::class.java) {
            priceCalculationService.calculatePrice(1L, "DE123456789", null)
        }

        assertEquals("Product not found", exception.message)
    }

    @Test
    fun `should throw exception for invalid coupon`() {
        val product = Product(1L, "Iphone", 100.0)
        `when`(productRepository.findById(1L)).thenReturn(Optional.of(product))
        `when`(couponRepository.findByCode("INVALID")).thenReturn(null)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            priceCalculationService.calculatePrice(1L, "DE123456789", "INVALID")
        }

        assertEquals("Invalid coupon code", exception.message)
    }

    @Test
    fun `should throw exception for invalid tax number`() {
        val product = Product(1L, "Iphone", 100.0)
        `when`(productRepository.findById(1L)).thenReturn(Optional.of(product))

        val exception = assertThrows(IllegalArgumentException::class.java) {
            priceCalculationService.calculatePrice(1L, "XX123456789", null)
        }

        assertEquals("Invalid tax number format", exception.message)
    }
}
