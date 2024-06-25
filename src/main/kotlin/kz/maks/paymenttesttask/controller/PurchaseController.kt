package kz.maks.paymenttesttask.controller

import kz.maks.paymenttesttask.auth.AuthUser
import kz.maks.paymenttesttask.auth.IsAuthenticated
import kz.maks.paymenttesttask.dto.ErrorResponse
import kz.maks.paymenttesttask.dto.PurchaseRequest
import kz.maks.paymenttesttask.service.PurchaseService
import kz.maks.paymenttesttask.service.PriceCalculationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/purchase")
class PurchaseController @Autowired constructor(
    private val priceCalculationService: PriceCalculationService,
    private val purchaseService: PurchaseService
) {
    @IsAuthenticated
    @PostMapping
    fun purchase(
        @Valid @RequestBody request: PurchaseRequest,
        @AuthenticationPrincipal authUser: AuthUser
    ): ResponseEntity<Any> {
        return try {
            val price = priceCalculationService.calculatePrice(request.product, request.taxNumber, request.couponCode)
            purchaseService.purchase(authUser.id, request.product, request.paymentProcessor, price)
            ResponseEntity.ok().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(ErrorResponse(e.message ?: "Invalid input"))
        } catch (e: RuntimeException) {
            ResponseEntity.badRequest().body(ErrorResponse(e.message ?: "Payment error"))
        }
    }
}