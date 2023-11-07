package utils;

import com.botcamp.utils.GzipUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class GzipUtilsTests {
    private static final String INPUT_1 = "Hello my name is Toto, I love chickens, I really love the sea, and i really love to travel in the whole world. I'm really writing nothing to expand the size of this string. What about telling what I did last week? I ate a lot of chickens ! I really want to show you that my Utility function is really helpful to reduce the size of the content of each REST response i will be sending to improve the efficiency of my API";

    @Test
    public void compress_decompress_test() {
        log.info("Input string size: " + INPUT_1.length());
        InputStream is = new ByteArrayInputStream(INPUT_1.getBytes());
        ByteArrayOutputStream compressionResult = GzipUtils.compress(is);
        assertThat(compressionResult).isNotNull();
        log.info("Compressed string size: " + compressionResult.toString().length());
        InputStream is2 = new ByteArrayInputStream(compressionResult.toByteArray());
        String decompressionResult = GzipUtils.decompress(is2);
        assertThat(decompressionResult).isEqualTo(INPUT_1);
    }

    @Test
    public void test_compress_null() {
        assertThat(GzipUtils.compress(null)).isNull();
    }

    @Test
    public void test_decompress_null() {
        assertThat(GzipUtils.decompress(null)).isNull();
    }
}
