package com.gr2.custom.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Component
@Order(3)
public class GlobalExceptionHandlerFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandlerFilter.class);

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            if ("org.apache.catalina.connector.ClientAbortException".equals(e.getClass().getName())) {
                LOGGER.info("Client abort request!!");
            } else {
                LOGGER.error(e.getMessage());
                LOGGER.error("Sending error report mail");
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//                sendErrorReport(ExceptionUtils.getStackTrace(e), (HttpServletRequest) request);

                if (!httpServletRequest.getServletPath().startsWith("/api/")) {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.sendRedirect("/errorPage");
                } else {
                    throw e;
                }
            }
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    private String getRequestInfo(HttpServletRequest request) {
        return "METHOD: " + request.getMethod() + "\n" +
                "Content-Type: " + request.getContentType() + "\n" +
                "REQUEST PARAM: " + "\n" + getRequestParams(request) +
                "REQUEST BODY: " + getRequestBody(request);
    }

    private String getRequestParams(HttpServletRequest request) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            params.append(paramName).append(":").append(request.getParameter(paramName));
            params.append("\n");

        }
        return params.toString();
    }

    private String getRequestBody(HttpServletRequest request) {
        String requestData = "";
        try {
            requestData = request.getReader().lines().collect(Collectors.joining());
        } catch (IOException e) {
            LOGGER.info("Cannot get body!", e);
        }
        return requestData;
    }


}
