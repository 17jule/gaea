package com.qa.basic.base;

import freemarker.template.*;

/**
 * Created by chasen on 2021/1/30.
 */
public class FreeMarkerObjAdapter<T> extends WrappingTemplateModel implements TemplateScalarModel, AdapterTemplateModel {
    private final T t;

    public FreeMarkerObjAdapter(T t, ObjectWrapper objectWrapper) {
        super(objectWrapper);
        this.t = t;
    }

    public Object getAdaptedObject(Class<?> aClass) {
        return this.t;
    }

    public String getAsString() throws TemplateModelException {
        return this.t.getClass().getName() + "@" + this.t.hashCode();
    }
}

