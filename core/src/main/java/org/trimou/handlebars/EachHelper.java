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
package org.trimou.handlebars;

import static org.trimou.handlebars.OptionsHashKeys.APPLY;
import static org.trimou.handlebars.OptionsHashKeys.AS;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.trimou.engine.config.EngineConfigurationKey;
import org.trimou.engine.segment.ImmutableIterationMeta;
import org.trimou.exception.MustacheException;
import org.trimou.exception.MustacheProblem;
import org.trimou.util.ImmutableSet;
import org.trimou.util.Iterables;

/**
 * <code>
 * {{#each items}}
 *  {{name}}
 * {{/each}}
 * </code>
 *
 * <p>
 * It's possible to apply a function to each element. The function must be an
 * instance of {@link Function}. Note that the function cannot be type-safe. If
 * the result does not equal to {@link EachHelper#SKIP_RESULT} it's used instead
 * of the original element. If the result equals to
 * {@link EachHelper#SKIP_RESULT} the element is skipped. This might be useful
 * to filter out unnecessary elements or to wrap/transform elements.
 * </p>
 *
 * <code>
 * {{#each items apply=myFunction}}
 *  {{name}}
 * {{/each}}
 * </code>
 *
 * <p>
 * It's also possible to supply an alias to access the value of the current
 * iteration:
 * </p>
 *
 * <code>
 * {{#each items as='item'}}
 *  {{item.name}}
 * {{/each}}
 * </code>
 *
 * <p>
 * This helper could be used to iterate over multiple objects:
 * <p>
 *
 * <code>
 * {{! First iterate over list1 and then iterate over list2}}
 * {{#each list1 list2}}
 *  {{name}}
 * {{/each}}
 * </code>
 *
 * @see Function
 * @author Martin Kouba
 */
public class EachHelper extends BasicSectionHelper {

    public static final String SKIP_RESULT = "org.trimou.handlebars.skipResult";

    private String iterationMetadataAlias;

    @Override
    public void init() {
        super.init();
        this.iterationMetadataAlias = configuration.getStringPropertyValue(
                EngineConfigurationKey.ITERATION_METADATA_ALIAS);
    }

    @Override
    public void execute(Options options) {
        if (options.getParameters().size() == 1) {
            Object param = options.getParameters().get(0);
            if (param == null) {
                // Treat null values as empty objects
                return;
            }
            processParameter(param, options, 1, getSize(param));
        } else {
            int size = 0;
            int index = 1;
            List<Object> params = new ArrayList<>(options.getParameters());
            for (Iterator<Object> iterator = params.iterator(); iterator
                    .hasNext();) {
                Object param = iterator.next();
                int paramSize = 0;
                if (param != null) {
                    paramSize = getSize(param);
                }
                if (paramSize > 0) {
                    size += paramSize;
                } else {
                    // Treat null values as empty objects
                    iterator.remove();
                }
            }
            if (size == 0) {
                return;
            }
            for (Object param : params) {
                index = processParameter(param, options, index, size);
            }
        }
    }

    @Override
    protected Set<String> getSupportedHashKeys() {
        return ImmutableSet.of(APPLY, AS);
    }

    private int processParameter(Object param, Options options, int index,
            int size) {
        if (param instanceof Iterable) {
            return processIterable((Iterable<?>) param, options, index, size);
        } else if (param.getClass().isArray()) {
            return processArray(param, options, index, size);
        } else {
            throw new MustacheException(
                    MustacheProblem.RENDER_HELPER_INVALID_OPTIONS,
                    "%s is nor an Iterable nor an array [%s]", param,
                    options.getTagInfo());
        }
    }

    private int processIterable(Iterable<?> iterable, Options options,
            int index, int size) {
        Iterator<?> iterator = iterable.iterator();
        Function function = initFunction(options);
        String alias = initValueAlias(options);
        while (iterator.hasNext()) {
            nextElement(options, iterator.next(), size, index++, function,
                    alias);
        }
        return index;
    }

    private int processArray(Object array, Options options, int index,
            int size) {
        int length = Array.getLength(array);
        Function function = initFunction(options);
        String alias = initValueAlias(options);
        for (int i = 0; i < length; i++) {
            nextElement(options, Array.get(array, i), size, index++, function,
                    alias);
        }
        return index;
    }

    private int getSize(Object param) {
        if (param instanceof Iterable) {
            return Iterables.size((Iterable<?>) param);
        } else if (param.getClass().isArray()) {
            return Array.getLength(param);
        }
        return 0;
    }

    private void nextElement(Options options, Object value, int size, int index,
            Function function, String valueAlias) {
        if (function != null) {
            value = function.apply(value);
            if (SKIP_RESULT.equals(value)) {
                return;
            }
        }
        if (valueAlias != null) {
            options.push(new ImmutableIterationMeta(iterationMetadataAlias,
                    size, index, valueAlias, value));
            options.fn();
            options.pop();
        } else {
            options.push(new ImmutableIterationMeta(iterationMetadataAlias,
                    size, index));
            options.push(value);
            options.fn();
            options.pop();
            options.pop();
        }
    }

    private Function initFunction(Options options) {
        Object function = options.getHash().get(APPLY);
        if (function == null) {
            return null;
        }
        if (function instanceof Function) {
            return (Function) function;
        }
        throw new MustacheException(
                MustacheProblem.RENDER_HELPER_INVALID_OPTIONS,
                "%s is not a valid function [%s]", function,
                options.getTagInfo());
    }

    private String initValueAlias(Options options) {
        Object as = options.getHash().get(AS);
        if (as == null) {
            return null;
        }
        return as.toString();
    }

}
