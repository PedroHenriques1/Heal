package br.anhembi.heal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.anhembi.heal.model.Consulta;
import br.anhembi.heal.model.Medico;
import br.anhembi.heal.model.Paciente;
import br.anhembi.heal.repository.ConsultaRepo;
import br.anhembi.heal.repository.MedicoRepo;
import br.anhembi.heal.repository.PacienteRepo;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PacienteController {

    @Autowired
    private PacienteRepo pr;

    @Autowired
    private ConsultaRepo cr;

    @Autowired
    private MedicoRepo mr;

    // Método para cadastrar o paciente
    @RequestMapping(value = "/cadastrarPaciente", method = RequestMethod.GET)
    public String formPaciente() {
        return "paciente/formPaciente";
    }

    // Método de cadastro de paciente
    @RequestMapping(value = "/cadastrarPaciente", method = RequestMethod.POST)
    public String form(@Valid Paciente paciente, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/cadastrarPaciente";
        }
        pr.save(paciente);
        attributes.addFlashAttribute("mensagem", "Paciente cadastrado com sucesso!");
        return "redirect:/cadastrarPaciente";
    }

    // Listar todos os pacientes
    @RequestMapping("/pacientes")
    public ModelAndView listarPacientes() {
        ModelAndView mv = new ModelAndView("index");
        Iterable<Paciente> pacientes = pr.findAll();
        mv.addObject("pacientes", pacientes);
        return mv;
    }

    // Detalhes do paciente
    @RequestMapping(value = "/detalhesPaciente/{rg}", method = RequestMethod.GET)
    public ModelAndView detalhesPaciente(@PathVariable("rg") String rg) {
        Paciente paciente = pr.findByRg(rg);
        ModelAndView mv = new ModelAndView("paciente/detalhesPaciente");
        mv.addObject("paciente", paciente);

        Iterable<Medico> medicos = mr.findAll();
        mv.addObject("medicos", medicos);

        Iterable<Consulta> consultas = cr.findByPaciente(paciente);
        mv.addObject("consultas", consultas);

        return mv;
    }

    // Adicionar consulta ao paciente
    @RequestMapping(value = "/detalhesPaciente/{rg}", method = RequestMethod.POST)
    public String salvarConsulta(@PathVariable("rg") String rg, @Valid Consulta consulta, BindingResult result,
            RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos");
            return "redirect:/detalhesPaciente/{rg}";
        }

        Paciente paciente = pr.findByRg(rg);
        consulta.setPaciente(paciente); // Atribui o paciente à consulta

        //verifica se o médico foi selecionado corretamente
        if (consulta.getMedico() == null || consulta.getMedico().getId() == null) {
            attributes.addFlashAttribute("mensagem", "Médico não informado!");
            return "redirect:/detalhesPaciente/{rg}";
        }

        //procura o médico no banco de dados
        Medico medico = mr.findById(consulta.getMedico().getId()).orElse(null);
        if (medico != null) {
            consulta.setMedico(medico); // médico à consulta
            cr.save(consulta); // salva a consulta no banco de dados
            attributes.addFlashAttribute("mensagem", "Consulta adicionada com sucesso!");
        } else {
            attributes.addFlashAttribute("mensagem", "Médico não encontrado!");
        }
        return "redirect:/detalhesPaciente/{rg}";
    }

    // Deletar paciente
    @RequestMapping("/deletarPaciente/{rg}")
    public String deletarPaciente(@PathVariable("rg") String rg) {
        Paciente paciente = pr.findByRg(rg);
        if (paciente != null) {
            pr.delete(paciente);
        }
        return "redirect:/pacientes";
    }

    // Deletar consulta
    @RequestMapping("/deletarConsulta")
    public String deletarConsulta(long codigo) {
        Consulta consulta = cr.findByCodigo(codigo);
        cr.delete(consulta);

        Paciente paciente = consulta.getPaciente();
        String rg = paciente.getRg();
        return "redirect:/detalhesPaciente/" + rg;
    }

    // Editar consulta
    @RequestMapping(value = "/editarConsulta", method = RequestMethod.GET)
    public ModelAndView editarConsulta(@RequestParam("codigo") long codigo) {
        ModelAndView mv = new ModelAndView("consulta/editarConsulta");
        Consulta consulta = cr.findByCodigo(codigo);
        mv.addObject("consulta", consulta);
        return mv;
    }

    // Atualizar consulta
    @RequestMapping(value = "/atualizarConsulta", method = RequestMethod.POST)
    public String atualizarConsulta(@Valid Consulta consulta, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/editarConsulta/" + consulta.getCodigo();
        }

        Consulta consultaExistente = cr.findByCodigo(consulta.getCodigo());

        if (consultaExistente != null) {
            consultaExistente.setNomeConsulta(consulta.getNomeConsulta());
            consultaExistente.setDataConsulta(consulta.getDataConsulta());
            consultaExistente.setHoraConsulta(consulta.getHoraConsulta());
            cr.save(consultaExistente); // Atualiza a consulta existente

            Paciente paciente = consultaExistente.getPaciente();
            String rg = paciente.getRg();

            attributes.addFlashAttribute("mensagem", "Consulta atualizada com sucesso!");
            return "redirect:/detalhesPaciente/" + rg;
        } else {
            attributes.addFlashAttribute("mensagem", "Consulta não encontrada!");
            return "redirect:/pacientes";
        }
    }

    // Editar paciente
    @RequestMapping(value = "/editarPaciente/{rg}", method = RequestMethod.GET)
    public ModelAndView editarPaciente(@PathVariable("rg") String rg) {
        ModelAndView mv = new ModelAndView("paciente/editarPaciente");
        Paciente paciente = pr.findByRg(rg);
        mv.addObject("paciente", paciente);
        return mv;
    }

    // Atualizar paciente
    @RequestMapping(value = "/atualizarPaciente", method = RequestMethod.POST)
    public String atualizarPaciente(@Valid Paciente paciente, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/editarPaciente/" + paciente.getRg();
        }

        Paciente pacienteExistente = pr.findByRg(paciente.getRg());

        if (pacienteExistente != null) {
            pacienteExistente.setNomeCompleto(paciente.getNomeCompleto());
            pacienteExistente.setDataNascimento(paciente.getDataNascimento());
            pacienteExistente.setTelefone(paciente.getTelefone());
            pacienteExistente.setSexo(paciente.getSexo());

            pr.save(pacienteExistente);

            attributes.addFlashAttribute("mensagem", "Paciente atualizado com sucesso!");
            return "redirect:/detalhesPaciente/" + paciente.getRg();
        } else {
            attributes.addFlashAttribute("mensagem", "Paciente não encontrado!");
            return "redirect:/pacientes";
        }
    }
}