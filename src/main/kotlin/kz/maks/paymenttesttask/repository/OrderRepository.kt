package kz.maks.paymenttesttask.repository

import kz.maks.paymenttesttask.model.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>
