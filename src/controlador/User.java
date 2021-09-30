/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import static controlador.Controlador.Base;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author CARLOS ORELLANO
 */
public class User {

    private int idusuario;
    private int idpersona;

    private String dni;
    private String nombre;
    private String apellidos;
    private String direccion;
    private String telefono;

    private String usuario;
    private String pass;
    private String rol;

    private ArrayList<User> users = new ArrayList<>();
    Controlador cont = new Controlador();

    public User() {
    }

    public User(String dni, String nombre, String apellidos, String direccion, String telefono, String usuario, String pass, String rol) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.usuario = usuario;
        this.pass = pass;
        this.rol = rol;
    }

    public User(int idusuario, int idpersona, String dni, String nombre, String apellidos, String direccion, String telefono, String usuario, String pass, String rol) {
        this.idusuario = idusuario;
        this.idpersona = idpersona;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.usuario = usuario;
        this.pass = pass;
        this.rol = rol;
    }

    public static ArrayList All() {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM v_usuarios";

        try {
            Base.st = Base.conec.createStatement();
            Base.rt = Base.st.executeQuery(query);
            while (Base.rt.next()) {
                User usr = new User(
                        Integer.parseInt(Base.rt.getString(1)),
                        Integer.parseInt(Base.rt.getString(2)),
                        Base.rt.getString(3),
                        Base.rt.getString(4),
                        Base.rt.getString(5),
                        Base.rt.getString(6),
                        Base.rt.getString(7),
                        Base.rt.getString(8),
                        Base.rt.getString(9),
                        Base.rt.getString(10)
                );
                users.add(usr);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;

    }

    public User find(String dni) {

        users = User.All();

        User us = new User();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getDni().equals(dni)) {
                us = users.get(i);
            }
        }

        return us;

    }

    public void buscarPersona() {
        String query = "SELECT * FROM persona where dni = ?";

    }

    public boolean agregarPersona() {
       boolean b = true;
        User us = find(this.dni);

        if (us.getNombre() == null) {
            //  System.out.println("no esxite por lo tanto buscare en la tabla de personas " + us.getNombre() + " " + this.dni); 
            Map<String, String> datos = cont.getData(this.dni);
            if (datos.isEmpty()) {
                
                insertNuevaPersona();
                datos = cont.getData(this.dni);
                this.idpersona = Integer.parseInt(datos.get("idpersona"));
                insertUser();
                System.out.println("exito nueva persona");
                
            } else {
               this.idpersona = Integer.parseInt(datos.get("idpersona"));
               insertUser();
                System.out.println("exito con existente " + datos.get("idpersona") + " " + datos.get("nombre") );
            }

        } else {
            b= false;
            System.out.println("este usuario existe");
        }

        return b;
    }

    public void insertNuevaPersona() {
        String query = "INSERT INTO persona VALUES(null,?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = Conexion.conec.prepareStatement(query);
            st.setString(1, this.nombre);
            st.setString(2, this.apellidos);
            st.setString(3, this.dni);
            st.setString(4, this.direccion);
            st.setString(5, this.telefono);
            if (st.executeUpdate() == 1) {

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
    public void insertUser(){
        String query = "INSERT INTO usuario VALUES(null,?,MD5(?), ?, ?)";
        try {
            PreparedStatement st = Conexion.conec.prepareStatement(query);
            st.setString(1, this.usuario);
            st.setString(2, this.pass);
            st.setString(3, this.rol);
            st.setInt(4, this.idpersona);
      if (st.executeUpdate() == 1) {

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(int idpersona) {
        this.idpersona = idpersona;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}
