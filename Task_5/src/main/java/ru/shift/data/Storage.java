package ru.shift.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;

public class Storage {
    private static final Logger log = LoggerFactory.getLogger(Storage.class);

    private final ArrayDeque<Resource> resources;
    private final int storageSize;

    private final Object notFull = new Object();
    private final Object notEmpty = new Object();

    public Storage(int storageSize) {
        this.storageSize = storageSize;
        resources = new ArrayDeque<>(storageSize);
    }

    public void put(Resource resource, int producerId) throws InterruptedException {
        int resourceId = resource.getId();

        synchronized (notFull) {
            while (resources.size() >= storageSize) {
                log.info("Ожидание. Producer-{} ждет свободного места | Storage {}/{}", producerId, resources.size(), storageSize);
                notFull.wait();
                log.info("Возобновление. Producer-{} обнаружил свободное место", producerId);

            }
        }

        synchronized (resources) {
            if (resources.size() < storageSize) {
                resources.add(resource);
                log.info("Производство. Producer-{} произвел Resource-{} | Storage: {}", producerId, resourceId, resources.size());
            }
        }

        synchronized (notEmpty) {
            notEmpty.notify();
        }
    }

    public void get(int consumerId) throws InterruptedException {

        synchronized (notEmpty) {
            while (resources.isEmpty()) {
                log.info("Ожидание. Consumer-{} ждет появление ресурса | Storage-{}", consumerId, 0);
                notEmpty.wait();
                log.info("Возобновление. Consumer-{} обнаружил ресурс", consumerId);

            }
        }

        synchronized (resources) {
            if (!resources.isEmpty()) {
                Resource resource = resources.removeFirst();
                log.info("Потребление. Consumer-{} взял Resource-{} | Storage: {}", consumerId, resource.getId(), resources.size());
            }
        }

        synchronized (notFull) {
            notFull.notify();
        }
    }
}