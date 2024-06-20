package kz.maks.paymenttesttask.service

import kz.maks.paymenttesttask.model.Order
import kz.maks.paymenttesttask.paymentprocessor.PaymentProcessor
import kz.maks.paymenttesttask.repository.OrderRepository
import kz.maks.paymenttesttask.repository.ProductRepository
import kz.maks.paymenttesttask.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PurchaseService @Autowired constructor(
    paymentProcessorList: List<PaymentProcessor>,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) {
    private val paymentProcessors: Map<String, PaymentProcessor> = paymentProcessorList.associateBy { it.getName() }

    fun purchase(userId: Long, productId: Long, processorName: String, amount: Double) {
        val processor = paymentProcessors[processorName]
            ?: throw IllegalArgumentException("Invalid payment processor")

        val product = productRepository.findByIdOrNull(productId)
            ?: throw IllegalArgumentException("Invalid product")

        if (processor.processPayment(amount)) {
            val order = Order(
                user = userRepository.getReferenceById(userId),
                product = product,
                totalPrice = amount
            )
            orderRepository.save(order)
        }
    }
}