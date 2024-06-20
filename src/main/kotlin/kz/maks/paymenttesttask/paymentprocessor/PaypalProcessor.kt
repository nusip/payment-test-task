package kz.maks.paymenttesttask.paymentprocessor

import org.springframework.stereotype.Component

@Component
class PaypalProcessor : PaymentProcessor {
    override fun getName(): String = "paypal"

    override fun processPayment(amount: Double): Boolean {
        if (amount > 100000) {
            throw RuntimeException("Amount exceeds limit for Paypal")
        }
        return true
    }
}