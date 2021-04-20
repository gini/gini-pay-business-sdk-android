package net.gini.pay.ginipaybusiness.review.model

import net.gini.pay.ginipaybusiness.review.bank.BankApp

data class PaymentRequest(
    val id: String,
    val bankApp: BankApp,
)