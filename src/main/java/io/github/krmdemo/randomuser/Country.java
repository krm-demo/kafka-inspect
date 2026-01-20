package io.github.krmdemo.randomuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * This data-class represents all hierarchical properties of <b>Country</b>
 *
 * @see <a href="https://www.apicountries.com/">The Free RESTful Countries Data API</a>
 */
@Data
public class Country {

    private String name;
    @JsonProperty("topLevelDomain") private List<String> topLevelDomains;
    private String alpha2Code;
    private String alpha3Code;
    private List<String> callingCodes;
    private String capital;
    private List<String> altSpellings;
    private String region;
    private String subregion;
    private Long population;
    private String demonym;
    private Double area;
    private Double gini;
    private List<String> timezones;
    private List<String> borders;
    private String nativeName;
    private String numericCode;
    @JsonProperty("flag") private String flagLink;
    @JsonProperty("flags") private Map<String, String> flagLinksByExt;
    private List<Currency> currencies;
    private List<Language> languages;
    private Map<String, String> translations;

    public record Currency(
        String code,
        String name,
        String symbol
    ) {}

    public record Language(
        @JsonProperty("iso639_1") String alpha2Code,
        @JsonProperty("iso639_2") String alpha3Code,
        String name,
        String nativeName
    ) {}
}
