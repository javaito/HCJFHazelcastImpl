package org.hcjf.cloud.hazelcast;

import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import org.hcjf.properties.SystemProperties;

/**
 * @author javaito
 * @email javaito@gmail.com
 */
public class HazelcastProperties {

    public static final String INSTANCE_NAME = "hcjf.hazelcast.impl.instance.name";
    public static final String NETWORK_PORT = "hcjf.hazelcast.impl.network.port";
    public static final String NETWORK_PORT_AUTOINCREMENT = "hcjf.hazelcast.impl.network.port.autoincrement";
    public static final String NETWORK_JOIN_MULTICAST_ENABLED = "hcjf.hazelcast.impl.network.join.multicast.enabled";
    public static final String NETWORK_JOIN_MULTICAST_LOOPBACK_MODE_ENABLED = "hcjf.hazelcast.impl.network.join.multicast.loopback.mode.enabled";
    public static final String NETWORK_JOIN_MULTICAST_GROUP = "hcjf.hazelcast.impl.network.join.multicast.group";
    public static final String NETWORK_JOIN_MULTICAST_PORT = "hcjf.hazelcast.impl.network.join.multicast.port";
    public static final String NETWORK_JOIN_MULTICAST_TIMEOUT = "hcjf.hazelcast.impl.network.join.multicast.timeout";
    public static final String NETWORK_JOIN_MULTICAST_TIME_TO_LIVE = "hcjf.hazelcast.impl.network.join.multicast.time.to.live";
    public static final String NETWORK_JOIN_MULTICAST_TRUSTED_INTERFACES = "hcjf.hazelcast.impl.network.join.multicast.trusted.interfaces";
    public static final String NETWORK_JOIN_TCPIP_ENABLED = "hcjf.hazelcast.impl.network.join.tcpip.enabled";
    public static final String NETWORK_JOIN_TCPIP_TIMEOUT = "hcjf.hazelcast.impl.network.join.tcpip.timeout";
    public static final String NETWORK_JOIN_TCPIP_MEMBERS = "hcjf.hazelcast.impl.network.join.tcpip.members";
    public static final String NETWORK_INTERFACES_ENABLED = "hcjf.hazelcast.impl.network.join.interfaces.enabled";
    public static final String NETWORK_INTERFACES = "hcjf.hazelcast.impl.network.join.interfaces";
    public static final String MAP_NAME = "hcjf.hazelcast.impl.map.name";
    public static final String MAP_BACKUP_COUNT = "hcjf.hazelcast.impl.map.backup.count";
    public static final String MAP_TIME_TO_LIVE_SECONDS = "hcjf.hazelcast.impl.map.time.to.live.seconds";
    public static final String MAP_STORE_CLASS = "hcjf.hazelcast.impl.map.store.class";
    public static final String MAP_STORE_ENABLE = "hcjf.hazelcast.impl.map.store.enable";
    public static final String GROUP_NAME = "hcjf.hazelcast.impl.group.name";
    public static final String LOCK_IMPL_SEMAPHORE_NAME = "hcjf.hazelcast.impl.lock.impl.semaphore.name";

    public static void init() {
        SystemProperties.putDefaultValue(INSTANCE_NAME, "hazelcast.instance");
        SystemProperties.putDefaultValue(NETWORK_PORT, Integer.toString(NetworkConfig.DEFAULT_PORT));
        SystemProperties.putDefaultValue(NETWORK_PORT_AUTOINCREMENT, "true");
        SystemProperties.putDefaultValue(NETWORK_JOIN_MULTICAST_ENABLED, Boolean.toString(MulticastConfig.DEFAULT_ENABLED));
        SystemProperties.putDefaultValue(NETWORK_JOIN_MULTICAST_LOOPBACK_MODE_ENABLED, Boolean.toString(MulticastConfig.DEFAULT_LOOPBACK_MODE_ENABLED));
        SystemProperties.putDefaultValue(NETWORK_JOIN_MULTICAST_GROUP, MulticastConfig.DEFAULT_MULTICAST_GROUP);
        SystemProperties.putDefaultValue(NETWORK_JOIN_MULTICAST_PORT, Integer.toString(MulticastConfig.DEFAULT_MULTICAST_PORT));
        SystemProperties.putDefaultValue(NETWORK_JOIN_MULTICAST_TIMEOUT, Integer.toString(MulticastConfig.DEFAULT_MULTICAST_TIMEOUT_SECONDS));
        SystemProperties.putDefaultValue(NETWORK_JOIN_MULTICAST_TIME_TO_LIVE, Integer.toString(MulticastConfig.DEFAULT_MULTICAST_TTL));
        SystemProperties.putDefaultValue(NETWORK_JOIN_MULTICAST_TRUSTED_INTERFACES, "[]");
        SystemProperties.putDefaultValue(NETWORK_JOIN_TCPIP_ENABLED, "false");
        SystemProperties.putDefaultValue(NETWORK_JOIN_TCPIP_TIMEOUT, "5");
        SystemProperties.putDefaultValue(NETWORK_JOIN_TCPIP_MEMBERS, "[]");
        SystemProperties.putDefaultValue(NETWORK_INTERFACES_ENABLED, "false");
        SystemProperties.putDefaultValue(NETWORK_INTERFACES, "[]");
        SystemProperties.putDefaultValue(MAP_NAME, "hazelcast.map");
        SystemProperties.putDefaultValue(MAP_BACKUP_COUNT, "2");
        SystemProperties.putDefaultValue(MAP_TIME_TO_LIVE_SECONDS, "100000");
        SystemProperties.putDefaultValue(MAP_STORE_CLASS, "");
        SystemProperties.putDefaultValue(MAP_STORE_ENABLE, "false");
        SystemProperties.putDefaultValue(GROUP_NAME, "hazelcast.group");
        SystemProperties.putDefaultValue(LOCK_IMPL_SEMAPHORE_NAME, "hazelcast.lock.impl");
    }

}
