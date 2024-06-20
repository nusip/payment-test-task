package kz.maks.paymenttesttask.paymentprocessor

interface PaymentProcessor {
    fun getName(): String
    fun processPayment(amount: Double): Boolean
}