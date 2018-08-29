package com.aquima.plugin.requestparameters.service;

import com.aquima.interactions.rule.InferenceContext;

/**
 * Interface for classes that map HTTP request parameters on an {@link InferenceContext}.
 * 
 * @author hj.van.veenendaal
 * @since 8.3
 */
public interface IRequestParameterMapping {

  /**
   * Maps the contained HTTP request parameters on the specified InferenceContext.
   * 
   * @param profile the {@link InferenceContext} to map the parameters on
   */
  void map(InferenceContext profile);
}
