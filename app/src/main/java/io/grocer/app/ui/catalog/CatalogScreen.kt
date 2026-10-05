package io.grocer.app.ui.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.grocer.app.data.Category
import io.grocer.app.data.Product
import io.grocer.app.ui.formatEuros

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grocer") },
                actions = {
                    TextButton(onClick = onCartClick, modifier = Modifier.testTag("catalog.cart")) {
                        BadgedBox(badge = {
                            if (state.cartCount > 0) {
                                Badge { Text("${state.cartCount}", modifier = Modifier.testTag("catalog.cartBadge")) }
                            }
                        }) {
                            Text("🛒 Cart")
                        }
                    }
                },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                placeholder = { Text("Search groceries") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("catalog.search"),
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Category.entries.forEach { category ->
                    FilterChip(
                        selected = state.category == category,
                        onClick = { viewModel.onCategorySelected(category) },
                        label = { Text(category.title) },
                        modifier = Modifier.testTag("catalog.category.${category.name}"),
                    )
                }
            }
            if (state.products.isEmpty()) {
                Text(
                    "No groceries match \"${state.query}\"",
                    modifier = Modifier.padding(24.dp).testTag("catalog.empty"),
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize().testTag("catalog.list"),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.products, key = { it.id }) { product ->
                    ProductRow(product, onClick = { onProductClick(product) }, onAdd = { viewModel.addToCart(product) })
                }
            }
        }
    }
}

@Composable
private fun ProductRow(product: Product, onClick: () -> Unit, onAdd: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick)
            .testTag("catalog.product.${product.id}"),
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(product.emoji, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text("${formatEuros(product.priceCents)} · ${product.unit}", style = MaterialTheme.typography.bodySmall)
            }
            FilledTonalButton(onClick = onAdd, modifier = Modifier.testTag("catalog.add.${product.id}")) {
                Text("Add")
            }
        }
    }
}
