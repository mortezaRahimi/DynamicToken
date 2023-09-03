package com.myket.farahani.dynamictoken.utils.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

object Utils {
    fun openNewVersion(location: String ,context: Context)  {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(
            getUriFromFile(location ,context),
            "application/vnd.android.package-archive"
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }

    private fun getUriFromFile(filePath: String, context: Context): Uri {
        return FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            File(filePath)
        )
    }
}