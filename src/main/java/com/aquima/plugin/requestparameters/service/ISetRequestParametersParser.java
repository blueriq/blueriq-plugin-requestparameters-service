package com.aquima.plugin.requestparameters.service;

import com.aquima.interactions.portal.IServiceContext;

import java.util.List;

/**
 * Interface for use by the {@link SetRequestParametersService} to parse the {@link IServiceContext} and retrieve the
 * {@link RequestParameterMapping}'s.
 * 
 * @author hj.van.veenendaal
 * @since 8.3
 */
public interface ISetRequestParametersParser {

  /**
   * Parses the specified IServiceContext to extract the RequestParameterMappings.
   * 
   * @param context the {@link IServiceContext} to parse
   * @return the extracted {@link RequestParameterMapping}'s
   */
  List<IRequestParameterMapping> parse(IServiceContext context);
}
