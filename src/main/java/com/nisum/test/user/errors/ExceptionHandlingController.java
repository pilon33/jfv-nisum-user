package com.nisum.test.user.errors;

import com.nisum.test.user.utils.HeaderUtil;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlingController implements ProblemHandling, SecurityAdviceTrait {
    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String MESSAGE_KEY = "message";
    private static final String PATH_KEY = "path";
    private static final String VIOLATIONS_KEY = "violations";

    private String applicationName;

    /**
     * Post-process the Problem payload to add the message key for the front-end if
     * needed.
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null) {
            return entity;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }
        ProblemBuilder builder = Problem.builder()
                .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE
                        : problem.getType())
                .withStatus(problem.getStatus()).withTitle(problem.getTitle())
                .with(PATH_KEY, request.getNativeRequest(HttpServletRequest.class).getRequestURI());

        if (problem instanceof ConstraintViolationProblem) {
            builder.with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations()).with(MESSAGE_KEY,
                    ErrorConstants.ERR_VALIDATION);
        } else {
            builder.withCause(((DefaultProblem) problem).getCause()).withDetail(problem.getDetail())
                    .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey(MESSAGE_KEY) && problem.getStatus() != null) {
                builder.with(MESSAGE_KEY, "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream()
                .map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getCode())).collect(Collectors.toList());
        List<FieldError> fieldErrores = result.getFieldErrors();
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrores) {
            errors.add(fieldError.getDefaultMessage());
        }
        Problem problem = Problem.builder().withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
                .withTitle("Los datos de entrada presentan problemas").withStatus(defaultConstraintViolationStatus())
                .with(MESSAGE_KEY, errors)//
                .with(FIELD_ERRORS_KEY, fieldErrors).build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
        Problem problem = Problem.builder().withStatus(Status.NOT_FOUND)
                .with(MESSAGE_KEY, ErrorConstants.ENTITY_NOT_FOUND_TYPE).build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex,
            NativeWebRequest request) {
        return create(ex, request, HeaderUtil.createFailureAlert(applicationName, false, ex.getEntityName(),
                ex.getErrorKey(), ex.getMessage()));
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Problem> handleConstraintViolation(ConstraintViolationException ex,
            NativeWebRequest request) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            errors.add(constraintViolation.getMessage());
        }
        String message = "Ha ocurrido un problema de validación con los datos de entrada";
        Problem problem = Problem.builder().withStatus(Status.UNPROCESSABLE_ENTITY).withTitle(message)
                .with(MESSAGE_KEY, ErrorConstants.ERR_CONCURRENCY_FAILURE).build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex, NativeWebRequest request) {
        Problem problem = Problem.builder().withStatus(Status.CONFLICT)
                .with(MESSAGE_KEY, ErrorConstants.ERR_CONCURRENCY_FAILURE).build();
        return create(ex, problem, request);
    }
}
