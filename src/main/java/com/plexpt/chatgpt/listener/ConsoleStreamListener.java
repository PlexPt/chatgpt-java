package com.plexpt.chatgpt.listener;

import lombok.extern.slf4j.Slf4j;

/**
 * 控制台测试
 * Console Stream Test Listener
 *
 * @author plexpt
 */
@Slf4j
public class ConsoleStreamListener extends AbstractStreamListener {


    @Override
    public void onMsg(String message) {
        log.info(message);
    }

    @Override
    public void onError(Throwable throwable, String response) {
        log.error("ConsoleStreamListener error: {}", throwable);
    }

}
