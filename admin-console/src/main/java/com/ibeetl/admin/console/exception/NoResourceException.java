package com.ibeetl.admin.console.exception;

import com.ibeetl.admin.core.util.PlatformException;

/**
 * 描述: 資源不存在異常
 *
 * @author : xiandafu
 */
public class NoResourceException extends PlatformException {
    public NoResourceException() {
        super();
    }

    public NoResourceException(String message) {
        super(message);
    }
}
