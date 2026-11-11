package Logica.util;

import Logica.DTsClasses.DTActaEstudio;
import Logica.DTsClasses.DTFilaActa;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Genera el PDF del acta de estudio (Requerimiento 7.4) con iText: los cursos del
 * programa, el resultado de aprobación y la fecha de resultado de cada edición.
 */
public final class ActaPdf {

    /** Nota mínima de aprobación (la misma que usa el Servidor Central). */
    public static final int NOTA_APROBACION = 6;

    private static final Font TITULO = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font SUBTITULO = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL = new Font(Font.FontFamily.HELVETICA, 10);
    private static final Font NEGRITA = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font CABEZAL = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
    private static final Font PIE = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);
    private static final BaseColor GRIS_OSCURO = new BaseColor(33, 37, 41);
    private static final BaseColor VERDE = new BaseColor(25, 135, 84);
    private static final BaseColor ROJO = new BaseColor(220, 53, 69);

    private ActaPdf() {
    }

    /**
     * Escribe el acta en {@code out}.
     *
     * @param logo imagen del logo (PNG/JPG) o null para omitirlo
     */
    public static void escribir(DTActaEstudio acta, byte[] logo, OutputStream out) throws IOException {
        Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(doc, out);
            doc.addTitle("Acta de Estudio - " + acta.getPrograma());
            doc.addCreator("edEXT");
            doc.open();

            if (logo != null) {
                try {
                    Image img = Image.getInstance(logo);
                    img.scaleToFit(120, 60);
                    img.setAlignment(Element.ALIGN_CENTER);
                    doc.add(img);
                } catch (IOException | DocumentException e) {
                    // sin logo si la imagen no se puede leer
                }
            }

            Paragraph titulo = new Paragraph("Acta de Estudio", TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(4);
            doc.add(titulo);
            Paragraph programa = new Paragraph("Programa de formación: " + acta.getPrograma(), SUBTITULO);
            programa.setAlignment(Element.ALIGN_CENTER);
            programa.setSpacingAfter(18);
            doc.add(programa);

            PdfPTable datos = new PdfPTable(new float[]{1.3f, 3f});
            datos.setWidthPercentage(100);
            datoFila(datos, "Estudiante", acta.getNombre() + " " + acta.getApellido() + " (" + acta.getNickname() + ")");
            datoFila(datos, "Programa", acta.getPrograma());
            if (acta.getInstituto() != null && !acta.getInstituto().isBlank()) {
                datoFila(datos, "Instituto", acta.getInstituto());
            }
            datoFila(datos, "Fecha de emisión", fecha(acta.getFechaEmision()));
            datos.setSpacingAfter(18);
            doc.add(datos);

            PdfPTable tabla = new PdfPTable(new float[]{2.8f, 2.8f, 1.9f, 1.8f, 1.8f});
            tabla.setWidthPercentage(100);
            tabla.setHeaderRows(1);
            for (String c : new String[]{"Curso", "Edición", "Calificación", "Resultado", "Fecha de resultado"}) {
                PdfPCell celda = new PdfPCell(new Phrase(c, CABEZAL));
                celda.setBackgroundColor(GRIS_OSCURO);
                celda.setPadding(6);
                tabla.addCell(celda);
            }
            if (acta.getFilas().isEmpty()) {
                PdfPCell vacia = new PdfPCell(new Phrase("El programa no tiene cursos asociados.", NORMAL));
                vacia.setColspan(5);
                vacia.setPadding(6);
                vacia.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(vacia);
            }
            for (DTFilaActa f : acta.getFilas()) {
                tabla.addCell(celda(f.getCurso(), NORMAL, Element.ALIGN_LEFT));
                tabla.addCell(celda(f.getEdicion() != null ? f.getEdicion() : "-", NORMAL, Element.ALIGN_LEFT));
                tabla.addCell(celda(f.getNota() != null ? String.valueOf(f.getNota()) : "-", NORMAL, Element.ALIGN_CENTER));
                Font fuente = new Font(NEGRITA);
                if (f.isAprobado()) {
                    fuente.setColor(VERDE);
                } else if ("Reprobado".equals(f.getResultado())) {
                    fuente.setColor(ROJO);
                }
                tabla.addCell(celda(f.getResultado(), fuente, Element.ALIGN_CENTER));
                tabla.addCell(celda(fecha(f.getFechaResultado()), NORMAL, Element.ALIGN_CENTER));
            }
            tabla.setSpacingAfter(14);
            doc.add(tabla);

            doc.add(new Paragraph("Cursos aprobados: " + acta.getCantAprobados() + " de " + acta.getFilas().size(), NEGRITA));
            Font estado = new Font(SUBTITULO);
            estado.setColor(acta.isProgramaAprobado() ? VERDE : GRIS_OSCURO);
            Paragraph resultado = new Paragraph("Estado del programa: "
                    + (acta.isProgramaAprobado() ? "APROBADO" : "NO COMPLETADO"), estado);
            resultado.setSpacingBefore(4);
            resultado.setSpacingAfter(24);
            doc.add(resultado);

            doc.add(new Paragraph("Un curso se considera aprobado con calificación " + NOTA_APROBACION
                    + " o superior (escala de 1 a 12). La fecha de resultado es la fecha de cierre de la edición.", PIE));
            doc.add(new Paragraph("Documento generado por edEXT el "
                    + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()) + ".", PIE));
        } catch (DocumentException e) {
            throw new IOException("No se pudo generar el PDF del acta: " + e.getMessage(), e);
        } finally {
            if (doc.isOpen()) {
                doc.close();
            }
        }
    }

    /** Nombre de archivo seguro (solo ASCII) para la descarga. */
    public static String nombreArchivo(DTActaEstudio acta) {
        String base = "Acta_" + acta.getPrograma() + "_" + acta.getNickname();
        String ascii = Normalizer.normalize(base, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return ascii.replaceAll("[^A-Za-z0-9._-]+", "_") + ".pdf";
    }

    private static void datoFila(PdfPTable t, String etiqueta, String valor) {
        PdfPCell e = new PdfPCell(new Phrase(etiqueta, NEGRITA));
        e.setBorder(PdfPCell.NO_BORDER);
        e.setPadding(3);
        t.addCell(e);
        PdfPCell v = new PdfPCell(new Phrase(valor != null ? valor : "-", NORMAL));
        v.setBorder(PdfPCell.NO_BORDER);
        v.setPadding(3);
        t.addCell(v);
    }

    private static PdfPCell celda(String texto, Font fuente, int alineacion) {
        PdfPCell c = new PdfPCell(new Phrase(texto != null ? texto : "-", fuente));
        c.setPadding(5);
        c.setHorizontalAlignment(alineacion);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return c;
    }

    private static String fecha(Date d) {
        return d == null ? "-" : new SimpleDateFormat("dd/MM/yyyy").format(d);
    }
}
