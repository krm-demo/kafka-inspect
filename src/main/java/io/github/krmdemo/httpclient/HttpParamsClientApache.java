package io.github.krmdemo.httpclient;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Timeout;
import org.apache.hc.core5.util.VersionInfo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class HttpParamsClientApache implements HttpParamsClient, AutoCloseable {

    /**
     * The name of HTTP-header that would correspond to the version of "Apache HTTP-Client"
     */
    public static final String HTTP_HEADER_NAME__VERSION =
        "X-Http-Params-Client-Apache-Client-Version";

    private final String baseUrl;
    private final CloseableHttpClient httpclient = HttpClients.createDefault();

    HttpParamsClientApache(HttpFactory httpFactory) {
        this.baseUrl = httpFactory.baseUrl();
    }

    @Override
    public String httpGetBodyString(Map<String, String> paramsMap) {
        HttpGet httpGet = httpGetParams(paramsMap);
        httpGet.addHeader(HTTP_HEADER_NAME__KIND, HttpClientKind.APACHE_HTTP);
        httpGet.addHeader(HTTP_HEADER_NAME__VERSION, versionInfo.getRelease() + " (classic)");
        httpGet.setConfig(requestConfig);
        try {
            return httpclient.execute(httpGet, response -> {
                if (response.getCode() != 200) {
                    throw new IllegalStateException(String.format(
                        "HttpGet request is failed with code %d", response.getCode()));
                }
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            });
        } catch (IOException ex) {
            throw new IllegalStateException(String.format(
                "Failed to get HTTP-Response from HttpGet(%s)", httpGet), ex);
        }
    }

    private HttpGet httpGetParams(Map<String, String> paramsMap) {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseUrl);
            paramsMap.forEach(uriBuilder::addParameter);
            return new HttpGet(uriBuilder.build());
        } catch (URISyntaxException uriEx) {
            throw new IllegalArgumentException(String.format(
                "Failed to create HttpGet for basUrl(%s) and params --> %s",
                baseUrl, paramsMap), uriEx);
        }
    }

    @Override
    public void close() throws Exception {
        httpclient.close();
    }

    private static final VersionInfo versionInfo =
        VersionInfo.loadVersionInfo("org.apache.hc.client5", null);

    private static final Timeout timeout = Timeout.of(HTTP_TIMEOUT);

    private static final RequestConfig requestConfig = RequestConfig.custom()
        .setRedirectsEnabled(false)
        .setConnectionRequestTimeout(timeout)
        .setResponseTimeout(timeout)
        .build();

}
