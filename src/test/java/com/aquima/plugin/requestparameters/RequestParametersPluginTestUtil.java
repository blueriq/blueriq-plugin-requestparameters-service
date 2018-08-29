package com.aquima.plugin.requestparameters;

import com.aquima.interactions.foundation.DataType;
import com.aquima.interactions.foundation.types.StringValue;
import com.aquima.interactions.portal.IServiceContext;
import com.aquima.interactions.profile.IProfile;
import com.aquima.interactions.test.templates.ApplicationTemplate;
import com.aquima.interactions.test.templates.context.ServiceContextTemplate;
import com.aquima.interactions.test.templates.model.EntityTemplate;
import com.aquima.interactions.test.templates.model.MetaModelTemplate;

/**
 * Utility functions for the service test cases.
 * 
 * @author H.J. van Veenendaal
 * @since 8.3
 */
public final class RequestParametersPluginTestUtil {

  public static final String ENTITY = "singleton";
  public static final String ATTRIBUTE = "attribute";
  public static final String TEST_VALUE = "test";
  public static final String HTTP_ATTRIBUTE = "httpAttribute";

  /**
   * Do not instantiate.
   */
  private RequestParametersPluginTestUtil() {}

  public static IServiceContext createServiceContext() {
    ServiceContextTemplate template = new ServiceContextTemplate(createApplication());
    template.addParameter("NameValuePair", HTTP_ATTRIBUTE + "=" + ENTITY + "." + ATTRIBUTE);
    IServiceContext ctx = template.toContext();
    ctx.getRequestScope().setAttribute(HTTP_ATTRIBUTE, TEST_VALUE);
    fillProfile(ctx.getProfile());
    return ctx;
  }

  private static ApplicationTemplate createApplication() {
    ApplicationTemplate app = new ApplicationTemplate();
    MetaModelTemplate metamodel = app.getMetaModel();

    EntityTemplate singleton = metamodel.addEntity(ENTITY, null, true);
    singleton.addAttribute(ATTRIBUTE, DataType.STRING, false);

    return app;
  }

  private static void fillProfile(IProfile profile) {
    profile.getSingletonInstance(ENTITY, true).setValue(ATTRIBUTE, new StringValue(""));
  }
}
