/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;


import androidx.annotation.NonNull;

/**
 * Helper class for creating a Sort query parameter
 */
class Sort {
    private final UGCOption option;
    private final SortOrder sortOrder;

    Sort(@NonNull UGCOption option, @NonNull SortOrder sortOrder) {
        this.option = option;
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", option.getKey(), sortOrder.getKey());
    }
}