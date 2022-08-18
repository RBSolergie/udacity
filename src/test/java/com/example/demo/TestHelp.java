package com.example.demo;

import com.example.demo.controllers.UserController;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;

public class TestHelp {
    public static void addObjects(Object outputController, String name, Object inputController) {
        boolean wasPrivate = false;

        try {
            Field test = outputController.getClass().getDeclaredField(name);
            if (!test.canAccess(outputController)) {
                test.setAccessible(true);
                wasPrivate = true;
            }
            test.set(outputController, inputController);
            if (wasPrivate) {
                test.setAccessible(false);
            }
        } catch (NoSuchFieldException e ) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
