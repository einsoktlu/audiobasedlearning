package com.tlu.audiobasedlearning

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

class Helpers {
    companion object {
        fun resolveFileNameFromUri(contentResolver: ContentResolver, uri: Uri?): String? {
            if (uri == null) return null

            val returnCursor = contentResolver.query(uri, null, null, null, null)
            val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor?.moveToFirst()
            val filename = nameIndex?.let { returnCursor.getString(it) }
            returnCursor?.close()
            return filename
        }
    }
}