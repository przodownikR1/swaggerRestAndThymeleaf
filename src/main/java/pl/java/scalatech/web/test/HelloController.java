package pl.java.scalatech.web.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @Value("${message}")
    private String message;

    @RequestMapping
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", message);
        return "hello";
    }
}