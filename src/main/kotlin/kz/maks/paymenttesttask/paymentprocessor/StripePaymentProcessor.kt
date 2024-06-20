package kz.maks.paymenttesttask.paymentprocessor

import org.springframework.stereotype.Component

@Component
class StripePaymentProcessor : PaymentProcessor {
    override fun getName(): String = "stripe"

    override fun processPayment(amount: Double): Boolean {
        return amount >= 100
    }
}