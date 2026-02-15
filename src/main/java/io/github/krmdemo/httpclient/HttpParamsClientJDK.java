package io.github.krmdemo.httpclient;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpParamsClientJDK implements HttpParamsClient {

    private final String baseUrl;
    private final HttpClient httpClient = HttpClient.newBuilder().build();

    HttpParamsClientJDK(HttpFactory httpFactory) {
        this.baseUrl = httpFactory.baseUrl();
    }

    @Override
    public String httpGetBodyString(String apiPath, Map<String, String> paramsMap) {
        URI uri = URI.create(baseUrl + apiPath + urlParams(paramsMap));
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .timeout(HTTP_TIMEOUT)
            .uri(uri)
            .header(HTTP_HEADER_NAME__KIND, HttpClientKind.JDK.name())
            .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalStateException(String.format(
                    "Unexpected HTTP-Status(%d) in HTTP-Response from URI(%s)",
                    response.statusCode(), uri));
            }
            return response.body();
        } catch (IOException | InterruptedException ex) {
            throw new IllegalStateException(String.format(
                "Failed to get HTTP-Response from URI(%s)", uri), ex);
        }
    }

    // ------------------------------------------------------------------------------------
    //  TODO: think about move it to a separate utility-class, which works with URL-params:
    // ------------------------------------------------------------------------------------

    public static String urlParams(Map<String, String> paramsMap) {
        return urlParams(paramsMap.entrySet().stream());
    }

    public static String urlParams(Stream<Map.Entry<String, String>> params) {
        String paramsJoined = params.map(HttpParamsClientJDK::urlEncode)
            .collect(Collectors.joining("&"));
        return StringUtils.isBlank(paramsJoined) ? "" : "?" + paramsJoined;
    }

    public static String urlEncode(Map.Entry<String, String> paramPair) {
        return urlEncode(paramPair.getKey()) + "=" + urlEncode(paramPair.getValue());
    }

    public static String urlEncode(String str) {
        return URLEncoder.encode(str, Charset.defaultCharset());
    }
}
