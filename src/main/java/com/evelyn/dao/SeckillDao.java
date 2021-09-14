package com.evelyn.dao;

import com.evelyn.pojo.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId 需要减库存的id
     * @param killTime 减库存的时间
     * @return 如果影响行数>1，表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据Id查询库存升序
     * @param seckillId 商品id
     * @return 当前商品的信息
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset 偏移量
     * @param limit 显示几条数据
     * @return 参与秒杀的商品信息
     */
    List<Seckill> queryAll(@Param("offset")int offset, @Param("limit")int limit);

    void killByProcedure(Map<String,Object> paramMap);
}
