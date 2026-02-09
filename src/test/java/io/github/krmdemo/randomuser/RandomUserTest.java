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
 * This unit-test verifies serialization and de-serialization abilities of REST-data-class {@link RandomUser}
 *
 * @see <a href="https://github.com/lukas-krecan/JsonUnit">json-unit-assertj</a>
 */
public class RandomUserTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "unit-tests/sample-user--Abel-Moreau.json",
        "unit-tests/sample-user--Dya-XXX.json",      // <-- seed: "00fdd9bf0ac99b24"
        "unit-tests/sample-user--Adam-Morris.json"   // <-- seed: "215b765ce74a2f9e"
    })
    void testWholeJSON(String resourcePath) {
        String originalJson = CoreResourceUtils.resourceAsText(resourcePath);
        RandomUser randomUser = JacksonUtils.jsonValueFromResource(resourcePath, RandomUser.class);
        String dumpedJson = DumpUtils.dumpAsJsonTxt(randomUser);

        // assertThat(dumpedJson).isEqualTo(originalJson);  // <-- does not work !!!
        // assertThat(dumpedJson).isEqualToIgnoringWhitespace(originalJson);  // <-- it's not enough

        // An example of lenient JSON-comparison using https://github.com/skyscreamer/JSONassert
        //JSONAssert.assertEquals(originalJson, dumpedJson, JSONCompareMode.LENIENT);  // <-- problems with integers

        // An example of lenient JSON-comparison using https://github.com/lukas-krecan/JsonUnit?tab=readme-ov-file#features
        assertThatJson(dumpedJson)
            .whenIgnoringPaths(
                "dob.age",
                "location.postcode",
                "location.street.number",
                "registered.age")
            .isEqualTo(originalJson);
        // TODO: investigate this and abilities of <<...and( a -> assertThat(a.node("id")...)...>>
    }

    @ParameterizedTest(name = "[{index}] check some properties of {1}")
    @CsvSource({
        "unit-tests/sample-user--Abel-Moreau.json, 'Mr Abel Moreau', 77, 64573, 3175, 7",
        "unit-tests/sample-user--Dya-XXX.json, 'Miss Oya Topaloğlu', 27, 65290, 6055, 23",
        "unit-tests/sample-user--Adam-Morris.json, 'Mr Adam Morris', 64, 24705, 6533, 18"
    })
    void testSomeProps(String resourcePath, String fullName, int ageBirth, String postcode, int streetNum, int ageReg) {
        RandomUser randomUser = JacksonUtils.jsonValueFromResource(resourcePath, RandomUser.class);
        assertThat(randomUser.getName().full()).isEqualTo(fullName);
        assertThat(randomUser.getDateOfBirth().age()).isEqualTo(ageBirth);
        assertThat(randomUser.getLocation().postcode()).isEqualTo(postcode);
        assertThat(randomUser.getLocation().streetHouse().number()).isEqualTo(streetNum);
        assertThat(randomUser.getRegisteredAt().age()).isEqualTo(ageReg);
    }
}
