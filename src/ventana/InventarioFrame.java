package ventana;

import Clases.Exporter;
import controlador.Articulo;
import controlador.Controlador;
import controlador.Inventario;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author CARLOS ORELLANO
 */
public class InventarioFrame extends javax.swing.JFrame {

    ArrayList<Articulo> articulos = Articulo.mapear();
    ArrayList<Inventario> inventarios = Inventario.all();
    Controlador cont = new Controlador();
    private TableRowSorter trsfiltro;
    String filtro;

    DefaultTableModel modelo = new DefaultTableModel();

    public InventarioFrame() {
        initComponents();
        this.setLocationRelativeTo(null);

        //dar colores
        this.getContentPane().setBackground(new Color(255, 255, 255));
        this.datosArticulo.setBackground(new Color(250, 248, 255));
        categoria_c.setBackground(new Color(101, 251, 210));
        estado_c.setBackground(new Color(101, 251, 210));
        area_c.setBackground(new Color(101, 251, 210));
        agregar.setBackground(new Color(0, 139, 105));
        actualizar.setBackground(new Color(255, 171, 20));
        delete.setBackground(new Color(163, 104, 84));
        inventario.setBackground(new Color(0, 206, 215));
        confirmar.setBackground(new Color(0, 139, 105));
        deshacer.setBackground(new Color(163, 104, 84));
        //cargar combos
        cont.LlenarCombo(categoria_c, "SELECT * FROM categoria", 2);
        cont.LlenarCombo(estado_c, "SELECT * FROM estado", 2);
        cont.LlenarCombo(area_c, "SELECT * FROM area", 2);
        llenar_combo_inv();

        //inicializar botones
        deshacer.setVisible(false);
        confirmar.setVisible(false);

//        actualizar.setEnabled(false);
//        delete.setEnabled(false);
        //crear columnas de modelo
        modelo.setColumnIdentifiers(new String[]{"ID", "Nombre", "Descripcion", "Area", "Estado", "Categoria"});       
         
        //agregar oyente para el textfield buscar
        buscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                String cadena = (buscar.getText()).toUpperCase();
                buscar.setText(cadena);
                repaint();
                filtro();
            }
        });

        //listar articulos en la tabla articulos
        listarArticulos();
    }
    
    public void llenar_combo_inv(){
        this.c_inventarios.removeAllItems();
        
        for (int i = 0; i < inventarios.size(); i++) {
            this.c_inventarios.addItem(inventarios.get(i).getNombre());
        }
        
        c_inventarios.setSelectedIndex(-1);
        
    }

    public void listarArticulos() {

        cont.LimTabla(modelo);
        Object O[] = null;
        for (int i = 0; i < articulos.size(); i++) {

            modelo.addRow(O);
            Articulo getAr = articulos.get(i);
            modelo.setValueAt(getAr.getIdArticulo(), i, 0);
            modelo.setValueAt(getAr.getNombre().toUpperCase(), i, 1);
            modelo.setValueAt(getAr.getDescripcion().toUpperCase(), i, 2);
            modelo.setValueAt(getAr.getArea().toUpperCase(), i, 3);
            modelo.setValueAt(getAr.getEstado().toUpperCase(), i, 4);
            modelo.setValueAt(getAr.getCategoria().toUpperCase(), i, 5);
        }
        this.t_articulos.setModel(modelo);
    }

    public void seleccion() {
        if (t_articulos.getSelectedRow() > -1) {

            int fila = t_articulos.getSelectedRow();
            nombre.setText(t_articulos.getValueAt(fila, 1).toString());
            descripcion.setText(t_articulos.getValueAt(fila, 2).toString());
            area_c.setSelectedItem(t_articulos.getValueAt(fila, 3));
            estado_c.setSelectedItem(t_articulos.getValueAt(fila, 4));
            categoria_c.setSelectedItem(t_articulos.getValueAt(fila, 5));

        } else {

            limpiar();
        }
    }

    public void filtro() {
        filtro = buscar.getText();
        int indice = cb_filro.getSelectedIndex() + 1;
        trsfiltro.setRowFilter(RowFilter.regexFilter(buscar.getText(), indice));
    }

    public void registrarInventario() {
        for (int i = 0; i < articulos.size(); i++) {
            String query = "UPDATE articulo SET"
                    + " nombre = '" + articulos.get(i).getNombre() + "'  "
                    + ", descripcion = '" + articulos.get(i).getDescripcion() + "' "
                    + ", idArea = '" + articulos.get(i).getIdArea() + "' "
                    + ", idestado = '" + articulos.get(i).getIdEstado() + "' "
                    + ", idcategoria = '" + articulos.get(i).getIdCategoria() + "' "
                    + "WHERE idarticulo = '" + articulos.get(i).getIdArticulo() + "'";

            cont.act(query);

            //System.out.println(query);
        }
        articulos = Articulo.mapear();
        listarArticulos();
    }

    public void desahcer() {
        if (JOptionPane.showConfirmDialog(null, "Esta seguro de deshacer todos los cambios", "Confirmar", 0) == 0) {
            articulos = Articulo.mapear();
            listarArticulos();
            limpiar();
            volverEstado();

        }
    }

    public void pre_actualizar() {
        if (t_articulos.getSelectedRow() > -1) {
            int index = t_articulos.getSelectedRow();
            Articulo art = find(Integer.parseInt(t_articulos.getValueAt(index, 0).toString()));
            art.setNombre(nombre.getText());
            art.setDescripcion(descripcion.getText());
            art.setArea(area_c.getSelectedItem().toString());
            art.setEstado(estado_c.getSelectedItem().toString());
            art.setCategoria(categoria_c.getSelectedItem().toString());
            art.setIdArea(area_c.getSelectedIndex() + 1);
            art.setIdEstado(estado_c.getSelectedIndex() + 1);
            art.setIdCategoria(categoria_c.getSelectedIndex() + 1);
            limpiar();
            listarArticulos();

        } else {
            JOptionPane.showMessageDialog(null, "Debe Seleccionar un articulo");

        }

    }

    public void volverEstado() {
        nombreInventario.setText("");
        titulo.setText("INVENTARIO");
        this.getContentPane().setBackground(new Color(255, 255, 255));
        titulo.setForeground(Color.BLACK);
        deshacer.setVisible(false);
        confirmar.setVisible(false);

    }

    public Articulo find(int id) {
        Articulo art = new Articulo();
        for (int i = 0; i < articulos.size(); i++) {
            if (articulos.get(i).getIdArticulo() == id) {
                art = articulos.get(i);
            }
        }

        return art;
    }
    
