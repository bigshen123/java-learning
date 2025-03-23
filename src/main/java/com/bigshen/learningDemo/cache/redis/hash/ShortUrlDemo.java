package com.bigshen.learningDemo.cache.redis.hash;

import redis.clients.jedis.Jedis;

/**
 * @Author BYJ
 * @Date 2025/3/4 20:57
 * @Describe 短网址追踪案例
 *
 * Redis的Hash数据结构可以实现网址点击追踪机制。
 * 需要对某个原地址进行追踪时：
 * 首先通过Redis的incr命令获取一个自增的10进制数。
 * 然后将10进制数转换为36进制数，并将转换后的36进制数作为短网址。
 * 接着通过hset命令设置短网址的点击次数为0，以及通过hset命令设置短网址和原地址的映射关系。
 * 当有用户访问该短网址时，就可以通过 hincrBy命令对点击次数进行自增。
 */
public class ShortUrlDemo {
    private static final String[] X36_ARRAY = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
    private Jedis jedis = new Jedis("127.0.0.1");

    public ShortUrlDemo() {
        jedis.set("short_url_seed", "51167890045");
    }

    //获取短网址
    public String getShortUrl(String url) {
        //通过Redis的incr命令获取一个自增的10进制数
        long shortUrlSeed = jedis.incr("short_url_seed");

        //将10进制数转换为36进制数，并将转换后的36进制数作为短网址
        StringBuffer buffer = new StringBuffer();
        while (shortUrlSeed > 0) {
            buffer.append(X36_ARRAY[(int)(shortUrlSeed % 36)]);
            shortUrlSeed = shortUrlSeed / 36;
        }
        String shortUrl = buffer.reverse().toString();

        jedis.hset("short_url_access_count", shortUrl, "0");
        jedis.hset("url_mapping", shortUrl, url);
        return shortUrl;
    }

    //增加短网址的访问次数
    public void incrementShortUrlAccessCount(String shortUrl) {
        jedis.hincrBy("short_url_access_count", shortUrl, 1);
    }

    //获取短网址的访问次数
    public long getShortUrlAccessCount(String shortUrl) {
        return Long.valueOf(jedis.hget("short_url_access_count", shortUrl));
    }

    public static void main(String[] args) throws Exception {
        ShortUrlDemo shortUrlDemo = new ShortUrlDemo();
        String shortUrl = shortUrlDemo.getShortUrl("http://redis.com/index.html");
        System.out.println("页面上展示的短网址为：" + shortUrl);
        //假设访问152次
        for (int i = 0; i < 152; i++) {
            shortUrlDemo.incrementShortUrlAccessCount(shortUrl);
        }
        long accessCount = shortUrlDemo.getShortUrlAccessCount(shortUrl);
        System.out.println("短网址被访问的次数为：" + accessCount);
    }
}
