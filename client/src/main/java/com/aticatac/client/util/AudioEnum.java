package com.aticatac.client.util;

import org.apache.log4j.Logger;

public enum AudioEnum {

    INSTANCE;
    private final Logger logger;

    AudioEnum(){

        this.logger = Logger.getLogger(getClass());

        this.logger.trace("boo");

    }
}
