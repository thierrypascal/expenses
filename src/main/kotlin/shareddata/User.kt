package shareddata

data class User(val id: Int,
                val name: String,
                val password: String,
                val supervisor: Boolean)
