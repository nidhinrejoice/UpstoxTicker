//package com.nidhin.upstoxclient.utils
//
//import kotlinx.coroutines.DelicateCoroutinesApi
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import okhttp3.Interceptor
//import okhttp3.Response
//import javax.inject.Inject
//
//class ApiInterceptor constructor(private val eventProcessor: EventProcessor) :
//    Interceptor {
//
//    @OptIn(DelicateCoroutinesApi::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val response = chain.proceed(chain.request())
//        if (response.code != 200 && response.code != 201 && response.code != 202) {
//            GlobalScope.launch {
//
//                try {
//                    response.body?.string().let {
//                        val stringBuilder =
//                            StringBuilder("Http Error : ${chain.request().url}").append("\n")
//                        stringBuilder.append(it)
//                        eventProcessor.logEvent(stringBuilder.toString())
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        return response
//    }
//}