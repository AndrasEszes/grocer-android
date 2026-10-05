package io.grocer.app.ui.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.grocer.app.data.AddressField
import io.grocer.app.data.Order
import io.grocer.app.ui.BackTopBar
import io.grocer.app.ui.formatEuros

@Composable
fun CheckoutScreen(viewModel: CheckoutViewModel, onOrderPlaced: (Order) -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.placedOrder) {
        state.placedOrder?.let(onOrderPlaced)
    }

    Scaffold(topBar = { BackTopBar("Delivery address", onBack) }) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            val address = state.address
            AddressTextField("Full name", address.fullName, AddressField.FullName, state, "checkout.name") {
                viewModel.onAddressChange(address.copy(fullName = it))
            }
            AddressTextField("Street and number", address.street, AddressField.Street, state, "checkout.street") {
                viewModel.onAddressChange(address.copy(street = it))
            }
            AddressTextField("City", address.city, AddressField.City, state, "checkout.city") {
                viewModel.onAddressChange(address.copy(city = it))
            }
            AddressTextField(
                "Postal code",
                address.postalCode,
                AddressField.PostalCode,
                state,
                "checkout.postalCode",
                errorText = "Enter a 4 or 5 digit postal code",
                keyboardType = KeyboardType.Number,
            ) {
                viewModel.onAddressChange(address.copy(postalCode = it))
            }
            Spacer(Modifier.height(16.dp))
            Text("Total to pay: ${formatEuros(state.totalCents)}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.testTag("checkout.total"))
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = viewModel::placeOrder,
                enabled = !state.placing,
                modifier = Modifier.fillMaxWidth().testTag("checkout.placeOrder"),
            ) {
                Text("Place order")
            }
        }
    }
}

@Composable
private fun AddressTextField(
    label: String,
    value: String,
    field: AddressField,
    state: CheckoutState,
    tag: String,
    errorText: String = "Required",
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
) {
    val invalid = field in state.invalidFields
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = invalid,
        supportingText = if (invalid) {
            { Text(errorText, modifier = Modifier.testTag("$tag.error")) }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag(tag),
    )
}

