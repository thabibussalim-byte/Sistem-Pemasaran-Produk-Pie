package com.example.zliepie.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.zliepie.data.dataclass.CartItem
import com.example.zliepie.data.dataclass.User
import com.example.zliepie.data.repository.AppDatabase
import com.example.zliepie.data.repository.Product
import com.example.zliepie.data.repository.ZLiePieRepository
import kotlinx.coroutines.launch

class ZLiePieViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ZLiePieRepository
    val allProducts: LiveData<List<Product>>
    val cartItems: LiveData<List<CartItem>>

    private val _loginStatus = MutableLiveData<Result<User>?>()
    val loginStatus: LiveData<Result<User>?> = _loginStatus

    private val _registerStatus = MutableLiveData<Result<Unit>?>()
    val registerStatus: LiveData<Result<Unit>?> = _registerStatus

    init {
        val appDao = AppDatabase.getDatabase(application).appDao()
        repository = ZLiePieRepository(appDao)
        allProducts = repository.allProducts.asLiveData()
        cartItems = repository.cartItems.asLiveData()
        
        viewModelScope.launch {
            repository.syncProducts()
        }
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginStatus.value = Result.failure(Exception("Username dan password harus diisi"))
            return
        }
        
        viewModelScope.launch {
            val result = repository.login(username, password)
            _loginStatus.value = result
        }
    }

    fun register(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _registerStatus.value = Result.failure(Exception("Username dan password tidak boleh kosong"))
            return
        }

        viewModelScope.launch {
            val result = repository.register(username, password)
            _registerStatus.value = result
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            repository.addToCart(product)
        }
    }

    fun updateCartItem(item: CartItem) {
        viewModelScope.launch {
            repository.updateCartItem(item)
        }
    }

    fun removeFromCart(item: CartItem) {
        viewModelScope.launch {
            repository.deleteCartItem(item)
        }
    }

    fun checkout() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun resetLoginStatus() {
        _loginStatus.value = null
    }

    fun resetRegisterStatus() {
        _registerStatus.value = null
    }

    class Factory(private val mApplication: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ZLiePieViewModel::class.java)) {
                return ZLiePieViewModel(mApplication) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
