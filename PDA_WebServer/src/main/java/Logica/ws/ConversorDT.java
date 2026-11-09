package Logica.ws;

import Logica.Classes.Enum_Estado_inscripcion;
import Logica.DTsClasses.DTCategoria;
import Logica.DTsClasses.DTCurso;
import Logica.DTsClasses.DTDocente;
import Logica.DTsClasses.DTEdi_Usu;
import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTInstituto;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTProgramaForm;
import Logica.DTsClasses.DTUsuario;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.EnumDT;
import Logica.DTsClasses.Vigencia;
import Logica.ws.cliente.DtCategoria;
import Logica.ws.cliente.DtCurso;
import Logica.ws.cliente.DtDocente;
import Logica.ws.cliente.DtEdiUsu;
import Logica.ws.cliente.DtEdicionCurso;
import Logica.ws.cliente.DtInstituto;
import Logica.ws.cliente.DtMaster;
import Logica.ws.cliente.DtProgramaForm;
import Logica.ws.cliente.DtUsuarioBase;
import Logica.ws.cliente.EnumEstadoInscripcion;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Convierte entre las clases generadas por wsimport (Logica.ws.cliente) y los
 * DTs que usan los servlets y JSP del web (Logica.DTsClasses). Así la capa de
 * presentación sigue trabajando con Date y con los mismos getters de siempre.
 */
public final class ConversorDT {

    private static final DatatypeFactory DATATYPE_FACTORY;

    static {
        try {
            DATATYPE_FACTORY = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConversorDT() {
    }

    // ===================== Fechas y enums =====================

    public static Date fecha(XMLGregorianCalendar x) {
        return x == null ? null : x.toGregorianCalendar().getTime();
    }

    public static XMLGregorianCalendar fecha(Date d) {
        if (d == null) {
            return null;
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        return DATATYPE_FACTORY.newXMLGregorianCalendar(gc);
    }

    public static Logica.ws.cliente.EnumDT enumDT(EnumDT e) {
        return e == null ? null : Logica.ws.cliente.EnumDT.valueOf(e.name());
    }

    public static Enum_Estado_inscripcion estado(EnumEstadoInscripcion e) {
        return e == null ? null : Enum_Estado_inscripcion.valueOf(e.name());
    }

    public static EnumEstadoInscripcion estado(Enum_Estado_inscripcion e) {
        return e == null ? null : EnumEstadoInscripcion.valueOf(e.name());
    }

    private static List<String> copia(List<String> l) {
        return l == null ? new ArrayList<>() : new ArrayList<>(l);
    }

    // ===================== DTs =====================

    /** Convierte cualquier DT recibido del Web Service al DT del web (null si es null). */
    public static DTMaster dt(DtMaster dt) {
        if (dt == null) {
            return null;
        }
        if (dt instanceof DtUsuarioBase u) {
            return usuario(u);
        }
        if (dt instanceof DtCurso c) {
            return curso(c);
        }
        if (dt instanceof DtEdicionCurso e) {
            return edicion(e);
        }
        if (dt instanceof DtProgramaForm p) {
            return programa(p);
        }
        if (dt instanceof DtEdiUsu i) {
            return inscripcion(i);
        }
        if (dt instanceof DtInstituto i) {
            return new DTInstituto(i.getNombre());
        }
        if (dt instanceof DtCategoria c) {
            return new DTCategoria(c.getNombre());
        }
        return new DTMaster();
    }

    public static List<DTMaster> lista(List<? extends DtMaster> l) {
        List<DTMaster> res = new ArrayList<>();
        if (l != null) {
            for (DtMaster dt : l) {
                DTMaster conv = dt(dt);
                if (conv != null) {
                    res.add(conv);
                }
            }
        }
        return res;
    }

    /** La contraseña nunca viaja por el Web Service: queda en null. */
    public static DTUsuarioBase usuario(DtUsuarioBase u) {
        if (u == null) {
            return null;
        }
        if (u instanceof DtDocente d) {
            return new DTDocente(d.getNickname(), d.getNombre(), d.getApellido(), d.getCorreo(), null,
                    fecha(d.getFNac()), d.getInstituto(), d.getImgPath(), copia(d.getCursos()),
                    copia(d.getEdiciones()), copia(d.getProgramas()));
        }
        return new DTUsuario(u.getNickname(), u.getNombre(), u.getApellido(), u.getCorreo(), null,
                fecha(u.getFNac()), u.getImgPath(), copia(u.getEdiciones()), copia(u.getProgramas()));
    }

    public static DTCurso curso(DtCurso c) {
        DTCurso dt = new DTCurso(c.getInstituto(), c.getNombre(), c.getDescripcion(), c.getDuracion(), c.getCantHoras(),
                c.getCantCreditos(), c.getURL(), fecha(c.getFechaAlta()), copia(c.getListPrevias()),
                copia(c.getListEdiCursos()), copia(c.getListProgCursos()), copia(c.getListCategorias()),
                c.getDocente(), c.getImgPath());
        dt.setVisitas(c.getVisitas());
        return dt;
    }

    public static DTEdicionCurso edicion(DtEdicionCurso e) {
        List<DTEdi_Usu> inscripciones = new ArrayList<>();
        for (DtEdiUsu i : e.getMisIscripciones()) {
            if (i != null) {
                inscripciones.add(inscripcion(i));
            }
        }
        return new DTEdicionCurso(e.getInstituto(), e.getCurso(), e.getNombre(), fecha(e.getFInicio()),
                fecha(e.getFFin()), e.getCupo(), e.getCupoActual(), copia(e.getListDocentes()),
                fecha(e.getFechaAlta()), inscripciones, copia(e.getMisCategorias()), e.getImgPath());
    }

    public static DTEdi_Usu inscripcion(DtEdiUsu i) {
        return new DTEdi_Usu(i.getNickEstudiante(), i.getNomEdicion(), fecha(i.getFInscripcion()),
                estado(i.getEstadoIns()));
    }

    public static DTProgramaForm programa(DtProgramaForm p) {
        Vigencia v = null;
        if (p.getVigenciaPrograma() != null) {
            v = new Vigencia(fecha(p.getVigenciaPrograma().getFechaInicio()),
                    fecha(p.getVigenciaPrograma().getFechaFin()));
        }
        return new DTProgramaForm(p.getNombre(), p.getDescripcion(), v, copia(p.getCursos()),
                fecha(p.getFechaAlta()), copia(p.getMisCategorias()), p.getImgPath());
    }
}
