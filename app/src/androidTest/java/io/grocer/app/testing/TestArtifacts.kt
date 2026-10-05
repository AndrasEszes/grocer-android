package io.grocer.app.testing

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
 * Declare it as an inner rule of the compose rule (a higher `order`), so [failed] runs
 * before the activity is torn down. As the outer rule, the failure screenshot would show
 * the launcher instead of the screen that failed.
 */
class TestArtifacts(private val compose: ComposeTestRule) : TestWatcher() {
    private lateinit var testCase: String
    private var step = 0

    override fun starting(description: Description) {
        testCase = "${description.className}__${description.methodName}"
        step = 0
    }

    fun screenshot(label: String) {
        compose.waitForIdle()
        step++
        takeScreenshot().writeToTestStorage("${testCase}__%02d-%s".format(step, label))
    }

    override fun failed(e: Throwable, description: Description) {
        takeScreenshot().writeToTestStorage("${testCase}__failure")
        write("${testCase}__failure-logcat.log", logcat())
        write("${testCase}__failure-ui.txt", compose.onRoot(useUnmergedTree = true).printToString(Int.MAX_VALUE))
    }

    private fun write(fileName: String, content: String) {
        TestStorage().openOutputFile(fileName).use { it.write(content.toByteArray()) }
    }

    private fun logcat(): String {
        val process = ProcessBuilder("logcat", "-d", "--pid", Process.myPid().toString()).start()
        return process.inputStream.bufferedReader().use { it.readText() }
    }
}
