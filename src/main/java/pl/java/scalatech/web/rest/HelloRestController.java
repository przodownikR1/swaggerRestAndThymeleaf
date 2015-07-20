package pl.java.scalatech.web.rest;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "Test controller", description = "API test ")
@RestController
public class HelloRestController {
    @RequestMapping("/api/hello")
    @ApiOperation(value = "find person", httpMethod = "GET")
    public Person getPerson() {
        return new Person("przodownik", BigDecimal.valueOf(123));
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Person {
    private String login;
    private BigDecimal salary;
}