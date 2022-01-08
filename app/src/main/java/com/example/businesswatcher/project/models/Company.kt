package com.example.businesswatcher.project.models

data class Company (
    val id: Long,
    var name: String,
    var type: Type,
    var group: Group,
    var adress: String,
    var latitude: Double,
    var longitude: Double,
    var turnover: Int,
    var customers: List<Customer>,
    var description: String
    )