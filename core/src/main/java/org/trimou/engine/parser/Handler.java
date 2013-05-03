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
package org.trimou.engine.parser;

import org.trimou.api.engine.MustacheEngine;

/**
 * Handler for parsing events.
 */
public interface Handler {

	/**
	 * Parsing started.
	 *
	 * @param name
	 * @param delimiters
	 */
	public void startTemplate(String name, Delimiters delimiters,
			MustacheEngine engine);

	/**
	 * Flush a text segment.
	 *
	 * @param text
	 */
	public void text(String text);

	/**
	 * Flush a tag.
	 *
	 * @param tag
	 */
	public void tag(MustacheTag tag);

	/**
	 * Flush a line separator.
	 *
	 * @param separator
	 */
	public void lineSeparator(String separator);

	/**
	 * Parsing ended.
	 */
	public void endTemplate();

}
