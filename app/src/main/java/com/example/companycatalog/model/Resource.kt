package com.example.companycatalog.model

data class Resource<T>(
    val status: Status,
    val data: T?,
    val error: Throwable?
) {
    enum class Status {
        LOADING,
        SUCCESS,
        ERROR
    }
}