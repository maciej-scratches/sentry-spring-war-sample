package io.sentry.samples.spring;

import io.sentry.spring.tracing.SentryTracingFilter;
import javax.servlet.Filter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected String[] getServletMappings() {
    return new String[] {"/*"};
  }

  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class<?>[] {AppConfig.class };
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[] {WebConfig.class};
  }

  @Override
  protected Filter[] getServletFilters() {
    // creates Sentry transactions around incoming HTTP requests
    SentryTracingFilter sentryTracingFilter = new SentryTracingFilter();

    // sets request on RequestContextHolder
    RequestContextFilter requestContextFilter = new RequestContextFilter();

    // sets Sentry user on the scope
    DelegatingFilterProxy sentryUserFilterProxy = new DelegatingFilterProxy();
    sentryUserFilterProxy.setTargetBeanName("sentryUserFilter");

    return new Filter[] {
      sentryTracingFilter, requestContextFilter, sentryUserFilterProxy
    };
  }


}
