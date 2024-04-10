package com.bigshen.learningDemo.collection.map;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author byj
 * @date 2024/4/7
 * @Description LRU 算法、线程安全的 Map 实现
 */
public class ThreadSafeLRUMap<K, V> implements Map<K, V> {

    private final LinkedHashMap<K, V> map;
    private final ReentrantReadWriteLock lock;
    @Getter
    private final int capacity;

    public ThreadSafeLRUMap(int capacity) {
        this.map = new LinkedHashMap<K, V>((int) (capacity * 1.4f), 0.75f, true) {
            private static final long serialVersionUID = 8167752143068737813L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
        this.lock = new ReentrantReadWriteLock();
        this.capacity = capacity;
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return map.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return map.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean containsKey(Object key) {
        lock.readLock().lock();
        try {
            return map.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        lock.readLock().lock();
        try {
            return map.containsValue(value);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.readLock().lock();
        try {
            return map.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        lock.writeLock().lock();
        try {
            return map.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V remove(Object key) {
        lock.writeLock().lock();
        try {
            return map.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        lock.writeLock().lock();
        try {
            map.putAll(m);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            map.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        lock.readLock().lock();
        try {
            return map.keySet();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Collection<V> values() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(map.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        lock.readLock().lock();
        try {
            return new HashSet<>(map.entrySet());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        lock.readLock().lock();
        try {
            return map.getOrDefault(key, defaultValue);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        lock.writeLock().lock();
        try {
            map.forEach(action);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        lock.writeLock().lock();
        try {
            map.replaceAll(function);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        lock.writeLock().lock();
        try {
            return map.putIfAbsent(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        lock.writeLock().lock();
        try {
            return map.remove(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        lock.writeLock().lock();
        try {
            return map.replace(key, oldValue, newValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V replace(K key, V value) {
        lock.writeLock().lock();
        try {
            return map.replace(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        lock.writeLock().lock();
        try {
            return map.computeIfAbsent(key, mappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        lock.writeLock().lock();
        try {
            return map.computeIfPresent(key, remappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        lock.writeLock().lock();
        try {
            return map.compute(key, remappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        lock.writeLock().lock();
        try {
            return map.merge(key, value, remappingFunction);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