//falta confirmar -- 
    public void insertar_articulo() {
        Articulo art = new Articulo(nombre.getText(), descripcion.getText(),
                area_c.getSelectedIndex() + 1,
                estado_c.getSelectedIndex() + 1,
                categoria_c.getSelectedIndex() + 1);
        if (art.agregarArticulo()) {
            JOptionPane.showMessageDialog(null, "Agregado con éxito !!");
            articulos = Articulo.mapear();
            listarArticulos();

        } else {
            JOptionPane.showMessageDialog(null, "Error al insertar");
        }

    }
    
    

    public void limpiar() {
        nombre.setText("");
        descripcion.setText("");
        area_c.setSelectedIndex(-1);
        estado_c.setSelectedIndex(-1);
        categoria_c.setSelectedIndex(-1);
        buscar.setText("");
    }

    public Inventario nuevoInventario(){
        
        Inventario inve = new Inventario();
        
        String nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Inventario: " + LocalDate.now());
        inve.setNombre(nombre);
        inve.setFecha_inicio(LocalDate.now().toString());
        nombreInventario.setText(nombre.toUpperCase() + " - " + LocalDate.now().getYear());
        titulo.setText("MODO INVENTARIO");
        this.getContentPane().setBackground(new Color(179, 236, 245));
        titulo.setForeground(Color.BLUE);
        deshacer.setVisible(true);
        confirmar.setVisible(true);
        return inve;
    }
    

    public void confirmarInventario(){
       // String nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Inventario: " + LocalDate.now());
        Inventario inve = nuevoInventario();
        inve.setFecha_fin(LocalDate.now().toString());
        
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_articulos = new javax.swing.JTable();
        datosArticulo = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        descripcion = new javax.swing.JTextArea();
        area_c = new javax.swing.JComboBox<>();
        categoria_c = new javax.swing.JComboBox<>();
        estado_c = new javax.swing.JComboBox<>();
        agregar = new javax.swing.JButton();
        actualizar = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        buscar = new javax.swing.JTextField();
        cb_filro = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        inventario = new javax.swing.JButton();
        c_inventarios = new javax.swing.JComboBox<>();
        confirmar = new javax.swing.JButton();
        deshacer = new javax.swing.JButton();
        nombreInventario = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        ta_obser = new javax.swing.JTextArea();
        ex = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        titulo.setFont(new java.awt.Font("Raleway", 1, 32)); // NOI18N
        titulo.setText("Registro de Artículos e Inventarios");
        getContentPane().add(titulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 600, -1));

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        t_articulos.setFont(new java.awt.Font("Raleway", 0, 12)); // NOI18N
        t_articulos.setModel(new javax.swing.table.DefaultTableModel(
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
        t_articulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_articulosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_articulos);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 1244, 179));

        datosArticulo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(102, 102, 102), new java.awt.Color(204, 255, 255), null));

        jLabel2.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel2.setText("Nombre");

        jLabel3.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel3.setText("Descripcion");

        jLabel4.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel4.setText("Área");

        jLabel5.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel5.setText("Categoría");

        jLabel6.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel6.setText("Estado");

        descripcion.setColumns(20);
        descripcion.setRows(5);
        jScrollPane2.setViewportView(descripcion);

        area_c.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N

        categoria_c.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N

        estado_c.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N

        agregar.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        agregar.setForeground(new java.awt.Color(255, 255, 255));
        agregar.setText("Agregar");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        actualizar.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        actualizar.setForeground(new java.awt.Color(255, 255, 255));
        actualizar.setText("Actualizar");
        actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarActionPerformed(evt);
            }
        });

        delete.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        delete.setForeground(new java.awt.Color(255, 255, 255));
        delete.setText("Eliminar");

        javax.swing.GroupLayout datosArticuloLayout = new javax.swing.GroupLayout(datosArticulo);
        datosArticulo.setLayout(datosArticuloLayout);
        datosArticuloLayout.setHorizontalGroup(
            datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datosArticuloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(datosArticuloLayout.createSequentialGroup()
                        .addGap(0, 50, Short.MAX_VALUE)
                        .addComponent(agregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(actualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delete))
                    .addGroup(datosArticuloLayout.createSequentialGroup()
                        .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(area_c, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(categoria_c, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(estado_c, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        datosArticuloLayout.setVerticalGroup(
            datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datosArticuloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(area_c, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(datosArticuloLayout.createSequentialGroup()
                        .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(categoria_c, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(estado_c, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(agregar)
                            .addComponent(actualizar)
                            .addComponent(delete)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        getContentPane().add(datosArticulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 690, 260));

        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                buscarKeyTyped(evt);
            }
        });
        getContentPane().add(buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 400, 444, -1));

        cb_filro.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        cb_filro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "Descripcion", "Area", "Estado", "Categoria" }));
        getContentPane().add(cb_filro, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 400, 120, -1));

        jLabel1.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel1.setText("Buscar por:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, -1, -1));

        jLabel7.setFont(new java.awt.Font("Raleway", 1, 18)); // NOI18N
        jLabel7.setText("Datos ");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 255, 153), null));

        inventario.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        inventario.setForeground(new java.awt.Color(255, 255, 255));
        inventario.setText("Nuevo Inventario");
        inventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventarioActionPerformed(evt);
            }
        });

        confirmar.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        confirmar.setForeground(new java.awt.Color(255, 255, 255));
        confirmar.setText("Finalizar");
        confirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmarActionPerformed(evt);
            }
        });

        deshacer.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        deshacer.setForeground(new java.awt.Color(255, 255, 255));
        deshacer.setText("Cancelar");
        deshacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deshacerActionPerformed(evt);
            }
        });

        nombreInventario.setFont(new java.awt.Font("Raleway", 1, 24)); // NOI18N

        jButton1.setText("Editar Nombre");

        ta_obser.setColumns(20);
        ta_obser.setRows(5);
        jScrollPane3.setViewportView(ta_obser);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(deshacer, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(confirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(16, 16, 16))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(c_inventarios, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(inventario, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(c_inventarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inventario)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(nombreInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmar)
                    .addComponent(deshacer))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 110, 520, 260));

        ex.setText("Exportar");
        ex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exActionPerformed(evt);
            }
        });
        getContentPane().add(ex, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 390, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventarioActionPerformed
       nuevoInventario();

    }//GEN-LAST:event_inventarioActionPerformed

    private void buscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyTyped
        trsfiltro = new TableRowSorter(t_articulos.getModel());
        t_articulos.setRowSorter(trsfiltro);
    }//GEN-LAST:event_buscarKeyTyped

    private void actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarActionPerformed
        pre_actualizar();
    }//GEN-LAST:event_actualizarActionPerformed

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void t_articulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_articulosMouseClicked
        seleccion();
    }//GEN-LAST:event_t_articulosMouseClicked

    private void confirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmarActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Esta seguro de guardar todos los cambios", "Confirmar", 0) == 0) {
            registrarInventario();
        }
    }//GEN-LAST:event_confirmarActionPerformed

    private void deshacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deshacerActionPerformed
        desahcer();
    }//GEN-LAST:event_deshacerActionPerformed

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        insertar_articulo();
    }//GEN-LAST:event_agregarActionPerformed

    private void exActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exActionPerformed
        if (t_articulos.getRowCount() > 0) {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel", "xls");
            chooser.setFileFilter(filter);
            chooser.setDialogTitle("Guardar archivo");
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                List tb = new ArrayList();
                List nom = new ArrayList();
                tb.add(t_articulos);
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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InventarioFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InventarioFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InventarioFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InventarioFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InventarioFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar;
    private javax.swing.JButton agregar;
    private javax.swing.JComboBox<String> area_c;
    private javax.swing.JTextField buscar;
    private javax.swing.JComboBox<String> c_inventarios;
    private javax.swing.JComboBox<String> categoria_c;
    private javax.swing.JComboBox<String> cb_filro;
    private javax.swing.JButton confirmar;
    private javax.swing.JPanel datosArticulo;
    private javax.swing.JButton delete;
    private javax.swing.JTextArea descripcion;
    private javax.swing.JButton deshacer;
    private javax.swing.JComboBox<String> estado_c;
    private javax.swing.JButton ex;
    private javax.swing.JButton inventario;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField nombre;
    private javax.swing.JLabel nombreInventario;
    private javax.swing.JTable t_articulos;
    private javax.swing.JTextArea ta_obser;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
