package Classes;

import Classes.Edi_Usu;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author mateo
 */
@Entity
@Table(name="Usuario")
@PrimaryKeyJoinColumn(name = "nickname")
public class Usuario extends UsuarioBase {

    // Relación con Edi_Usu (Refactorizada con @MapsId)
    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT) 
    private List<Edi_Usu> misInscripciones;
    
    // Relación con Prog_Usu (Sigue usando la clave compuesta embebida tradicional)
    @OneToMany(mappedBy = "id.miUsuario", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Prog_Usu> misInscripcionesProg;

    public Usuario() {
        super();
    }

    public Usuario(String nick, String nombre, String apellido, String correo, String contrasenia, Date fNac, String img) {
        super(nick, nombre, apellido, correo, contrasenia, fNac, img);
    }

    @Override
    public void ModificarMisDatos(String nom, String apellido, String correo, Date fNac, String img) {
        super.ModificarMisDatos(nom, apellido, correo, fNac, img);
    }

    public void AddEdicionCurso(Edi_Usu ec) {
        if (misInscripciones == null) {
            misInscripciones = new ArrayList<>();
        }
        misInscripciones.add(ec);
    }

    public void AddPrograma(Prog_Usu pu) {
        if (misInscripcionesProg == null) {
            misInscripcionesProg = new ArrayList<>();
        }
        misInscripcionesProg.add(pu);
    }

    public List<Edi_Usu> getMisInscripciones() {
        return this.misInscripciones;
    }

    public List<Prog_Usu> getMisInscripcionesPro() {
        return this.misInscripcionesProg;
    }

    public int getCantMisInscripcionesRechazadas(String nomCurso) {
        if (misInscripciones == null || nomCurso == null || nomCurso.isBlank()) {
            return 0;
        }

        int cant = 0;
        for (Edi_Usu eu : misInscripciones) {
            if (eu != null && eu.getMiEstado() == Enum_Estado_inscripcion.RECHAZADO) {
                if (eu.getEdicion() != null 
                        && eu.getEdicion().getCurso() != null 
                        && eu.getEdicion().getCurso().getNombre() != null) {
                    
                    String nombreCursoInscripcion = eu.getEdicion().getCurso().getNombre().trim();
                    if (nombreCursoInscripcion.equalsIgnoreCase(nomCurso.trim())) {
                        cant++;
                    }
                }
            }
        }
        return cant;
    }
}