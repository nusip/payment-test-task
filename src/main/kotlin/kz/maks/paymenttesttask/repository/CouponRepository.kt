package kz.maks.paymenttesttask.repository

import kz.maks.paymenttesttask.model.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, Long> {
    fun findByCode(code: String): Coupon?
}