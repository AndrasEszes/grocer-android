package io.grocer.app.ui.confirmation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmationScreen(orderNumber: String, onContinueShopping: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("✅", style = MaterialTheme.typography.displayLarge)
        Text("Order placed", style = MaterialTheme.typography.headlineMedium)
        Text("Order number $orderNumber", modifier = Modifier.testTag("confirmation.orderNumber"))
        Text("Your groceries arrive today between 17:00 and 19:00.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onContinueShopping, modifier = Modifier.fillMaxWidth().testTag("confirmation.continue")) {
            Text("Continue shopping")
        }
    }
}
