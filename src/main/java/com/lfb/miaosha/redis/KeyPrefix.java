package com.lfb.miaosha.redis;

public interface KeyPrefix {
    public int expireSeconds();

    public String getProfix();

}
