/*
 * Copyright 2013 Martin Kouba
 *
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
package org.trimou.api.engine;

import java.util.List;
import java.util.Map;

import org.trimou.api.Lambda;
import org.trimou.spi.engine.LocaleSupport;
import org.trimou.spi.engine.Resolver;
import org.trimou.spi.engine.TemplateLocator;
import org.trimou.spi.engine.TextSupport;

/**
 * Mustache engine configuration.
 *
 * @author Martin Kouba
 */
public interface Configuration {

	/**
	 * @return an ordered immutable list of resolvers, or <code>null</code> if
	 *         no resolvers defined
	 */
	public List<Resolver> getResolvers();

	/**
	 * @return the immutable map of global lamdas, or <code>null</code> if no
	 *         global lambdas defined
	 */
	public Map<String, Lambda> getGlobalLambdas();

	/**
	 * @return an ordered immutable list of template locators, or <code>null</code> if
	 *         no template locators defined
	 */
	public List<TemplateLocator> getTemplateLocators();

	/**
	 * @return the text support implementation
	 */
	public TextSupport getTextSupport();

	/**
	 * @return the locale support implementation
	 */
	public LocaleSupport getLocaleSupport();

	/**
	 *
	 * @param configurationKey
	 * @return
	 */
	public Long getLongPropertyValue(ConfigurationKey configurationKey);

	/**
	 *
	 * @param configurationKey
	 * @return
	 */
	public String getStringPropertyValue(ConfigurationKey configurationKey);

	/**
	 *
	 * @param configurationKey
	 * @return
	 */
	public Boolean getBooleanPropertyValue(ConfigurationKey configurationKey);

}
