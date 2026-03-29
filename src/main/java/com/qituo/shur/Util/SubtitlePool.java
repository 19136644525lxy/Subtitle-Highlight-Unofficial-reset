package com.qituo.shur.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 字幕对象池
 * 用于管理字幕对象，减少垃圾回收
 */
public class SubtitlePool<T> {
    private final List<T> pool;
    private final int maxSize;
    private final ObjectFactory<T> factory;

    /**
     * 对象工厂接口
     * 用于创建新的对象
     */
    public interface ObjectFactory<T> {
        T create();
    }

    /**
     * 构造函数
     * @param maxSize 池的最大大小
     * @param factory 对象工厂
     */
    public SubtitlePool(int maxSize, ObjectFactory<T> factory) {
        this.pool = new ArrayList<>(maxSize);
        this.maxSize = maxSize;
        this.factory = factory;
    }

    /**
     * 从池中获取对象
     * @return 字幕对象
     */
    public synchronized T acquire() {
        if (pool.isEmpty()) {
            return factory.create();
        }
        return pool.remove(pool.size() - 1);
    }

    /**
     * 释放对象回池
     * @param object 要释放的对象
     */
    public synchronized void release(T object) {
        if (pool.size() < maxSize) {
            pool.add(object);
        }
    }

    /**
     * 清空池
     */
    public synchronized void clear() {
        pool.clear();
    }

    /**
     * 获取池的大小
     * @return 池的大小
     */
    public synchronized int size() {
        return pool.size();
    }
}