package at.rueckgr.spotlight.service

import at.rueckgr.spotlight.util.Logging
import at.rueckgr.spotlight.util.logger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class MetricsService : Logging {
    private val metricValues = ConcurrentHashMap<Metric, AtomicInteger>()

    fun recordMetric(metric: Metric) {
        metricValues.putIfAbsent(metric, AtomicInteger())
        metricValues[metric]!!.incrementAndGet()
    }

    fun logMetrics() {
        for(metric in Metric.values()) {
            logger().info("{}: {}", metric, metricValues[metric] ?: 0)
        }
    }

    enum class Metric {
        RENAME_IMAGE, DOWNLOAD_IMAGE, NO_IMAGE_DATA, DOWNLOAD_IMAGE_DATA
    }
}
