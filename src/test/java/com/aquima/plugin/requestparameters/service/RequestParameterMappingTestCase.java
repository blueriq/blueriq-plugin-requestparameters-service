package com.aquima.plugin.requestparameters.service;

import static org.junit.Assert.assertEquals;

import com.aquima.interactions.foundation.DataType;
import com.aquima.interactions.metamodel.impl.MetaModel;
import com.aquima.interactions.profile.IEntityInstance;
import com.aquima.interactions.profile.model.Profile;
import com.aquima.interactions.rule.InferenceContext;
import com.aquima.interactions.rule.inference.DefaultContext;
import com.aquima.interactions.test.templates.model.EntityTemplate;
import com.aquima.interactions.test.templates.model.MetaModelTemplate;

import org.junit.Test;

/**
 * Testcase for the {@link RequestParameterMapping}.
 * 
 * @author hj.van.veenendaal
 * @since 8.3
 */
public class RequestParameterMappingTestCase {

  @Test
  public void setOnSingleton() {
    // setup
    RequestParameterMapping mapping = new RequestParameterMapping("test", "singleton", null, "attribute");
    InferenceContext profile = this.createProfile();

    // SUT
    mapping.map(profile);

    // verify
    assertEquals("test",
        profile.getActiveInstance("singleton").getAttributeState("attribute").getValue().stringValue());
  }

  @Test
  public void setOnKnownInstance() {
    // setup
    RequestParameterMapping mapping = new RequestParameterMapping("test2", "nonsingleton", "instance1", "attribute");
    InferenceContext profile = this.createProfile();

    // SUT
    mapping.map(profile);

    // verify
    IEntityInstance[] instances = profile.getAllInstancesForEntity("nonsingleton", false);
    IEntityInstance instance = this.getInstanceByName(instances, "instance1");
    assertEquals("test2", instance.getAttributeState("attribute").getValue().stringValue());
  }

  @Test
  public void setOnUnknownInstance() {
    // setup
    RequestParameterMapping mapping = new RequestParameterMapping("test3", "nonsingleton", "instance2", "attribute");
    InferenceContext profile = this.createProfile();
    int oldSize = profile.getAllInstancesForEntity("nonsingleton", false).length;

    // SUT
    mapping.map(profile);

    // verify
    IEntityInstance[] instances = profile.getAllInstancesForEntity("nonsingleton", false);
    assertEquals(oldSize + 1, instances.length);

    IEntityInstance instance = this.getInstanceByName(instances, "instance2");
    assertEquals("test3", instance.getAttributeState("attribute").getValue().stringValue());
  }

  @Test
  public void setStaticInstance() {
    // setup
    RequestParameterMapping mapping =
        new RequestParameterMapping("test4", "nonsingleton", "staticInstance", "attribute");
    InferenceContext profile = this.createProfile();

    // SUT
    mapping.map(profile);

    // verify
    IEntityInstance staticInstance = profile.getInstanceByName("nonSingleton", "staticInstance");

    assertEquals("test4", staticInstance.getAttributeState("attribute").getValue().stringValue());
  }

  private IEntityInstance getInstanceByName(IEntityInstance[] instances, String name) {
    for (IEntityInstance instance : instances) {
      if (instance.getName().equals(name)) {
        return instance;
      }
    }
    return null;
  }

  private InferenceContext createProfile() {
    MetaModelTemplate template = new MetaModelTemplate();

    EntityTemplate singleton = template.addEntity("singleton", null, true);
    singleton.addAttribute("attribute", DataType.STRING, false);

    EntityTemplate nonSingleton = template.addEntity("nonSingleton", null, false);
    nonSingleton.addAttribute("attribute", DataType.STRING, false);
    template.addStaticInstance("nonSingleton", "staticInstance");

    Profile profile = new Profile(new MetaModel(template.toDataSource()));
    profile.createInstance("nonSingleton", null, "entity1");
    return new DefaultContext(profile);
  }
}
