package ru.arkhipov.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.arkhipov.MySecondTestAppSpringBoot.model.*;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ModifySourceRequestService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ModifySystemNameRequestService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ValidationService;
import ru.arkhipov.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.time.Instant;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifySourceRequestService modifySourceRequestService;
    private final ModifySystemNameRequestService modifySystemNameRequestService;


    @Autowired
    public MyController(
            ValidationService validationService,
            ModifySourceRequestService modifyRequestService,
            ModifySystemNameRequestService modifySystemNameRequestService) {
        this.validationService = validationService;
        this.modifySourceRequestService = modifyRequestService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
    }


    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult){

        log.info("Request: {}", request.toString());
        request.setSystemTime(DateTimeUtil.getCustomFormat().format(Instant.now()));
        modifySystemNameRequestService.modify(request);
        modifySourceRequestService.modify(request);
        log.info("Request: {}", request.toString());

        new RestTemplate().exchange("http://localhost:8084/feedback",
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<>() { });

        var uid = request.getUid();

        var response = Response.builder()
                .uid(uid)
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(Instant.now()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        try {
            validationService.isValid(bindingResult);
            ensureSupportedUid(uid);
        } catch (ValidationFailedException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.info("Response: {}", response.toString());

            log.error("Validation error: {}", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);
            log.info("Response: {}", response.toString());

            log.error("Unsupported code error: {}", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e){
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.info("Response: {}", response.toString());

            log.error("Unknown error: {}", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void ensureSupportedUid(String uid) throws UnsupportedCodeException {
        if (uid.equals("123"))
            throw new UnsupportedCodeException("unsupported uid: 123");
    }
}
