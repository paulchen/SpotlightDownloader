package at.rueckgr.spotlight.service;

import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class MetricsService {
    private final Map<Metric, AtomicInteger> metricValues = new ConcurrentHashMap<>();

    public void recordMetric(final Metric metric) {
        metricValues.putIfAbsent(metric, new AtomicInteger());
        metricValues.get(metric).incrementAndGet();
    }

    public void logMetrics() {
        for (final Metric metric : Metric.values()) {
            log.info("{}: {}", metric, metricValues.getOrDefault(metric, new AtomicInteger()));
        }
    }

    public enum Metric {
        RENAME_IMAGE, DOWNLOAD_IMAGE, NO_IMAGE_DATA, DOWNLOAD_IMAGE_DATA
    }
}
