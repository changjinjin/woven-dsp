// package com.info.baymax.common.validation.config;
//
// import javax.validation.ValidatorFactory;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.MessageSource;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
// import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
//
// @Configuration
// public class ValidationConfiguration {
// @Autowired
// private MessageSource messageSource;
//
// @Bean
// public MethodValidationPostProcessor methodValidationPostProcessor(
// @Autowired final ValidatorFactory validatorFactory) {
// MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
// postProcessor.setValidator(validatorFactory.getValidator());
// return postProcessor;
// }
//
// @Bean
// public LocalValidatorFactoryBean validatorFactory() {
// LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
// bean.setValidationMessageSource(messageSource);
// return bean;
// }
// }
