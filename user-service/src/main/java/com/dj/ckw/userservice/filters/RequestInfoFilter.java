package com.dj.ckw.userservice.filters;

import com.dj.ckw.userservice.model.RequestInfo;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class RequestInfoFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(RequestInfoFilter.class);

  private final RequestInfo requestInfo;

  public RequestInfoFilter(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    log.info("RequestInfoFilter hit: {} {}", request.getMethod(), request.getRequestURI());

    String header = request.getHeader("X-User-Info");
    log.info("X-User-Info header length: {}", header != null ? header.length() : "null");

    if (header != null) {
      try {
        String decoded = new String(Base64.getUrlDecoder().decode(header));
        requestInfo.setEmail(decoded);
      } catch (Exception e) {
        log.error("Failed to process X-User-Info header: {}", e.getMessage(), e);
      }
    }

    chain.doFilter(req, res);
  }
}
