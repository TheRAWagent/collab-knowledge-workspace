package com.dj.ckw.workspaceservice.filters;

import com.dj.ckw.workspaceservice.model.RequestInfo;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class RequestInfoFilter implements Filter {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RequestInfoFilter.class);
  private final RequestInfo requestInfo;

  public RequestInfoFilter(RequestInfo requestInfo) {
    this.requestInfo = requestInfo;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    String uri = request.getRequestURI();
    boolean isActuator = uri.startsWith("/actuator");

    if (!isActuator) {
      log.info("RequestInfoFilter hit: {} {}", request.getMethod(), uri);
    }

    String header = request.getHeader("X-User-Info");
    if (!isActuator) {
      log.info("X-User-Info header length: {}", header != null ? header.length() : "null");
    }

    if (header != null) {
      try {
        String decoded = new String(Base64.getUrlDecoder().decode(header));
        requestInfo.setEmail(decoded);
      } catch (Exception e) {
        if (!isActuator) {
          log.error("Failed to process X-User-Info header: {}", e.getMessage(), e);
        }
      }
    }

    chain.doFilter(req, res);
  }
}
