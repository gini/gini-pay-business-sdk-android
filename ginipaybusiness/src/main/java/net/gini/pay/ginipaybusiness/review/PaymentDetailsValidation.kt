package net.gini.pay.ginipaybusiness.review

import net.gini.pay.ginipaybusiness.review.model.PaymentDetails
import net.gini.pay.ginipaybusiness.util.isValidIban

enum class PaymentField { Recipient, Iban, Amount, Purpose }

sealed class ValidationMessage(val field: PaymentField) {
    class Empty(field: PaymentField): ValidationMessage(field)
    object InvalidIban: ValidationMessage(PaymentField.Iban)
    object InvalidCurrency: ValidationMessage(PaymentField.Amount)
    object NoCurrency: ValidationMessage(PaymentField.Amount)
    object AmountFormat: ValidationMessage(PaymentField.Amount)
}

fun PaymentDetails.validate(): List<ValidationMessage> = mutableListOf<ValidationMessage>().apply {
    addAll(validateRecipient(this@validate.recipient))
    addAll(validateIban(this@validate.iban))
    addAll(validateAmount(this@validate.amount))
    addAll(validatePurpose(this@validate.purpose))
}

fun validateRecipient(recipient: String): List<ValidationMessage> = mutableListOf<ValidationMessage>().apply {
    if (recipient.trim().isEmpty()) add(ValidationMessage.Empty(PaymentField.Recipient))
}

fun validateIban(iban: String): List<ValidationMessage> = mutableListOf<ValidationMessage>().apply {
    if (iban.trim().isEmpty()) add(ValidationMessage.Empty(PaymentField.Iban))
    if (!isValidIban(iban.trim())) add(ValidationMessage.InvalidIban)
}

fun validateAmount(amount: String): List<ValidationMessage> = mutableListOf<ValidationMessage>().apply {
    if (amount.trim().isEmpty()) add(ValidationMessage.Empty(PaymentField.Amount))
}

fun validatePurpose(purpose: String): List<ValidationMessage> = mutableListOf<ValidationMessage>().apply {
    if (purpose.trim().isEmpty()) add(ValidationMessage.Empty(PaymentField.Purpose))
}
