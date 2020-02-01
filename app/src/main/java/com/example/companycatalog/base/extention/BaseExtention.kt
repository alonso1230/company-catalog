package com.example.companycatalog.base.extention

import android.os.Build
import android.telephony.PhoneNumberUtils
import java.util.*

fun String.formatNumberPhone(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        return PhoneNumberUtils.formatNumber(this, Locale.getDefault().country);
    } else {
        return PhoneNumberUtils.formatNumber(this);
    }
}