package com.ineatconseil.yougo.ws.rs.impl.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Custom stereotype to define a RESTful Service. 
 * 
 * @author David BRASSELY
 * @author Hubert SABLONNIERE
 *
 */
@Component
@Scope("singleton")
@Retention(RetentionPolicy.RUNTIME)
public @interface RestService {
}
