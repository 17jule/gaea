package com.qa.basic.base;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;

/**
 * Created by chasen on 2021/1/30.
 */
public class CustomizedObjWrapper extends DefaultObjectWrapper {
    public CustomizedObjWrapper(Version incompatibleImprovements) {
        super(incompatibleImprovements);
    }

    protected TemplateModel handleUnknownType(Object obj) throws TemplateModelException {
        return (TemplateModel)(!obj.getClass().getName().startsWith("java.lang.")
                && obj.getClass() != Integer.TYPE
                && obj.getClass() != Byte.TYPE
                && obj.getClass() != Long.TYPE
                && obj.getClass() != Short.TYPE
                && obj.getClass() != Double.TYPE
                && obj.getClass() != Float.TYPE
                && obj.getClass() != Boolean.TYPE
                && obj.getClass() != Character.TYPE
                ? new FreeMarkerObjAdapter(obj, this) : super.handleUnknownType(obj));
    }
}
