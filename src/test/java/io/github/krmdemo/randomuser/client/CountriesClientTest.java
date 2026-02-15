package io.github.krmdemo.randomuser.client;

import io.github.krmdemo.httpclient.HttpClientKind;
import io.github.krmdemo.randomuser.Country;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * This is a <b>live-test</b> to multiple implementations of {@link RandomUsersClient},
 * which performs a remote HTTP-calls to real REST-API.
 * <hr/>
 * TODO: implement and cover all other API-methods with tests
 */
@Slf4j
public class CountriesClientTest {

    @ParameterizedTest
    @EnumSource(names = {
        "JDK",
        "APACHE_HTTP",
        "OK_HTTP"
    })
    void testListAll(HttpClientKind httpClientKind) {
        CountriesClient client = CountriesClient.kind(httpClientKind).create();
        List<Country> listAll = client.listAll();
        log.info("{}: listAll.size = {}", httpClientKind, listAll.size());
        assertThat(listAll).hasSize(250);
    }

    @ParameterizedTest
    @EnumSource(names = {
        "JDK",
        "APACHE_HTTP",
        "OK_HTTP"
    })
    void testGetByAlpha_USA(HttpClientKind httpClientKind) {
        CountriesClient client = CountriesClient.kind(httpClientKind).create();
        Country countryUSA = client.getByAlpha("USA");
        log.info("{}: countryUSA --> {}", httpClientKind, countryUSA);
        assertThat(countryUSA)
            .extracting(
                Country::getName,
                Country::getRegion,
                Country::getCapital)
            .containsExactly(
                "United States of America",
                "Americas",
                "Washington, D.C.");
    }

    @ParameterizedTest
    @EnumSource(names = {
        "JDK",
        "APACHE_HTTP",
        "OK_HTTP"
    })
    void testGetByAlpha_Unknown(HttpClientKind httpClientKind) {
        CountriesClient client = CountriesClient.kind(httpClientKind).create();
        assertThatThrownBy(
            () -> client.getByAlpha("la-la-la")
        ).isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("alpha/la-la-la")
            .hasMessageContaining("HTTP-Status(404)");
    }
}
