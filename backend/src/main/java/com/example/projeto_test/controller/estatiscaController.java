import com.example.projeto_test.dto.StatisticsDTO;
import com.example.projeto_test.service.TarefaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/statistics")
@CrossOrigin(origins = "http://localhost:3000")
public class EstatiscaController {
    private final TarefaService tarefaService;

    public EstatiscaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping("/summary")
    public ResponseEntity<StatisticsDTO> getTaskStatistics() {
        log.info("Fetching task statistics summary");
        StatisticsDTO statistics = tarefaService.getTaskStatistics();
        return ResponseEntity.ok(statistics);
    }
}
package com.example.projeto_test.controllerz;

