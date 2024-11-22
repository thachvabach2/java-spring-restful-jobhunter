package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.error.IdInvalidException;

@RestController
public class HelloController {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<String> handleIdException(IdInvalidException idException) {
        return ResponseEntity.badRequest().body(idException.getMessage());
    }

    @GetMapping("/")
    public String getHelloWorld() throws IdInvalidException {
        if (true) {
            throw new IdInvalidException("check");
        }
        return "Hello World (Hỏi Dân IT & Eric)";
    }

}