package br.anhembi.eventosapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "inicial";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/cadastro")
    public String cadastro(){
        return "cadastro";
    }
}
