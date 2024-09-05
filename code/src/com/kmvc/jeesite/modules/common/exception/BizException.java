package com.kmvc.jeesite.modules.common.exception;

public class BizException extends RuntimeException
{
    public BizException() {
    }

    public BizException(final String message) {
        super(message);
    }

    public BizException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BizException(final Throwable cause) {
        super(cause);
    }

    public static void throwWhenFalse(final boolean maybeFalse, final String msgToUsr, final Throwable cause) throws BizException {
        if (!maybeFalse) {
            throw new BizException(msgToUsr, cause);
        }
    }

    public static void throwWhenFalse(final boolean maybeFalse, final String msgToUsr) throws BizException {
        if (!maybeFalse) {
            throw new BizException(msgToUsr);
        }
    }

    public static void throwWhenTrue(final boolean maybeTrue, final String msgToUsr, final Throwable cause) throws BizException {
        if (!maybeTrue) {
            throw new BizException(msgToUsr, cause);
        }
    }

    public static void throwWhenTrue(final boolean maybeTrue, final String msgToUsr) throws BizException {
        if (maybeTrue) {
            throw new BizException(msgToUsr);
        }
    }

    public static void throwWhenNull(final Object objMayBeNull, final String msgToUsr, final Throwable cause) throws BizException {
        if (objMayBeNull == null) {
            throw new BizException(msgToUsr, cause);
        }
    }

    public static void throwWhenNull(final Object objMayBeNull, final String msgToUsr) throws BizException {
        if (objMayBeNull == null) {
            throw new BizException(msgToUsr);
        }
    }
}
