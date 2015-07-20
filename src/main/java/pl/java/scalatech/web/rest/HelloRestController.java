package pl.java.scalatech.web.rest;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.collect.Maps;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "Test controller", description = "API test ")
@RequestMapping("/api/person")
@RestController
@Slf4j
public class HelloRestController {

    Map<Long, Person> maps = Maps.newHashMap();

    @PostConstruct
    public void init() {

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "find person", httpMethod = "GET")
    public Person getPerson(@PathVariable Long id) {

        Person person = new Person(id, "przodownik", BigDecimal.valueOf(123));
        log.info("get person {}", person);
        return person;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody Person person, HttpServletResponse response) {
        log.info("create person {}", person);
        String location = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").buildAndExpand(person.getId()).toUriString();
        Person p = maps.putIfAbsent(person.getId(), person);

        response.setHeader("Location", location);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody Person person) {
        person.setId(id);
        maps.put(id, person);
        log.info("update person {}", person);
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Person {
    private Long id;
    private String login;
    private BigDecimal salary;
}