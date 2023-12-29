package utils;

import com.botcamp.common.utils.GzipUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class GzipUtilsTests {
    private static final String INPUT_1 = "Hello my name is Toto, I love chickens, I really love the sea, and i really love to travel in the whole world. I'm really writing nothing to expand the size of this string. What about telling what I did last week? I ate a lot of chickens ! I really want to show you that my Utility function is really helpful to reduce the size of the content of each REST response i will be sending to improve the efficiency of my API";

    @Test
    void compress_decompress_test() throws IOException {
        log.info("Input string size: " + INPUT_1.length());
        byte[] compressionResult = GzipUtils.compress(INPUT_1);
        assertThat(compressionResult).isNotNull();
        log.info("Compressed string size: " + compressionResult.length);
        String decompressionResult = GzipUtils.decompress(compressionResult);
        log.info("Decompressed string size: " + decompressionResult.length());
        assertThat(decompressionResult).isEqualTo(INPUT_1);
        assertThat(compressionResult).hasSizeLessThan(decompressionResult.length());
    }

    @Test
    void test_compress_null() throws IOException {
        assertThat(GzipUtils.compress(null)).isEqualTo(new byte[0]);
    }

    @Test
    void test_decompress_null() throws IOException {
        assertThat(GzipUtils.decompress(null)).isEmpty();
    }
}
