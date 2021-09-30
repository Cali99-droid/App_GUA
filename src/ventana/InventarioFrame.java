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
import javax.swing.ImageIcon;
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

    ImageIcon iconBack = new ImageIcon("src/src/icos/back.png");

    private ArrayList<Articulo> articulos = Articulo.mapear();
    ArrayList<Inventario> inventarios = Inventario.all();
    Controlador cont = new Controlador();
    private TableRowSorter trsfiltro;
    String filtro;

    DefaultTableModel modelo = new DefaultTableModel();

    public InventarioFrame() {
        initComponents();
        // inventarios.get(1).validarCantidad();
        this.setLocationRelativeTo(null);
        //this.setExtendedState(MAXIMIZED_BOTH);
        //dar colores
        this.getContentPane().setBackground(new Color(255, 255, 255));
        //this.datosArticulo.setBackground(new Color(250, 248, 255));
//        categoria_c.setBackground(new Color(101, 251, 210));
//        estado_c.setBackground(new Color(101, 251, 210));
//        area_c.setBackground(new Color(101, 251, 210));
//        agregar.setBackground(new Color(0, 139, 105));
//        actualizar.setBackground(new Color(255, 171, 20));
//        delete.setBackground(new Color(163, 104, 84));
//        inventario.setBackground(new Color(0, 206, 215));
//        confirmar.setBackground(new Color(0, 139, 105));
//        deshacer.setBackground(new Color(163, 104, 84));

        //cargar combos
        cont.LlenarCombo(categoria_c, "SELECT * FROM categoria", 2);
        cont.LlenarCombo(estado_c, "SELECT * FROM estado", 2);
        cont.LlenarCombo(area_c, "SELECT * FROM area", 2);
        llenar_combo_inv();

        //inicializar botones
        deshacer.setVisible(false);
        confirmar.setVisible(false);
        nomInventario.setEnabled(false);
        ta_obser.setEnabled(false);
        initBotones(1);
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

    public void initBotones(int acti) {

        if (acti == 1) {
            actualizar.setEnabled(false);
            delete.setEnabled(false);
        } else {
            actualizar.setEnabled(true);
            delete.setEnabled(true);
        }

    }

    public void llenar_combo_inv() {
        this.c_inventarios.removeAllItems();
        this.c_inventarios.addItem("--Todos--");
        for (int i = 0; i < inventarios.size(); i++) {
            this.c_inventarios.addItem(inventarios.get(i).getNombre().toUpperCase());
        }

        //c_inventarios.setSelectedIndex(-1);
    }

    public void imprimirArticulos() {

        int index = this.c_inventarios.getSelectedIndex();
        if (index >= 1) {

            Inventario inv = inventarios.get(index - 1);
            articulos.clear();
            articulos = inv.getArticulos();
            this.ta_obser.setText(inv.getObservaciones());
            this.nomInventario.setText(inv.getNombre());
            this.agregar.setEnabled(false);
            listarArticulos();
        } else {
            this.articulos = Articulo.mapear();
            listarArticulos();
            this.agregar.setEnabled(true);
        }
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

//no usado
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

            //articulos.add(index, art);
            limpiar();
            listarArticulos();

        } else {
            JOptionPane.showMessageDialog(null, "Debe Seleccionar un articulo");

        }

    }

    public void volverEstado() {
        nomInventario.setText("");
        lbCabecera.setText("Registro de articulos e Inventario");
        this.getContentPane().setBackground(new Color(255, 255, 255));
        PnCabecera.setBackground(new Color(0, 99, 174));
        deshacer.setVisible(false);
        confirmar.setVisible(false);
        nomInventario.setEnabled(false);
        ta_obser.setEnabled(false);
        labInven.setVisible(false);
        labObser.setVisible(false);
        inventario.setEnabled(true);
        this.c_inventarios.setEnabled(true);
        this.nombre.setEditable(true);
        this.descripcion.setEditable(true);
        this.categoria_c.setEditable(true);
        inventarios = Inventario.all();
         llenar_combo_inv();
         articulos = Articulo.mapear();
         listarArticulos();

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
                area_c.getSelectedIndex(),
                estado_c.getSelectedIndex(),
                categoria_c.getSelectedIndex());

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
        ta_obser.setText("");
    }

    public void nuevoInventario() {
        if (Inventario.validarCantidad()) {
            this.c_inventarios.setSelectedIndex(-1);
            this.articulos = Articulo.mapear();
            limpiar();
            listarArticulos();
            String nombre = JOptionPane.showInputDialog(this, "Ingrese el Nombre del Inventario: " + LocalDate.now(), "Entrada", 3);
            if (nombre == null || nombre.equals("")) {
                JOptionPane.showMessageDialog(this, "Debe asignar un nombre al inventario !!", "Mensaje", 2);
            } else {
                initBotones(2);
                nomInventario.setText(nombre.toUpperCase() + " - " + LocalDate.now().getYear());
                lbCabecera.setText("Modo Inventario Activado");
                this.nombre.setEditable(false);
                this.descripcion.setEditable(false);
                this.categoria_c.setEnabled(false);
                PnCabecera.setBackground(new Color(0, 175, 196));
                deshacer.setVisible(true);
                confirmar.setVisible(true);
                nomInventario.setEnabled(true);
                ta_obser.setEnabled(true);
                labInven.setVisible(true);
                labObser.setVisible(true);
                inventario.setEnabled(false);
                this.c_inventarios.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Excedió el número de Inventarios por año", "Mensaje", 2);
        }

    }

    public void confirmarInventario() {

        // String nombre = JOptionPane.showInputDialog("Ingrese el Nombre del Inventario: " + LocalDate.now());
        Inventario inve = new Inventario();
        inve.setNombre(nomInventario.getText());
        inve.setFecha_inicio("2010-05-12");
        inve.setFecha_fin("2010-05-12");
        inve.setObservaciones(ta_obser.getText());
        inve.setIdusuario(1);
        inve.setArticulos(this.articulos);

        inve.insertInvenArt();
        
        volverEstado();
        limpiar();

    }

    public void eliminar() {
        if (t_articulos.getSelectedRow() > -1) {
            int index = t_articulos.getSelectedRow();
            articulos.remove(index);
            limpiar();
            listarArticulos();
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un artículo !!!");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        t_articulos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        c_inventarios = new javax.swing.JComboBox<>();
        ver = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
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
        jPanel1 = new javax.swing.JPanel();
        inventario = new javax.swing.JButton();
        confirmar = new javax.swing.JButton();
        deshacer = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        ta_obser = new javax.swing.JTextArea();
        labObser = new javax.swing.JLabel();
        labInven = new javax.swing.JLabel();
        nomInventario = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        buscar = new javax.swing.JTextField();
        cb_filro = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        ex = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        PnCabecera = new javax.swing.JPanel();
        lbCabecera = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

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

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        c_inventarios.setFont(new java.awt.Font("Raleway", 0, 12)); // NOI18N
        c_inventarios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));
        c_inventarios.setToolTipText("seleccione");
        c_inventarios.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                c_inventariosItemStateChanged(evt);
            }
        });

        ver.setBackground(new java.awt.Color(0, 99, 174));
        ver.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        ver.setForeground(new java.awt.Color(255, 255, 255));
        ver.setText("Ver Inventario");
        ver.setBorderPainted(false);
        ver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(c_inventarios, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ver)
                .addGap(28, 28, 28))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ver, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(c_inventarios, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        datosArticulo.setBackground(new java.awt.Color(255, 255, 255));
        datosArticulo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Artículo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Raleway", 1, 18), new java.awt.Color(64, 71, 86))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(64, 71, 86));
        jLabel2.setText("Nombre");

        jLabel3.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(64, 71, 86));
        jLabel3.setText("Descripcion");

        jLabel4.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(64, 71, 86));
        jLabel4.setText("Área");

        jLabel5.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(64, 71, 86));
        jLabel5.setText("Categoría");

        jLabel6.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(64, 71, 86));
        jLabel6.setText("Estado");

        descripcion.setColumns(20);
        descripcion.setRows(5);
        jScrollPane2.setViewportView(descripcion);

        area_c.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N

        categoria_c.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N

        estado_c.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N

        agregar.setBackground(new java.awt.Color(0, 99, 174));
        agregar.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        agregar.setForeground(new java.awt.Color(255, 255, 255));
        agregar.setText("Agregar");
        agregar.setBorderPainted(false);
        agregar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        actualizar.setBackground(new java.awt.Color(217, 162, 27));
        actualizar.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        actualizar.setForeground(new java.awt.Color(255, 255, 255));
        actualizar.setText("Actualizar");
        actualizar.setBorderPainted(false);
        actualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarActionPerformed(evt);
            }
        });

        delete.setBackground(new java.awt.Color(219, 79, 72));
        delete.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        delete.setForeground(new java.awt.Color(255, 255, 255));
        delete.setText("Eliminar");
        delete.setBorderPainted(false);
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout datosArticuloLayout = new javax.swing.GroupLayout(datosArticulo);
        datosArticulo.setLayout(datosArticuloLayout);
        datosArticuloLayout.setHorizontalGroup(
            datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datosArticuloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(datosArticuloLayout.createSequentialGroup()
                        .addGap(0, 42, Short.MAX_VALUE)
                        .addComponent(agregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(actualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delete))
                    .addGroup(datosArticuloLayout.createSequentialGroup()
                        .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel4)
                        .addComponent(area_c, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(datosArticuloLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(datosArticuloLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(datosArticuloLayout.createSequentialGroup()
                        .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(categoria_c, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(estado_c, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inventrario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Raleway", 1, 18), new java.awt.Color(64, 71, 86))); // NOI18N

        inventario.setBackground(new java.awt.Color(0, 99, 174));
        inventario.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        inventario.setForeground(new java.awt.Color(255, 255, 255));
        inventario.setText("Nuevo Inventario");
        inventario.setBorderPainted(false);
        inventario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        inventario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventarioActionPerformed(evt);
            }
        });

        confirmar.setBackground(new java.awt.Color(0, 99, 174));
        confirmar.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        confirmar.setForeground(new java.awt.Color(255, 255, 255));
        confirmar.setText("Finalizar");
        confirmar.setBorderPainted(false);
        confirmar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        confirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmarActionPerformed(evt);
            }
        });

        deshacer.setBackground(new java.awt.Color(219, 79, 72));
        deshacer.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        deshacer.setForeground(new java.awt.Color(255, 255, 255));
        deshacer.setText("Cancelar");
        deshacer.setBorderPainted(false);
        deshacer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        deshacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deshacerActionPerformed(evt);
            }
        });

        ta_obser.setColumns(20);
        ta_obser.setRows(5);
        jScrollPane3.setViewportView(ta_obser);

        labObser.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        labObser.setForeground(new java.awt.Color(64, 71, 86));
        labObser.setText("Observaciones");

        labInven.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        labInven.setForeground(new java.awt.Color(64, 71, 86));
        labInven.setText("Nombre");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labObser)
                            .addComponent(labInven))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(inventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nomInventario)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(confirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(deshacer, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(inventario, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labInven))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labObser)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deshacer, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(confirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                buscarKeyTyped(evt);
            }
        });

        cb_filro.setFont(new java.awt.Font("Raleway", 0, 12)); // NOI18N
        cb_filro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "Descripcion", "Area", "Estado", "Categoria" }));

        jLabel1.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(64, 71, 86));
        jLabel1.setText("Buscar por:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_filro, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(382, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(buscar)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cb_filro, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                        .addComponent(jLabel1)))
                .addContainerGap())
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ex, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(datosArticulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(datosArticulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ex)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(64, 71, 86));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/icos/mi.png"))); // NOI18N
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/icos/clos.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        PnCabecera.setBackground(new java.awt.Color(0, 99, 174));

        lbCabecera.setFont(new java.awt.Font("Raleway", 1, 36)); // NOI18N
        lbCabecera.setForeground(new java.awt.Color(255, 255, 255));
        lbCabecera.setText("Registro de artículos e Inventario");

        javax.swing.GroupLayout PnCabeceraLayout = new javax.swing.GroupLayout(PnCabecera);
        PnCabecera.setLayout(PnCabeceraLayout);
        PnCabeceraLayout.setHorizontalGroup(
            PnCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnCabeceraLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, 1251, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        PnCabeceraLayout.setVerticalGroup(
            PnCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbCabecera, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(17, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PnCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(PnCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

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
            confirmarInventario();
        }
    }//GEN-LAST:event_confirmarActionPerformed

    private void deshacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deshacerActionPerformed
        desahcer();
    }//GEN-LAST:event_deshacerActionPerformed

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        if (Controlador.estaVacio(nombre) || Controlador.estaVacioTa(descripcion)) {
            JOptionPane.showMessageDialog(this, "Debe llenar todos los campos", "Mensaje", 2);
        } else {
            if (area_c.getSelectedIndex() < 1 || estado_c.getSelectedIndex() < 1 || categoria_c.getSelectedIndex() < 1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar todos los campos", "Mensaje", 2);
            } else {
                insertar_articulo();
            }
        }
        // 
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

    private void verActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verActionPerformed
        imprimirArticulos();
    }//GEN-LAST:event_verActionPerformed

    private void c_inventariosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_c_inventariosItemStateChanged

    }//GEN-LAST:event_c_inventariosItemStateChanged

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        eliminar();
    }//GEN-LAST:event_deleteActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        new Principal().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

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
    private javax.swing.JPanel PnCabecera;
    private javax.swing.JButton actualizar;
    private javax.swing.JButton agregar;
    private javax.swing.JComboBox<String> area_c;
    private javax.swing.JButton btnBack;
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
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labInven;
    private javax.swing.JLabel labObser;
    private javax.swing.JLabel lbCabecera;
    private javax.swing.JTextField nomInventario;
    private javax.swing.JTextField nombre;
    private javax.swing.JTable t_articulos;
    private javax.swing.JTextArea ta_obser;
    private javax.swing.JButton ver;
    // End of variables declaration//GEN-END:variables
}
