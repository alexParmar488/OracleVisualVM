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
package com.sun.tools.visualvm.core.scheduler;


/**
 * A task scheduled within the <code>Scheduler</code> service
 * @author Jaroslav Bachorik
 */
public interface ScheduledTask {
    //~ Static fields/initializers -----------------------------------------------------------------------------------------------
    /**
     * Property-change support
     */
    public static final String INTERVAL_PROPERTY = "Task.Interval"; // NOI18N

    //~ Methods ------------------------------------------------------------------------------------------------------------------

    /**
     * Modifies the interval the task is scheduled to run at
     * @param quantum The new interval
     */
    void setInterval(Quantum quantum);

    /**
     * The interval the task is scheduled to run at
     * @return Returns the number of seconds between two runs
     */
    Quantum getInterval();

    /**
     * Suspends the task
     * A shortcut to calling <code>setInterval(Quantum.SUSPENDED)</code>
     */
    void suspend();
    
    /**
     * Resumes a previously suspended task
     */
    void resume();
    
    /**
     * Indicates the suspension status of the task
     * @return Returns the suspension status of the task
     */
    boolean isSuspended();
    
}
