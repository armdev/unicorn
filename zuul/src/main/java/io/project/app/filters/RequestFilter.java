package io.project.app.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class RequestFilter extends ZuulFilter {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FilterUtils filterUtils;

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        final String userAgent = request.getHeader("User-Agent");
        final String authToken = request.getHeader("token");

        if (ctx.getRequest().getRequestURI().contains("swagger")) {
            log.info("SWAGGER BLOCKED");
            ctx.setResponseStatusCode(401);
            ctx.setSendZuulResponse(false);
            ctx.setResponseBody("Swagger is an open-source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful Web services.");
            return null;
        }

        return null;
    }

    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private boolean isCorrelationIdPresent() {
        return filterUtils.getCorrelationId() != null;
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

    public void printHttpRequest(HttpServletRequest request) {

        Enumeration<String> headerNames = request.getHeaderNames();
        log.info("***************REQUEST BODY******************************");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.warn("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));

            //log.info("REQUEST METHOD  " + request.getMethod());
        }
        log.info("**************FINISH REQUEST******************");

    }
}
