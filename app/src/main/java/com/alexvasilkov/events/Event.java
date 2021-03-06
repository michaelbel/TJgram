package com.alexvasilkov.events;

import com.alexvasilkov.events.internal.Dispatcher;
import com.alexvasilkov.events.internal.EventBase;
import com.alexvasilkov.events.internal.EventsParams;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class Event extends EventBase {

    private final Dispatcher dispatcher;
    private final String key;
    private final Object[] params;
    private final Object[] tags;

    Event(Builder builder) {
        this.dispatcher = builder.dispatcher;
        this.key = builder.key;
        this.params = ListUtils.toArray(builder.params);
        this.tags = ListUtils.toArray(builder.tags);
    }

    public String getKey() {
        return key;
    }

    /**
     * Returns value at {@code index} position and implicitly casts it to {@code T}.
     * Returns {@code null} if there is no value for specified {@code index}.
     */
    public <T> T getParam(int index) {
        return ListUtils.get(params, index);
    }

    public int getParamsCount() {
        return ListUtils.count(params);
    }

    /**
     * Returns value at {@code index} position and implicitly casts it to {@code T}.
     * Returns {@code null} if there is no value for specified {@code index}.
     */
    public <T> T getTag(int index) {
        return ListUtils.get(tags, index);
    }

    public int getTagsCount() {
        return ListUtils.count(tags);
    }

    /**
     * Schedules event's {@code result} to be passed to all available subscribers.<br>
     * This method should only be called inside of method marked with {@link Events.Subscribe}.<br>
     * See {@link Events.Result}.
     */
    public Event postResult(EventResult result) {
        dispatcher.postEventResult(this, result);
        return this;
    }

    /**
     * Wraps given {@code params} as {@link EventResult} and passes them to
     * {@link #postResult(EventResult)}.
     */
    public Event postResult(Object... params) {
        return postResult(EventResult.create().result(params).build());
    }

    /**
     * <p>Two events are considered deeply equal if they have same key and exactly same
     * parameters lists.</p>
     * <p>Parameters are compared using {@link Arrays#deepEquals(Object[], Object[])}, so be sure
     * to have correct implementation of {@link Object#equals(Object)} method for all parameters.
     * </p>
     * <p>If you don't want some of parameters to be compared pass them as tags using
     * {@link Builder#tag(Object...)} builder method.</p>
     */
    public static boolean isDeeplyEqual(@NonNull Event e1, @NonNull Event e2) {
        return e1 == e2 || (e1.key.equals(e2.key) && Arrays.deepEquals(e1.params, e2.params));
    }

    public static class Builder {

        private final Dispatcher dispatcher;
        private final String key;
        private List<Object> params;
        private List<Object> tags;

        private boolean isPosted;

        Builder(Dispatcher dispatcher, @NonNull String key) {
            if (EventsParams.EMPTY_KEY.equals(key)) {
                throw new EventsException("Event key \"" + key
                        + "\" is reserved and cannot be used");
            }

            this.dispatcher = dispatcher;
            this.key = key;
        }

        /**
         * <p>Appends event's parameters. These values can be accessed either by
         * {@link Event#getParam(int)} method or as method's parameters of corresponding
         * subscribed methods, see {@link Events.Subscribe} annotation.</p>
         */
        public Builder param(Object... params) {
            this.params = ListUtils.append(this.params, params);
            return this;
        }

        /**
         * <p>Appends additional (dynamic) event's parameters. These values can be accessed only by
         * {@link Event#getTag(int)} method.</p>
         * <p>See {@link #param(Object...)} method.</p>
         */
        public Builder tag(Object... tags) {
            this.tags = ListUtils.append(this.tags, tags);
            return this;
        }

        public Event post() {
            if (isPosted) {
                throw new EventsException("Event " + key + " | Already posted");
            } else {
                isPosted = true;
                Event event = new Event(this);
                dispatcher.postEvent(event);
                return event;
            }
        }

    }
}