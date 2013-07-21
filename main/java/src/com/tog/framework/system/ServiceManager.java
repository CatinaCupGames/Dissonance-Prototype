package com.tog.framework.system;

import java.util.ArrayList;

public class ServiceManager {
    private static final ArrayList<Service> services = new ArrayList<>();

    public static Service createService(Class<?> class_) {
        Service s;
        if ((s = getService(class_.getCanonicalName())) != null)
            return s;
        try {
            s = (Service) class_.newInstance();
            s.start();
            services.add(s);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static Service getService(String name) {
        for (Service s : services) {
            if (s.getClass().getName().equals(name))
                return s;
        }
        return null;
    }

    public static Service getService(Class<?> class_) {
        for (Service s : services) {
            if (s.getClass().equals(class_))
                return s;
        }
        return null;
    }
}
