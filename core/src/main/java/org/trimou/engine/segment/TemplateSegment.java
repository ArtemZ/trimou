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
package org.trimou.engine.segment;

import static org.trimou.engine.EngineConfigurationKey.DEBUG_MODE_ENABLED;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.trimou.MustacheException;
import org.trimou.MustacheProblem;
import org.trimou.api.Mustache;
import org.trimou.api.engine.MustacheEngine;
import org.trimou.engine.context.DebugExecutionContext;
import org.trimou.engine.context.DefaultExecutionContext;
import org.trimou.engine.context.ExecutionContext;

/**
 * Template segment.
 *
 * @author Martin Kouba
 */
public class TemplateSegment extends ContainerSegment implements Mustache {

	private MustacheEngine engine;

	private boolean readyToUse = false;

	public TemplateSegment(String name, MustacheEngine engine) {
		super(name, null);
		this.engine = engine;
	}

	@Override
	public void render(Writer writer, Map<String, Object> data) {
		if (!isReadyToUse()) {
			throw new MustacheException(MustacheProblem.TEMPLATE_NOT_READY);
		}
		super.execute(writer, newExecutionContext(data));
		flush(writer);
	}

	@Override
	public String render(Map<String, Object> data) {
		StringWriter writer = new StringWriter();
		render(writer, data);
		return writer.toString();
	}

	@Override
	public SegmentType getType() {
		return SegmentType.TEMPLATE;
	}

	@Override
	public String getName() {
		return getText();
	}

	/**
	 * Make the template ready to use.
	 */
	public void setReadyToUse() {
		this.readyToUse = true;
	}

	/**
	 * @return <code>true</code> if ready to use, <code>false</code> otherwise
	 */
	public boolean isReadyToUse() {
		return readyToUse;
	}

	protected MustacheEngine getEngine() {
		return engine;
	}

	private ExecutionContext newExecutionContext(Map<String, Object> data) {

		ExecutionContext ctx = null;

		if (engine.getConfiguration().getBooleanPropertyValue(
				DEBUG_MODE_ENABLED)) {
			ctx = new DebugExecutionContext(engine.getConfiguration()
					.getResolvers());
		} else {
			ctx = new DefaultExecutionContext(engine.getConfiguration()
					.getResolvers());
		}

		Map<String, Object> contextData = new HashMap<String, Object>();

		if (engine.getConfiguration().getGlobalLambdas() != null) {
			contextData.putAll(engine.getConfiguration().getGlobalLambdas());
		}
		if (data != null) {
			contextData.putAll(data);
		}
		if (!contextData.isEmpty()) {
			ctx.push(contextData);
		}
		return ctx;
	}

}
