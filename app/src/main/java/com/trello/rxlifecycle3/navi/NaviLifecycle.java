/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trello.rxlifecycle3.navi;

import com.trello.navi2.NaviComponent;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

import static com.trello.rxlifecycle3.internal.Preconditions.checkNotNull;

@Deprecated
/**
 * @Deprecated switch to using rxlifecycle-android-lifecycle
 */
public final class NaviLifecycle {

    @NonNull
    @CheckResult
    public static LifecycleProvider<ActivityEvent> createActivityLifecycleProvider(@NonNull NaviComponent activity) {
        checkNotNull(activity, "activity == null");
        return new ActivityLifecycleProviderImpl(activity);
    }

    @NonNull
    @CheckResult
    public static LifecycleProvider<FragmentEvent> createFragmentLifecycleProvider(@NonNull NaviComponent fragment) {
        checkNotNull(fragment, "fragment == null");
        return new FragmentLifecycleProviderImpl(fragment);
    }

    private NaviLifecycle() {
        throw new AssertionError("No instances!");
    }
}
