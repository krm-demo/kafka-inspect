package io.github.krmdemo.httpclient;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.json;
import static org.krmdemo.techlabs.core.utils.CoreStreamUtils.linkedMap;
import static org.krmdemo.techlabs.core.utils.CoreStreamUtils.nameValue;

public class HttpParamsClientTest {

    @ParameterizedTest
    @EnumSource(names = {
        "JDK",
        "APACHE_HTTP",
        "OK_HTTP"
    })
    void testHttpBin_GET(HttpClientKind httpClientKind) {
        HttpParamsClient httpClient = HttpParamsClient
            .httpKind(httpClientKind)
            .baseUrl("https://httpbin.org/get")
            .create();
        String responseBody = httpClient.httpGetBodyString(linkedMap(
            nameValue("param-one", "1"),
            nameValue("param-two", "" + Math.E)
        ));
        System.out.printf("responseBody for '%s' --> %s%n",
            httpClientKind, responseBody);
        assertThatJson(responseBody)
            .inPath("$.args").isEqualTo(json("""
                {
                  "param-one": "1",
                  "param-two": "2.718281828459045"
                }"""
            ));
        assertThatJson(responseBody)
            .inPath("$.headers.X-Http-Params-Client-Kind")
            .isEqualTo(httpClientKind.name());
    }
}
