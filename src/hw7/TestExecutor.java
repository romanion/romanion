package hw7;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestExecutor {
    private static Method beforeMethod = null;
    private static List<Method> testMethodsOrdered = new LinkedList<>();
    private static Method afterMethod = null;

    public static void main(String[] args) {
        start(TestClass.class);
        clearFields();
    }

    private static void start(Class testedClass) {
        try {
            sortMethods(testedClass);
        } catch (RuntimeException re) {
            System.out.println("Before or after methods must be in a single copy");
            return;
        }
        invokeMethods();
    }

    private static void sortMethods(@NotNull Class testedClass) {
        Method[] methods = testedClass.getDeclaredMethods();
        List<Method> beforeSuiteMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterSuiteMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.getAnnotation(BeforeSuite.class) != null) beforeSuiteMethods.add(method);
            if (method.getAnnotation(Test.class) != null) testMethods.add(method);
            if (method.getAnnotation(AfterSuite.class) != null) afterSuiteMethods.add(method);
        }
        if (beforeSuiteMethods.size() != 1 || afterSuiteMethods.size() != 1) throw new RuntimeException();
        beforeMethod = beforeSuiteMethods.get(0);
        beforeMethod.setAccessible(true);
        afterMethod = afterSuiteMethods.get(0);
        afterMethod.setAccessible(true);
        int priority = 10;
        do {
            for (Method method : testMethods) {
                if (method.getAnnotation(Test.class).priority() == priority) {
                    method.setAccessible(true);
                    testMethodsOrdered.add(method);
                }
            }
            priority--;
        } while (priority >= 0);
    }

    private static void invokeMethods() {
        String str1 = "str1", str2 = "str2";
        byte byteNumber = 26;
        int intNumber = 1024;
        float floatNumber = 26.26f;
        double doubleNumber = 266144.524288;
        boolean bool = true;
        try {
            TestClass testable = (TestClass) beforeMethod.invoke(null);
            testMethodsOrdered.forEach((method) -> {
                Class[] type = method.getParameterTypes();
                System.out.printf("Priority: %d. ", method.getAnnotation(Test.class).priority());
                try {
                    if (type.length == 0) method.invoke(testable);
                    else if (type.length == 2) method.invoke(testable, str1, str2);
                    else if (type[0] == byte.class) method.invoke(testable, byteNumber);
                    else if (type[0] == int.class) method.invoke(testable, intNumber);
                    else if (type[0] == float.class) method.invoke(testable, floatNumber);
                    else if (type[0] == double.class) method.invoke(testable, doubleNumber);
                    else if (type[0] == boolean.class) method.invoke(testable, bool);
                    else if (type[0] == String.class) method.invoke(testable, str1);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
            afterMethod.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void clearFields() {
        beforeMethod = null;
        testMethodsOrdered = new LinkedList<>();
        afterMethod = null;
    }

}
