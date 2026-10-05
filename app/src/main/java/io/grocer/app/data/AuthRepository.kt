package io.grocer.app.data

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Boolean
}

class FakeAuthRepository : AuthRepository {
    override suspend fun signIn(email: String, password: String): Boolean =
        email.trim().equals(DEMO_EMAIL, ignoreCase = true) && password == DEMO_PASSWORD

    companion object {
        const val DEMO_EMAIL = "demo@grocer.app"
        const val DEMO_PASSWORD = "grocer123"
    }
}
