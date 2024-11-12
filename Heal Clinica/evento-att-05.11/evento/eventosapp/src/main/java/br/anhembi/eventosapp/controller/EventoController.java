package br.anhembi.eventosapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.anhembi.eventosapp.model.Convidado;
import br.anhembi.eventosapp.model.Evento;
import br.anhembi.eventosapp.repository.ConvidadoRepo;
import br.anhembi.eventosapp.repository.EventoRepo;
import jakarta.validation.Valid;

@Controller
public class EventoController {

    @Autowired
    private EventoRepo er;

    @Autowired
    private ConvidadoRepo cr;

    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.GET)
    public String form() {
        return "formEvento";
    }

    @RequestMapping(value = "/cadastrarEvento", method = RequestMethod.POST)
    public String form(@Valid Evento evento, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/cadastrarEvento";
        }

        er.save(evento);
        attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
        return "redirect:/cadastrarEvento";
    }

    @RequestMapping("/eventos")
    public ModelAndView listaEventos() {
        ModelAndView mv = new ModelAndView("index");
        Iterable<Evento> eventos = er.findAll();
        mv.addObject("eventos", eventos);
        return mv;
    }

    // redireciona para o respectivo código do evento
    // mostra os detalhes do evento
    @RequestMapping(value = "/detalhesEvento/{codigo}", method = RequestMethod.GET)
    public ModelAndView detalhesEventos(@PathVariable("codigo") long codigo) {
        Evento evento = er.findByCodigo(codigo);
        ModelAndView mv = new ModelAndView("events/detalhesEvento");
        mv.addObject("evento", evento);
        Iterable<Convidado> convidados = cr.findByEvento(evento);
        mv.addObject("convidados", convidados);
        return mv;
    }

    @RequestMapping("/deletarEvento")
    public String deletarEvento(long codigo) {
        Evento evento = er.findByCodigo(codigo);
        er.delete(evento);
        return "redirect:/eventos";
    }

    @RequestMapping(value = "/detalhesEvento/{codigo}", method = RequestMethod.POST)
    public String detalhesEventos(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem",
                    "Verifique os campos!");
            return "redirect:/detalhesEvento/{codigo}";
        }

        Evento evento = er.findByCodigo(codigo);
        convidado.setEvento(evento);
        cr.save(convidado);
        attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
        return "redirect:/detalhesEvento/{codigo}";
    }

    @RequestMapping("/deletarConvidado")
    public String deletarConvidado(String rg) {
        Convidado convidado = cr.findByRg(rg);
        cr.delete(convidado);

        Evento evento = convidado.getEvento();
        long codigoLong = evento.getCodigo();
        String codigo = "" + codigoLong;
        return "redirect:/detalhesEvento/" + codigo;
    }

    @RequestMapping(value = "/editarConvidado", method = RequestMethod.GET)
    public ModelAndView editarConvidado(@RequestParam("rg") String rg) {
        ModelAndView mv = new ModelAndView("events/editarConvidado");
        Convidado convidado = cr.findByRg(rg);
        mv.addObject("convidado", convidado);
        return mv;
    }

    @RequestMapping(value = "/atualizarConvidado", method = RequestMethod.POST)
    public String atualizarConvidado(@Valid Convidado convidado, BindingResult result,
            RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/editarConvidado/" + convidado.getRg();
        }

        Convidado convidadoExistente = cr.findByRg(convidado.getRg());

        if (convidadoExistente != null) {
            convidadoExistente.setNomeConvidado(convidado.getNomeConvidado());
            cr.save(convidadoExistente);

            Evento evento = convidadoExistente.getEvento();
            long codigoLong = evento.getCodigo();
            String codigo = "" + codigoLong;

            attributes.addFlashAttribute("mensagem", "Convidado atualizado com sucesso!");
            return "redirect:/detalhesEvento/" + codigo;
        } else {
            attributes.addFlashAttribute("mensagem", "Convidado não encontrado!");
            return "redirect:/eventos";
        }
    }

    @RequestMapping(value = "/editarEvento", method = RequestMethod.GET)
    public ModelAndView editarEvento(@RequestParam("codigo") long codigo) {
        ModelAndView mv = new ModelAndView("events/editarEvento");
        Evento evento = er.findByCodigo(codigo);
        mv.addObject("evento", evento);
        return mv;
    }

    @RequestMapping(value = "/atualizarEvento", method = RequestMethod.POST)
    public String atualizarEvento(@Valid Evento evento, BindingResult result,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos!");
            return "redirect:/editarEvento/" + evento.getCodigo();
        }
        Evento eventoExistente = er.findByCodigo(evento.getCodigo());

        if (eventoExistente != null) {
            eventoExistente.setNome(evento.getNome());
            eventoExistente.setData(evento.getData());
            eventoExistente.setLocal(evento.getLocal());
            eventoExistente.setHorario(evento.getHorario());

            er.save(eventoExistente);

            attributes.addFlashAttribute("mensagem", "Evento atualizado com sucesso!");
            return "redirect:/detalhesEvento/" + evento.getCodigo();
        } else {
            attributes.addFlashAttribute("mensagem", "Evento não encontrado!");
            return "redirect:/eventos";
        }
    }
}
