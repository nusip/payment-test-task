package kz.maks.paymenttesttask.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [TaxNumberValidator::class])
annotation class ValidTaxNumber(
    val message: String = "Invalid tax number",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)