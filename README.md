# easy-socket

# 简介 #
当你按客户需求哐哧哐哧写了一通tcp，却被客户临时告知改成udp后，不需要拿菜刀砍客户了，只需要修改一个注解值，轻松切换。支持bio方式、nio方式、aio方式下的tcp编程，支持bio方式、nio方式下的udp编程。相信我，你需要的，是一个easy-socket。码云地址：https://gitee.com/xiaoyudeguang/easy-socket


# 使用 #

### AIO_TCP使用案例

```
内部引用了smart-socket的aio方式实现。     

@MsgHandler(level = Level.ATM, port = 10001, todo = { "AIO_TCP使用案例" })
public class ATMDemo implements IMsgHandler{

	@Override
	public String doHandler(String data) throws Exception {
		System.out.println("接收到消息了"+data);
		return this.hashCode()+":"+data;
	}
}
```
### BIO_TCP使用案例
```
@MsgHandler(level = Level.BTM, port = 10002, todo = { "BIO_TCP使用案例" })
public class BTMDemo implements IMsgHandler{

	@Override
	public String doHandler(String data) throws Exception {
		System.out.println("接收到消息"+data);
		return this.hashCode()+":"+data;
	}
}
```
### BIO_UDP使用案例
```
@MsgHandler(level = Level.BUM, port = 10003, todo = { "BIO_UDP使用案例" })
public class BUMDemo implements IMsgHandler{

	@Override
	public String doHandler(String data) throws Exception {
		System.out.println("接收到消息"+data);
		return this.hashCode()+":"+data;
	}
}
```
### NIO_TCP使用案例
```
@MsgHandler(level = Level.NTM, port = 10004, todo = { "NIO_TCP使用案例" })
public class NTMDemo implements IMsgHandler{

	@Override
	public String doHandler(String data) throws Exception {
		System.out.println("接收到消息"+data);
		return this.hashCode()+":"+data;
	}
}
```
### NIO_UDP使用案例
```
@MsgHandler(level = Level.NUM, port = 10005, todo = { "NIO_UDP使用案例" })
public class NUMDemo implements IMsgHandler{

	@Override
	public String doHandler(String data) throws Exception {
		System.out.println("接收到消息"+data);
		return this.hashCode()+":"+data;
	}
}
```
对，没错，就像你看到的这么简单，你能想象的到的快捷。从此，只需要关注业务即可。如感兴趣，以maven方式引入：

```
<dependency>
  <groupId>io.github.xiaoyudeguang</groupId>
  <artifactId>easy-socket</artifactId>
  <version>3.0.0-RELEASE</version>
</dependency>
```

