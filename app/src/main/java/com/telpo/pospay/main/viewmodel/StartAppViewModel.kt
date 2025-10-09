package com.telpo.pospay.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.telpo.base.model.BaseViewModel
import com.telpo.base.util.MLog
import com.telpo.pospay.AppContext
import com.telpo.pospay.db.bean.UserEntry
import com.telpo.pospay.db.repository.UserRepository
import com.telpo.pospay.main.data.GlobalParams
import com.telpo.pospay.main.task.TaskDownParameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StartAppViewModel(private val repository: UserRepository) : BaseViewModel() {


    private val _count = MutableSharedFlow<Int>()
    val count: Flow<Int> = _count

    protected val _loginResult = MutableStateFlow<Boolean?>(null)
    val loginResult: StateFlow<Boolean?> = _loginResult

    fun initUser() {
        viewModelScope.launch {

            repository.getUserById("00")?:repository.addUser(
                UserEntry(
                    userId = "00",
                    userPwd = "123456"))

            repository.getUserById("01")?:repository.addUser(
                UserEntry(
                    userId = "01",
                    userPwd = "0000"))
            repository.getUserById("99")?:repository.addUser(
                UserEntry(
                    userId = "99",
                    userPwd = "12345678"))


        }


    }

    fun login(userId: String, userPwd: String) {

        viewModelScope.launch {
            try {
                if (userId.isEmpty() || userPwd.isEmpty()) {
                    throw Exception("操作员和密码不能为空")
                }
                if (repository.getUserById(userId)?.userPwd != userPwd) {
                    throw Exception("操作员和密码错误")
                }
                when (userId) {
                    "00" -> ""
                    "99" -> ""
                    //else -> signIn()
                    else -> {
                        // 普通用户需要执行签到
                        val signInSuccess = signIn()
                        if(signInSuccess){
                            MLog.i("downParam in")
                            downParam()
                        }
                        _loginResult.value = signInSuccess
                    }
                }
            } catch (e: Exception) {
                _msg.value = e.message
            }
        }


    }

    suspend fun downParam() {
        withContext(Dispatchers.IO) {
            var signInSuccess = true
            MLog.i("downParam in")
            val isUpdateCapk = GlobalParams.get_isUpdateCapk(AppContext.instance)
            MLog.i("downParam isUpdateCapk="+isUpdateCapk)
            if(!isUpdateCapk){
                signInSuccess = TaskDownParameter(this@StartAppViewModel, AppContext.instance).downParam(
                    TaskDownParameter.TYPE_QUERY_CAPK_VERSION)
                if(!signInSuccess){
                    return@withContext
                }
            }


            val isUpdateParam = GlobalParams.get_isUpdateAID(AppContext.instance)
            MLog.i("downParam isUpdateParam="+isUpdateParam)
            if(!isUpdateParam) {
                signInSuccess = TaskDownParameter(this@StartAppViewModel, AppContext.instance).downParam(
                    TaskDownParameter.TYPE_QUERY_AID_VERSION
                )
                if(!signInSuccess){
                    return@withContext
                }
            }

//
//
//            TaskDownParameter(this@StartAppViewModel, AppContext.instance).downParam(
//                TaskDownParameter.TYPE_DOWN_PARAMETENFC)
//
//            TaskDownParameter(this@StartAppViewModel, AppContext.instance).downParam(
//                TaskDownParameter.TYPE_DOWN_PARAMETERBLACKLIST)

            _loading.postValue("")
        }

    }

    suspend fun signIn() : Boolean {
        return withContext(Dispatchers.IO) {
            //TaskSignIn(this@StartAppViewModel, AppContext.instance).doSignIn()
            true
        }
    }

    class StartAppViewModelFactory(private val repository: UserRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StartAppViewModel::class.java)) {
                return StartAppViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}

