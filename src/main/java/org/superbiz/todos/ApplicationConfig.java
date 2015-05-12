package org.superbiz.todos;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alsoto on 11/05/15.
 */
@ApplicationPath("/rest")
public class ApplicationConfig extends Application {
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(Arrays.asList(UiApplication.class));
    }
}
