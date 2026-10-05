package io.grocer.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.grocer.app.data.SampleCatalog
import io.grocer.app.ui.cart.CartScreen
import io.grocer.app.ui.cart.CartViewModel
import io.grocer.app.ui.catalog.CatalogScreen
import io.grocer.app.ui.catalog.CatalogViewModel
import io.grocer.app.ui.checkout.CheckoutScreen
import io.grocer.app.ui.checkout.CheckoutViewModel
import io.grocer.app.ui.confirmation.ConfirmationScreen
import io.grocer.app.ui.login.LoginScreen
import io.grocer.app.ui.login.LoginViewModel
import io.grocer.app.ui.product.ProductDetailScreen

@Composable
fun GrocerNavHost(container: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(viewModel { LoginViewModel(container.auth) }) {
                navController.navigate("catalog") { popUpTo("login") { inclusive = true } }
            }
        }
        composable("catalog") {
            CatalogScreen(
                viewModel = viewModel { CatalogViewModel(SampleCatalog.products, container.cart) },
                onProductClick = { navController.navigate("product/${it.id}") },
                onCartClick = { navController.navigate("cart") },
            )
        }
        composable("product/{id}") { entry ->
            val product = SampleCatalog.byId(entry.arguments?.getString("id").orEmpty()) ?: return@composable
            ProductDetailScreen(
                product = product,
                onAddToCart = { quantity ->
                    container.cart.add(product, quantity)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable("cart") {
            CartScreen(
                viewModel = viewModel { CartViewModel(container.cart) },
                onCheckout = { navController.navigate("checkout") },
                onBack = { navController.popBackStack() },
            )
        }
        composable("checkout") {
            CheckoutScreen(
                viewModel = viewModel { CheckoutViewModel(container.cart, container.orders) },
                onOrderPlaced = { order ->
                    navController.navigate("confirmation/${order.number}") { popUpTo("catalog") }
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable("confirmation/{orderNumber}") { entry ->
            ConfirmationScreen(entry.arguments?.getString("orderNumber").orEmpty()) {
                navController.popBackStack("catalog", inclusive = false)
            }
        }
    }
}
