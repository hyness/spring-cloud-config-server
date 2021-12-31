/*
 * Copyright 2018-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.config.server.environment;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;

import org.springframework.cloud.config.server.config.ConfigServerProperties;

/**
 * @author Iulian Antohe
 */
public class AwsParameterStoreEnvironmentRepositoryFactory implements
		EnvironmentRepositoryFactory<AwsParameterStoreEnvironmentRepository, AwsParameterStoreEnvironmentProperties> {

	private final ConfigServerProperties configServerProperties;

	public AwsParameterStoreEnvironmentRepositoryFactory(ConfigServerProperties configServerProperties) {
		this.configServerProperties = configServerProperties;
	}

	@Override
	public AwsParameterStoreEnvironmentRepository build(AwsParameterStoreEnvironmentProperties environmentProperties) {
		AWSSimpleSystemsManagementClientBuilder clientBuilder = AWSSimpleSystemsManagementClientBuilder.standard();

		if (environmentProperties.getRegion() != null) {
			if (environmentProperties.getEndpoint() != null) {
				AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
						environmentProperties.getEndpoint(), environmentProperties.getRegion());
				clientBuilder.withEndpointConfiguration(endpointConfiguration);
			}
			else {
				clientBuilder.withRegion(environmentProperties.getRegion());
			}
		}

		AWSSimpleSystemsManagement client = clientBuilder.build();

		return new AwsParameterStoreEnvironmentRepository(client, configServerProperties, environmentProperties);
	}

}
