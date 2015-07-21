package pl.java.scalatech.web.rest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;
@Api(value = "Test controller", description = "API test ")
@RequestMapping("/api/person")
@RestController
@Slf4j
public class HelloRestController {

    Map<Long, Person> maps = Maps.newHashMap();

    @PostConstruct
    public void init() {
        maps.put(1l, new Person(1l, "przodownik", new BigDecimal(12)));
        maps.put(2l,  new Person(2l, "przodownik2", new BigDecimal(23)));
        maps.put(3l, new Person(3l, "poka", new BigDecimal(6)));
        maps.put(4l, new Person(4l, "bak", new BigDecimal(13)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "find person", httpMethod = "GET",consumes = "application/json,application/xml",produces = "application/json,application/xml")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid Request"),
            @ApiResponse(code = 404, message = "Request not found") })
    public ResponseEntity<?> getPerson(@PathVariable Long id) {
        Person person = maps.get(id);
        if (person == null) { return ResponseEntity.notFound().build(); }
        log.info("get person {}", person);
        return ResponseEntity.ok(person);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "find all person", httpMethod = "GET")
    public ResponseEntity<List<Person>> getPersons() {
        return ResponseEntity.ok(Lists.newArrayList(maps.values()));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "create person", httpMethod = "POST")
    public void add(@RequestBody Person person, HttpServletResponse response) {
        log.info("create person {}", person);
        String location = ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").buildAndExpand(person.getId()).toUriString();
        Person p = maps.putIfAbsent(person.getId(), person);
        response.setHeader("Location", location);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "update person", httpMethod = "PUT",consumes = "application/json,application/xml", produces = "application/json,application/xml")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody Person person) {
        person.setId(id);
        maps.put(id, person);
        log.info("update person {}", person);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "delete person", httpMethod = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid Request"),
            @ApiResponse(code = 404, message = "Request not found") })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Person deleted = maps.remove(id);
        log.info("delete person {}", deleted);
        if (deleted != null) { return ResponseEntity.noContent().build(); }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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