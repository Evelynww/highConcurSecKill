package com.evelyn.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.evelyn.pojo.Seckill;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
    private Logger logger = Logger.getLogger(RedisDao.class);

    private final JedisPool jedisPool;
    public RedisDao(String ip, int port){
        jedisPool = new JedisPool(ip,port);
    }

    //基于class做一个模式
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
//    去找seckillId对应的对象
    public Seckill getSeckill(long seckillId){
//        redis操作逻辑
        try{
//            得到连接池
            Jedis jedis = jedisPool.getResource();

            try{
                String key = "seckill:"+seckillId;
//                并没有内部序列化操作
//                get获得byte[]->反序列化->Object(Seckill)
                //采用自定义序列化，将对象转化成二进制数组，传给redis进行缓存。
                //protostuff:pojo
                //把字节数组转化成pojo
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes!=null){
                    Seckill seckill = schema.newMessage();
                    //调用这句话之后seckill就已经被复赋值了
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
//    如果没有整个对象，就要put进去
    public String putSeckill(Seckill seckill){
        //Object转化成字节数组
        try {
                Jedis jedis = jedisPool.getResource();
                try {
                    String key = "seckill:"+seckill.getSeckillId();
                    byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
//                    超时缓存
                    int timeout = 60*60;//1小时
                    String result = jedis.setex(key.getBytes(), timeout, bytes);
                    return result;
                }finally {
                    jedis.close();
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
        }
        return null;
    }
}
