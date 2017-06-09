package org.hcjf.cloud.hazelcast.log;

import com.hazelcast.logging.LogEvent;
import com.hazelcast.logging.LogListener;
import org.hcjf.cloud.hazelcast.HazelcastProperties;
import org.hcjf.log.Log;
import org.hcjf.properties.SystemProperties;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author javaito
 * @email javaito@gmail.com
 */
public class HazelcastLogListener implements LogListener {

    @Override
    public void log(LogEvent logEvent) {
        LogRecord record = logEvent.getLogRecord();
        if(record.getLevel().equals(Level.SEVERE)) {
            Log.e(SystemProperties.get(HazelcastProperties.LOG_TAG), record.getMessage(), record.getThrown());
        } else if(record.getLevel().equals(Level.WARNING)) {
            Log.w(SystemProperties.get(HazelcastProperties.LOG_TAG), record.getMessage(), record.getThrown());
        } else if(record.getLevel().equals(Level.INFO)) {
            Log.i(SystemProperties.get(HazelcastProperties.LOG_TAG), record.getMessage(), record.getThrown());
        } else {
            Log.d(SystemProperties.get(HazelcastProperties.LOG_TAG), "[" + record.getLevel().toString() + "]" +
                    record.getMessage(), record.getThrown());
        }
    }

}
