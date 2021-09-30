/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import static controlador.Controlador.Base;
import java.sql.SQLException;
import java.util.ArrayList;

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
    

    public User() {
    }

    public User(String dni, String nombre, String apellidos, String direccion, String telefono, String usuario, String pass) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.usuario = usuario;
        this.pass = pass;
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
