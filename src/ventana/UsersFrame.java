/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventana;

import Clases.Exporter;
import controlador.Conexion;
import controlador.Controlador;
import controlador.User;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author CARLOS ORELLANO
 */
public class UsersFrame extends javax.swing.JFrame {

    private ArrayList<User> users = User.All();
    Controlador cont = new Controlador();
    DefaultTableModel modelo = new DefaultTableModel();

    /**
     * Creates new form UsersFrame
     */
    public UsersFrame() {
        initComponents();
        this.setLocationRelativeTo(null);

        this.getContentPane().setBackground(new Color(255, 255, 255));

        modelo.setColumnIdentifiers(new String[]{"ID", "DNI", "Nombre", "Apellido", "Usuario", "Rol", "Telefono"});
        listarUsers();
    }

    public void buscar_existente() {

        Map<String, String> datos = cont.getData(txtDni.getText());
        txtNombre.setText(datos.get("nombre"));
        txtApellido.setText(datos.get("apellido"));
        txtDir.setText(datos.get("direccion"));
        txtTel.setText(datos.get("telefono"));
        dniLab.setText(datos.get("dni"));
        //txtDni.setText(datos.get("dni"));       
    }

    public void listarUsers() {

        cont.LimTabla(modelo);
        Object O[] = null;
        for (int i = 0; i < users.size(); i++) {

            modelo.addRow(O);
            User us = users.get(i);
            modelo.setValueAt(us.getIdusuario(), i, 0);
            modelo.setValueAt(us.getDni().toUpperCase(), i, 1);
            modelo.setValueAt(us.getNombre().toUpperCase(), i, 2);
            modelo.setValueAt(us.getApellidos().toUpperCase(), i, 3);
            modelo.setValueAt(us.getUsuario().toUpperCase(), i, 4);
            modelo.setValueAt(us.getRol().toUpperCase(), i, 5);
            modelo.setValueAt(us.getTelefono().toUpperCase(), i, 6);
        }
        this.tableUsers.setModel(modelo);
    }

    public void seleccion() {
        if (tableUsers.getSelectedRow() > -1) {

            int fila = tableUsers.getSelectedRow();
            User us = new User();
            us = us.find(tableUsers.getValueAt(fila, 1).toString());
            // System.out.println(us.getDni());
            txtDni.setText(us.getDni());
            txtNombre.setText(us.getNombre());
            txtApellido.setText(us.getApellidos());
            txtDir.setText(us.getDireccion());
            txtTel.setText(us.getTelefono());
            txtUser.setText(us.getUsuario());
            // txtPass.setText(us.getPass());
            cbRol.setSelectedItem(us.getRol().toUpperCase());

        } else {

            limpiar();
        }
    }

    public void agregarUser() {
        if (validarCampos() || cbRol.getSelectedIndex() < 1) {
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
        } else {
            String dni = dniLab.getText();

            if (dni == null || dni.equals("")) {
                dni = txtDni.getText();
            }

            User us = new User(dni,
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtDir.getText(),
                    txtTel.getText(),
                    txtUser.getText(),
                    txtPass.getText(),
                    cbRol.getSelectedItem().toString());
            if (us.agregarPersona()) {
                JOptionPane.showMessageDialog(null, "Registrado con ??xito");
                users = User.All();
                listarUsers();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "El DNI del usuario ya existe !!", "Mensaje", 2);
            }
        }

    }
