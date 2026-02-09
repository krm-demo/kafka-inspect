package io.github.krmdemo.randomuser;

import org.json.JSONException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.krmdemo.techlabs.core.dump.DumpUtils;
import org.krmdemo.techlabs.core.utils.CoreResourceUtils;
import org.krmdemo.techlabs.core.utils.JacksonUtils;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

/**
 * This unit-test verifies serialization and de-serialization abilities of REST-data-class {@link RandomUsersResult}
 *
 * @see <a href="https://github.com/lukas-krecan/JsonUnit">json-unit-assertj</a>
 */
public class RandomUsersResultTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "unit-tests/smaple-user-result--Davita-Hebels.json",
        "unit-tests/smaple-user-result--Gabrielle-Smith.json",
        "unit-tests/smaple-user-result--Jeanine-Meunier.json",
        "unit-tests/smaple-user-result--Megan-Taylor.json",
    })
    void testWholeJSON(String resourcePath) throws JSONException {
        String originalJson = CoreResourceUtils.resourceAsText(resourcePath);
        RandomUsersResult randomUser = JacksonUtils.jsonValueFromResource(resourcePath, RandomUsersResult.class);
        String dumpedJson = DumpUtils.dumpAsJsonTxt(randomUser);

        assertThatJson(dumpedJson)
            .whenIgnoringPaths(
                "info.results",
                "info.page",
                "results[*].dob.age",
                "results[*].location.postcode",
                "results[*].location.street.number",
                "results[*].registered.age")
            .isEqualTo(originalJson);
    }
}
