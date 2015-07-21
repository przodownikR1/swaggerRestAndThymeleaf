package pl.java.scalatech.fetch;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.google.common.io.Files;

public class DownloadFileTest {
    private final String YAMAHA = "http://foto.scigacz.pl/gallery/aktualnosci/Yamaha_XJ6_naked/yamaha_XJ6_black.jpg";

    @Test
    public void shouldDownloadFile() throws IOException {
        List<HttpMessageConverter<?>> messageConverters = Lists.newArrayList();
        messageConverters.add(new ByteArrayHttpMessageConverter());

        RestTemplate restTemplate = new RestTemplate(messageConverters);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(YAMAHA, HttpMethod.GET, entity, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Files.write(response.getBody(), Paths.get("yamaha.jpg").toFile());
        }

    }
}