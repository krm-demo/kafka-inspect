package io.github.krmdemo.httpclient;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;

public class HttpParamsClientOkHttp implements HttpParamsClient {

    /**
     * The name of HTTP-header that would correspond to the version of "Apache HTTP-Client"
     */
    public static final String HTTP_HEADER_NAME__VERSION =
        "X-Http-Params-Client-OkHtpp-Version";

    private final HttpUrl baseUrl;
    OkHttpClient httpclient = new OkHttpClient.Builder()
        .readTimeout(HTTP_TIMEOUT)
        .writeTimeout(HTTP_TIMEOUT)
        .callTimeout(HTTP_TIMEOUT)
        .build();

    /**
     * Package-private constructor forces to use {@link HttpFactory}
     * @param httpFactory a factory with initializing parameters
     */
    HttpParamsClientOkHttp(HttpFactory httpFactory) {
        this.baseUrl = HttpUrl.parse(httpFactory.baseUrl());
        if (this.baseUrl == null) {
            throw new IllegalArgumentException(String.format(
                "baseUrl(%s) is null", httpFactory.baseUrl()));
        }
    }

    @Override
    public String httpGetBodyString(String apiPath, Map<String, String> paramsMap) {
        HttpUrl.Builder urlBuilder = baseUrl.newBuilder();
        if (StringUtils.isNotBlank(apiPath)) {
            urlBuilder.addPathSegments(apiPath);
        }
        paramsMap.forEach(urlBuilder::addQueryParameter);
        HttpUrl httpUrl = urlBuilder.build();

        Request request = new Request.Builder()
            .url(httpUrl)
            .addHeader(HTTP_HEADER_NAME__KIND, HttpClientKind.OK_HTTP.name())
            .addHeader(HTTP_HEADER_NAME__VERSION, OkHttp.VERSION)
            .build();
        try {
            Call call = httpclient.newCall(request);
            try (Response response = call.execute()) {
                if (!response.isSuccessful()) {
                    throw new IllegalStateException(String.format(
                        "Http request is failed with code %d(%s)",
                        response.code(), response.message()));
                }
                return response.body().string();
            }
        } catch (IOException ioEx) {
            throw new IllegalStateException(String.format(
                "Failed to get HTTP-Response from HttpUrl(%s)", httpUrl), ioEx);
        }
    }
}
