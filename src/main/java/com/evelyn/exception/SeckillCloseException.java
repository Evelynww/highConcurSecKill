package com.evelyn.exception;

import com.evelyn.pojo.Seckill;

//秒杀关闭异常:秒杀结束或超时都关闭
public class SeckillCloseException extends SeckillException{
    public SeckillCloseException(String message){
        super(message);
    }
    public SeckillCloseException(String message,Throwable cause){
        super(message,cause);
    }
}
