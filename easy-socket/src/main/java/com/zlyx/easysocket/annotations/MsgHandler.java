package com.zlyx.easysocket.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Desc("消息处理器")
public @interface MsgHandler {

   @Desc("描述")
   String[] todo();
   
   @Desc({"开启状态","默认开启"})
   boolean isOpen() default true;
   
   @Desc(value = { "Socket服务类型" })
   Level level();
   
   @Desc({"缓冲区大小","可根据接收数据长度动态配置","默认为1024*1"})
   int size() default 1;
   
   @Desc({"不允许出现端口一样的情况"})
   int port();
   
   public enum Level{
	   ATM,BTM,BUM,NTM,NUM;
   }
}