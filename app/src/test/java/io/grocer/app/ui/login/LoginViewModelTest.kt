package io.grocer.app.ui.login

import io.grocer.app.data.FakeAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = LoginViewModel(FakeAuthRepository())
    }

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `the demo account signs in`() {
        viewModel.onEmailChange(FakeAuthRepository.DEMO_EMAIL)
        viewModel.onPasswordChange(FakeAuthRepository.DEMO_PASSWORD)

        viewModel.signIn()

        assertTrue(viewModel.state.value.signedIn)
    }

    @Test
    fun `a wrong password shows an error`() {
        viewModel.onEmailChange(FakeAuthRepository.DEMO_EMAIL)
        viewModel.onPasswordChange("nope")

        viewModel.signIn()

        assertFalse(viewModel.state.value.signedIn)
        assertEquals("Wrong email or password", viewModel.state.value.error)
    }

    @Test
    fun `typing clears the previous error`() {
        viewModel.onEmailChange(FakeAuthRepository.DEMO_EMAIL)
        viewModel.onPasswordChange("nope")
        viewModel.signIn()

        viewModel.onPasswordChange("nope2")

        assertEquals(null, viewModel.state.value.error)
    }
}
