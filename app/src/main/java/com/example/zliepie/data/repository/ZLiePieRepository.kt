package com.example.zliepie.data.repository

import com.example.zliepie.R
import com.example.zliepie.data.dao.AppDao
import com.example.zliepie.data.dataclass.CartItem
import com.example.zliepie.data.dataclass.User
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class ZLiePieRepository(private val appDao: AppDao) {

    val allProducts: Flow<List<Product>> = appDao.getAllProducts()
    val cartItems: Flow<List<CartItem>> = appDao.getCartItems()

    suspend fun login(username: String, password: String): Result<User> {
        return try {
            val user = appDao.getUserByUsername(username)
            if (user != null) {
                if (user.password == password) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Password salah"))
                }
            } else {
                Result.failure(Exception("Username tidak terdaftar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, password: String): Result<Unit> {
        return try {
            val existingUser = appDao.getUserByUsername(username)
            if (existingUser != null) {
                Result.failure(Exception("Username sudah digunakan"))
            }else {
                appDao.insertUser(User(username = username, password = password))
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addToCart(product: Product) {
        val cartItem = CartItem(
            productId = product.id,
            productName = product.name,
            quantity = 1,
            price = product.price
        )
        appDao.addToCart(cartItem)
    }

    suspend fun updateCartItem(item: CartItem) {
        appDao.updateCartItem(item)
    }

    suspend fun deleteCartItem(item: CartItem) {
        appDao.deleteCartItem(item)
    }

    suspend fun clearCart() {
        appDao.clearCart()
    }

    suspend fun syncProducts() {
        try {
            val remoteProducts = listOf(
                Product(
                    1,
                    "Classic Apple Pie",
                    "Pie apel klasik dengan kayu manis",
                    25000.0,
                    R.drawable.pie_classic_apple
                ),
                Product(
                    2,
                    "Chocolate Pie",
                    "Pie cokelat lumer yang nikmat",
                    30000.0,
                    R.drawable.pie_chocolate
                ),
                Product(
                    3,
                    "Strawberry Pie",
                    "Pie buah strawberry segar",
                    28000.0,
                    R.drawable.pie_strawberry
                ),
                Product(
                    4,
                    "Blueberry Pie",
                    "Pie blueberry manis asam",
                    32000.0,
                    R.drawable.pie_lueberry
                )
            )
            appDao.insertProducts(remoteProducts)
        } catch (e: IOException) {
        }
    }
}