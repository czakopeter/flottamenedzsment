package com.flotta.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.flotta.service.record.UserDetailsImpl;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//    UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

//    if (auth != null && userDetails.isPasswordExpired()) {
//      response.sendRedirect(request.getContextPath() + "/profile");
//    } else {
      response.sendRedirect(request.getContextPath() + "/accessDenied");
//    }
  }

}
