package br.anhembi.heal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.anhembi.heal.model.Medico;
import br.anhembi.heal.repository.MedicoRepo;

@Controller
public class MedicoController {

    @Autowired
    private MedicoRepo mr;

    @PostMapping("/salvarMedico")
    public String salvarMedico(@RequestParam String nome, @RequestParam String sobrenome,
            @RequestParam String email, @RequestParam String celular,
            @RequestParam String senha, @RequestParam String confirmarSenha, @RequestParam String genero, @RequestParam String especialidade) {

        if (mr.findByEmail(email) != null) {
            return "redirect:/registro?erro=E-mail já cadastrado!";
        }

        if (!senha.equals(confirmarSenha)) {
            return "redirect:/registro?erro=As senha não coincidem!";
        }

        Medico medico = new Medico(nome, sobrenome, email, celular, senha, genero, especialidade); // Usa o novo construtor
        mr.save(medico); // Salva o médico no banco de dados

        return "redirect:/login"; // Redireciona para a tela de login
    }

    // Mostrar lista de médicos
    @RequestMapping("/medicos")
    public String listarMedicos(Model model) {
        List<Medico> medicos = mr.findAll(); // Pega todos os médicos do banco
        model.addAttribute("medicos", medicos);
        return "medicos"; // A página onde a lista de médicos será exibida
    }

    // Excluir médico
    @RequestMapping("/deletarMedico/{id}")
    public String deletarMedico(@PathVariable Long id) {
        mr.deleteById(id); // Exclui o médico pelo ID
        return "redirect:/medicos"; // Redireciona de volta para a lista de médicos
    }
}
