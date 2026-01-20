package io.github.krmdemo.randomuser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.krmdemo.techlabs.core.dump.DumpUtils;
import org.krmdemo.techlabs.core.utils.CoreResourceUtils;
import org.krmdemo.techlabs.core.utils.JacksonUtils;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This unit-test verifies serialization and de-serialization abilities of data-class {@link Country}
 */
public class CountryTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "unit-tests/sample-country--Brazil.json",   // <-- https://www.apicountries.com/name/Brazil
        "unit-tests/sample-country--USA.json",      // <-- https://www.apicountries.com/alpha/USA
    })
    void testWholeJSON(String resourcePath) {
        String originalJson = CoreResourceUtils.resourceAsText(resourcePath);
        Country randomUser = JacksonUtils.jsonValueFromResource(resourcePath, Country.class);
        String dumpedJson = DumpUtils.dumpAsJsonTxt(randomUser);
        assertThatJson(dumpedJson)
            .whenIgnoringPaths(
                "area",
                "gini",
                "population",
                "cioc",
                "independent",
                "latlng",
                "regionalBlocs")
            .isEqualTo(originalJson);
    }

    @ParameterizedTest(name = "[{index}] check some properties of country {1}")
    @CsvSource({
        "unit-tests/sample-country--Brazil.json, 'Brazil', 8_515_767, 53.4, 212_559_409",
        "unit-tests/sample-country--USA.json, 'United States of America', 9_629_091, 41.4, 329_484_123",
    })
    void testSomeProps(String resourcePath, String countryName, double countryArea, double gini, long population) {
        Country randomUser = JacksonUtils.jsonValueFromResource(resourcePath, Country.class);
        assertThat(randomUser.getName()).isEqualTo(countryName);
        assertThat(randomUser.getArea()).isEqualTo(countryArea);
        assertThat(randomUser.getGini()).isEqualTo(gini);
        assertThat(randomUser.getPopulation()).isEqualTo(population);
    }
}
