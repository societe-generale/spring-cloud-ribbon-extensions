/*
 * Copyright (c) 2017 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.enadim.spring.cloud.ribbon.predicate;

import com.github.enadim.spring.cloud.ribbon.support.FavoriteZoneConfig;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

/**
 * Filters Servers against the current micro-service instanceId.
 *
 * @author Nadim Benabdenbi
 * @see FavoriteZoneConfig for a concrete usage
 */
@Slf4j
public class InstanceIdMatcher extends DiscoveryEnabledServerPredicate {
    private final String instanceId;

    public InstanceIdMatcher(@NotNull String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doApply(DiscoveryEnabledServer server) {
        String actual = server.getInstanceInfo().getInstanceId();
        boolean accept = instanceId.equals(actual);
        log.trace("expected instanceId [{}] vs {}[{}={}] => {}",
                instanceId,
                server.getHostPort(),
                server.getInstanceInfo().getInstanceId(),
                accept);
        return accept;
    }
}
