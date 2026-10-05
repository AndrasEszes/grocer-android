package io.grocer.app.ui

fun formatEuros(cents: Long): String = "€%d.%02d".format(cents / 100, cents % 100)
