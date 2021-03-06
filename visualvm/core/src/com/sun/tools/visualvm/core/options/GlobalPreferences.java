/*
 * Copyright 2007-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package com.sun.tools.visualvm.core.options;

import com.sun.tools.visualvm.core.datasupport.ComparableWeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 * General VisualVM settings defined in Options.
 *
 * @author Jaroslav Bachorik
 */
public final class GlobalPreferences implements PreferenceChangeListener {
    private final static Logger LOGGER = Logger.getLogger("com.sun.tools.visualvm.core.options");   // NOI18N

    private static final String INT_KEY_MONHOST_POLL = "MonitoredHostPoll"; // NOI18N
    private static final String INT_KEY_THREADS_POLL = "ThreadsPoll";   // NOI18N
    private static final String INT_KEY_MONDATA_POLL = "MonitoredDataPoll"; // NOI18N
    private static final String INT_KEY_MONHOST_CACHE = "MonitoredHostCache";   // NOI18N
    private static final String INT_KEY_MONDATA_CACHE = "MonitoredDataCache";   // NOI18N
    
    private final static int MONHOST_POLL_DEFAULT = 3;
    private final static int THREADS_POLL_DEFAULT = 1;
    private final static int MONDATA_POLL_DEFAULT = 1;
    private final static int MONHOST_CACHE_DEFAULT = 60;
    private final static int MONDATA_CACHE_DEFAULT = 60;
    
    private final static GlobalPreferences INSTANCE = new GlobalPreferences();
    private final Preferences prefs;
    private final Map<String, Set<ComparableWeakReference<PreferenceChangeListener>>> listenerMap = new HashMap<String, Set<ComparableWeakReference<PreferenceChangeListener>>>();

    private final ExecutorService dispatcher = Executors.newCachedThreadPool();
    
    private GlobalPreferences() {
        prefs =  NbPreferences.forModule(GlobalPreferences.class);
        prefs.addPreferenceChangeListener(this);
    }
    
    /**
     * Returns singleton instance of GlobalPreferences.
     * 
     * @return singleton instance of GlobalPreferences.
     */
    public static GlobalPreferences sharedInstance() {
        return INSTANCE;
    }

    public void preferenceChange(final PreferenceChangeEvent evt) {
        synchronized(listenerMap) {
            Set<ComparableWeakReference<PreferenceChangeListener>> set = listenerMap.get(evt.getKey());
            if (set != null) {
                final Set<PreferenceChangeListener> tmpListeners = new HashSet<PreferenceChangeListener>();
                Collection<ComparableWeakReference<PreferenceChangeListener>> deadRefs = new ArrayList<ComparableWeakReference<PreferenceChangeListener>>();
                for(ComparableWeakReference<PreferenceChangeListener> pclRef : set) {
                    if (pclRef.get() != null) {
                        tmpListeners.add(pclRef.get());
                    } else {
                        deadRefs.add(pclRef);
                    }
                }
                set.removeAll(deadRefs);
                dispatcher.submit(new Runnable() {
                    public void run() {
                        for(PreferenceChangeListener pcl : tmpListeners) {
                            pcl.preferenceChange(evt);
                        }
                    }
                });
            }
        }
    }

    /**
     * Returns polling interval for monitored host.
     * 
     * @return polling interval for monitored host.
     */
    public int getMonitoredHostPoll() {
        return getPollingInterval(INT_KEY_MONHOST_POLL, MONHOST_POLL_DEFAULT);
    }
    
    /**
     * Sets polling interval for monitored host.
     * 
     * @param value polling interval for monitored host.
     */
    public void setMonitoredHostPoll(int value) {
        setPollingInterval(INT_KEY_MONHOST_POLL, value);
    }
    
    /**
     * Registers a listener for changes of polling interval for monitored host.
     * 
     * @param pcl listener for changes of polling interval for monitored host.
     */
    public void watchMonitoredHostPoll(PreferenceChangeListener pcl) {
        addListener(INT_KEY_MONHOST_POLL, pcl);
    }
    
    /**
     * Returns polling interval for threads.
     * 
     * @return polling interval for threads.
     */
    public int getThreadsPoll() {
        return getPollingInterval(INT_KEY_THREADS_POLL, THREADS_POLL_DEFAULT);
    }
    
