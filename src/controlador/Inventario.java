package controlador;

import static controlador.Controlador.Base;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author CARLOS ORELLANO
 * 
 */
public class Inventario {
   
    private int idinventario;
    private String nombre;
    private String fecha_inicio;
    private String fecha_fin;
    private String observaciones;
    private int idusuario;
    private ArrayList<Articulo> articulos;
    public Inventario() {
    }

    public Inventario(int idinventario, String nombre, String fecha_inicio, String fecha_fin, String observaciones, int idusuario) {
        this.idinventario = idinventario;
        this.nombre = nombre;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.observaciones = observaciones;
        this.idusuario = idusuario;
    }
    
    public static ArrayList all() {
        ArrayList<Inventario> inventarios = new ArrayList<>();
        String query = "SELECT * FROM inventario";
        
        try {
            Base.st = Base.conec.createStatement();
            Base.rt = Base.st.executeQuery(query);
            while (Base.rt.next()) {
                Inventario inv = new Inventario(Integer.parseInt(Base.rt.getString(1)),
                        Base.rt.getString(2), 
                        Base.rt.getString(3), 
                        Base.rt.getString(4),
                        Base.rt.getString(5) , 
                        Integer.parseInt(Base.rt.getString(6)));
                inventarios.add(inv);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return inventarios;
    }
    
//    public ArrayList<Articulo> getArticulos(){
//         String query = "SELECT idarticulos FROM articulo_x_inventario";
//         
//    }

    public int getIdinventario() {
        return idinventario;
    }

    public void setIdinventario(int idinventario) {
        this.idinventario = idinventario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }
    
    
    
    
}
