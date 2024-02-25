package com.rd.t1d.auth;

import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class SecurityFilter implements Filter {

    private static final String APP_ID = "d32769ce913f9f8ce54f06b002ac920186f3d2cdb17a1dcc96f1a30f5e1430a7";

    @Autowired
    private AuthHelper authHelper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;

        String servletPath = request.getServletPath();
        log.info("accessing path: " + servletPath);
        if(servletPath.contains("/health-check") || servletPath.contains("/favicon") || servletPath.contains("/image") || servletPath.contains("/record")
                || servletPath.contains("/api/v1") || servletPath.contains("/push-notification") || servletPath.contains("/city")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String appId = request.getHeader("app_id");
        if(!APP_ID.equals(appId)){
            log.error("app id is not matching while accessing path: " + servletPath);
            throw new T1DBuddyException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        if(servletPath.contains("/invitation/") || servletPath.contains("/avatar/")
                || servletPath.contains("/register") || servletPath.contains("/validate")){
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            String idToken = request.getHeader("id_token");
            String email = request.getHeader("email");

            try{
                if(null == idToken){
                    throw new T1DBuddyException(ErrorCode.UNAUTHORIZED_REQUEST);
                }

                String authEmail = authHelper.verifyAuth(idToken);
                if(!email.equals(authEmail)){
                    throw new T1DBuddyException(ErrorCode.UNAUTHORIZED_REQUEST);
                }
                filterChain.doFilter(servletRequest, servletResponse);
            }catch(Exception ex){
                log.error("Authorization error for email: " + email + " while accessing path: " + servletPath, ex);
                HttpServletResponse response = (HttpServletResponse)servletResponse;
                response.sendError(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
                return;

            }

        }

    }

}
