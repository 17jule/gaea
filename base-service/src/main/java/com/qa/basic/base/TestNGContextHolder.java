package com.qa.basic.base;

/**
 * Created by chasen on 2021/1/30.
 */
public class TestNGContextHolder<T> {
    private T storedObj = null;

    public TestNGContextHolder() {
    }

    public T get() {
        return this.storedObj;
    }

    public void set(T storedObj) {
        this.storedObj = storedObj;
    }
}
