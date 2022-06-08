package com.bukhalov.s3test.aspect.logging

import org.apache.commons.io.IOUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.Objects.isNull
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class HttpServletRequestCopier(request: HttpServletRequest?) : HttpServletRequestWrapper(request) {
    private var cachedBytes: ByteArrayOutputStream? = null

    @Throws(IOException::class)
    override fun getInputStream(): ServletInputStream {
        if (isNull(cachedBytes)) {
            cacheInputStream()
        }
        return CachedServletInputStream()
    }

    @Throws(IOException::class)
    override fun getReader(): BufferedReader = BufferedReader(InputStreamReader(inputStream))

    @Throws(IOException::class)
    private fun cacheInputStream() {
        cachedBytes = ByteArrayOutputStream()
        IOUtils.copy(super.getInputStream(), cachedBytes)
    }

    /**
     * CachedServletInputStream.
     */
    inner class CachedServletInputStream : ServletInputStream() {
        private val input: ByteArrayInputStream = ByteArrayInputStream(cachedBytes?.toByteArray())
        override fun isFinished(): Boolean = false

        override fun isReady(): Boolean = false

        override fun setReadListener(readListener: ReadListener) {}

        @Throws(IOException::class)
        override fun read(): Int = input.read()
    }
}
