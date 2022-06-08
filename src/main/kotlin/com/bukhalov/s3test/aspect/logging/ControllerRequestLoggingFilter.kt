package com.bukhalov.s3test.aspect.logging

import mu.KotlinLogging.logger
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

private val log = logger {}

@Component
class ControllerRequestLoggingFilter() : GenericFilterBean() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, chain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val requestCopy = HttpServletRequestCopier(request)
        log.info { " I'm ${request.requestURL}" }
        chain.doFilter(requestCopy, servletResponse)
    }
}
