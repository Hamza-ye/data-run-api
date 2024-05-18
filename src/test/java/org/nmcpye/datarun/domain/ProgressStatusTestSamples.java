package org.nmcpye.datarun.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProgressStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ProgressStatus getProgressStatusSample1() {
        return new ProgressStatus().id(1L).progressStatusLabel("progressStatusLabel1");
    }

    public static ProgressStatus getProgressStatusSample2() {
        return new ProgressStatus().id(2L).progressStatusLabel("progressStatusLabel2");
    }

    public static ProgressStatus getProgressStatusRandomSampleGenerator() {
        return new ProgressStatus().id(longCount.incrementAndGet()).progressStatusLabel(UUID.randomUUID().toString());
    }
}
