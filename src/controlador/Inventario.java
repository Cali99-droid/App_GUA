package controlador;

import static controlador.Controlador.Base;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private ArrayList<Articulo> articulos = new ArrayList<>();

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
                        Base.rt.getString(5),
                        Integer.parseInt(Base.rt.getString(6)));
                inventarios.add(inv);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return inventarios;
    }

    public boolean agregarInventario() {
        boolean b = false;
        String query = "INSERT INTO inventario VALUES(null,?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = Conexion.conec.prepareStatement(query);
            st.setString(1, this.nombre);
            st.setString(2, this.fecha_inicio);
            st.setString(3, this.fecha_fin);
            st.setString(4, this.observaciones);
            st.setInt(5, this.idusuario);
            if (st.executeUpdate() == 1) {
                b = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Articulo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }

    public void asignarId() {
        agregarInventario();
        String query = "select max(idinventario) from inventario;";
        try {
            Base.st = Base.conec.createStatement();
            Base.rt = Base.st.executeQuery(query);
            while (Base.rt.next()) {
                this.idinventario = Integer.parseInt(Base.rt.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void insertInvenArt() {
        asignarId();
        String query = "INSERT INTO articulo_x_inventario VALUES(null,?, ?, ?, ?)";

        for (int i = 0; i < this.articulos.size(); i++) {
            PreparedStatement st;
            try {
                st = Conexion.conec.prepareStatement(query);
                st.setInt(1, articulos.get(i).getIdArticulo());
                st.setInt(2, this.idinventario);
                st.setString(3, articulos.get(i).getEstado());
                st.setString(4, articulos.get(i).getArea());
                st.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList getArticulos() {
        String query = "SELECT * FROM v_articulos_x_invent WHERE idinventarios = " + this.idinventario;
 
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
                        Integer.parseInt(Base.rt.getString(8)));
                articulos.add(art);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return articulos;
    }

    public void setArticulo(Articulo art) {
        articulos.add(art);
    }

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

    public void setArticulos(ArrayList<Articulo> articulos) {
        this.articulos = articulos;
    }
    
    

}
