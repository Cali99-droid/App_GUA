package controlador;

import static controlador.Controlador.Base;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


/**
 *
 * @author CARLOS ORELLANO
 */
public class Articulo {
    private static final Logger LOG = Logger.getLogger(Articulo.class.getName());
    int idArticulo;
    String nombre;
    String descripcion;
    String area;
    String estado;
    String categoria;

   //ids
    int idArea;
    int idEstado;
    int idCategoria;

    public Articulo(int idArticulo, String nombre, String descripcion, String area, String estado,String categoria, int idArea, int idEstado, int idCategoria) {
        this.idArticulo = idArticulo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.area = area;
        this.estado = estado;
        this.categoria = categoria;
        this.idArea = idArea;
        this.idEstado = idEstado;
        this.idCategoria = idCategoria;
        
    }
    
    public Articulo(){
        
    }

    public static ArrayList mapear() {
        ArrayList<Articulo> articulos = new ArrayList<>();
        String query = "SELECT * FROM v_articulos";
        
        try {
            Base.st = Base.conec.createStatement();
            Base.rt = Base.st.executeQuery(query);
            while (Base.rt.next()) {
                Articulo art = new Articulo(
                        Integer.parseInt(Base.rt.getString(1)), 
                        Base.rt.getString(2), 
                        Base.rt.getString(3), 
                        Base.rt.getString(4), 
                        Base.rt.getString(5), 
                        Base.rt.getString(6),
                        Integer.parseInt(Base.rt.getString(7)),
                        Integer.parseInt(Base.rt.getString(8)),
                        Integer.parseInt(Base.rt.getString(9)));
                articulos.add(art);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return articulos;

    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
     public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    

    

    
    

        
       
    
   

}
