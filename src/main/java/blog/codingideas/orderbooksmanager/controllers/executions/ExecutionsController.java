package blog.codingideas.orderbooksmanager.controllers.executions;

import blog.codingideas.orderbooksmanager.model.Execution;
import blog.codingideas.orderbooksmanager.service.ExecutionsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/execution")
public class ExecutionsController implements ExecutionsControllerInterface {

    @Autowired
    ExecutionsManager executionsManager;

    @Override
    @PostMapping(value = "", consumes = "application/json")
    public ResponseEntity<Execution> createAndAddExecution(@RequestBody Execution execution) {
        Execution addedExecution = executionsManager.createAndAddExecution(execution.getInstrumentId(), execution.getQuantity(), execution.getPrice());
        return new ResponseEntity<>(addedExecution, HttpStatus.CREATED);
    }
}
