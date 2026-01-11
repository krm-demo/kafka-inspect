package io.github.krmdemo.randomuser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.krmdemo.techlabs.core.dump.DumpUtils;
import org.krmdemo.techlabs.core.utils.JacksonUtils;

public class RandomUserTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "unit-tests/sample-user--Abel-Moreau.json"
    })
    void testRandomUser(String resourcePath) {
        RandomUser randomUser = JacksonUtils.jsonValueFromResource(resourcePath, RandomUser.class);
        System.out.println("randomUser --> " + DumpUtils.dumpAsJsonTxt(randomUser));
    }
}
