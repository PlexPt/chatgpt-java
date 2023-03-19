package com.plexpt.chatgpt.listener;

import lombok.extern.slf4j.Slf4j;

/**
 * 控制台测试
 *
 * @author plexpt
 */
@Slf4j
public class ConsoleStreamListener extends AbstractStreamListener {


    @Override
    public void onMsg(String msg) {
        System.out.print(msg);
    }

    @Override
    public void onError(Throwable t, String response) {

    }

}
