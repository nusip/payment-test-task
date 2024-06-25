package kz.maks.paymenttesttask.paymentprocessor

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PaymentProcessorConfig {

    @Bean
    fun paymentProcessorList(
        paypalProcessor: PaypalProcessor,
        stripeProcessor: StripePaymentProcessor
    ): List<PaymentProcessor> {
        return listOf(paypalProcessor, stripeProcessor)
    }
}