package br.unitins.techflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiInfoController {

    @GetMapping("/")
    public Map<String, Object> info() {
        return Map.of(
                "nome", "TechFlow API",
                "descricao", "API do Sistema de Gestão do Conhecimento para Suporte de TI",
                "status", "online",
                "versao", "1.0.0",
                "endpoints", Map.of(
                        "categorias", "/categorias",
                        "tecnicos", "/tecnicos",
                        "problemas", "/problemas",
                        "solucoes", "/solucoes",
                        "chamados", "/chamados",
                        "dashboard", "/dashboard/resumo",
                        "ia", "/ia/analisar-problema"
                )
        );
    }
}