package io.sentry.samples.spring;

import io.sentry.SentryOptions;
import io.sentry.SentryOptions.TracesSamplerCallback;
import io.sentry.spring.EnableSentry;
import io.sentry.spring.tracing.SentryTracingConfiguration;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

// NOTE: Replace the test DSN below with YOUR OWN DSN to see the events from this app in your Sentry
// project/dashboard
@EnableSentry(
    dsn = "https://dfc87c7a4a3742e19f6cfb6f3e150e90@o420886.ingest.sentry.io/6090861",
    sendDefaultPii = true,
    maxRequestBodySize = SentryOptions.RequestSize.MEDIUM)
@Import(SentryTracingConfiguration.class)
public class SentryConfig {

  /**
   * Configures callback used to determine if transaction should be sampled.
   *
   * @return traces sampler callback
   */
  @Bean
  TracesSamplerCallback tracesSamplerCallback() {
    return samplingContext -> {
      HttpServletRequest request =
          (HttpServletRequest) samplingContext.getCustomSamplingContext().get("request");
      if ("/error".equals(request.getRequestURI())) {
        return 0.5d;
      } else {
        return 1.0d;
      }
    };
  }
}
