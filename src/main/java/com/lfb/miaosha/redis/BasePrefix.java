package com.lfb.miaosha.redis;

public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix){
        this(0, prefix);//0代表永不过期
    }

    public BasePrefix(int expireSeconds, String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }

    public String getProfix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
