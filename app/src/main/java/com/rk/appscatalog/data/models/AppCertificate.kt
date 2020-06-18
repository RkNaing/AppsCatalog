package com.rk.appscatalog.data.models

import com.rk.appscatalog.utils.defaultOnNullOrEmpty
import com.rk.appscatalog.utils.toReadableTimestamp
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
) {

    val desc: String
        get() = """
            Common Name         :   ${commonName.defaultOnNullOrEmpty()}
            Organization        :   ${organization.defaultOnNullOrEmpty()}
            Organization Unit   :   ${organizationUnit.defaultOnNullOrEmpty()}
            Location            :   ${location.defaultOnNullOrEmpty()}
            State               :   ${state.defaultOnNullOrEmpty()}
            Country             :   ${country.defaultOnNullOrEmpty()}
            Algorithm           :   ${algorithm.defaultOnNullOrEmpty()}
            Serial Number       :   ${serialNumber.defaultOnNullOrEmpty()}
            Issued Date         :   ${issuedDate.toReadableTimestamp().defaultOnNullOrEmpty()}
            Expiry Date         :   ${expiryDate.toReadableTimestamp().defaultOnNullOrEmpty()}
        """.trimIndent()
}