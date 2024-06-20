package kz.maks.paymenttesttask.controller

import kz.maks.paymenttesttask.dto.CalculatePriceRequest
import kz.maks.paymenttesttask.dto.ErrorResponse
import kz.maks.paymenttesttask.service.PriceCalculationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/calculate-price")
class PriceController @Autowired constructor(
    private val priceCalculationService: PriceCalculationService
) {
    @PostMapping
    fun calculatePrice(
        @Valid @RequestBody request: CalculatePriceRequest
    ): ResponseEntity<Any> {
        return try {
            val price = priceCalculationService.calculatePrice(request.product, request.taxNumber, request.couponCode)
            ResponseEntity.ok(mapOf("price" to price))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(ErrorResponse(e.message ?: "Invalid input"))
        }
    }
}