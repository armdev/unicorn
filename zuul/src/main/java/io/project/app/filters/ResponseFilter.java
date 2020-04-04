package io.project.app.filters;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ResponseFilter extends ZuulFilter {

    @Autowired
    private FilterUtils filterUtils;

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.getResponse().addHeader("Access-Control-Expose-Headers", "proxy_set_header, X-Real-IP, proxy_set_header, X-Forwarded-For, MP-AUTH-TOKEN, zuul-correlation-id, scope, client,Origin,Accept-Language,Accept-Encoding");

        final String correlationId = ctx.getResponse().getHeader("zuul-correlation-id");

        final int status = ctx.getResponse().getStatus();
        try {
            log.warn("RESPONSE STATUS: " + status);
            log.warn("RESPONSE BODY: " + ctx.getResponse().toString());
            String responseData = this.getResponseData(ctx);
            log.warn("***********  RESPONSE: BODY  ************ ");
            log.warn(responseData);
            log.warn("***************************************************");

        } catch (IOException ex) {
            log.warn("Error get body", ex);
        }

        return null;
    }

    private String getResponseData(RequestContext ctx) throws IOException {
        String responseData = "";

        final InputStream responseDataStream = ctx.getResponseDataStream();
        if (responseDataStream == null) {
            return "";
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayOutputStream copy = new ByteArrayOutputStream();
        int read = 0;
        byte[] buff = new byte[1024];
        while ((read = responseDataStream.read(buff)) != -1) {
            bos.write(buff, 0, read);
            copy.write(buff, 0, read);
        }
        InputStream isFromFirstData = new ByteArrayInputStream(bos.toByteArray());

        boolean responseGZipped = ctx.getResponseGZipped();
        try {
            InputStream zin = null;
            if (responseGZipped) {
                zin = new GZIPInputStream(isFromFirstData);
            } else {
                zin = responseDataStream;
            }
            responseData = CharStreams.toString(new InputStreamReader(zin, "UTF-8"));
            ctx.setResponseDataStream(new ByteArrayInputStream(copy.toByteArray()));

        } catch (IOException e) {
            log.warn("Error reading body {}", e.getMessage());
            return "";
        }

        return responseData;
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        //return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

}
