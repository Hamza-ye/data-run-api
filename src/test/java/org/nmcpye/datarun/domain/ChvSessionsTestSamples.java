package org.nmcpye.datarun.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ChvSessionsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ChvSessions getChvSessionsSample1() {
        return new ChvSessions()
            .id(1L)
            .uid("uid1")
            .code("code1")
            .name("name1")
            .sessions(1)
            .people(1)
            .comment("comment1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static ChvSessions getChvSessionsSample2() {
        return new ChvSessions()
            .id(2L)
            .uid("uid2")
            .code("code2")
            .name("name2")
            .sessions(2)
            .people(2)
            .comment("comment2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static ChvSessions getChvSessionsRandomSampleGenerator() {
        return new ChvSessions()
            .id(longCount.incrementAndGet())
            .uid(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .sessions(intCount.incrementAndGet())
            .people(intCount.incrementAndGet())
            .comment(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
