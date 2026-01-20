package io.github.krmdemo.httpclient;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;

/**
 * A simple wrapper over real HTTP-Client, which is represented by {@link HttpClientKind}
 * and HTTP-query and HTTP-header parameters are supposed to have unique names
 * and passed as a standard {@link Map Map&lt;String,String&gt;} of name-value pairs.
 * The base URL of REST-endpoint is represented by final property
 */
public interface HttpParamsClient {

    /**
     * The default HTTP-timeout for each remote REST-request
     */
    Duration HTTP_TIMEOUT = Duration.ofSeconds(5);

    /**
     * The name of HTTP-header that would correspond to {@link HttpClientKind}
     */
    String HTTP_HEADER_NAME__KIND = "X-Http-Params-Client-Kind";

    String httpGetBodyString(Map<String, String> paramsMap);

    static Factory httpKind(HttpClientKind kind) {
        return switch(kind) {
            case JDK -> new Factory(HttpParamsClientJDK::new);
            case APACHE_HTTP -> new Factory(HttpParamsClientApache::new);
            case OK_HTTP -> new Factory(HttpParamsClientOkHttp::new);
            default -> throw new IllegalArgumentException("Unsupported HttpKind: " + kind);
        };
    }

    class Factory {
        private final Function<Factory, HttpParamsClient> createFn;
        private String baseUrl;
        private Factory(Function<Factory, HttpParamsClient> createFn) {
            this.createFn = createFn;
        }
        public String baseUrl() {
            return baseUrl;
        }
        public Factory baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
        public HttpParamsClient create() {
            return createFn.apply(this);
        }
    }
}
