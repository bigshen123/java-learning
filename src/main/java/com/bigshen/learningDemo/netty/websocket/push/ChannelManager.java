package com.bigshen.learningDemo.netty.websocket.push;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author byj
 * @date 2025/5/14
 * @Description 消息推送系统的连接管理封装
 */

public class ChannelManager {
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final ConcurrentHashMap<String, ChannelId> channelIds = new ConcurrentHashMap<String, ChannelId>();

    public static void add(Channel channel) {
        channelGroup.add(channel);
        channelIds.put(channel.id().asShortText(), channel.id());
    }

    public static void remove(Channel channel) {
        channelGroup.remove(channel);
        channelIds.remove(channel.id().asShortText());
    }

    public static Channel get(String id) {
        return channelGroup.find(channelIds.get(id));
    }

    public static void pushToAllChannels(TextWebSocketFrame webSocketFrame) {
        channelGroup.writeAndFlush(webSocketFrame);
    }
}