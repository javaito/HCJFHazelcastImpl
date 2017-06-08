package org.hcjf.cloud.hazelcast;

import com.hazelcast.core.IAtomicLong;
import org.hcjf.cloud.counter.Counter;

/**
 * Distributed counter object for hazelcast implementation.
 * @author Javier Quiroga.
 */
public class HazelcastCounter implements Counter {

    private final IAtomicLong iAtomicLong;

    public HazelcastCounter(IAtomicLong iAtomicLong) {
        this.iAtomicLong = iAtomicLong;
    }

    /**
     * Add a unit to the counter and return the new value of the counter.
     * @return New value of the counter.
     */
    @Override
    public Long getAndAdd() {
        return getAndAdd(1L);
    }

    /**
     * Increase the counter using the offset value and return the new value fo the counter.
     * @param offset Counter offset.
     * @return New value of the counter.
     */
    @Override
    public Long getAndAdd(Long offset) {
        return iAtomicLong.getAndAdd(offset);
    }
}
