package org.hcjf.cloud.hazelcast;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import org.hcjf.cloud.CloudServiceImpl;
import org.hcjf.cloud.cache.CloudCache;
import org.hcjf.cloud.cache.CloudCacheStrategy;
import org.hcjf.cloud.counter.Counter;
import org.hcjf.cloud.hazelcast.log.HazelcastLogListener;
import org.hcjf.properties.SystemProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;

/**
 * Holanda catalina java framework Cloud service implementation using hazelcast api.
 * @author javaito
 */
public class HazelcastImpl implements CloudServiceImpl {

    private final HazelcastInstance hazelcastInstance;
    private Map<String, HazelcastCloudCache> cacheMap;

    public HazelcastImpl() {
        Config config = new Config();
        config.setInstanceName(SystemProperties.get(HazelcastProperties.INSTANCE_NAME));
        config.setProperty("hazelcast.shutdownhook.enabled", "false");
        config.setProperty("hazelcast.logging.type", "none");

        GroupConfig groupConfig = config.getGroupConfig();
        groupConfig.setName(SystemProperties.get(HazelcastProperties.GROUP_NAME));

        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPortAutoIncrement(SystemProperties.getBoolean(HazelcastProperties.NETWORK_PORT_AUTOINCREMENT));
        networkConfig.setPort(SystemProperties.getInteger(HazelcastProperties.NETWORK_PORT));

        InterfacesConfig interfacesConfig = networkConfig.getInterfaces();
        interfacesConfig.setEnabled(SystemProperties.getBoolean(HazelcastProperties.NETWORK_INTERFACES_ENABLED));
        interfacesConfig.setInterfaces(SystemProperties.getList(HazelcastProperties.NETWORK_INTERFACES));

        JoinConfig joinConfig = networkConfig.getJoin();

        MulticastConfig multicastConfig = joinConfig.getMulticastConfig();
        multicastConfig.setEnabled(SystemProperties.getBoolean(HazelcastProperties.NETWORK_JOIN_MULTICAST_ENABLED));
        multicastConfig.setLoopbackModeEnabled(SystemProperties.getBoolean(HazelcastProperties.NETWORK_JOIN_MULTICAST_LOOPBACK_MODE_ENABLED));
        multicastConfig.setMulticastGroup(SystemProperties.get(HazelcastProperties.NETWORK_JOIN_MULTICAST_GROUP));
        multicastConfig.setMulticastPort(SystemProperties.getInteger(HazelcastProperties.NETWORK_JOIN_MULTICAST_PORT));
        multicastConfig.setMulticastTimeoutSeconds(SystemProperties.getInteger(HazelcastProperties.NETWORK_JOIN_MULTICAST_TIMEOUT));
        multicastConfig.setMulticastTimeToLive(SystemProperties.getInteger(HazelcastProperties.NETWORK_JOIN_MULTICAST_TIME_TO_LIVE));
        multicastConfig.setTrustedInterfaces(SystemProperties.getSet(HazelcastProperties.NETWORK_JOIN_MULTICAST_TRUSTED_INTERFACES));

        TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig();
        tcpIpConfig.setEnabled(SystemProperties.getBoolean(HazelcastProperties.NETWORK_JOIN_TCPIP_ENABLED));
        tcpIpConfig.setConnectionTimeoutSeconds(SystemProperties.getInteger(HazelcastProperties.NETWORK_JOIN_TCPIP_TIMEOUT));
        tcpIpConfig.setMembers(SystemProperties.getList(HazelcastProperties.NETWORK_JOIN_TCPIP_MEMBERS));

        SemaphoreConfig semaphoreConfig = config.getSemaphoreConfig(SystemProperties.get(HazelcastProperties.LOCK_IMPL_SEMAPHORE_NAME));
        semaphoreConfig.setInitialPermits(1);

        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        hazelcastInstance.getLoggingService().addLogListener(Level.ALL, new HazelcastLogListener());
    }

    /**
     * This method provides an implementation of distributed map. All the nodes
     * on the cluster shares this instance.
     *
     * @param mapName Name of the map.
     * @return Return the instance of the distributed map.
     */
    @Override
    public <K, V> Map<K, V> getMap(String mapName) {
        return hazelcastInstance.getMap(mapName);
    }

    /**
     * This method provides an implementation of distributed queue. All the nodes
     * on the cluster shares this instance.
     *
     * @param queueName Name of the queue.
     * @return Return the instance of the distributed queue.
     */
    @Override
    public <V> Queue<V> getQueue(String queueName) {
        return hazelcastInstance.getQueue(queueName);
    }

    /**
     * This method provides an implementation of distributed set. All the nodes
     * on the cloud shares this instance.
     * @param setName Name of the set.
     * @param <V> Type of the set's values.
     * @return Return the instance of the distributed set.
     */
    public <V extends Object> Set<V> getSet(String setName) {
        return hazelcastInstance.getSet(setName);
    }

    /**
     * This method provides an implementation of distributed counter. All the nodes
     * on the cloud shares this instance.
     * @param counterName Name of the counter.
     * @return Return thr instance of the counter.
     */
    public Counter getCounter(String counterName) {
        return new HazelcastCounter(hazelcastInstance.getAtomicLong(counterName));
    }

    /**
     * This method takes a resource an lock this for all the thread around the cluster
     * and this resource has locked for all the thread for execution.
     * This method is blocked until you can get the lock.
     *
     * @param resourceName The name of the resource to lock.
     */
    @Override
    public void lock(String resourceName) throws InterruptedException {
        hazelcastInstance.getSemaphore(SystemProperties.get(HazelcastProperties.LOCK_IMPL_SEMAPHORE_NAME)).acquire();
    }

    /**
     * This method unlocks a previously locked resource.
     * @param resourceName The name of the resource locked.
     */
    @Override
    public void unlock(String resourceName) throws InterruptedException {
        hazelcastInstance.getSemaphore(SystemProperties.get(HazelcastProperties.LOCK_IMPL_SEMAPHORE_NAME)).release();
    }

    /**
     * Return the implementation of the Lock interface distributed.
     * @param lockName Name of the lock.
     * @return Distributed lock implementation.
     */
    @Override
    public Lock getLock(String lockName) {
        return hazelcastInstance.getLock(lockName);
    }

    /**
     * Return the distributed lock condition over specific lock object.
     * @param conditionName Lock condition name.
     * @param lock Specific lock object.
     * @return Return the lock condition.
     */
    @Override
    public Condition getCondition(String conditionName, Lock lock) {
        return ((ILock)lock).newCondition(conditionName);
    }

    /**
     * This method creates an instance of distributed cache object.
     * @param cacheName Name of the cache.
     * @param strategies Set with the strategies for the cache instance.
     */
    @Override
    public void createCache(String cacheName, Set<CloudCacheStrategy> strategies) {
        synchronized (this) {
            if(cacheMap == null) {
                cacheMap = new HashMap<>(); //getMap(SystemProperties.get(HazelcastProperties.CACHE_MAP_NAME));
            }
        }
        cacheMap.put(cacheName, new HazelcastCloudCache(cacheName, strategies));
    }

    /**
     * Return the cache object associated to the specific name.
     * @param cacheName Name of the cache.
     * @return Distributed cache instance.
     */
    @Override
    public CloudCache getCache(String cacheName) {
        return null;
    }

    /**
     * Shutdown hook method.
     */
    @Override
    public void shutdown() {
        hazelcastInstance.shutdown();
    }
}
