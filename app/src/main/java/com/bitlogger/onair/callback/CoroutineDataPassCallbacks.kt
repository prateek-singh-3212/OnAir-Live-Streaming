package com.bitlogger.onair.callback

interface CoroutineDataPassCallbacks {

    /**
     * Called when data is loading
     * */
    fun isDataLoading(dataLoading: Boolean)

    /**
     * Called when data load is complete
     * */
    fun <T> onLoadComplete(data: T)

    /**
     * Called when there is error in loading the data
     * */
    fun onLoadFailed(errorCode: String, errorMessage: String)
}