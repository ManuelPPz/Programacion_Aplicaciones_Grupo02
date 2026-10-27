package Classes;

import java.util.ArrayList;
import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private Usuario crear() {
        return new Usuario("nick", "Nombre", "Apellido", "mail@mail.com", "pw", new Date(0), "img.png");
    }

    private Edi_Usu inscripcion(Usuario u, String nombreCurso, Enum_Estado_inscripcion estado) {
        Curso curso = new Curso();
        curso.setNombre(nombreCurso);
        EdicionCurso ed = new EdicionCurso("Ed-" + nombreCurso, null, curso, new Date(), new Date(),
                10, new Date(), new ArrayList<>());
        Edi_Usu eu = new Edi_Usu(u, ed, new Date());
        eu.setEstado(estado);
        return eu;
    }

    @Test
    public void testConstructores() {
        Usuario vacio = new Usuario();
        assertNull(vacio.getNickname());
        assertNull(vacio.getMisInscripciones());
        assertNull(vacio.getMisInscripcionesPro());

        Usuario u = crear();
        assertEquals("nick", u.getNickname());
        assertEquals("Nombre", u.getNombre());
    }

    @Test
    public void testModificarMisDatos() {
        Usuario u = crear();
        Date f = new Date(5000);

        u.ModificarMisDatos("Otro", "Ape", "nuevaPw", f, "x.png");

        assertEquals("Otro", u.getNombre());
        assertEquals("Ape", u.getApellido());
        assertEquals("nuevaPw", u.getPassword());
        assertSame(f, u.getFNac());
        assertEquals("x.png", u.getImage());
    }

    @Test
    public void testAddEdicionCursoCreaLista() {
        Usuario u = crear();
        Edi_Usu eu = new Edi_Usu();

        u.AddEdicionCurso(eu);
        u.AddEdicionCurso(new Edi_Usu());

        assertEquals(2, u.getMisInscripciones().size());
        assertSame(eu, u.getMisInscripciones().get(0));
    }

    @Test
    public void testAddProgramaCreaLista() {
        Usuario u = crear();
        Prog_Usu pu = new Prog_Usu();

        u.AddPrograma(pu);
        u.AddPrograma(new Prog_Usu());

        assertEquals(2, u.getMisInscripcionesPro().size());
        assertSame(pu, u.getMisInscripcionesPro().get(0));
    }

    @Test
    public void testRechazadasSinInscripcionesOParametroInvalido() {
        Usuario u = crear();

        assertEquals(0, u.getCantMisInscripcionesRechazadas("Java"));

        u.AddEdicionCurso(inscripcion(u, "Java", Enum_Estado_inscripcion.RECHAZADO));
        assertEquals(0, u.getCantMisInscripcionesRechazadas(null));
        assertEquals(0, u.getCantMisInscripcionesRechazadas("   "));
    }

    @Test
    public void testRechazadasCuentaSoloLasDelCursoYRechazadas() {
        Usuario u = crear();
        u.AddEdicionCurso(inscripcion(u, "Java", Enum_Estado_inscripcion.RECHAZADO));
        u.AddEdicionCurso(inscripcion(u, " java ", Enum_Estado_inscripcion.RECHAZADO));
        u.AddEdicionCurso(inscripcion(u, "Java", Enum_Estado_inscripcion.ACEPTADO));
        u.AddEdicionCurso(inscripcion(u, "Java", Enum_Estado_inscripcion.INSCRIPTO));
        u.AddEdicionCurso(inscripcion(u, "Python", Enum_Estado_inscripcion.RECHAZADO));

        assertEquals(2, u.getCantMisInscripcionesRechazadas("JAVA"));
        assertEquals(1, u.getCantMisInscripcionesRechazadas("Python"));
        assertEquals(0, u.getCantMisInscripcionesRechazadas("SQL"));
    }

    @Test
    public void testRechazadasConDatosIncompletos() {
        Usuario u = crear();

        u.AddEdicionCurso(null);

        Edi_Usu sinEdicion = new Edi_Usu(u, null, new Date());
        sinEdicion.setEstado(Enum_Estado_inscripcion.RECHAZADO);
        u.AddEdicionCurso(sinEdicion);

        EdicionCurso edSinCurso = new EdicionCurso("E", null, null, new Date(), new Date(), 5, new Date(), new ArrayList<>());
        Edi_Usu sinCurso = new Edi_Usu(u, edSinCurso, new Date());
        sinCurso.setEstado(Enum_Estado_inscripcion.RECHAZADO);
        u.AddEdicionCurso(sinCurso);

        EdicionCurso edCursoSinNombre = new EdicionCurso("E2", null, new Curso(), new Date(), new Date(), 5, new Date(), new ArrayList<>());
        Edi_Usu cursoSinNombre = new Edi_Usu(u, edCursoSinNombre, new Date());
        cursoSinNombre.setEstado(Enum_Estado_inscripcion.RECHAZADO);
        u.AddEdicionCurso(cursoSinNombre);

        assertEquals(0, u.getCantMisInscripcionesRechazadas("Java"));
    }
}
