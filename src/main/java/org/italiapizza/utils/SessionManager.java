package org.italiapizza.utils;

import org.italiapizza.model.dto.Empleado;

public class SessionManager {

    private static Empleado empleadoActual;

    public static void setEmpleadoActual(Empleado empleado) {
        empleadoActual = empleado;
    }

    public static Empleado getEmpleadoActual() {
        return empleadoActual;
    }
}