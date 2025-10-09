package ru.arkhipov.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;

import java.util.stream.Collectors;

@Service
public class RequestValidationService implements ValidationService {
    @Override
    public void isValid(BindingResult bindingResult) throws ValidationFailedException {
        if (bindingResult.hasErrors())
            throw new ValidationFailedException(
                    bindingResult.getFieldErrors().stream()
                    .map(this::errorToString)
                    .collect(Collectors.joining("; ")));
    }

    private String errorToString(FieldError error){
        return error.getField() + ": " + error.getDefaultMessage();
    }
}
