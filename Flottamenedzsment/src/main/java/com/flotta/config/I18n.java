package com.flotta.config;


import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class I18n implements WebMvcConfigurer {
  
  @Bean
  public CookieLocaleResolver localeResolver()  {
      CookieLocaleResolver resolver= new CookieLocaleResolver();
      resolver.setCookieName("locale");
      resolver.setCookieMaxAge(-1);
      resolver.setDefaultLocale(Locale.ENGLISH);
      resolver.setCookiePath("/");
      return resolver;
  } 
  
  @Bean
  public LocaleChangeInterceptor getLocaleChangeInterception() {
    LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
    localeInterceptor.setParamName("lang");
    return localeInterceptor;
  }
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(getLocaleChangeInterception());
  }
  
  @Bean
  public MessageSource messageSource() {
      ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
      messageSource.setBasenames("messages/messages",
                                 "messages/message_messages",
                                 "messages/messages_common",
                                 "messages/auth/messages_auth",
                                 "messages/sim/messages_sim",
                                 "messages/subscription/messages_subscription",
                                 "messages/user/messages_user"
                                 );
      messageSource.setDefaultEncoding("UTF-8");
      return messageSource;
  }
  
}