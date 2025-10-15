package com.telpo.base.internet.http

import android.annotation.SuppressLint
import com.telpo.base.internet.http.factory.FastJsonConverterFactory
import com.telpo.gxss.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.http.conn.ssl.SSLSocketFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * Retrofit帮助类
 *
 * @author Kelly
 * @version 1.0.0
 * @filename RetrofitHelper.java
 * @time 2018/1/29 15:09
 * @copyright(C) 2018 song
 */
class RetrofitHelper private constructor() {
    private lateinit var mRetrofit: Retrofit


//    init {
//        mRetrofit = Retrofit.Builder()
//            .baseUrl("http://www.tpai-plat.com:8888")
//            .client(this.okHttpClient)
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(FastJsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//    }


    /**
     * 返回接口服务实例
     *
     * @param clz
     * @param <T>
     * @return
    </T> */
    fun <T> getApiService(url: String, clz: Class<T?>): T? {
        mRetrofit = Retrofit.Builder()
            //.baseUrl("http://www.tpai-plat.com:8888")
            .baseUrl(url)
            .client(this.okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(FastJsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return mRetrofit.create<T?>(clz)
    }

    private val okHttpClient: OkHttpClient
        get() {
            val builder = newUnsafeClientBuilder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(logging)
            }
            return builder.build()
        }


    companion object {
        @Volatile
        private var sInstance: RetrofitHelper? = null //使线程共享一个实例
        val instance: RetrofitHelper?
            get() {
                if (sInstance == null) {
                    synchronized(RetrofitHelper::class.java) {
                        if (sInstance == null) {
                            sInstance = RetrofitHelper()
                        }
                    }
                }
                return sInstance
            }

        private fun newUnsafeClientBuilder(): OkHttpClient.Builder {
            val okHttpBuilder = OkHttpClient.Builder()

            //.connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS);
            return setSSL(okHttpBuilder)
        }

        @SuppressLint("TrustAllX509TrustManager")
        private fun setSSL(okHttpBuilder: OkHttpClient.Builder): OkHttpClient.Builder {
            try {
                val sslContext = SSLContext.getInstance("SSL")

                val trustManager: X509TrustManager = object : X509TrustManager {
                    @Throws(CertificateException::class)
                    public override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    public override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return kotlin.arrayOfNulls<X509Certificate>(0)
                    }
                }


                sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
                okHttpBuilder.sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                    .hostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return okHttpBuilder
        }
    }
}
