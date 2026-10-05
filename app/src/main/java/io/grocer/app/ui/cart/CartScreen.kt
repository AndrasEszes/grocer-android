package io.grocer.app.ui.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.grocer.app.data.CartLine
import io.grocer.app.ui.BackTopBar
import io.grocer.app.ui.formatEuros

@Composable
fun CartScreen(viewModel: CartViewModel, onCheckout: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(topBar = { BackTopBar("Your cart", onBack) }) { padding ->
        if (state.lines.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("🧺", style = MaterialTheme.typography.displayLarge)
                Text("Your cart is empty", modifier = Modifier.testTag("cart.empty"))
            }
            return@Scaffold
        }

        Column(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(modifier = Modifier.weight(1f).testTag("cart.lines")) {
                items(state.lines, key = { it.product.id }) { line ->
                    CartLineRow(line, onQuantityChange = { viewModel.setQuantity(line.product.id, it) })
                    HorizontalDivider()
                }
            }
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = state.promoText,
                        onValueChange = viewModel::onPromoChange,
                        label = { Text("Promo code") },
                        singleLine = true,
                        isError = state.promoError != null,
                        supportingText = state.promoError?.let { { Text(it, modifier = Modifier.testTag("cart.promoError")) } },
                        modifier = Modifier.weight(1f).testTag("cart.promo"),
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(onClick = viewModel::applyPromo, modifier = Modifier.testTag("cart.applyPromo")) {
                        Text("Apply")
                    }
                }
                Spacer(Modifier.height(8.dp))
                SummaryRow("Subtotal", formatEuros(state.quote.subtotalCents), "cart.subtotal")
                if (state.quote.discountCents > 0) {
                    SummaryRow("Discount", "−" + formatEuros(state.quote.discountCents), "cart.discount")
                }
                SummaryRow(
                    "Delivery",
                    if (state.quote.deliveryCents == 0L) "Free" else formatEuros(state.quote.deliveryCents),
                    "cart.delivery",
                )
                SummaryRow("Total", formatEuros(state.quote.totalCents), "cart.total", emphasized = true)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth().testTag("cart.checkout")) {
                    Text("Checkout")
                }
            }
        }
    }
}

@Composable
private fun CartLineRow(line: CartLine, onQuantityChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("cart.line.${line.product.id}"),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(line.product.emoji, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(line.product.name)
            Text(formatEuros(line.totalCents), style = MaterialTheme.typography.bodySmall)
        }
        OutlinedButton(
            onClick = { onQuantityChange(line.quantity - 1) },
            modifier = Modifier.testTag("cart.decrease.${line.product.id}"),
        ) { Text("−") }
        Text("${line.quantity}", modifier = Modifier.padding(horizontal = 12.dp).testTag("cart.quantity.${line.product.id}"))
        OutlinedButton(
            onClick = { onQuantityChange(line.quantity + 1) },
            modifier = Modifier.testTag("cart.increase.${line.product.id}"),
        ) { Text("+") }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, tag: String, emphasized: Boolean = false) {
    val style = if (emphasized) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(label, style = style, modifier = Modifier.weight(1f))
        Text(value, style = style, modifier = Modifier.testTag(tag))
    }
}
