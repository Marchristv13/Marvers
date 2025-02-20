package com.example.Marvers

import java.io.Serializable

class Book : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    var id: Int = 0
    var name: String? = null
    var nickname: String? = null
    var photoPath: String? = null
    var email: String? = null
    var address: String? = null
    var birthDate: String? = null
    var phoneNumber: String? = null
    var timestamp: Long = 0

    constructor(
        name: String?,
        nickname: String?,
        photoPath: String?,
        email: String?,
        address: String?,
        birthDate: String?,
        phoneNumber: String?,
        timestamp: Long
    ) {
        this.name = name
        this.nickname = nickname
        this.photoPath = photoPath
        this.email = email
        this.address = address
        this.birthDate = birthDate
        this.phoneNumber = phoneNumber
        this.timestamp = timestamp
    }

    constructor()

    // Tambahkan toString() untuk debugging
    override fun toString(): String {
        return "Book(id=$id, name=$name, nickname=$nickname, email=$email)"
    }
}