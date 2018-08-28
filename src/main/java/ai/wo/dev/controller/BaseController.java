package ai.wo.dev.controller;


import ai.wo.dev.model.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleException(Exception ex) {

        APIResponse res = new APIResponse();
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
