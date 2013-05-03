package org.trimou.lambda.i18n;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.trimou.AbstractTest;
import org.trimou.api.Mustache;
import org.trimou.engine.MustacheEngineBuilder;
import org.trimou.spi.engine.LocaleSupport;

import com.google.common.collect.ImmutableMap;

/**
 *
 * @author Martin Kouba
 */
public class ResourceBundleLambdaTest extends AbstractTest {

	@Override
	@Before
	public void buildEngine() {

		ResourceBundleLambda resourceBundleLambda = new ResourceBundleLambda(
				"messages");

		engine = MustacheEngineBuilder.newBuilder()
				.setLocaleSupport(new LocaleSupport() {
					@Override
					public Locale getCurrentLocale() {
						return new Locale("en");
					}
				}).addGlobalLambda(resourceBundleLambda, "bundle")
				.registerCallback(resourceBundleLambda).build();
	}

	@Test
	public void testInterpolation() {

		String templateContents = "{{#bundle}}{{key}}{{/bundle}}";
		Mustache mustache = engine.compile("bundle_lambda", templateContents);

		assertEquals("Hello", mustache.render(ImmutableMap.<String, Object> of(
				"key", "echo_one")));
	}

}
