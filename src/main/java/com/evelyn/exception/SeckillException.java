package com.evelyn.exception;

//秒杀相关业务异常都继承这个异常
public class SeckillException extends RuntimeException{
    public SeckillException(String message){
        super(message);
    }
    public SeckillException(String message,Throwable cause){
        super(message,cause);
    }
}
