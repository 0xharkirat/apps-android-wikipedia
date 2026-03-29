package org.wikipedia.oacp

import android.content.ContentProvider
import android.content.ContentValues
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.FileNotFoundException

class OacpMetadataProvider : ContentProvider() {
    override fun onCreate(): Boolean = true

    override fun getType(uri: Uri): String? = when (uri.pathSegments.firstOrNull()) {
        "manifest" -> "application/json"
        "context" -> "text/markdown"
        else -> null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor {
        if (mode != "r") {
            throw FileNotFoundException("OACP metadata is read-only.")
        }

        val mimeType = getType(uri) ?: throw FileNotFoundException("Unsupported URI: $uri")
        val payload = when (uri.pathSegments.firstOrNull()) {
            "manifest" -> loadManifestBytes()
            "context" -> loadAssetBytes("OACP.md")
            else -> throw FileNotFoundException("Unsupported URI: $uri")
        }

        return openPipeHelper(uri, mimeType, null, payload) { output, _, _, _, bytes ->
            val safeBytes = bytes ?: ByteArray(0)
            ParcelFileDescriptor.AutoCloseOutputStream(output).use { stream ->
                stream.write(safeBytes)
            }
        }
    }

    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor {
        val parcelFileDescriptor = openFile(uri, mode)
        return AssetFileDescriptor(parcelFileDescriptor, 0, AssetFileDescriptor.UNKNOWN_LENGTH)
    }

    private fun loadManifestBytes(): ByteArray {
        val appContext = context ?: throw FileNotFoundException("Missing context")
        val rawManifest = loadAssetBytes("oacp.json").toString(Charsets.UTF_8)
        return rawManifest
            .replace("__APPLICATION_ID__", appContext.packageName)
            .toByteArray(Charsets.UTF_8)
    }

    private fun loadAssetBytes(assetPath: String): ByteArray {
        val appContext = context ?: throw FileNotFoundException("Missing context")
        return appContext.assets.open(assetPath).use { input -> input.readBytes() }
    }
}
