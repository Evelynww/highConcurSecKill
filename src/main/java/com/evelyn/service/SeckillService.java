package com.evelyn.service;

import com.evelyn.dto.Exposer;
import com.evelyn.dto.SeckillExecution;
import com.evelyn.exception.RepeatKillException;
import com.evelyn.exception.SeckillCloseException;
import com.evelyn.exception.SeckillException;
import com.evelyn.pojo.Seckill;

import java.util.List;

/**
 * 业务接口：站在"使用者"角度设计接口
 * 三个方面：
 *  方法定义粒度：比如使用者只秒杀，而不需要关注库存减少审美点
 *  参数：越简单越明确越好
 *  返回：类型，异常
 */
public interface SeckillService {
    /**
     * 查询秒杀商品列表
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀商品
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址，否则输出系统时间和秒杀时间
     * 意思就是秒杀还没开始的时候是没有地址的
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId 秒杀商品id
     * @param userPhone 用户手机号，这里是作为用户id的作用
     * @param md5 加密后的秒杀商品id,用于生成链接。
     */
    SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 通过存储过程执行秒杀操作
     * @param seckillId 秒杀商品id
     * @param userPhone 用户手机号，这里是作为用户id的作用
     * @param md5 加密后的秒杀商品id,用于生成链接。
     */
    SeckillExecution excuteSeckillByProcedure(long seckillId, long userPhone, String md5);

}
