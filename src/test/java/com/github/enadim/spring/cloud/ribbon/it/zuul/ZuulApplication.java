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
package com.github.enadim.spring.cloud.ribbon.it.zuul;

import com.github.enadim.spring.cloud.ribbon.it.api.application1.Application1Resource;
import com.github.enadim.spring.cloud.ribbon.it.api.application2.Application2Resource;
import com.github.enadim.spring.cloud.ribbon.it.ribbon.RibbonClientsConfig;
import com.github.enadim.spring.cloud.ribbon.support.EnableRibbonContextPropagation;
import groovy.util.logging.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringBootApplication
@EnableEurekaClient
@EnableRibbonContextPropagation
@EnableZuulProxy
@EnableFeignClients(basePackageClasses = {Application1Resource.class, Application2Resource.class})
@RibbonClients(defaultConfiguration = RibbonClientsConfig.class)
@Slf4j
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, "--spring.config.name=zuul");
    }
}
