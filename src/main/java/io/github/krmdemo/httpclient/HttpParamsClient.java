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

    /**
     * Getting the HTTP-response body for executed HTTP-request of HTTP-method {@code GET}.
     * The URL of HTTP-request to execute corresponds to {@link HttpFactory#baseUrl(String) base url},
     * which is appending with {@code apiPath} and map of URL-parameters {@code paramsMap}.
     *
     * @param apiPath an API-path that will be appended to {@link HttpFactory#baseUrl(String) base url}
     * @param paramsMap a map with URL-parameter's names as {@link Map.Entry#getKey() key}
     *                  and URL-parameter's values as {@link Map.Entry#getValue() value}
     * @return the body of HTTP-response as {@link String}
     */
    String httpGetBodyString(String apiPath, Map<String, String> paramsMap);

    /**
     * The same as {@link #httpGetBodyString(String, Map)}, but with <b>empty</b> API-path
     *
     * @param paramsMap a map with URL-parameter's names as {@link Map.Entry#getKey() key}
     *                  and URL-parameter's values as {@link Map.Entry#getValue() value}
     * @return the body of HTTP-response as {@link String}
     */
    default String httpGetBodyString(Map<String, String> paramsMap) {
        return httpGetBodyString("", paramsMap);
    }

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
    class HttpFactory {
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
