package com.plexpt.chatgpt.util;

import java.net.InetSocketAddress;
import java.net.Proxy;

import lombok.experimental.UtilityClass;


@UtilityClass
public class Proxys {


    /**
     * http 代理
     *
     * @param ip
     * @param port
     * @return
     */
    public static Proxy http(String ip, int port) {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    }

    /**
     * socks5 代理
     *
     * @param ip
     * @param port
     * @return
     */
    public static Proxy socks5(String ip, int port) {
        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ip, port));
    }
}
