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

package com.sun.tools.visualvm.core.model;

import com.sun.tools.visualvm.core.datasource.DataSource;
import com.sun.tools.visualvm.core.datasupport.ClassNameComparator;
import com.sun.tools.visualvm.core.datasupport.DataChangeListener;
import com.sun.tools.visualvm.core.datasupport.DataChangeSupport;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

/**
 * This is abstact factory class for getting model
 * for datasource. It as two functions. First it serves
 * as cache for the model associated with datasource. 
 * Second, ModelFactory uses list of ModelProviders registered 
 * via {@link #registerProvider(ModelProvider )} to
 * determine the order of in which they are consulted 
 * to obtain the model for dataSource. First model 
 * obtained from ModelProvider is associated with
 * dataSource and returned in the future.
 * @author Tomas Hurka
 */
public abstract class ModelFactory<M extends Model,D extends DataSource> {
    final protected static Logger LOGGER = Logger.getLogger(ModelFactory.class.getName());
    
    /** special marker for null model  */
    private final Reference<M> NULL_MODEL;
    /** set of registered providers */
    private SortedSet<ModelProvider<M, D>> providers;
    /** providers cannot be changed, when getModel() is running */
    private ReadWriteLock providersLock;
    /** model cache */
    private Map<DataSourceKey<D>,Reference<M>> modelCache;    
    /** asynchronous change support */
    private DataChangeSupport<ModelProvider<M, D>> factoryChange;
    
    protected ModelFactory() {
        NULL_MODEL = new SoftReference(null);
        providers = new TreeSet(new ModelProviderComparator());
        modelCache = Collections.synchronizedMap(new HashMap());
        factoryChange = new DataChangeSupport();
        providersLock = new ReentrantReadWriteLock();
    }
    
    /**
     * Returns model for dataSource. If model is in the cache
     * return it, otherwise consult registered ModelProviders.
     * @param dataSource {@link DataSource} for which {@link Model} should be returned
     * @return model for dataSource or <CODE>null</CODE> 
     * if there is not model associated with this dataSource.
     */ 
    public final M getModel(D dataSource) {
        // take a read lock for providers
        Lock rlock = providersLock.readLock();
        rlock.lock();
        try {
            // allow concurrent access to cache for different instances of DataSource
            // note that DataSourceKey uses reference-equality in place of object-equality 
            // for DataSource
            synchronized (dataSource) {
                DataSourceKey<D> key = new DataSourceKey(dataSource);
                Reference<M> modelRef = modelCache.get(key);
                M model = null;

                if (modelRef != null) {
                    if (modelRef == NULL_MODEL) {  // cached null model, return null
                        return null;
                    }
                    model = modelRef.get(); // if model is in cache return it,
                    if (model != null) {    // otherwise get it from providers
                        return model;
                    }
                }
                // try to get model from registered providers
                for (ModelProvider<M, D> factory : providers) {
                    model = factory.createModelFor(dataSource);
                    if (model != null) {  // we have model, put it into cache
                        modelCache.put(key,new SoftReference(model));
                        break;
                    }
                }
                if (model == null) {  // model was not found - cache null model
                    modelCache.put(key,NULL_MODEL);
                }
                return model;
            }
        } finally {
            rlock.unlock();
        }
    }
    
    /**
     * register new {@link ModelProvider}. 
     * Model provider can be registered only once.
     * @param newProvider to register
     * @return <CODE>true</CODE> if this ModelFactory does not contain registered provider.
     */
    public final boolean registerProvider(ModelProvider<M, D> newProvider) {
        // take a write lock on providers
        Lock wlock = providersLock.writeLock();
        wlock.lock();
        try {
            LOGGER.finer("Registering " + newProvider.getClass().getName());    // NOI18N
            boolean added = providers.add(newProvider);
            if (added) {
                clearCache();
                factoryChange.fireChange(providers,Collections.singleton(newProvider),null);
            }
            return added;
        } finally {
            wlock.unlock();
        }
    }
    
    /**
     * Unregister {@link ModelProvider}.
     * @param oldProvider provider, which should be unregistered
     * @return <CODE>true</CODE> if provider was unregistered.
     */
    public final boolean unregisterProvider(ModelProvider<M, D> oldProvider) {
        // take a write lock on providers
        Lock wlock = providersLock.writeLock();
        wlock.lock();
        try {
            LOGGER.finer("Unregistering " + oldProvider.getClass().getName());  // NOI18N
            boolean removed = providers.remove(oldProvider);
            if (removed) {
                clearCache();
                factoryChange.fireChange(providers,null,Collections.singleton(oldProvider));
            }
            return removed;
         } finally {
            wlock.unlock();
         }
    }
    
    /**
     * Add data change listener. Data change is fired when 
     * {@link ModelProvider} is registered/unregister. 
     * @param listener {@link DataChangeListener} to be added
     */
    public final void addFactoryChangeListener(DataChangeListener<ModelProvider<M, D>> listener) {
        factoryChange.addChangeListener(listener);
    }
    
    /**
     * Remove data change listener.
     * @param listener {@link DataChangeListener} to be removed
     */
    public final void removeFactoryChangeListener(DataChangeListener<ModelProvider<M, D>> listener) {
        factoryChange.removeChangeListener(listener);
    }
    
    /**
     * Default priority. Subclass of ModelFactory can implement 
     * {@link ModelProvider}. In that case such provider should be
     * used as last provider. This is ensured by returning -1, since
     * providers, which subclass {@link AbstractModelProvider}, return 
     * positive numbers. 
     * @return -1
     */
    public int priority() {
        return -1;
    }
    
    private void clearCache() {
        modelCache.clear();
    }
    
    /** compare ModelProvider-s using priority. Providers with higher priority
     * gets precedence over those with lower priority
     */
    private class ModelProviderComparator implements Comparator<ModelProvider<M,D>> {
        
        public int compare(ModelProvider<M, D> provider1, ModelProvider<M, D> provider2) {
            int thisVal = provider1.priority();
            int anotherVal = provider2.priority();
            
            if (thisVal<anotherVal) {
                return 1;
            }
            if (thisVal>anotherVal) {
                return -1;
            }
            // same depth -> use class name to create artifical ordering
            return ClassNameComparator.INSTANCE.compare(provider1, provider2);
        }
    }
    
    /**
     * DataSource wrapper object, which weakly reference datasource and uses 
     *  reference-equality of DataSources when implementing hashCode and equals
     *  this class is used as keys in modelCache
     */
    private static class DataSourceKey<D extends DataSource>  {
        Reference<D> weakReference;
        
        DataSourceKey(D ds) {
            weakReference = new WeakReference(ds);
        }
        
        public int hashCode() {
            D ds = weakReference.get();
            if (ds != null) {
                return ds.hashCode();
            }
            return 0;
        }
        
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj instanceof DataSourceKey) {
                D ds = weakReference.get();
                D otherDs = ((DataSourceKey<D>)obj).weakReference.get();
                
                return ds != null && ds == otherDs;
            }
            throw new IllegalArgumentException(obj.getClass().getName());
        }
        
        public String toString() {
            DataSource ds = weakReference.get();
            return "DataSourceKey for "+System.identityHashCode(this)+" for "+ds==null?"NULL":ds.toString();    // NOI18N
        }
    }
}
