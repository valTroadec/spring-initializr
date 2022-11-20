package com.tempproject.initializr.config;




import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import io.spring.initializr.web.support.Agent;
import io.spring.initializr.web.support.Agent.AgentId;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

public class CustomInitializrWebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/info", "/actuator/info");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentTypeStrategy(new CommandLineContentNegotiationStrategy());
    }

    private static class CommandLineContentNegotiationStrategy implements ContentNegotiationStrategy {

        private final UrlPathHelper urlPathHelper = new UrlPathHelper();

        @Override
        public List<MediaType> resolveMediaTypes(NativeWebRequest request) {
            String path = this.urlPathHelper
                    .getPathWithinApplication(request.getNativeRequest(HttpServletRequest.class));
            if (!StringUtils.hasText(path) || !path.equals("/")) { // Only care about "/"
                return MEDIA_TYPE_ALL_LIST;
            }
            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            if (userAgent != null) {
                Agent agent = Agent.fromUserAgent(userAgent);
                if (agent != null) {
                    if (AgentId.CURL.equals(agent.getId()) || AgentId.HTTPIE.equals(agent.getId())) {
                        return Collections.singletonList(MediaType.TEXT_PLAIN);
                    }
                }
            }
            return Collections.singletonList(MediaType.APPLICATION_JSON);
        }

    }

}