package com.beeleza.order_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidOrderException.class)
    public ProblemDetail handleInvalidOrder(InvalidOrderException ex) {
        return problem(HttpStatus.UNPROCESSABLE_CONTENT, "Pedido inválido", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> Map.of(
                        "campo", e.getField(),
                        "mensagem", String.valueOf(e.getDefaultMessage())))
                .toList();

        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "Dados do pedido inválidos",
                "Um ou mais campos não passaram na validação");
        pd.setProperty("erros", erros);
        return pd;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException ex) {
        return problem(HttpStatus.BAD_REQUEST, "Requisição malformada",
                "O corpo da requisição não é um JSON válido ou tem tipos incorretos");
    }

    @ExceptionHandler(OrderPublishException.class)
    public ProblemDetail handlePublish(OrderPublishException ex) {
        log.error("Falha ao publicar pedido {}", ex.getOrderId(), ex);
        return problem(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de mensageria indisponível",
                "Não foi possível registrar o pedido. Tente novamente em instantes.");
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}
