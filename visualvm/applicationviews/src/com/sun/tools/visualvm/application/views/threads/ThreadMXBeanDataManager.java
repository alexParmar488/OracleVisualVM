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

package com.sun.tools.visualvm.application.views.threads;

import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import org.netbeans.lib.profiler.global.CommonConstants;
import org.netbeans.lib.profiler.wireprotocol.MonitoredNumbersResponse;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Tomas Hurka
 */
class ThreadMXBeanDataManager extends VisualVMThreadsDataManager {

    private static final long[] dummyLong = new long[0];
    private static final Logger LOGGER = Logger.getLogger(ThreadMXBeanDataManager.class.getName());
    private ThreadMXBean threadBean;
    private Set<Long> threadIdSet = new HashSet();
    private boolean refreshRunning;

    ThreadMXBeanDataManager(ThreadMXBean tb) {
        threadBean = tb;
    }

    // Non-blocking call for general usage
    void refreshThreadsAsync() {
        if (refreshRunning) return;
        refreshRunning = true;
        RequestProcessor.getDefault().post(new Runnable() {
            public void run() { refreshThreadsSync(); }
        });
    }

    // Blocking call used to save application snapshot for not opened application
    void refreshThreadsSync() {
        try {
            ThreadMonitoredDataResponse resp = new ThreadMonitoredDataResponse();
            resp.fillInThreadData();
            processData(org.netbeans.lib.profiler.client.MonitoredData.getMonitoredData(resp));
        } catch (Exception ex) {
            LOGGER.throwing(ThreadMXBeanDataManager.class.getName(), "refreshThreads", ex); // NOI18N
        } finally {
            refreshRunning = false;
        }
    }

    int getDaemonThreadCount() {
        return threadBean.getDaemonThreadCount();
    }

    int getThreadCount() {
        return threadBean.getThreadCount();
    }

    class ThreadMonitoredDataResponse extends MonitoredNumbersResponse {

        ThreadMonitoredDataResponse() {
            super(dummyLong);
            setGCstartFinishData(dummyLong, dummyLong);
        }

        private void fillInThreadData() {
            long[] currentThreadIds = threadBean.getAllThreadIds();
            ThreadInfo[] threadInfos = threadBean.getThreadInfo(currentThreadIds, 1);
            Set<Long> currentIdSet = new HashSet(currentThreadIds.length * 4 / 3);
            int nThreads = 0;
            long timeStamps[] = {System.currentTimeMillis()};
            int maxThreads = currentThreadIds.length + threadIdSet.size();
            int tids[] = new int[maxThreads];
            byte states[] = new byte[maxThreads];

            int nNewThreads = 0;
            int newThreadsId[] = new int[currentThreadIds.length];
            String[] newThreadsNames = new String[currentThreadIds.length];
            String[] newThreadsClasses = new String[currentThreadIds.length];

            for (int i = 0; i < currentThreadIds.length; i++) {
                ThreadInfo tinfo = threadInfos[i];
                long threadId = currentThreadIds[i];
                Long threadIdLong;

                if (tinfo == null) {
                    continue;
                }
                threadIdLong = Long.valueOf(threadId);
                currentIdSet.add(threadIdLong);
                tids[nThreads] = (int) threadId;
                states[nThreads] = getState(tinfo);
                nThreads++;

                if (!threadIdSet.remove(threadIdLong)) { // New Thread
                    newThreadsId[nNewThreads] = (int) threadId;
                    newThreadsNames[nNewThreads] = tinfo.getThreadName();
                    newThreadsClasses[nNewThreads] = "";
                    nNewThreads++;
                }
            }
            // set remaining threads as terminated
            for (Iterator it = threadIdSet.iterator(); it.hasNext();) {
                Long elem = (Long) it.next();
                tids[nThreads] = elem.intValue();
                states[nThreads] = CommonConstants.THREAD_STATUS_ZOMBIE;
                nThreads++;
            }
            threadIdSet = currentIdSet;
            setDataOnNewThreads(nNewThreads, newThreadsId, newThreadsNames, newThreadsClasses);
            setDataOnThreads(nThreads, timeStamps.length, tids, timeStamps, states);
        }

        byte getState(ThreadInfo threadInfo) {
            Thread.State state = threadInfo.getThreadState();
            switch (state) {
                case BLOCKED:
                    return CommonConstants.THREAD_STATUS_MONITOR;
                case RUNNABLE:
                    return CommonConstants.THREAD_STATUS_RUNNING;
                case TIMED_WAITING:
                case WAITING:
                    return isSleeping(threadInfo.getStackTrace()[0]) ?
                        CommonConstants.THREAD_STATUS_SLEEPING :
                        CommonConstants.THREAD_STATUS_WAIT;
                case TERMINATED:
                case NEW:
                    return CommonConstants.THREAD_STATUS_ZOMBIE;
            }
            return CommonConstants.THREAD_STATUS_UNKNOWN;
        }

        boolean isSleeping(StackTraceElement element) {
            return Thread.class.getName().equals(element.getClassName()) &&
                    "sleep".equals(element.getMethodName());    // NOI18N
        }
    }
}