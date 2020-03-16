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

package io.reactivex.rxjava3.core;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * Interface to compose {@link Observable}s.
 *
 * @param <Upstream> the upstream value type
 * @param <Downstream> the downstream value type
 */
@FunctionalInterface
public interface ObservableTransformer<@NonNull Upstream, @NonNull Downstream> {
    /**
     * Applies a function to the upstream {@link Observable} and returns an {@link ObservableSource} with
     * optionally different element type.
     * @param upstream the upstream {@code Observable} instance
     * @return the transformed {@code ObservableSource} instance
     */
    @NonNull
    ObservableSource<Downstream> apply(@NonNull Observable<Upstream> upstream);
}