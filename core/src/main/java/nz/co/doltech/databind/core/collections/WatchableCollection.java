/*
 * Copyright 2015 Doltech Systems Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package nz.co.doltech.databind.core.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import nz.co.doltech.databind.util.Action;

/**
 * A Watchable List. Notifications are grouped and are deferred through
 * Scheduler.scheduleDeferred() method.
 *
 * @param <T> Data type
 * @author Arnaud
 */
public class WatchableCollection<T> implements List<T> {
    private final List<T> list;

    private List<Change> scheduledChanges = new ArrayList<>();

    private List<Action<List<Change>>> callbacks = new ArrayList<>();

    public WatchableCollection() {
        this(new ArrayList<T>());
    }

    public WatchableCollection(List<T> list) {
        this.list = list;
    }

    public void addCallback(Action<List<Change>> callback) {
        callbacks.add(callback);
    }

    public void addCallbackAndSendAll(Action<List<Change>> callback) {
        callbacks.add(callback);
        callback.exec(Change.ForItems(ChangeType.ADD, list, 0));
    }

    public void removeCallback(Action<List<Change>> callback) {
        callbacks.remove(callback);
    }

    private void scheduleChange(Change change) {
        scheduledChanges.add(change);

        for (Action<List<Change>> callback : callbacks)
            callback.exec(scheduledChanges);

        scheduledChanges.clear();
    }

    private void scheduleChanges(Collection<Change> changes) {
        scheduledChanges.addAll(changes);

        for (Action<List<Change>> callback : callbacks)
            callback.exec(scheduledChanges);

        scheduledChanges.clear();
    }

    public void add(int arg0, T arg1) {
        list.add(arg0, arg1);

        scheduleChange(new Change(ChangeType.ADD, arg1, arg0));
    }

    public boolean add(T arg0) {
        boolean res = list.add(arg0);

        scheduleChange(new Change(ChangeType.ADD, arg0, list.size() - 1));

        return res;
    }

    public boolean addAll(Collection<? extends T> arg0) {
        int startIndex = list.size();
        boolean res = list.addAll(arg0);
        scheduleChanges(Change.ForItems(ChangeType.ADD, arg0, startIndex));
        return res;
    }

    public boolean addAll(int arg0, Collection<? extends T> arg1) {
        boolean res = list.addAll(arg0, arg1);
        scheduleChanges(Change.ForItems(ChangeType.ADD, arg1, arg0));
        return res;
    }

    public void clear() {
        Collection<Change> changes = Change.ForItems(ChangeType.REMOVE, list, 0);
        list.clear();
        scheduleChanges(changes);
    }

    public boolean contains(Object arg0) {
        return list.contains(arg0);
    }

    public boolean containsAll(Collection<?> arg0) {
        return list.containsAll(arg0);
    }

    public boolean equals(Object arg0) {
        return list.equals(arg0);
    }

    public T get(int arg0) {
        return list.get(arg0);
    }

    public int hashCode() {
        return list.hashCode();
    }

    public int indexOf(Object arg0) {
        return list.indexOf(arg0);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public Iterator<T> iterator() {
        return list.iterator();
    }

    public int lastIndexOf(Object arg0) {
        return list.lastIndexOf(arg0);
    }

    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    public ListIterator<T> listIterator(int arg0) {
        return list.listIterator(arg0);
    }

    public T remove(int arg0) {
        T res = list.remove(arg0);
        scheduleChange(new Change(ChangeType.REMOVE, res, arg0));
        return res;
    }

    public boolean remove(Object arg0) {
        int index = list.indexOf(arg0);
        boolean res = list.remove(arg0);
        scheduleChange(new Change(ChangeType.REMOVE, arg0, index));
        return res;
    }

    public boolean removeAll(Collection<?> arg0) {
        assert false : "This implementation is bugged !";
        boolean res = list.removeAll(arg0);
        scheduleChanges(Change.ForItems(ChangeType.REMOVE, arg0, 0));
        return res;
    }

    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException();
    }

    public T set(int index, T element) {
        if (list.size() > index)
            scheduleChange(new Change(ChangeType.REMOVE, list.get(index), index));
        scheduleChange(new Change(ChangeType.ADD, element, index));

        return list.set(index, element);
    }

    public int size() {
        return list.size();
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return list.toArray();
    }

    @SuppressWarnings("hiding")
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }
}
