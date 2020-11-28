package com.flotta.config;


import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class I18n implements WebMvcConfigurer {
	
//    @Bean
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver slr = new SessionLocaleResolver();
//        slr.setDefaultLocale(Locale.US);
//        return slr;
//    }
  
// TODO   ezzel bizonyos oldalakon a locale nem változik nyelvválasztásnál
  @Bean
  public CookieLocaleResolver localeResolver()  {
      CookieLocaleResolver resolver= new CookieLocaleResolver();
      resolver.setCookieName("locale");
      resolver.setCookieMaxAge(60000);
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
//      LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
//      localeInterceptor.setParamName("lang");
      registry.addInterceptor(getLocaleChangeInterception());
  }
}