package com.edag.skillmanagementsystem.application.config.i18n;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Configuration for internationalization (i18n) support.
 *
 * <p>Configures message sources for German (de) and English (en) languages, locale resolution based
 * on Accept-Language header, and locale change interceptor for manual locale switching.
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

  /**
   * Configures the message source for internationalized messages.
   *
   * @return configured {@link MessageSource}
   */
  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setDefaultLocale(Locale.GERMAN);
    messageSource.setFallbackToSystemLocale(false);
    messageSource.setUseCodeAsDefaultMessage(true);
    return messageSource;
  }

  /**
   * Configures locale resolution based on Accept-Language header.
   *
   * @return configured {@link LocaleResolver}
   */
  @Bean
  public LocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
    localeResolver.setDefaultLocale(Locale.ENGLISH);
    localeResolver.setSupportedLocales(java.util.List.of(Locale.ENGLISH, Locale.GERMAN));
    return localeResolver;
  }

  /**
   * Configures interceptor for manual locale switching via query parameter.
   *
   * @return configured {@link LocaleChangeInterceptor}
   */
  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }
}
