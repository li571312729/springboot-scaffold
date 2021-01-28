package com.baili.component;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.baili.common.entity.Result;
import com.baili.common.exception.BaseException;
import org.springframework.aop.AopInvocationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author Administrator
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebExceptionHandler {

    //处理自定义异常
    @ExceptionHandler(BaseException.class)
    public Result bailiException(BaseException ex){
        return Result.error(ex.getCode(), ex.getMessage());
    }

    //处理认证异常
    @ExceptionHandler(BadCredentialsException.class)
    public Result dealException(BadCredentialsException ex){
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public Result dealunrecognException(UnrecognizedPropertyException ex){
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result accessDeal(AccessDeniedException ae){
        return Result.error(ae.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result dealMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        StringBuffer stringBuffer = new StringBuffer();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            stringBuffer.append(error.getDefaultMessage() + ",");
        });
        return Result.error(stringBuffer);
    }

    @ExceptionHandler(IOException.class)
    public Result dealIOException(IOException ioe){
        return Result.error(777,ioe.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handle(HttpServletRequest request,NoHandlerFoundException e) {
        return Result.error(404,"not【"+request.getMethod()+"】"+request.getRequestURI());
    }

    @ExceptionHandler(AopInvocationException.class)
    public Result dealAopInvocationException(AopInvocationException ae){
        return Result.error(776,ae.getMessage());
    }

    @ExceptionHandler(FdfsUnsupportStorePathException.class)
    public Result dealFdfsUnsupportStorePathException(FdfsUnsupportStorePathException fe){
        return Result.error(778,fe.getMessage());
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public Result dealUnsupportedEncodingException(UnsupportedEncodingException ue){
        return Result.error(779,ue.getMessage());
    }
}