package com.aquima.plugin.requestparameters.service;

import static org.junit.Assert.assertEquals;

import com.aquima.interactions.portal.IServiceContext;
import com.aquima.interactions.profile.IEntityInstance;
import com.aquima.plugin.requestparameters.RequestParametersPluginTestUtil;

import org.junit.Test;

/**
 * Testcase for the {@link SetRequestParametersService}.
 * 
 * @author hj.van.veenendaal
 * @since 8.3
 */
public class SetRequestParametersServiceTestCase {

  @Test
  public void singleAttributeMapping() throws Exception {
    // setup
    SetRequestParametersService service = new SetRequestParametersService();
    IServiceContext context = RequestParametersPluginTestUtil.createServiceContext();

    // SUT
    IEntityInstance instance = context.getProfile().getActiveInstance(RequestParametersPluginTestUtil.ENTITY);
    assertEquals("", instance.getAttributeState(RequestParametersPluginTestUtil.ATTRIBUTE).getValue().stringValue());
    service.handle(context);

    // verify
    instance = context.getProfile().getActiveInstance(RequestParametersPluginTestUtil.ENTITY);
    assertEquals(RequestParametersPluginTestUtil.TEST_VALUE,
        instance.getAttributeState(RequestParametersPluginTestUtil.ATTRIBUTE).getValue().stringValue());
  }
}
