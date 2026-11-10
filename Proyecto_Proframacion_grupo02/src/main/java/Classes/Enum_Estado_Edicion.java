package Classes;

/**
 * Estado de una edición de curso. SIN_INICIAR y EN_CURSO se calculan según las
 * fechas; FINALIZADA la fija el docente al cerrar la edición y ya no cambia.
 * NORMAL se usa cuando la edición no tiene fechas cargadas.
 */
public enum Enum_Estado_Edicion {
    EN_CURSO,
    FINALIZADA,
    SIN_INICIAR,
    NORMAL
}
