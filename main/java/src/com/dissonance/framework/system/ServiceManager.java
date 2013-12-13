package com.dissonance.framework.system;

import com.dissonance.framework.system.utils.Validator;

import java.util.ArrayList;

public class ServiceManager {
    private static final ArrayList<Service> services = new ArrayList<>();

    public static <T extends Service> T createService(Class<?> class_) {
        Validator.validateNotNull(class_, "class_");
        Service s;
        if ((s = getService(class_.getName())) != null)
            return (T) s;
        try {
            s = (Service) class_.newInstance();

            services.add(s);
            s.start();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) s;
    }

    public static Service getService(String name) {
        for (Service s : services) {
            if (s.getClass().getName().equals(name))
                return s;
        }
        return null;
    }

    public static Service getService(Class<?> class_) {
        return getService(class_.getName());
    }
}
