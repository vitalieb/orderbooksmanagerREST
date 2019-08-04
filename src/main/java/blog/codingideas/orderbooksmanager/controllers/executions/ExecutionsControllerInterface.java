package blog.codingideas.orderbooksmanager.controllers.executions;

import blog.codingideas.orderbooksmanager.model.Execution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface ExecutionsControllerInterface {

    ResponseEntity<Execution> createAndAddExecution(@RequestBody Execution execution);
}
