package com.leeo.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameterSpy {

    private static final String  fmt = "%24s: %s%n";

    // for the morbidly curious
    <E extends RuntimeException> void genericThrow() throws E {}

    public static void printClassConstructors(Class c) {
        Constructor[] allConstructors = c.getConstructors();
        System.out.format(MethodParameterSpy.fmt, "Number of constructors",
            allConstructors.length);
        for (Constructor currentConstructor : allConstructors) {
            MethodParameterSpy.printConstructor(currentConstructor);
        }
        Constructor[] allDeclConst = c.getDeclaredConstructors();
        System.out.format(MethodParameterSpy.fmt,
            "Number of declared constructors",
            allDeclConst.length);
        for (Constructor currentDeclConst : allDeclConst) {
            MethodParameterSpy.printConstructor(currentDeclConst);
        }
    }

    public static void printClassMethods(Class c) {
       Method[] allMethods = c.getDeclaredMethods();
        System.out.format(MethodParameterSpy.fmt, "Number of methods",
            allMethods.length);
        for (Method m : allMethods) {
            MethodParameterSpy.printMethod(m);
        }
    }

    public static void printConstructor(Constructor c) {
        System.out.format("%s%n", c.toGenericString());
        Parameter[] params = c.getParameters();
        System.out.format(MethodParameterSpy.fmt, "Number of parameters",
            params.length);
        for (Parameter param : params) {
            MethodParameterSpy.printParameter(param);
        }
    }

    public static void printMethod(Method m) {
        System.out.format("%s%n", m.toGenericString());
        System.out.format(MethodParameterSpy.fmt, "Return type",
            m.getReturnType());
        System.out.format(MethodParameterSpy.fmt, "Generic return type",
            m.getGenericReturnType());

        Parameter[] params = m.getParameters();
        for (Parameter param : params) {
            MethodParameterSpy.printParameter(param);
        }
    }

    public static void printParameter(Parameter p) {
        System.out.format(MethodParameterSpy.fmt, "Parameter class",
            p.getType());
        System.out
            .format(MethodParameterSpy.fmt, "Parameter name", p.getName());
        System.out
            .format(MethodParameterSpy.fmt, "Modifiers", p.getModifiers());
        System.out.format(MethodParameterSpy.fmt, "Is implicit?",
            p.isImplicit());
        System.out.format(MethodParameterSpy.fmt, "Is name present?",
            p.isNamePresent());
        System.out.format(MethodParameterSpy.fmt, "Is synthetic?",
            p.isSynthetic());
    }

    public static void main(String... args) {

        try {
            MethodParameterSpy.printClassConstructors(Class.forName(args[0]));
            MethodParameterSpy.printClassMethods(Class.forName(args[0]));
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        }
    }
}