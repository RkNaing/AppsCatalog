package com.rkzmn.appsdataprovider.utils

import android.Manifest
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

internal typealias AndroidVersions = Build.VERSION_CODES
internal typealias Permissions = Manifest.permission

@ChecksSdkIntAtLeast(parameter = 0)
internal fun isSDKIntAtLeast(sdkInt: Int): Boolean = Build.VERSION.SDK_INT >= sdkInt

private val androidVersionLabels: Map<Int, String> = mapOf(
    AndroidVersions.BASE to "Base",
    AndroidVersions.BASE_1_1 to "Base 1.1",
    AndroidVersions.CUPCAKE to "Cupcake",
    AndroidVersions.DONUT to "Donut",
    AndroidVersions.ECLAIR to "Eclair",
    AndroidVersions.ECLAIR_0_1 to "Eclair 0.1",
    AndroidVersions.ECLAIR_MR1 to "Eclair MR1",
    AndroidVersions.FROYO to "Froyo",
    AndroidVersions.GINGERBREAD to "Gingerbread",
    AndroidVersions.GINGERBREAD_MR1 to "Gingerbread MR1",
    AndroidVersions.HONEYCOMB to "Honeycomb",
    AndroidVersions.HONEYCOMB_MR1 to "Honeycomb MR1",
    AndroidVersions.HONEYCOMB_MR2 to "Honeycomb MR2",
    AndroidVersions.ICE_CREAM_SANDWICH to "Ice Cream Sandwich",
    AndroidVersions.ICE_CREAM_SANDWICH_MR1 to "Ice Cream Sandwich MR1",
    AndroidVersions.JELLY_BEAN to "Jelly Bean",
    AndroidVersions.JELLY_BEAN_MR1 to "Jelly Bean MR1",
    AndroidVersions.JELLY_BEAN_MR2 to "Jelly Bean MR2",
    AndroidVersions.KITKAT to "KitKat",
    AndroidVersions.KITKAT_WATCH to "KitKat Watch",
    AndroidVersions.LOLLIPOP to "Lollipop",
    AndroidVersions.LOLLIPOP_MR1 to "Lollipop MR1",
    AndroidVersions.M to "Marshmallow",
    AndroidVersions.N to "Nougat",
    AndroidVersions.N_MR1 to "Nougat MR1",
    AndroidVersions.O to "Oreo",
    AndroidVersions.O_MR1 to "Oreo MR1",
    AndroidVersions.P to "Pie",
    AndroidVersions.Q to "Android 10",
    AndroidVersions.R to "Android 11",
    AndroidVersions.S to "Android 12",
    AndroidVersions.S_V2 to "Android 12L",
    AndroidVersions.TIRAMISU to "Android 13",
)

fun getAndroidVersionLabel(
    sdkInt: Int,
    fallbackLabel: () -> String = { "Android $sdkInt" }
): String = androidVersionLabels.getOrElse(sdkInt, fallbackLabel)
