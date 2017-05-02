package org.hcjf.cloud.hazelcast;

import com.hazelcast.core.IAtomicLong;
import org.hcjf.cloud.counter.Counter;

/**
 * @author Javier Quiroga.
 * @email javier.quiroga@sitrack.com
 */
public class HazelcastCounter implements Counter {

    private final IAtomicLong iAtomicLong;

    public HazelcastCounter(IAtomicLong iAtomicLong) {
        this.iAtomicLong = iAtomicLong;
    }

    @Override
    public Long getAndAdd() {
        return getAndAdd(1L);
    }

    @Override
    public Long getAndAdd(Long offset) {
        return iAtomicLong.getAndAdd(offset);
    }
}
