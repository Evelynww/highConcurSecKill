package com.evelyn.dao;

import com.evelyn.pojo.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 首先配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test,junit
 * @RunWith(SpringJUnit4ClassRunner.class)
 *
 * 根据ContextConfiguration注解告诉junit spring配置文件
 *
 * 然后注入Dao实现类依赖 @Resource 会去springIOC容器中查找SeckillDao的实现类
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void queryById() {
        long id=1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    //报错

    /**
     * Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offset' not found. Available parameters are [arg1, arg0, param1, param2]
     * 	at org.apache.ibatis.binding.MapperMethod$ParamMap.get(MapperMethod.java:212)
     * 	at org.apache.ibatis.reflection.wrapper.MapWrapper.get(MapWrapper.java:45)
     * 	at org.apache.ibatis.reflection.MetaObject.getValue(MetaObject.java:122)
     * 	at org.apache.ibatis.executor.BaseExecutor.createCacheKey(BaseExecutor.java:219)
     */
    //原因:Java没有保存形参的记录：queryAll(int offset,int limit)->queryAll(arg0,arg1)
    //有多个参数的时候，要告诉Java到底是那个参数，这样通过#去提取参数的时候mybatis才能帮我们找到参数代表的具体值。
    @Test
    public void queryAll() {
        List<Seckill> seckills = seckillDao.queryAll(0, 10);
        for(Seckill seckill:seckills){
            System.out.println(seckill);
        }
    }

    @Test
    public void reduceNumber() {
        Date killTime = new Date();
        int isUpdate = seckillDao.reduceNumber(10000L, killTime);
        System.out.println(isUpdate);
    }
}