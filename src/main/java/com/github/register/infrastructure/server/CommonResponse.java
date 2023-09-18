package com.github.register.infrastructure.server;

import com.github.register.domain.payload.response.CodeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author sniper
 * @date 18 Sep 2023
 */
public abstract class CommonResponse {

    private static final Logger log = LoggerFactory.getLogger(CommonResponse.class);

    private static ResponseEntity send(HttpStatusCode status, String message) {
        Integer code = (status.is2xxSuccessful() ? CodeMessage.CODE_SUCCESS : CodeMessage.CODE_DEFAULT_FAILURE);
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CodeMessage(code, message));
    }

    private static ResponseEntity send(String headerName, String headerValues, HttpStatusCode status, String message) {
        Integer code = (status.is2xxSuccessful() ? CodeMessage.CODE_SUCCESS : CodeMessage.CODE_DEFAULT_FAILURE);
        return ResponseEntity.status(status)
                .header(headerName, headerValues)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new CodeMessage(code, message));
    }

    private static ResponseEntity<CodeMessage> send(String headerName, String headerValues, Object body) {
        CodeMessage codeMessage = new CodeMessage(CodeMessage.CODE_SUCCESS, "ok");
        codeMessage.setData(body);
        return ResponseEntity.ok()
                .header(headerName, headerValues)
                .contentType(MediaType.APPLICATION_JSON)
                .body(codeMessage);
    }

    private static <T> ResponseEntity<T> send(T body) {
        return ResponseEntity.ok().body(body);
    }

    public static ResponseEntity badRequest(String message) {
        return send(HttpStatus.BAD_REQUEST, message);
    }

    public static ResponseEntity failure(String message) {
        return send(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static ResponseEntity success(String message) {
        return send(HttpStatus.OK, message);
    }

    public static ResponseEntity success() {
        return send(HttpStatus.OK, "operation success");
    }

    public static ResponseEntity success(String headerName, String headerValues, Object body) {
        return send(headerName, headerValues, body);
    }

    public static <T> ResponseEntity<T> success(T body) {
        return send(body);
    }

    public static <T> ResponseEntity<T> run(Supplier<T> s) {
        try {
            T data = s.get();
            return success(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return CommonResponse.failure(e.getMessage());
        }
    }

    public static ResponseEntity op(Runnable executor) {
        return op(executor, e -> log.error(e.getMessage(), e));
    }

    public static ResponseEntity op(Runnable executor, Consumer<Exception> exceptionConsumer) {
        try {
            executor.run();
            return CommonResponse.success();
        } catch (Exception e) {
            exceptionConsumer.accept(e);
            return CommonResponse.failure(e.getMessage());
        }
    }

}
