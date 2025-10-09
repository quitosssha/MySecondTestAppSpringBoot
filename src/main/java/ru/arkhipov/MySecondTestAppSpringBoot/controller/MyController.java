package ru.arkhipov.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Request;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Response;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ValidationService;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class MyController {

    private final ValidationService validationService;

    @Autowired
    public MyController(ValidationService validationService) {
        this.validationService = validationService;
    }


    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        var uid = request.getUid();

        var response = Response.builder()
                .uid(uid)
                .operationUid(request.getOperationUid())
                .systemTime(simpleDateFormat.format(new Date()))
                .code("success")
                .errorCode("")
                .errorMessage("")
                .build();

        try {
            validationService.isValid(bindingResult);
            ensureSupportedUid(uid);
        } catch (ValidationFailedException e) {
            response.setCode("failed");
            response.setErrorCode(e.getClass().getSimpleName());
            response.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            response.setCode("unsupportedCode");
            response.setErrorCode(e.getClass().getSimpleName());
            response.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e){
            response.setCode("error");
            response.setErrorCode("UnknownException");
            response.setErrorMessage("Произошла непредвиденная ошибка");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void ensureSupportedUid(String uid) throws UnsupportedCodeException {
        if (uid.equals("123"))
            throw new UnsupportedCodeException("unsupported uid: 123");
    }
}
