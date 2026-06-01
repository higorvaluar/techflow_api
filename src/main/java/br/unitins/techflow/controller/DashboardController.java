package br.unitins.techflow.controller;

import br.unitins.techflow.dto.DashboardResumoDTO;
import br.unitins.techflow.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumo")
    public ResponseEntity<DashboardResumoDTO> gerarResumo() {
        return ResponseEntity.ok(dashboardService.gerarResumo());
    }

    @GetMapping("/charts")
    public ResponseEntity<Map<String, Object>> gerarCharts() {
        return ResponseEntity.ok(dashboardService.gerarCharts());
    }
}