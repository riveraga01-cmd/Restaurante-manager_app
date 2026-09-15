package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Utility helper to handle picking, resizing, compressing, and persisting
 * photos from the device's gallery or camera for Dishes, Web Banners, and Restaurant Logos.
 */
object ImageStorageHelper {

    private const val TAG = "ImageStorageHelper"
    private const val MAX_IMAGE_DIMENSION = 900
    private const val COMPRESS_QUALITY = 82

    /**
     * Converts a content Uri from Gallery / PhotoPicker into a compact, optimized Base64 Data URI
     * and also saves a local backup file.
     * The returned string (data:image/jpeg;base64,...) is 100% compatible with Jetpack Compose (Coil),
     * Web browsers (index.html), and Room/Firestore offline-first persistence.
     */
    fun processAndSaveImageUri(context: Context, uri: Uri): String? {
        return try {
            val bitmap = loadAndRotateBitmap(context, uri) ?: return null
            val scaledBitmap = scaleBitmapToMaxDimensions(bitmap, MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION)

            // 1. Save to local app storage cache/files directory
            val imagesDir = File(context.filesDir, "restaurant_images")
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }
            val fileName = "img_${System.currentTimeMillis()}.jpg"
            val localFile = File(imagesDir, fileName)
            FileOutputStream(localFile).use { out ->
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, out)
            }

            // 2. Generate Base64 Data URL for universal instant rendering across Web and App
            val byteArrayOutputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)

            "data:image/jpeg;base64,$base64String"
        } catch (e: Exception) {
            Log.e(TAG, "Error processing image uri: ${e.message}", e)
            null
        }
    }

    private fun loadAndRotateBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            } ?: return null

            if (bitmap == null) return null

            // Correct orientation from EXIF
            var orientation = ExifInterface.ORIENTATION_NORMAL
            try {
                context.contentResolver.openInputStream(uri)?.use { exifStream ->
                    val exif = ExifInterface(exifStream)
                    orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Could not read EXIF orientation: ${e.message}")
            }

            rotateBitmap(bitmap, orientation)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading bitmap: ${e.message}", e)
            null
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            else -> return bitmap
        }
        return try {
            val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            if (rotated != bitmap) {
                bitmap.recycle()
            }
            rotated
        } catch (e: OutOfMemoryError) {
            bitmap
        }
    }

    private fun scaleBitmapToMaxDimensions(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxWidth && height <= maxHeight) {
            return bitmap
        }

        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

        var finalWidth = maxWidth
        var finalHeight = maxHeight
        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }

        val scaled = Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
        if (scaled != bitmap) {
            bitmap.recycle()
        }
        return scaled
    }
}
