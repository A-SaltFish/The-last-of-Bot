package com.tlb.backend.controller.pk;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/pk/")
public class IndexController {

    @RequestMapping("/")
    public String index(){
        //注意themeleaf，可能是版本问题
        return "pk/index.html";
    }
}
