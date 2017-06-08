package org.hcjf.cloud.hazelcast;

import org.hcjf.cloud.cache.CloudCache;
import org.hcjf.cloud.cache.CloudCacheStrategy;

import java.util.Set;

/**
 * Cache implementation for hazelcast
 * @author javaito
 */
public class HazelcastCloudCache extends CloudCache {

    protected HazelcastCloudCache(String cacheName, Set<CloudCacheStrategy> strategies) {
        super(cacheName, strategies);
    }

}
