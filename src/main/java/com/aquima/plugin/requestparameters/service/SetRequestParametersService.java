package com.aquima.plugin.requestparameters.service;

import com.aquima.interactions.framework.service.ServiceResult;
import com.aquima.interactions.portal.IService;
import com.aquima.interactions.portal.IServiceContext;
import com.aquima.interactions.portal.IServiceResult;
import com.aquima.interactions.portal.ServiceException;

import com.blueriq.component.api.annotation.AquimaService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Aquima service that copies HTTP request parameters into the active Aquima profile.
 * 
 * @author hj.van.veenendaal
 * @since 8.3
 */
@AquimaService("BB_SetRequestParameters")
public class SetRequestParametersService implements IService {
  private ISetRequestParametersParser paramParser;

  /**
   * Creates a new service with the {@link DefaultSetRequestParametersParser} as parser.
   */
  public SetRequestParametersService() {
    this.paramParser = new DefaultSetRequestParametersParser();
  }

  /**
   * Sets a different parser.
   * 
   * @param parser The {@link ISetRequestParametersParser} to use
   */
  @Autowired(required = false)
  public void setSetRequestParametersParser(ISetRequestParametersParser parser) {
    this.paramParser = parser;
  }

  @Override
  public IServiceResult handle(IServiceContext context) throws ServiceException, Exception {
    List<IRequestParameterMapping> mappings = this.paramParser.parse(context);

    for (IRequestParameterMapping mapping : mappings) {
      mapping.map(context.getProfile());
    }

    return new ServiceResult();
  }
}
