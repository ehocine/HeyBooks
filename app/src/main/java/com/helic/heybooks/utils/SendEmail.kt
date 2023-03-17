package com.helic.heybooks.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material.SnackbarDuration
import androidx.core.content.ContextCompat.startActivity

fun sendEmailToOwner(
    context: Context,
    emailAddress: String,
    subject: String,
    message: String,
    snackbar: (String, SnackbarDuration) -> Unit
) {

    val uriText = "mailto:$emailAddress" +
            "?subject=" + subject +
            "&body=" + message
    val uri = Uri.parse(uriText)
    val sendIntent = Intent(Intent.ACTION_SENDTO)
    sendIntent.data = uri

    if (sendIntent.resolveActivity(context.packageManager) != null) {
        startActivity(context, sendIntent, null)
    } else {
        snackbar("Couldn't find an email app installed", SnackbarDuration.Short)
    }
}