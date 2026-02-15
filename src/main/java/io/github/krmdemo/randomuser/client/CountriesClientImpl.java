package io.github.krmdemo.randomuser.client;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.krmdemo.httpclient.HttpParamsClient;
import io.github.krmdemo.randomuser.Country;
import lombok.extern.slf4j.Slf4j;
import org.krmdemo.techlabs.core.utils.JacksonUtils;

import java.util.List;

import static java.util.Collections.emptyMap;

/**
 * Implementation of {@link CountriesClient} that is based on {@link HttpParamsClient}
 * <b>without</b> any high-level wrappers like Spring's {@code RestTemplate} or {@code RestClient}..
 */
@Slf4j
public class CountriesClientImpl implements CountriesClient {

    private final HttpParamsClient httpParamsClient;

    CountriesClientImpl(CountriesClient.Factory factory) {
        this.httpParamsClient = factory.httpFactory().create();
    }

    private List<Country> listByApiPath(String apiPath) {
        String responseBody = httpParamsClient.httpGetBodyString(apiPath, emptyMap());
        return JacksonUtils.jsonValueFromString(responseBody, new TypeReference<>(){});
    }

    @Override
    public List<Country> listAll() {
        return listByApiPath("countries");
    }

    @Override
    public Country getByAlpha(String alphaCode) {
        String responseBody = httpParamsClient.httpGetBodyString("alpha/" + alphaCode, emptyMap());
        return JacksonUtils.jsonValueFromString(responseBody, new TypeReference<>(){});
    }

    @Override
    public List<Country> listByName(String name) {
        return listByApiPath("name/" + name);
    }

    @Override
    public List<Country> listByLang(String lang) {
        return listByApiPath("lang/" + lang);
    }

    @Override
    public List<Country> listByRegion(String region) {
        return listByApiPath("region/" + region);
    }
}
