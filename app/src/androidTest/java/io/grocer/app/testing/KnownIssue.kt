package io.grocer.app.testing

/**
 * A test that reproduces an open bug and is expected to fail until the bug is fixed.
 * The default UI test run leaves these out with the `notAnnotation` runner argument.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class KnownIssue(val description: String)
