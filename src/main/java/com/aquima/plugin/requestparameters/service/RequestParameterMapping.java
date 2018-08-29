package com.aquima.plugin.requestparameters.service;

import com.aquima.interactions.foundation.DataType;
import com.aquima.interactions.profile.IEntityInstance;
import com.aquima.interactions.profile.exception.UnknownInstanceException;
import com.aquima.interactions.project.impl.ValueFormatter;
import com.aquima.interactions.project.impl.XssSafeValueFormatter;
import com.aquima.interactions.rule.InferenceContext;

import org.springframework.util.Assert;

/**
 * This class maps string values on attributes of entities.
 * 
 * @author hj.van.veenendaal
 * @since 8.3
 */
public class RequestParameterMapping implements IRequestParameterMapping {

  private final String value;
  private final String entity;
  private final String instance;
  private final String attribute;

  /**
   * Contructs a new mapping with the specified properties.
   * 
   * @param value the value to map on an attribute
   * @param entity the entity to set the value on
   * @param instance the instance to set the value on (optional)
   * @param attribute the attribute to set the value on
   */
  public RequestParameterMapping(String value, String entity, String instance, String attribute) {
    Assert.notNull(value, "value must not be null");
    Assert.hasLength(entity, "entity must not be empty");
    Assert.hasLength(attribute, "attibute must not be empty");
    this.value = value;
    this.entity = entity;
    this.instance = instance;
    this.attribute = attribute;
  }

  @Override
  public void map(InferenceContext profile) {
    IEntityInstance entityInstance = null;

    if (this.instance == null) {
      entityInstance = profile.getActiveInstance(this.entity);
    } else {
      try {
        entityInstance = profile.getInstanceByName(this.entity, this.instance);
      } catch (UnknownInstanceException e) {
        entityInstance = profile.createInstance(this.entity, null, this.instance);
      }
    }

    // note: we're working with strings only, so the passed formatter does not need to understand other formars
    XssSafeValueFormatter formatter =
        new XssSafeValueFormatter(new ValueFormatter(null, null, null, null, null, null, null));
    entityInstance.setValue(this.attribute, formatter.parse(this.value, DataType.STRING));
  }
}
