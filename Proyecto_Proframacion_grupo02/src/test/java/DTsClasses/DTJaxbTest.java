package DTsClasses;

import Classes.Enum_Estado_inscripcion;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifica que los DTs se puedan enviar por el Web Service (JAXB): ida y vuelta
 * a XML conservando el tipo concreto y los datos, sin la contraseña.
 */
public class DTJaxbTest {

    @SuppressWarnings("unchecked")
    private static <T extends DTMaster> T idaYVuelta(DTMaster dt, StringWriter xml) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(DTMaster.class);
        Marshaller m = ctx.createMarshaller();
        m.marshal(new JAXBElement<>(new QName("dt"), DTMaster.class, dt), xml);
        return (T) ctx.createUnmarshaller()
                .unmarshal(new StreamSource(new StringReader(xml.toString())), DTMaster.class).getValue();
    }

    private static <T extends DTMaster> T idaYVuelta(DTMaster dt) throws Exception {
        return idaYVuelta(dt, new StringWriter());
    }

    @Test
    public void testDocenteSinPassword() throws Exception {
        Date d = new Date(1000);
        StringWriter xml = new StringWriter();
        DTDocente r = idaYVuelta(new DTDocente("n", "No", "Ap", "c@x", "secreta", d, "INCO", "i.png",
                List.of("C1"), List.of("E1"), List.of("P1")), xml);
        assertFalse(xml.toString().contains("secreta"));
        assertNull(r.getPassword());
        assertEquals("n", r.getNickname());
        assertEquals("INCO", r.getInstituto());
        assertEquals(List.of("C1"), r.getCursos());
        assertEquals(List.of("E1"), r.getEdiciones());
        assertEquals(d, r.getFNac());
        assertEquals("i.png", r.getImg());
    }

    @Test
    public void testUsuarioCursoEdicionPrograma() throws Exception {
        Date d = new Date(2000);
        DTUsuario u = idaYVuelta(new DTUsuario("u", "N", "A", "c", "p", d, null, List.of(), List.of("P")));
        assertEquals("u", u.getNickname());
        assertEquals(List.of("P"), u.getProgramas());

        DTCurso c = idaYVuelta(new DTCurso("INCO", "Java", "d", 1, 2.5f, 3, "url", d, List.of("Prev"),
                List.of("E"), List.of("P"), List.of("Cat"), "doc", "c.png"));
        assertEquals("Java", c.getNombre());
        assertEquals(2.5f, c.getCantHoras());
        assertEquals(List.of("Prev"), c.getPrevias());
        assertEquals("c.png", c.getImage());

        DTEdi_Usu ins = new DTEdi_Usu("u", "E", d, Enum_Estado_inscripcion.values()[0]);
        DTEdicionCurso e = idaYVuelta(new DTEdicionCurso("INCO", "Java", "E", d, d, 10, 2, List.of("doc"), d,
                List.of(ins), List.of("Cat"), "e.png"));
        assertEquals("E", e.getNombre());
        assertEquals(2, e.getCupoActual());
        assertEquals("u", e.getMisInscripciones().get(0).getNickEstudiante());
        assertEquals(Enum_Estado_inscripcion.values()[0], e.getMisInscripciones().get(0).getEstadoIns());

        DTProgramaForm p = idaYVuelta(new DTProgramaForm("P", "d", new Vigencia(d, d), List.of("Java"), d,
                List.of("Cat"), "p.png"));
        assertEquals("P", p.getNombre());
        assertEquals(d, p.getVigenciaProg().getFechaFin());
    }

    @Test
    public void testDTsSimples() throws Exception {
        DTCategoria cat = idaYVuelta(new DTCategoria("Cat"));
        assertEquals("Cat", cat.getNombre());
        DTInstituto ins = idaYVuelta(new DTInstituto("INCO"));
        assertEquals("INCO", ins.getNombre());
        DTEdi_Usu eu = idaYVuelta(new DTEdi_Usu("u", "E", new Date(5), Enum_Estado_inscripcion.values()[0]));
        assertEquals("E", eu.getNomEdicion());
        assertEquals(new Date(5), eu.getfInscripcion());
        DTEdi_Usu conVideo = idaYVuelta(new DTEdi_Usu("u", "E", new Date(5), Enum_Estado_inscripcion.values()[0], "https://v"));
        assertEquals("https://v", conVideo.getUrlVideo());

        DTCambioEstado ce = idaYVuelta(new DTCambioEstado("ana", Enum_Estado_inscripcion.values()[0]));
        assertEquals("ana", ce.getNickname());
        assertEquals(Enum_Estado_inscripcion.values()[0], ce.getEstado());

        assertNotNull(idaYVuelta(new DTMaster()));
    }
}
