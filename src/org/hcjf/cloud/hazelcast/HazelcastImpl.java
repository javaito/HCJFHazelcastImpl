package org.hcjf.cloud.hazelcast;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ILock;
import org.hcjf.cloud.CloudCache;
import org.hcjf.cloud.CloudCacheStrategy;
import org.hcjf.cloud.CloudServiceImpl;
import org.hcjf.properties.SystemProperties;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author javaito
 * @mail javaito@gmail.com
 */
public class HazelcastImpl implements CloudServiceImpl {

    private final HazelcastInstance hazelcastInstance;

    public HazelcastImpl() {
        Config config = new Config();
        config.setInstanceName(SystemProperties.get(HazelcastProperties.INSTANCE_NAME));

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

        hazelcastInstance= Hazelcast.newHazelcastInstance(config);
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
     *
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

    @Override
    public void createCache(String cacheName, CloudCacheStrategy strategy) {

    }

    @Override
    public CloudCache getCache(String cacheName) {
        return null;
    }

}
