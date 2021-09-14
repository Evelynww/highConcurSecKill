package com.evelyn.service;

import com.evelyn.dto.Exposer;
import com.evelyn.dto.SeckillExecution;
import com.evelyn.exception.RepeatKillException;
import com.evelyn.exception.SeckillCloseException;
import com.evelyn.pojo.Seckill;
import javafx.scene.effect.Bloom;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration({
        "classpath:spring/spring-service.xml",
        "classpath:spring/spring-dao.xml"})
public class SeckillServiceTest {
    private Logger logger = Logger.getLogger(SeckillServiceTest.class);
    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> seckillList = seckillService.getSeckillList();
//        for(Seckill seckill:seckillList){
//            System.out.println(seckillList);
//        }
        /**
         * [Seckill
         * {seckillId=1000,name='1000元秒杀iphone6',number=89,startTime=Thu Sep 16 08:00:00 CST 2021,
         * endTime=Fri Sep 24 08:00:00 CST 2021,createTime=Mon Sep 13 05:58:58 CST 2021},
         * Seckill{seckillId=1001,name='500元秒杀iPad2',number=198, startTime=Wed Sep 08 08:00:00 CST 2021,
         * endTime=Thu Sep 23 08:00:00 CST 2021, createTime=Mon Sep 13 05:58:58 CST 2021},
         * Seckill{seckillId=1002, name='300元秒杀小米4', number=300, startTime=Sun May 22 08:00:00 CST 2016,
         * endTime=Thu May 23 08:00:00 CST 2019, createTime=Mon Sep 13 05:58:58 CST 2021},
         * Seckill{seckillId=1003, name='200元秒杀红米note', number=400, startTime=Sun May 22 08:00:00 CST 2016,
         * endTime=Mon May 23 08:00:00 CST 2016, createTime=Mon Sep 13 05:58:58 CST 2021}]
         */

        logger.info("seckillList ");
        System.out.println(seckillList);
    }

    @Test
    public void getById() {
        Seckill seckill = seckillService.getById(1000L);
        /**
         * seckill Seckill{seckillId=1000, name='1000元秒杀iphone6', number=89, startTime=Thu Sep 16 08:00:00 CST 2021,
         * endTime=Fri Sep 24 08:00:00 CST 2021, createTime=Mon Sep 13 05:58:58 CST 2021}
         */
        logger.info("seckill "+seckill);
    }

    //测试代码完整逻辑，注意重复秒杀可能的问题
    @Test
    public void testSeckillLogic() throws Exception{
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            logger.info("exposer: "+exposer);
            long phone = 13458938588L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.excuteSeckill(id, phone, md5);
                System.out.println("输出输出");
                System.out.println(seckillExecution);
            }catch (RepeatKillException e1){
                logger.error(e1.getMessage());
                System.out.println("重复了吗");
            }catch (SeckillCloseException e2){
                logger.error(e2.getMessage());
                System.out.println("关闭");
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }else{
            //秒杀未开启
            logger.warn("exposer:"+exposer);
        }
    }

    @Test
    public void excuteSeckillByProcedure() {
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer: " + exposer);
            String md5 = exposer.getMd5();
            long phone = 13458938588L;
            SeckillExecution seckillExecution = seckillService.excuteSeckillByProcedure(id, phone, md5);
            logger.info(seckillExecution.getStateInfo());
        }
    }
}