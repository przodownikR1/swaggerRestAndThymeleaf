package pl.java.scalatech.web.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @RequestMapping("/admin")
    public String getMessage() {
        return "ok";
    }

}
