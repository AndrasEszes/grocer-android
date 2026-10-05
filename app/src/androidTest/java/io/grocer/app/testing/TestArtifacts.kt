package io.grocer.app.testing

import android.os.Build
import android.os.Process
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString
import androidx.test.core.app.takeScreenshot
import androidx.test.core.graphics.writeToTestStorage
import androidx.test.services.storage.TestStorage
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Saves screenshots and failure diagnostics under the file names Bitrise uses to link
 * attachments to test cases on the Tests tab:
 *
 *     <classname>__<name>__<label>.<ext>
 *
 * The files go to Test Storage, and AGP pulls them into
 * `build/outputs/connected_android_test_additional_output/`.
 *
 * Declare it as an inner rule of the compose rule (a higher `order`), so [failed] runs
 * before the activity is torn down. As the outer rule, the failure screenshot would show
 * the launcher instead of the screen that failed.
 */
class TestArtifacts(private val compose: ComposeTestRule) : TestWatcher() {
    private lateinit var testCase: String
    private var step = 0

    override fun starting(description: Description) {
        testCase = cleaned(description.className) + "__" + cleaned(description.methodName)
        step = 0
    }

    fun screenshot(label: String) {
        compose.waitForIdle()
        step++
        takeScreenshot().writeToTestStorage(fileName("%02d-%s".format(step, label)))
    }

    override fun failed(e: Throwable, description: Description) {
        runCatching { takeScreenshot().writeToTestStorage(fileName("failure")) }
        runCatching { write(fileName("failure-ui") + ".txt", compose.onRoot(useUnmergedTree = true).printToString(Int.MAX_VALUE)) }
        runCatching { write(fileName("failure-logcat") + ".log", logcat()) }
    }

    private fun fileName(label: String): String {
        val safeLabel = cleaned(label).replace(Regex("_{2,}"), "_").trimStart('_')
        return "${testCase}__$safeLabel-api${Build.VERSION.SDK_INT}"
    }

    private fun write(fileName: String, content: String) {
        if (content.isBlank()) return
        TestStorage().openOutputFile(fileName).use { it.write(content.toByteArray()) }
    }

    private fun logcat(): String {
        val process = ProcessBuilder("logcat", "-d", "-v", "threadtime", "--pid", Process.myPid().toString())
            .redirectErrorStream(true)
            .start()
        return process.inputStream.bufferedReader().use { it.readText() }
    }

    private companion object {
        private val reservedCharacters = Regex("""["*/:<>?\\|]""")

        fun cleaned(value: String) = value.replace(reservedCharacters, "_")
    }
}
