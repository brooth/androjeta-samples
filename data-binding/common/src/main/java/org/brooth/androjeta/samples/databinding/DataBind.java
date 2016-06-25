package org.brooth.androjeta.samples.databinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author Oleg Khalidov (brooth@gmail.com)
 */
@Target(ElementType.TYPE)
public @interface DataBind {
    String layout();
}
