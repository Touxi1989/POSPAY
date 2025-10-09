package com.telpo.pospay.db.repository

sealed class SocketResult {
    data class Success(val data: ByteArray) : SocketResult() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Success

            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }

    data class Error(val message: String, val cause: Throwable? = null) : SocketResult()
}