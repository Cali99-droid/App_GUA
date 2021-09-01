package controlador;

import java.sql.*;

public class Conexion {
//***************ATRIBUTOS**************

    public PreparedStatement prest = null;
    public CallableStatement cllst = null;
    public static Connection conec = null;
    public Statement st = null;
    public ResultSet rt = null;
    public static String usu, contraseña, host, db;
    //***************FIN ATRIBUTOS**************

    //**************METODOS**************
    public Conexion() {
        Conectar();
        
    }

    public void Conectar() {
        try {
            usu = "root";
            contraseña = "root";
            host = "localhost";  
            db = "colegioBD" ;
            String url = "jdbc:mysql://localhost:3306/" + db + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conec = DriverManager.getConnection(url, usu, contraseña);
            
        } catch (Exception e) { 
            e.printStackTrace();    
             
        }
    }
//***************FIN METODOS**************
}
