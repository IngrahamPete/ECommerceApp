package com.pete.ecommerceapp

import android.content.Context
import com.pete.domain.di.model.UserDomainModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ShopperSession :KoinComponent{
    val context: Context by inject()

    fun storeUser(user: UserDomainModel) {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("id", user.id!!)
            putString("username", user.username)
            putString("email", user.email)
            putString("name", user.name)
            apply()
        }
    }

    fun getUser(): UserDomainModel? {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = sharedPref.getInt("id", 0)
        val username = sharedPref.getString("username", null)
        val email = sharedPref.getString("email", null)
        val name = sharedPref.getString("name", null)
        return if (id != 0 && username != null && email != null && name != null) {
            UserDomainModel(id, username, email, name)
        } else {
            null
        }
    }

    fun logout() {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }
}