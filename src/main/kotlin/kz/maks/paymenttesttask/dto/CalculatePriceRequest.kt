package kz.maks.paymenttesttask.dto

import kz.maks.paymenttesttask.validation.ValidTaxNumber
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CalculatePriceRequest(
    @field:NotNull
    val product: Long,

    @field:NotBlank
    @field:ValidTaxNumber
    val taxNumber: String,

    val couponCode: String? = null
)