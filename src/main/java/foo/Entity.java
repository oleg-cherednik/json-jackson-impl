package foo;


import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;

public class Entity {
    protected Entity() {
        List<AccessibleObject> ids = getAccessibleObjectWithAnnotation(Id.class);
        List<AccessibleObject> columns = getAccessibleObjectWithAnnotation(Id.class);
    }

    private List<AccessibleObject> getAccessibleObjectWithAnnotation(Class<? extends Annotation> cls) {
        List<AccessibleObject> res = new ArrayList<>();
        res.addAll(FieldUtils.getFieldsListWithAnnotation(getClass(), cls));
        res.addAll(MethodUtils.getMethodsListWithAnnotation(getClass(), cls));
        return res;
    }
}