    /**
     * Sets polling interval for threads.
     * 
     * @param value polling interval for threads.
     */
    public void setThreadsPoll(int value) {
        setPollingInterval(INT_KEY_THREADS_POLL, value);
    }
    
    /**
     * Registers a listener for changes of polling interval for threads.
     * 
     * @param pcl listener for changes of polling interval for threads.
     */
    public void watchThreadsPoll(PreferenceChangeListener pcl) {
        addListener(INT_KEY_THREADS_POLL, pcl);
    }
    
    /**
     * Returns polling interval for monitored data.
     * 
     * @return polling interval for monitored data.
     */
    public int getMonitoredDataPoll() {
        return getPollingInterval(INT_KEY_MONDATA_POLL, MONDATA_POLL_DEFAULT);
    }
    
    /**
     * Sets polling interval for monitored data.
     * 
     * @param value polling interval for monitored data.
     */
    public void setMonitoredDataPoll(int value) {
        setPollingInterval(INT_KEY_MONDATA_POLL, value);
    }
    
    /**
     * Registerz a listener for changes of polling interval for monitored data.
     * 
     * @param pcl listener for changes of polling interval for monitored data.
     */
    public void watchMonitoredDataPoll(PreferenceChangeListener pcl) {
        addListener(INT_KEY_MONDATA_POLL, pcl);
    }
    
    /**
     * Returns size of cache for monitored host data.
     * 
     * @return size of cache for monitored host data.
     */
    public int getMonitoredHostCache() {
        return getPollingInterval(INT_KEY_MONHOST_CACHE, MONHOST_CACHE_DEFAULT);
    }
    
    /**
     * Sets size of cache for monitored host data.
     * 
     * @param value size of cache for monitored host data.
     */
    public void setMonitoredHostCache(int value) {
        setPollingInterval(INT_KEY_MONHOST_CACHE, value);
    }
    
    /**
     * Registers a listener for changes of size of cache for monitored host data.
     * 
     * @param pcl listener for changes of size of cache for monitored host data.
     */
    public void watchMonitoredHostCache(PreferenceChangeListener pcl) {
        addListener(INT_KEY_MONHOST_CACHE, pcl);
    }
    
    /**
     * Returns size of cache for monitored data.
     * 
     * @return size of cache for monitored data.
     */
    public int getMonitoredDataCache() {
        return getPollingInterval(INT_KEY_MONDATA_CACHE, MONDATA_CACHE_DEFAULT);
    }
    
    /**
     * Sets size of cache for monitored data.
     * 
     * @param value size of cache for monitored data.
     */
    public void setMonitoredDataCache(int value) {
        setPollingInterval(INT_KEY_MONDATA_CACHE, value);
    }
    
    /**
     * Registers a listener for changes of size of cache for monitored data.
     * 
     * @param pcl listener for changes of size of cache for monitored data.
     */
    public void watchMonitoredDataCache(PreferenceChangeListener pcl) {
        addListener(INT_KEY_MONDATA_CACHE, pcl);
    }
    
    /**
     * Persistently stores preferences values. This method is called automatically,
     * typically you don't need to call it explicitely.
     * 
     * @return true if the preferences have been stored successfuly, false otherwise.
     */
    public boolean store() {
        try {
            prefs.sync();
            return true;
        } catch (BackingStoreException ex) {
            LOGGER.log(Level.SEVERE, "Error saving preferences", ex);   // NOI18N
        }
        return false;
    }
    
    private void addListener(String property, PreferenceChangeListener pcl) {
        synchronized(listenerMap) {
            if (listenerMap.containsKey(property)) {
                Set<ComparableWeakReference<PreferenceChangeListener>> set = listenerMap.get(property);
                set.add(new ComparableWeakReference<PreferenceChangeListener>(pcl));
            } else {
                Set<ComparableWeakReference<PreferenceChangeListener>> set = new HashSet<ComparableWeakReference<PreferenceChangeListener>>();
                set.add(new ComparableWeakReference<PreferenceChangeListener>(pcl));
                listenerMap.put(property, set);
            }
        }
    }
    
    private int getPollingInterval(String property, int deflt) {
        int value = -1;
        synchronized (prefs) {
            value = prefs.getInt(property, -1);
            if (value == -1) {
                value = deflt;
                prefs.putInt(property, value);
            }
        }
        return value;
    }
    
    private void setPollingInterval(String property, int value) {
        synchronized(prefs) {
            prefs.putInt(property, value);
        }
    }
}
