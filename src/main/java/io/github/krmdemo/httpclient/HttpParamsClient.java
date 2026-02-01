package io.github.krmdemo.httpclient;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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

    /**
     * Creator (factory-method) for {@link HttpFactory}
     *
     * @param kind a kind of low-level HTTP-client
     * @return an instance of {@link HttpFactory} according to {@code kind}
     */
    static HttpFactory httpKind(HttpClientKind kind) {
        return switch(kind) {
            case JDK -> new HttpFactory(HttpParamsClientJDK::new);
            case APACHE_HTTP -> new HttpFactory(HttpParamsClientApache::new);
            case OK_HTTP -> new HttpFactory(HttpParamsClientOkHttp::new);
        };
    }

    @Getter
    @Setter
    @Accessors(fluent = true, chain = true)
    public class HttpFactory {
        private final Function<HttpFactory, HttpParamsClient> createFn;
        private String baseUrl;
        private HttpFactory(Function<HttpFactory, HttpParamsClient> createFn) {
            this.createFn = createFn;
        }
        public HttpParamsClient create() {
            return createFn.apply(this);
        }
    }
}
