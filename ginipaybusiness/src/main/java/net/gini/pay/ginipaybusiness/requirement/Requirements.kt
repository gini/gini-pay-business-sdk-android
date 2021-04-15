package net.gini.pay.ginipaybusiness.requirement

import android.content.pm.PackageManager
import net.gini.pay.ginipaybusiness.review.bank.getBanks

sealed class Requirement {
    object NoBank : Requirement()
}

internal fun internalCheckRequirements(packageManager: PackageManager): List<Requirement> = mutableListOf<Requirement>().apply {
    if (!atLeastOneBank(packageManager)) add(Requirement.NoBank)
}

private fun atLeastOneBank(packageManager: PackageManager): Boolean = packageManager.getBanks().isNotEmpty()