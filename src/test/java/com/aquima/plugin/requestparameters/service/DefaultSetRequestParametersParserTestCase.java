package com.aquima.plugin.requestparameters.service;

import static org.junit.Assert.assertEquals;

import com.aquima.interactions.portal.IServiceContext;
import com.aquima.interactions.profile.IEntityInstance;
import com.aquima.plugin.requestparameters.RequestParametersPluginTestUtil;

import org.junit.Test;

import java.util.List;

/**
 * Testcase for the {@link DefaultSetRequestParametersParser}.
 * 
 * @author hj.van.veenendaal
 * @since 8.3
 */
public class DefaultSetRequestParametersParserTestCase {

  @Test
  public void singleAttributeMapping() {
    // setup
    DefaultSetRequestParametersParser parser = new DefaultSetRequestParametersParser();
    IServiceContext context = RequestParametersPluginTestUtil.createServiceContext();

    // SUT
    IEntityInstance instance = context.getProfile().getActiveInstance(RequestParametersPluginTestUtil.ENTITY);
    assertEquals("", instance.getAttributeState(RequestParametersPluginTestUtil.ATTRIBUTE).getValue().stringValue());
    List<IRequestParameterMapping> mappings = parser.parse(context);

    // verify
    assertEquals(1, mappings.size());

    mappings.get(0).map(context.getProfile());
    instance = context.getProfile().getActiveInstance(RequestParametersPluginTestUtil.ENTITY);
    assertEquals(RequestParametersPluginTestUtil.TEST_VALUE,
        instance.getAttributeState(RequestParametersPluginTestUtil.ATTRIBUTE).getValue().stringValue());
  }
}
