package io.github.krmdemo.randomuser.client;

import io.github.krmdemo.httpclient.HttpClientKind;
import io.github.krmdemo.httpclient.HttpParamsClient;
import io.github.krmdemo.randomuser.Country;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * This interface represents a REST-API to {@value BASE_API_URL__COUNTRIES}.
 *
 * @see <a href="https://www.apicountries.com/">The Free RESTful Countries Data API</a>
 */
public interface CountriesClient {

    /**
     * Base (root) URL of <a href="https://www.apicountries.com/">The Free RESTful Countries Data API</a>
     */
    String BASE_API_URL__COUNTRIES = "https://www.apicountries.com/";

    /**
     * Getting the list of all countries at our planet (as of today's year - 2026)
     *
     * @return all countries as the list of {@link Country}
     */
    List<Country> listAll();

    /**
     * Getting the country by either 2-letters or 3-letters alpha-characters code.
     *
     * @param alphaCode 2-letters or 3-letters alpha-characters code
     * @return the country as {@link Country}
     */
    Country getByAlpha(String alphaCode);

    List<Country> listByName(String name);

    default Country getByName(String name) {
        return listByName(name).getFirst();
    }

    List<Country> listByLang(String lang);

    List<Country> listByRegion(String region);

    /**
     * Creator (factory-method) for {@link Factory}
     *
     * @param kind a kind of low-level HTTP-client
     * @return an instance of {@link HttpParamsClient.HttpFactory} according to {@code kind}
     */
    static Factory kind(HttpClientKind kind) {
        return new Factory(kind);
    }

    @Getter
    @Setter
    @Accessors(fluent = true, chain = true)
    class Factory {
        private final HttpParamsClient.HttpFactory httpFactory;
        private Factory(HttpClientKind kind) {
            this.httpFactory = HttpParamsClient.httpKind(kind)
                .baseUrl(BASE_API_URL__COUNTRIES);
        }
        public CountriesClient create() {
            return new CountriesClientImpl(this);
        }
    }
}
