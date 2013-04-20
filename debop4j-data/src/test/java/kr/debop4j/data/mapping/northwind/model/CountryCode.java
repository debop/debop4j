package kr.debop4j.data.mapping.northwind.model;

/**
 * kr.debop4j.data.mapping.northwind.model.CountryCode
 *
 * @author sunghyouk.bae@gmail.com
 * @since 13. 2. 23.
 */
public enum CountryCode {
    Australia("Australis"),
    Brazil("Brazil"),
    Canada("Canada"),
    Denmark("Denmark"),
    Finland("Finland"),
    France("France"),
    Germany("Germany"),
    Italy("Italy"),
    Japan("Japan"),
    Korea("Korea"),
    Netherlands("Netherlands"),
    Norway("Norway"),
    Singapore("Singapore"),
    Spain("Spain"),
    Sweden("Sweden"),
    UK("UK"),
    USA("USA");

    private String country;

    CountryCode(String country) {
        this.country = country;
    }
}
