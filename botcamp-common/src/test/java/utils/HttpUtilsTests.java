package utils;

import com.botcamp.common.utils.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HttpUtilsTests {

    @Test
    void test_url() {
        String protocol = "http";
        String url = "localhost";
        int port = 7501;
        String endpoint = "/api/test";
        assertThat(HttpUtils.buildUrl(protocol, url, port, endpoint)).isEqualTo("http://localhost:7501/api/test");
    }

    @Test
    void test_url2() {
        String protocol = "https";
        String url = "127.0.0.1";
        int port = 6969;
        String endpoint = "api/test";

        assertThat(HttpUtils.buildUrl(protocol, url, port, endpoint)).isEqualTo("https://127.0.0.1:6969/api/test");
    }
}
