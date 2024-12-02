package br.anhembi.heal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.anhembi.heal.model.Medico;
import br.anhembi.heal.repository.MedicoRepo;

@Controller
public class LoginController {

    @Autowired
    private MedicoRepo mr;

    @RequestMapping("/registro")
    public String registro() {
        return "registro";
    }

    @RequestMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, Model model) {
        Medico medico = mr.findByEmail(email);

        if (medico != null && medico.getSenha().equals(senha)) {
            model.addAttribute("medicoLogado", medico);
            return "redirect:/pacientes";
        } else {
            model.addAttribute("erro", "Usuário ou senha inválidos!");
            return "login";
        }
    }
}