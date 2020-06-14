package com.rk.appscatalog

import java.util.*

data class AppCertificate(
    val commonName: String?,
    val organization: String?,
    val organizationUnit: String?,
    val location: String?,
    val state: String?,
    val country: String?,
    val algorithm: String?,
    val serialNumber: String?,
    val issuedDate: Date?,
    val expiryDate: Date?
)