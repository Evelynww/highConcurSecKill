package com.evelyn.dao;

import com.evelyn.pojo.SuccessKilled;
import org.apache.ibatis.annotations.Param;

public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复
     * @param seckillId 秒杀商品的id
     * @param userPhone 收货人手机号
     * @return 是否秒杀成功 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据商品id查询successKilled并携带秒杀产品对象实体
     * @param seckillId 商品id
     * @param userPhone
     * @return 某个商品秒杀成功的所有记录
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
