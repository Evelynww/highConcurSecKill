package com.evelyn.controller;


import com.evelyn.dto.Exposer;
import com.evelyn.dto.SeckillExecution;
import com.evelyn.dto.SeckillResult;
import com.evelyn.enums.SeckillStateEnum;
import com.evelyn.exception.RepeatKillException;
import com.evelyn.exception.SeckillCloseException;
import com.evelyn.pojo.Seckill;
import com.evelyn.service.SeckillService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller //@Service @Component
@RequestMapping("/seckill")  //url:/模块/资源/{id}/细分/seckill/list
public class SeckillController {
    //日志
    private final Logger logger = Logger.getLogger(SeckillController.class);

    //注入service对象
    @Autowired
    private SeckillService seckillService;

//    指定二级url,以及请求模式

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
//        获取列表页
        //list.jsp+model=ModelAndView
        List<Seckill> seckillList = seckillService.getSeckillList();
        model.addAttribute("list",seckillList);

        return "list";// /WEB-INF/jsp/"list".jsp
    }

    //通过Url获取相关内容，传入函数，再返回。

    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId,Model model){
        if(seckillId==null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if(seckill==null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

//    ajax jason输出秒杀地址
    //POST的意思是你直接在浏览器敲入这个地址是无效的
    //produce乱码问题解决
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody //封装成json
    public SeckillResult exposer(@PathVariable Long seckillId){
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result = new SeckillResult<>(false,e.getMessage());
        }
        return  result;
    }

//    执行秒杀
    //这里phone是因为我们没做登录模块，后面可以把登录模块加上,然后是从cookie获取的
    //@CookieValue(value = "killPhone",required = false) 这里的false指的是如果没有该字段，不会报错，而是在程序中再处理

//    @RequestMapping(value = "/{seckillId}/{md5}/execution",
//                        method = RequestMethod.POST,
//                        produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
//    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
//                                                   @PathVariable("md5") String md5,
//                                                   @CookieValue(value = "killPhone",required = false) Long phone){
//        SeckillResult<SeckillExecution> result;
////        springmvc valid方式可以去了解一下
//        if(phone==null){
//            return new SeckillResult<>(false,"未注册");
//        }
//        try {
//            //执行秒杀，返回结果
//            SeckillExecution seckillExecution = seckillService.excuteSeckill(seckillId, phone, md5);
//            return new SeckillResult<>(true, seckillExecution);
//        }catch (RepeatKillException e1){
//            //如果捕捉到重复秒杀异常，返回对应的错误
//            logger.error(e1.getMessage(),e1);
//            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
//            return new SeckillResult<>(true, seckillExecution);
//        }catch (SeckillCloseException e2){
//            logger.error(e2.getMessage(),e2);
//            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
//            return new SeckillResult<>(true, seckillExecution);
//        } catch (Exception e){
//            logger.error(e.getMessage(),e);
//            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
//            return new SeckillResult<>(true, seckillExecution);
//        }
//    }


    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult(true,now.getTime());
    }


    //    通过存储过程
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> executeByProcedure(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone",required = false) Long phone){
        SeckillResult<SeckillExecution> result;
    //        springmvc valid方式可以去了解一下
        if(phone==null){
            return new SeckillResult<>(false,"未注册");
        }
        try {
            //执行秒杀，返回结果
            SeckillExecution seckillExecution = seckillService.excuteSeckillByProcedure(seckillId, phone, md5);
            return new SeckillResult<>(true, seckillExecution);
        }catch (RepeatKillException e1){
            //如果捕捉到重复秒杀异常，返回对应的错误
            logger.error(e1.getMessage(),e1);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<>(true, seckillExecution);
        }catch (SeckillCloseException e2){
            logger.error(e2.getMessage(),e2);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<>(true, seckillExecution);
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<>(true, seckillExecution);
        }
    }
}
