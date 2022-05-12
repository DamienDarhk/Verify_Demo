//package com.vn.ncb.service.info.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.servlet.DispatcherServlet;
//import org.springframework.web.servlet.HandlerExecutionChain;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//import org.springframework.web.util.WebUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//
//public class LoggableDispatcherServlet extends DispatcherServlet {
//    private static final Logger logger = LoggerFactory.getLogger(LoggableDispatcherServlet.class);
//
//    @Override
//    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if (!(request instanceof ContentCachingRequestWrapper)) {
//            request = new ContentCachingRequestWrapper(request);
//        }
//        if (!(response instanceof ContentCachingResponseWrapper)) {
//            response = new ContentCachingResponseWrapper(response);
//        }
//        HandlerExecutionChain handler = getHandler(request);
//
//        try {
//            super.doDispatch(request, response);
//        } finally {
//            log(request, response, handler);
//            updateResponse(response);
//        }
//    }
//
//    private void log(HttpServletRequest requestToCache, HttpServletResponse responseToCache, HandlerExecutionChain handler) {
//        logger.info("HttpStatus: "+responseToCache.getStatus());
//        logger.info("HttpMethod: "+requestToCache.getMethod());
//        logger.info("Path: "+requestToCache.getRequestURI());
//        logger.info("ClientIp: "+requestToCache.getRemoteAddr());
//        logger.info("JavaMethod: "+handler.toString());
//        logger.info("Response: "+getResponsePayload(responseToCache));
//    }
//
//    private String getResponsePayload(HttpServletResponse response) {
//        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
//        if (wrapper != null) {
//
//            byte[] buf = wrapper.getContentAsByteArray();
//            if (buf.length > 0) {
//                int length = Math.min(buf.length, 5120);
//                try {
//                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
//                }
//                catch (UnsupportedEncodingException ex) {
//                    // NOOP
//                }
//            }
//        }
//        return "[unknown]";
//    }
//
//    private void updateResponse(HttpServletResponse response) throws IOException {
//        ContentCachingResponseWrapper responseWrapper =
//                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
//        responseWrapper.copyBodyToResponse();
//    }
//}
