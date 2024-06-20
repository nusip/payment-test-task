package kz.maks.paymenttesttask.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class TaxNumberValidator : ConstraintValidator<ValidTaxNumber, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        return when {
            value == null -> false
            value.startsWith("DE") && value.length == 11 -> value.matches(Regex("DE\\d{9}"))
            value.startsWith("IT") && value.length == 13 -> value.matches(Regex("IT\\d{11}"))
            value.startsWith("FR") && value.length == 13 -> value.matches(Regex("FR[A-Z]{2}\\d{9}"))
            value.startsWith("GR") && value.length == 11 -> value.matches(Regex("GR\\d{9}"))
            else -> false
        }
    }
}