package io.grocer.app.data

import org.junit.Assert.assertEquals
import org.junit.Test

class AddressValidatorTest {
    private val valid = Address("Anna Kovács", "Andrássy út 12", "Budapest", "1061")

    @Test
    fun `a complete address is valid`() {
        assertEquals(emptySet<AddressField>(), AddressValidator.validate(valid))
    }

    @Test
    fun `blank fields are reported`() {
        val invalid = AddressValidator.validate(Address(" ", "", "Budapest", "1061"))

        assertEquals(setOf(AddressField.FullName, AddressField.Street), invalid)
    }

    @Test
    fun `postal codes have 4 or 5 digits`() {
        assertEquals(setOf(AddressField.PostalCode), AddressValidator.validate(valid.copy(postalCode = "106")))
        assertEquals(setOf(AddressField.PostalCode), AddressValidator.validate(valid.copy(postalCode = "BUD-1")))
        assertEquals(emptySet<AddressField>(), AddressValidator.validate(valid.copy(postalCode = "10115")))
    }
}
