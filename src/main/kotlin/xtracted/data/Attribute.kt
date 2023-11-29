package xtracted.data

data class Attribute<T : Any>(val caption: String,
                              val value : T,
                              val valueAsText : String = "",
                              val isValid: Boolean = true)
