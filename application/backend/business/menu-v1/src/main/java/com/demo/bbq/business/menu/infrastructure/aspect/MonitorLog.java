package com.demo.bbq.business.menu.infrastructure.aspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MonitorLog {
}
