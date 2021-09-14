package com.evelyn.exception;

//重复秒杀异常:很明显是一种运行期异常
public class RepeatKillException extends SeckillException{
    public RepeatKillException(String message){
        super(message);
    }
    public RepeatKillException(String message,Throwable cause){
        super(message,cause);
    }
}
