package com.music.topalbums

object Utilities
{
    fun getResourceAsText(path: String): String? = this::class.java.classLoader.getResource(path).readText()

}