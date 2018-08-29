package com.aquima.plugin.requestparameters.service;

import com.aquima.interactions.foundation.IListValue;
import com.aquima.interactions.foundation.IPrimitiveValue;
import com.aquima.interactions.foundation.exception.AppException;
import com.aquima.interactions.foundation.logging.LogFactory;
import com.aquima.interactions.foundation.logging.Logger;
import com.aquima.interactions.foundation.text.StringUtil;
import com.aquima.interactions.portal.IServiceContext;
import com.aquima.interactions.portal.model.def.TypedParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Default context parser for the {@link SetRequestParametersService}.
 *
 * @author hj.van.veenendaal
 * @since 8.3
 */
public class DefaultSetRequestParametersParser implements ISetRequestParametersParser {

  private static final Logger LOG = LogFactory.getLogger(DefaultSetRequestParametersParser.class);

  @Override
  public List<IRequestParameterMapping> parse(IServiceContext context) {
    ArrayList<IRequestParameterMapping> mappings = new ArrayList<IRequestParameterMapping>();

    TypedParameter nameValuePair = (TypedParameter) context.getParameters().getTypedParameter("NameValuePair");
    IListValue list = nameValuePair.getValue().toListValue();
    for (IPrimitiveValue value : list.getValues()) {
      IRequestParameterMapping mapping = createMapping(value.stringValue(), context);

      if (mapping != null) {
        mappings.add(mapping);
      }
    }

    return mappings;
  }

  private IRequestParameterMapping createMapping(String mapping, IServiceContext context) throws AppException {
    int splitParameterAt = mapping.indexOf("=");

    // check if there is exactly 1 '=' character
    if (splitParameterAt == -1) {
      throw new AppException("Key-Value pair is not valid (no '=' chars found)");
    }
    if (mapping.substring(splitParameterAt + 1).indexOf("=") != -1) {
      throw new AppException("Key-Value pair is not valid (multiple '=' chars found)");
    }

    String parameterName = mapping.substring(0, splitParameterAt);
    String attributeFullName = mapping.substring(splitParameterAt + 1);

    Object value = context.getRequestScope().getAttribute(parameterName);
    if (value == null || StringUtil.isEmpty(value.toString())) {
      LOG.debug("The parameter " + parameterName + " is null and hence will not be set.");
      return null;
    } else {
      LOG.debug("Setting value '" + value + "' to aquima attribute: " + attributeFullName);

      AttributeInfo attr = new AttributeInfo(attributeFullName);

      return new RequestParameterMapping(value.toString(), attr.getEntityName(), attr.getInstanceName(),
          attr.getAttributeName());
    }
  }

  /**
   * Allows easy extraction of the attribute, entity and instance names.
   *
   * @author hj.van.veenendaal
   */
  private class AttributeInfo {

    private String entityName;
    private String attributeName;
    private String instanceName;

    /**
     * Extracts the attribute, entity and instance names from the complete attribute name. Supported formats are
     * ENTITY.ATTRIBUTE and ENTITY[INSTANCE].ATTRIBUTE.
     *
     * @param attributeFullName the complete name of the attribute
     */
    public AttributeInfo(String attributeFullName) {
      if (StringUtil.isEmpty(attributeFullName)) {
        throw new IllegalArgumentException(
            "Not a valid metamodel attribute [" + attributeFullName + "], it should be [EntityName.AttributeName]");
      }

      String[] parts = attributeFullName.split("\\.");
      if (parts.length != 2 || StringUtil.isEmpty(parts[0])) {
        throw new IllegalArgumentException(
            "Not a valid metamodel attribute [" + attributeFullName + "], it should be [EntityName.AttributeName]");
      }

      entityName = parts[0];
      attributeName = parts[1];

      // check if an instance is specified
      if (entityName.indexOf('[') > -1) {
        setEntityAndInstanceName(entityName);
      }
    }

    /**
     * Extracts the entity and instance names from the complete entity name.
     *
     * @param entityInstance The entity and instance string in the format ENTITY_NAME[INSTANCE_NAME] (i.e.:
     *        person[Peter])
     */
    private void setEntityAndInstanceName(String entityFullName) {
      String[] entityInstanceParts = entityFullName.split("\\[");

      entityName = entityInstanceParts[0];
      if (entityInstanceParts[1].indexOf(']') > -1) {
        String[] instanceParts = entityInstanceParts[1].split("]");

        if (instanceParts.length > 0) {
          instanceName = instanceParts[0];
        }
      }
    }

    public String getEntityName() {
      return entityName;
    }

    public String getAttributeName() {
      return attributeName;
    }

    public String getInstanceName() {
      return instanceName;
    }
  }
}
