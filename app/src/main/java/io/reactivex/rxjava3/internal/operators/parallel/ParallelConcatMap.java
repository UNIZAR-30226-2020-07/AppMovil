/**
 * Copyright (c) 2016-present, RxJava Contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package io.reactivex.rxjava3.internal.operators.parallel;

import org.reactivestreams.*;

import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.operators.flowable.FlowableConcatMap;
import io.reactivex.rxjava3.internal.util.ErrorMode;
import io.reactivex.rxjava3.parallel.ParallelFlowable;

import java.util.Objects;

/**
 * Concatenates the generated Publishers on each rail.
 *
 * @param <T> the input value type
 * @param <R> the output value type
 */
public final class ParallelConcatMap<T, R> extends ParallelFlowable<R> {

    final ParallelFlowable<T> source;

    final Function<? super T, ? extends Publisher<? extends R>> mapper;

    final int prefetch;

    final ErrorMode errorMode;

    public ParallelConcatMap(
            ParallelFlowable<T> source,
            Function<? super T, ? extends Publisher<? extends R>> mapper,
            int prefetch, ErrorMode errorMode) {
        this.source = source;
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        this.prefetch = prefetch;
        this.errorMode = Objects.requireNonNull(errorMode, "errorMode");
    }

    @Override
    public int parallelism() {
        return source.parallelism();
    }

    @Override
    public void subscribe(Subscriber<? super R>[] subscribers) {
        if (!validate(subscribers)) {
            return;
        }

        int n = subscribers.length;

        @SuppressWarnings("unchecked") final Subscriber<T>[] parents = new Subscriber[n];

        for (int i = 0; i < n; i++) {
            parents[i] = FlowableConcatMap.subscribe(subscribers[i], mapper, prefetch, errorMode);
        }

        source.subscribe(parents);
    }
}
