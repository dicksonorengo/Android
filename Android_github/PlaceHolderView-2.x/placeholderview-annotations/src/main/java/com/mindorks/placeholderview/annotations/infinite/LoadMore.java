package com.mindorks.placeholderview.annotations.infinite;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by janisharali on 18/08/16.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface LoadMore {
}
