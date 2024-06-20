package kz.maks.paymenttesttask.repository

import kz.maks.paymenttesttask.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>