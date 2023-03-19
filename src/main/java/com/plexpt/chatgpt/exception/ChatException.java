package com.plexpt.chatgpt.exception;

/**
 * 异常
 *
 * @author plexpt
 */
public class ChatException extends RuntimeException {


    public ChatException(IError error) {
        super(error.msg());
    }

    public ChatException(String msg) {
        super(msg);
    }


}
