package controlador;

import javax.swing.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.*;

public class Controlador {

    public static Conexion Base = new Conexion();
    //public Imprimir Impresora = new Imprimir();

    public void LlenarCombo(JComboBox cbo, String Consulta, int pos) {
        cbo.removeAllItems();
        try {
            Base.rt = DevolverRegistro(Consulta);
            while (Base.rt.next()) {
                cbo.addItem(Base.rt.getString(pos).toUpperCase());
            }
            cbo.setSelectedIndex(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LlenarLista(DefaultListModel list, String Consulta, int pos) {
        list.removeAllElements();

        try {
            Base.rt = DevolverRegistro(Consulta);
            while (Base.rt.next()) {

                list.addElement(Base.rt.getString(pos));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet DevolverRegistro(String sq) {
        try {
            Base.st = Base.conec.createStatement();
            Base.rt
                    = Base.st.executeQuery(sq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base.rt;
    }

    public String DevolverRegistroDto(String sq, int pos) {
        String rs = "";
        try {
            Base.st = Base.conec.createStatement();
            Base.rt = Base.st.executeQuery(sq);
            if (Base.rt.next()) {
                rs = Base.rt.getString(pos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public boolean Verificarconsulta(String sql) {
        boolean bd = false;
        try {
            Base.st = Base.conec.createStatement();
            Base.rt = Base.st.executeQuery(sql);
            bd = Base.rt.next();
        } catch (Exception e) {
        }
        return bd;
    }

    public void ActualizarRegistro(String sq) {
        try {
            Base.st = Base.conec.createStatement();
            Base.st.executeUpdate(sq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean act(String sq) {
        boolean b= true ;
        try {
            Base.st = Base.conec.createStatement();
           if( Base.st.executeUpdate(sq) > 0){
               b = true;
               
           }else{
               b=false;
           }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return b;
    }

    public void LimTabla(DefaultTableModel mdl) {
        while (mdl.getRowCount() > 0) {
            mdl.removeRow(0);
        }
    }

    public void LlenarJTabla(DefaultTableModel mdl, String sq, int cdt) {
        LimTabla(mdl);
        String Data[] = new String[20];
        try {
            Base.st = Base.conec.createStatement();
            Base.rt = Base.st.executeQuery(sq);
            while (Base.rt.next()) {
                for (int i = 1; i <= cdt; i++) {
                    Data[i - 1] = Base.rt.getString(i);
                }
                mdl.addRow(Data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Map getData(String dni) {

        String dnif = "%" + dni + "%";
        String sql = "SELECT * FROM persona WHERE dni LIKE  ? ";
        Map<String, String> datos = new HashMap<String, String>();

        try {
            PreparedStatement st = Conexion.conec.prepareStatement(sql);
            st.setString(1, dnif);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                datos.put("nombre", rs.getString("nombre"));
                datos.put("apellido", rs.getString("apellido"));
                datos.put("dni", rs.getString("dni"));
                datos.put("direccion", rs.getString("direccion"));
                datos.put("telefono", rs.getString("telefono"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return datos;
    }

}
