package com.dj.ckw.userservice.filters;

import com.dj.ckw.userservice.model.RequestInfo;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class RequestInfoFilter implements Filter {

    private final RequestInfo requestInfo;

    public RequestInfoFilter(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        String header = request.getHeader("X-User-Info");
        if (header != null) {
            String decoded = new String(Base64.getDecoder().decode(header));
            requestInfo.setEmail(decoded);
        }

        chain.doFilter(req, res);
    }
}
