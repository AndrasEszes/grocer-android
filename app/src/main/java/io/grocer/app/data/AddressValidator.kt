package io.grocer.app.data

enum class AddressField { FullName, Street, City, PostalCode }

object AddressValidator {
    private val postalCode = Regex("^\\d{4,5}$")

    fun validate(address: Address): Set<AddressField> = buildSet {
        if (address.fullName.isBlank()) add(AddressField.FullName)
        if (address.street.isBlank()) add(AddressField.Street)
        if (address.city.isBlank()) add(AddressField.City)
        if (!postalCode.matches(address.postalCode.trim())) add(AddressField.PostalCode)
    }
}
