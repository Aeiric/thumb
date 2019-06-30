package com.aeiric.thumb.lib;


/**
 * @author xujian
 * @desc NoMainThreadException
 * @from v1.0.0
 */
class NoMainThreadException extends RuntimeException {

    NoMainThreadException(String msgDes) {
        super((msgDes));
    }
}
