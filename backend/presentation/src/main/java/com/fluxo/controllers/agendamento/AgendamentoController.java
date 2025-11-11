package com.fluxo.controllers.agendamento;


import agendamento.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {

    @Autowired
    private final AgendamentoService service;

    @PostMapping
}
