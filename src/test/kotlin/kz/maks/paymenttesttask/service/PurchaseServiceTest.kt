package kz.maks.paymenttesttask.service

import kz.maks.paymenttesttask.model.Order
import kz.maks.paymenttesttask.model.Product
import kz.maks.paymenttesttask.model.User
import kz.maks.paymenttesttask.paymentprocessor.PaypalProcessor
import kz.maks.paymenttesttask.repository.OrderRepository
import kz.maks.paymenttesttask.repository.ProductRepository
import kz.maks.paymenttesttask.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class PurchaseServiceTest {

    @Mock
    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var paypalProcessor: PaypalProcessor

    private lateinit var purchaseService: PurchaseService

    @BeforeEach
    fun setUp() {
        `when`(paypalProcessor.getName()).thenReturn("paypal")

        // Manually create the PurchaseService with the list of mocked PaymentProcessors
        purchaseService = PurchaseService(
            listOf(paypalProcessor),
            productRepository,
            orderRepository,
            userRepository
        )
    }

    @Test
    fun `should throw exception for invalid payment processor`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            purchaseService.purchase(1L, 1L, "invalidProcessor", 100.0)
        }

        assertEquals("Invalid payment processor", exception.message)
    }

    @Test
    fun `should throw exception for invalid product`() {
        `when`(productRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows(IllegalArgumentException::class.java) {
            purchaseService.purchase(1L, 1L, "paypal", 100.0)
        }

        assertEquals("Invalid product", exception.message)
    }

    @Test
    fun `should process payment and create order`() {
        val user = User(1L, "testUser")
        val product = Product(1L, "Iphone", 100.0)
        `when`(userRepository.getReferenceById(1L)).thenReturn(user)
        `when`(productRepository.findById(1L)).thenReturn(Optional.of(product))
        `when`(paypalProcessor.processPayment(100.0)).thenReturn(true)

        purchaseService.purchase(1L, 1L, "paypal", 100.0)

        verify(orderRepository).save(argThat {
            it.user == user && it.product == product && it.totalPrice == 100.0
        })
    }

    @Test
    fun `should not create order if payment fails`() {
        val product = Product(1L, "Iphone", 100.0)
        `when`(productRepository.findById(1L)).thenReturn(Optional.of(product))
        `when`(paypalProcessor.processPayment(100.0)).thenReturn(false)

        purchaseService.purchase(1L, 1L, "paypal", 100.0)

        verify(orderRepository, never()).save(any(Order::class.java))
    }
}
