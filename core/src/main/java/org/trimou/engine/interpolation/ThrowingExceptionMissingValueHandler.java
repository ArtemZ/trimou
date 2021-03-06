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
package org.trimou.engine.interpolation;

import org.trimou.engine.MustacheTagInfo;
import org.trimou.engine.config.AbstractConfigurationAware;
import org.trimou.engine.config.EngineConfigurationKey;
import org.trimou.exception.MustacheException;
import org.trimou.exception.MustacheProblem;

/**
 * Throws an exception when a variable miss occurs.
 *
 * @author Martin Kouba
 * @see EngineConfigurationKey#NO_VALUE_INDICATES_PROBLEM
 */
public class ThrowingExceptionMissingValueHandler extends
        AbstractConfigurationAware implements MissingValueHandler {

    @Override
    public Object handle(MustacheTagInfo info) {
        throw new MustacheException(
                MustacheProblem.RENDER_NO_VALUE,
                "No value for the given key found: %s [template: %s, line: %s]",
                info.getText(), info.getTemplateName(), info.getLine());
    }

}