public boolean verificar (){
    String pass = txtPass.getText();
        boolean session = false;

        String sql = "SELECT idusuario FROM usuario WHERE  pass = md5('" + pass + "')";

        int id = cont.traerId(sql);
 
        ArrayList<User> users = User.All();
        for (User us : users) {
            if (us.getIdusuario() == id) { 
                User.sesion = us.getRol();
                session = true;
                Principal prin = new Principal();
                prin.setVisible(true);
                dispose();
            }
        }
        
//        if(!session){
//            JOptionPane.showMessageDialog(null, "Credenciales Incorrectas");
//          
//            txtPass.setText("");
//        }
        return session;
}
    public void actualizarUser() {

        if (tableUsers.getSelectedRow() > -1) {

            if (validarCampos() || cbRol.getSelectedIndex() < 1) {
                JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
            } else {
                if(verificar()){
                int fila = tableUsers.getSelectedRow();
                ArrayList<User> userss = User.All();

                User us = new User();
                for (int i = 0; i < userss.size(); i++) {
                    if (userss.get(i).getIdusuario() == Integer.parseInt(tableUsers.getValueAt(fila, 0).toString())) {
                        us = userss.get(i);
                    }
                }
                // System.out.println("se actualiza: " + us.getNombre() + "  su idpersona es " + us.getIdpersona());
                us.setNombre(txtNombre.getText());
                us.setApellidos(txtApellido.getText());
                us.setDireccion(txtDir.getText());
                us.setTelefono(txtTel.getText());
                us.setUsuario(txtUser.getText());
                us.setPass(nueva_pass.getText());
                us.setRol(cbRol.getSelectedItem().toString());

                us.actualizarPers();
                us.actualizarUser();

                this.users = User.All();
                listarUsers();
                JOptionPane.showMessageDialog(null, "Actualizado con ??xito");
                limpiar();
                }else{
                    JOptionPane.showMessageDialog(null, "Contrase??a Incorrecta");
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario !!", "Mensaje", 2);
        }
    }

    public void eliminar() {
        if (tableUsers.getSelectedRow() > -1) {
            int fila = tableUsers.getSelectedRow();
            User.eliminar(Integer.parseInt(tableUsers.getValueAt(fila, 0).toString()));
            this.users = User.All();
            listarUsers();
            JOptionPane.showMessageDialog(null, "Eliminado con ??xito");
            limpiar();

        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario !!", "Mensaje", 2);
        }
    }

    public boolean validarCampos() {

        return cont.estaVacio(txtNombre, txtApellido, txtDir, txtDni, txtTel, txtUser, txtPass);

    }

    public void limpiar() {
        Controlador.limpiar(txtNombre, txtApellido, txtDir, txtDni, txtTel, txtUser, txtPass);
        cbRol.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labDatos = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtDir = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        txtTel = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        cbRol = new javax.swing.JComboBox<>();
        txtPass = new javax.swing.JPasswordField();
        dniLab = new javax.swing.JLabel();
        nueva_pass = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableUsers = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btn_act = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        ex = new javax.swing.JButton();
        labTitulo = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        labDatos.setBackground(new java.awt.Color(255, 255, 255));
        labDatos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos Usuarios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Raleway", 1, 18), new java.awt.Color(64, 71, 86))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(64, 71, 86));
        jLabel2.setText("Nombres");

        jLabel5.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(64, 71, 86));
        jLabel5.setText("Apellidos.");

        jLabel3.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(64, 71, 86));
        jLabel3.setText("Direcci??n");

        jLabel4.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(64, 71, 86));
        jLabel4.setText("Telefono");

        txtNombre.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        txtApellido.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidoActionPerformed(evt);
            }
        });
        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });

        txtDir.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDirActionPerformed(evt);
            }
        });
        txtDir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDirKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(64, 71, 86));
        jLabel13.setText("Buscar DNI: ");

        txtDni.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtDni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDniActionPerformed(evt);
            }
        });
        txtDni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDniKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniKeyTyped(evt);
            }
        });

        txtTel.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelActionPerformed(evt);
            }
        });
        txtTel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(64, 71, 86));
        jLabel6.setText("Contrase??a anterior");

        jLabel7.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(64, 71, 86));
        jLabel7.setText("Usuario");

        jLabel8.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(64, 71, 86));
        jLabel8.setText("Rol");

        txtUser.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });

        cbRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--SELECCIONE--", "ADMINISTRADOR", "PERSONAL", "DIRECTOR" }));

        txtPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPassActionPerformed(evt);
            }
        });

        dniLab.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel1.setText("Contrase??a Nueva");

        javax.swing.GroupLayout labDatosLayout = new javax.swing.GroupLayout(labDatos);
        labDatos.setLayout(labDatosLayout);
        labDatosLayout.setHorizontalGroup(
            labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(labDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(labDatosLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(6, 6, 6)
                        .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dniLab, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(labDatosLayout.createSequentialGroup()
                        .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(labDatosLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDir, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(labDatosLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel8)
                    .addGroup(labDatosLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(nueva_pass, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(labDatosLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(80, 80, 80)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        labDatosLayout.setVerticalGroup(
            labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, labDatosLayout.createSequentialGroup()
                .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(dniLab, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(labDatosLayout.createSequentialGroup()
                        .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(labDatosLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jLabel3)
                                .addGap(11, 11, 11))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, labDatosLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(labDatosLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(jLabel4))
                            .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addComponent(nueva_pass))
                    .addGroup(labDatosLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addGap(4360, 4360, 4360)
                .addGroup(labDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tableUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableUsersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableUsers);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(0, 99, 174));
        jButton1.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Agregar");
        jButton1.setBorderPainted(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btn_act.setBackground(new java.awt.Color(217, 162, 27));
        btn_act.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        btn_act.setForeground(new java.awt.Color(255, 255, 255));
        btn_act.setText("Actualizar");
        btn_act.setBorderPainted(false);
        btn_act.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_act.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_actActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(219, 79, 72));
        jButton3.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Eliminar");
        jButton3.setBorderPainted(false);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(btn_act, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btn_act, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        ex.setBackground(new java.awt.Color(0, 99, 174));
        ex.setFont(new java.awt.Font("Raleway", 1, 11)); // NOI18N
        ex.setForeground(new java.awt.Color(255, 255, 255));
        ex.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/icos/excel.png"))); // NOI18N
        ex.setText("Exportar");
        ex.setBorderPainted(false);
        ex.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ex, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(ex)
                .addGap(19, 19, 19))
        );

        labTitulo.setBackground(new java.awt.Color(0, 99, 174));

        jLabel12.setFont(new java.awt.Font("Raleway", 1, 36)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Usuarios");

        jPanel1.setBackground(new java.awt.Color(64, 71, 86));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/icos/mi.png"))); // NOI18N
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/icos/clos.png"))); // NOI18N
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        btnBack.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/icos/atras.png"))); // NOI18N
        btnBack.setText("Volver");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout labTituloLayout = new javax.swing.GroupLayout(labTitulo);
        labTitulo.setLayout(labTituloLayout);
        labTituloLayout.setHorizontalGroup(
            labTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, labTituloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(437, 437, 437))
        );
        labTituloLayout.setVerticalGroup(
            labTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(labTituloLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(labDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(labTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(labTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoActionPerformed
         ((JComponent) evt.getSource()).transferFocus();
    }//GEN-LAST:event_txtApellidoActionPerformed

    private void txtDniKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniKeyReleased
        buscar_existente();
    }//GEN-LAST:event_txtDniKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        new Principal().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void tableUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableUsersMouseClicked
        seleccion();
    }//GEN-LAST:event_tableUsersMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        agregarUser();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btn_actActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_actActionPerformed
        actualizarUser();
    }//GEN-LAST:event_btn_actActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        eliminar();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void exActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exActionPerformed
        if (tableUsers.getRowCount() > 0) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel", "xls");
            chooser.setFileFilter(filter);
            chooser.setDialogTitle("Guardar archivo");
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                List tb = new ArrayList();
                List nom = new ArrayList();
                tb.add(tableUsers);
                nom.add("Compras por factura");
                String file = chooser.getSelectedFile().toString().concat(".xls");
                try {
                    Clases.Exporter e = new Exporter(new File(file), tb, nom);
                    if (e.export()) {
                        JOptionPane.showMessageDialog(null, "Los datos fueron exportados a excel en el directorio seleccionado", "Mensaje de Informacion", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Hubo un error " + e.getMessage(), " Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay datos para exportar", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_exActionPerformed

    private void txtDniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDniActionPerformed
        ((JComponent) evt.getSource()).transferFocus();
    }//GEN-LAST:event_txtDniActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
         ((JComponent) evt.getSource()).transferFocus();
    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDirActionPerformed
        ((JComponent) evt.getSource()).transferFocus();
    }//GEN-LAST:event_txtDirActionPerformed

    private void txtTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelActionPerformed
        ((JComponent) evt.getSource()).transferFocus();
    }//GEN-LAST:event_txtTelActionPerformed

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserActionPerformed
        ((JComponent) evt.getSource()).transferFocus();
    }//GEN-LAST:event_txtUserActionPerformed

    private void txtPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPassActionPerformed
((JComponent) evt.getSource()).transferFocus();
    }//GEN-LAST:event_txtPassActionPerformed

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        Controlador.soloLetras(evt);
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
         Controlador.soloLetras(evt);
    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtDirKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDirKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDirKeyTyped

    private void txtTelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelKeyTyped
        Controlador.soloNumeros(evt);
        Controlador.aplicarLongitudTelf(txtTel, evt);
    }//GEN-LAST:event_txtTelKeyTyped

    private void txtDniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniKeyTyped
         Controlador.soloNumeros(evt);
         Controlador.aplicarLongitudDNI(txtDni, evt);
    }//GEN-LAST:event_txtDniKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UsersFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UsersFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UsersFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UsersFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UsersFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btn_act;
    private javax.swing.JComboBox<String> cbRol;
    private javax.swing.JLabel dniLab;
    private javax.swing.JButton ex;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel labDatos;
    private javax.swing.JPanel labTitulo;
    private javax.swing.JPasswordField nueva_pass;
    private javax.swing.JTable tableUsers;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtDir;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtTel;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
