package io.grocer.app.ui.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import io.grocer.app.data.Product
import io.grocer.app.ui.BackTopBar
import io.grocer.app.ui.formatEuros

@Composable
fun ProductDetailScreen(product: Product, onAddToCart: (Int) -> Unit, onBack: () -> Unit) {
    var quantity by rememberSaveable { mutableIntStateOf(1) }

    Scaffold(topBar = { BackTopBar(product.name, onBack) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(product.emoji, style = MaterialTheme.typography.displayLarge)
            Spacer(Modifier.height(16.dp))
            Text(product.name, style = MaterialTheme.typography.headlineSmall)
            Text("${formatEuros(product.priceCents)} · ${product.unit}", modifier = Modifier.testTag("product.price"))
            Spacer(Modifier.height(8.dp))
            Text(product.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = { quantity = (quantity - 1).coerceAtLeast(1) },
                    modifier = Modifier.testTag("product.decrease"),
                ) { Text("−") }
                Text("$quantity", style = MaterialTheme.typography.titleLarge, modifier = Modifier.testTag("product.quantity"))
                OutlinedButton(onClick = { quantity++ }, modifier = Modifier.testTag("product.increase")) { Text("+") }
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { onAddToCart(quantity) },
                modifier = Modifier.fillMaxWidth().testTag("product.addToCart"),
            ) {
                Text("Add to cart · ${formatEuros(product.priceCents * quantity)}")
            }
        }
    }
}
