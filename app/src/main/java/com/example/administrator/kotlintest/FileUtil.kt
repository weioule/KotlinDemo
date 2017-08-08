package com.example.administrator.kotlintest

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import java.io.*
import java.text.DecimalFormat

object FileUtil {

    private val KB: Long = 1024
    private val MB = KB * 1024
    private val GB = MB * 1024
    private val TB = GB * 1024

    fun getCacheDir(context: Context, name: String): File {
        var dir = getExternalCacheDir(context, name)
        if (dir == null) {
            dir = getInternalCacheDir(context, name)
        }
        return dir
    }

    fun getFileDir(context: Context, name: String): File {
        var dir = getExternalFileDir(context, name)
        if (dir == null) {
            dir = getInternalFileDir(context, name)
        }
        return dir
    }

    /**
     * SD card缓存目录

     * @param context
     * *
     * @param dirName
     * *
     * @return
     */
    fun getExternalCacheDir(context: Context, dirName: String): File? {
        var cachePath: File? = null

        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val dataDir = File(File(Environment.getExternalStorageDirectory(), "Android"), "data")
            val appCacheDir = File(File(dataDir, context.packageName), "cache")
            if (appCacheDir.exists() || appCacheDir.mkdirs()) {

                cachePath = appCacheDir
                createNoMediaFile(cachePath)
            }
        }
        if (cachePath == null) {
            return null
        }

        val dir = File(cachePath, dirName)
        if (!dir.exists()) {
            dir.mkdirs()
            createNoMediaFile(dir)
        }

        return dir
    }

    /**
     * 应用缓存目录

     * @param context
     * *
     * @param name
     * *
     * @return
     */
    fun getInternalCacheDir(context: Context, name: String): File {

        var cachePath: File? = null

        if (TextUtils.isEmpty(name)) {
            cachePath = context.cacheDir
        } else {
            cachePath = File(context.cacheDir, name)
        }
        if (!cachePath!!.exists()) {
            cachePath.mkdirs()
            createNoMediaFile(cachePath)
        }

        return cachePath
    }

    fun getExternalFileDir(context: Context, name: String): File? {

        var fileDir: File? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val dataDir = File(File(Environment.getExternalStorageDirectory(), "Android"), "data")
            val appFileDir = File(File(dataDir, context.packageName), "files")
            if (appFileDir.exists() || appFileDir.mkdirs()) {

                fileDir = appFileDir
                createNoMediaFile(fileDir)
            }
        }

        if (fileDir == null) {
            return null
        }

        val dir = File(fileDir, name)
        if (!dir.exists()) {
            dir.mkdirs()
            createNoMediaFile(dir)
        }

        return dir
    }

    fun getInternalFileDir(context: Context, name: String): File {
        var fileDir: File? = null

        if (TextUtils.isEmpty(name)) {
            fileDir = context.filesDir
        } else {
            fileDir = File(context.filesDir, name)
        }
        if (!fileDir!!.exists()) {
            fileDir.mkdirs()
            createNoMediaFile(fileDir)
        }

        return fileDir
    }

    private fun createNoMediaFile(dir: File) {
        try {
            File(dir, ".nomedia").createNewFile()
        } catch (e: IOException) {
        }

    }

//    fun read(path: String): String {
//
//        return read(File(path))
//    }
//
//    fun read(file: File): String {
//        val buffer = StringBuffer()
//
//        try {
//            val reader = BufferedReader(FileReader(file))
//
//            var line: String
//            while ((line = reader.readLine()) != null) {
//                buffer.append(line)
//            }
//            reader.close()
//
//        } catch (e: IOException) {
//        }
//
//        return buffer.toString()
//    }
//
//    fun write(path: String, content: String): Boolean {
//        return write(File(path), content)
//    }

    fun write(file: File, content: String): Boolean {
        if (TextUtils.isEmpty(content)) {
            return false
        }

        try {
            val writer = BufferedWriter(FileWriter(file))
            writer.write(content)
            writer.close()
            file.setLastModified(System.currentTimeMillis())

            return true
        } catch (e: IOException) {
        }

        return false
    }

    fun write(path: String, data: ByteArray): Boolean {

        return write(File(path), data)
    }

    fun write(file: File, data: ByteArray): Boolean {

        try {
            val fos = FileOutputStream(file)
            fos.write(data)
            fos.flush()
            fos.close()

            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 获取文件(夹)大小

     * @param file
     * *
     * @return 文件大小 单位字节
     */
    fun getSize(file: File?): Long {

        if (file == null || !file.exists()) {
            return 0L
        } else if (file.isFile) {
            return file.length()
        } else if ("." == file.name || ".." == file.name) {
            return 0L
        } else {
            var fileSize = 0L

            val files = file.listFiles()
            for (f in files) {
                fileSize += getSize(f)
            }

            return fileSize
        }
    }

    /**
     * 删除文件(夹)

     * @param file
     * *
     * @return
     */
    fun delete(file: File?): Boolean {

        if (file == null || !file.exists()) {
            return false
        } else if (file.isFile) {
            return file.delete()
        } else if ("." == file.name || ".." == file.name) {
            return false
        } else {

            val files = file.listFiles()
            if (files == null || files.size == 0) {
                file.delete()
                return true
            }

            for (f in files) {
                delete(f)
            }
            file.delete()
            return true
        }
    }

    fun formatSize(length: Long): String {

        val df = DecimalFormat("#.0")
        var size = length * 1.0 / TB
        if (size > 1) {
            return df.format(size) + "T"
        }

        size = length * 1.0 / GB
        if (size > 1) {
            return df.format(size) + "G"
        }

        size = length * 1.0 / MB
        if (size > 1) {
            return df.format(size) + "M"
        }

        size = length * 1.0 / KB
        if (size > 1) {
            return df.format(size) + "K"
        }

        return length.toString() + "B"
    }

    fun loadFromPkg(ctx: Context, filename: String?): ByteArray? {
        if (filename == null) {
            return null
        } else {
            try {
                var `is`: FileInputStream? = ctx.openFileInput(filename)
                val lens = `is`!!.available()
                val buffer = ByteArray(lens)
                if (lens > 0) {
                    `is`.read(buffer)
                }

                `is`.close()
                `is` = null
                return buffer
            } catch (var5: Exception) {
                return null
            }

        }
    }

    fun saveToPkg(ctx: Context, filename: String?, data: ByteArray?): Boolean {
        if (data != null && filename != null) {
            try {
                var fos: FileOutputStream? = ctx.openFileOutput(filename, 0)
                fos!!.write(data)
                fos.close()
                fos = null
                return true
            } catch (var4: Exception) {
                return false
            }

        } else {
            return false
        }
    }

//    fun loadTextFromAssets(ctx: Context, filepath: String): String? {
//        val data = readFromAssets(ctx, filepath)
//        if (data != null) {
//            try {
//                return String(data, 0, data.size, "utf16le")
//            } catch (var4: Exception) {
//
//            }
//
//        }
//
//        return null
//    }

    fun readFromAssets(ctx: Context, filepath: String?): ByteArray? {
        if (filepath != null) {
            try {
                var `is`: InputStream? = ctx.assets.open(filepath)
                val lens = `is`!!.available()
                val buffer = ByteArray(lens)
                if (lens > 0) {
                    `is`.read(buffer)
                }

                `is`.close()
                `is` = null
                return buffer
            } catch (var5: Exception) {

            }

        }

        return null
    }
}
