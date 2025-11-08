package kr.or.mari.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
	    log.warn("Unsupported HTTP method: {}", e.getMethod());
	    return ResponseEntity
	            .status(HttpStatus.METHOD_NOT_ALLOWED)
	            .body(new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."));
	}
	
	@ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleJsonParseError(org.springframework.http.converter.HttpMessageNotReadableException e) {
	    log.warn("Malformed JSON request: {}", e.getMessage());
	    return ResponseEntity
	            .badRequest()
	            .body(new ErrorResponse(HttpStatus.BAD_REQUEST, "요청 본문(JSON)을 읽을 수 없습니다."));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
		log.warn("Entity not found: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException e) {
		String message = e.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + " : " + err.getDefaultMessage()).findFirst().orElse("요청 값이 유효하지 않습니다.");
		log.warn("Validation failed: {}", message);
		return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, message));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		String message = String.format("파라미터 '%s' 값 '%s'은(는) 잘못된 형식입니다.", e.getName(), e.getValue());
		log.warn("Type mismatch: {}", message);
		return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, message));
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorResponse> handleDatabaseError(DataAccessException e) {
		log.error("Database error: {}", e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."));
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException e) {
		log.warn("No handler found: {}", e.getRequestURL());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(HttpStatus.NOT_FOUND, "요청한 API 경로를 찾을 수 없습니다."));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalError(Exception e) {
		log.error("Unexpected error: ", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."));
	}

	// 공통 에러 응답 DTO
	record ErrorResponse(int status, String error, String message, LocalDateTime timestamp) {
		public ErrorResponse(HttpStatus status, String message) {
			this(status.value(), status.getReasonPhrase(), message, LocalDateTime.now());
		}
	}
}
