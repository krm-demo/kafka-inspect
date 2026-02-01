package io.github.krmdemo.httpclient;

/**
 * This enumeration represents a non-mutable kind of HTTP-Client implementation,
 * where the concrete instance of HTTP-Client corresponds to the set of kinds
 */
public enum HttpClientKind {

    JDK,

    APACHE_HTTP,

    OK_HTTP
}
