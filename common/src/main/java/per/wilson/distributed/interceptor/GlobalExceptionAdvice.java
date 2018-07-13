package per.wilson.distributed.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * ParameterValidation
 *
 * @author Wilson
 * @date 18-7-11
 */
@Component
@ControllerAdvice
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {
    @Resource
    private ReloadableResourceBundleMessageSource messageSource;


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object postParamExceptionHandler(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();
        for (FieldError each : exception.getBindingResult().getFieldErrors()) {
            errors.add(each.getField() + messageSource.getMessage(each.getCode(), each.getArguments(), Locale.getDefault()));
        }
        Map<String, Object> map = new HashMap<>(4);
        map.put("code", HttpStatus.BAD_REQUEST.value());
        map.put("msg", "参数错误");
        map.put("data", errors);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object getMappingParamExceptionHandler(ConstraintViolationException exception) {
        List<String> errors = new ArrayList<>();
        exception.getConstraintViolations()
                .stream()
                .filter(e -> !e.getPropertyPath().toString().contains("arg"))
                .forEach(e -> errors.add(StringUtils.join(StringUtils.substringAfter(e.getPropertyPath().toString(), "."), e.getMessage())));
        Map<String, Object> map = new HashMap<>(4);
        map.put("code", HttpStatus.BAD_REQUEST.value());
        map.put("msg", errors);
        map.put("data", errors);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object unauthenticatedExceptionHandler() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", HttpStatus.UNAUTHORIZED.value());
        map.put("msg", "请先登录");
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Object unauthorizedExceptionHandler() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", HttpStatus.NOT_ACCEPTABLE.value());
        map.put("msg", "权限不足");
        return new ResponseEntity<>(map, HttpStatus.NOT_ACCEPTABLE);
    }
}
