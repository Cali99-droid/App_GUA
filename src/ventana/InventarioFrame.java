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

        for (int i = 0; i < inventarios.size(); i++) {
            this.c_inventarios.addItem(inventarios.get(i).getNombre());
        }

        c_inventarios.setSelectedIndex(-1);

    }

    //metodo auxiliar de prueba 
    public void imprimirArticulos() {

        int index = this.c_inventarios.getSelectedIndex();
        if (index > -1) {
            Inventario inv = inventarios.get(index);
            articulos.clear();
            articulos = inv.getArticulos();
            this.ta_obser.setText(inv.getObservaciones());
            this.nomInventario.setText(inv.getNombre());

            listarArticulos();
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
            limpiar();
            listarArticulos();

        } else {
            JOptionPane.showMessageDialog(null, "Debe Seleccionar un articulo");

        }

    }

    public void volverEstado() {
        nomInventario.setText("");
        titulo.setText("INVENTARIO");
        this.getContentPane().setBackground(new Color(255, 255, 255));
        titulo.setForeground(Color.BLACK);
        deshacer.setVisible(false);
        confirmar.setVisible(false);
        nomInventario.setEnabled(false);
        ta_obser.setEnabled(false);
        labInven.setVisible(false);
        labObser.setVisible(false);
        inventario.setEnabled(true);
        this.c_inventarios.setEnabled(true);

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
                titulo.setText("MODO INVENTARIO");
                this.getContentPane().setBackground(new Color(179, 236, 245));
                titulo.setForeground(Color.BLUE);
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
        inve.setFecha_inicio(LocalDate.now().toString());
        inve.setFecha_fin(LocalDate.now().toString());
        inve.setObservaciones(ta_obser.getText());
        inve.setIdusuario(1);
        inve.setArticulos(this.articulos);
        inve.insertInvenArt();
        volverEstado();

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
        labTitulo = new javax.swing.JPanel();
        titulo = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
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

        labTitulo.setBackground(new java.awt.Color(0, 99, 174));

        titulo.setFont(new java.awt.Font("Raleway", 1, 32)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setText("Registro de Artículos e Inventarios");

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/icos/back.png"))); // NOI18N
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout labTituloLayout = new javax.swing.GroupLayout(labTitulo);
        labTitulo.setLayout(labTituloLayout);
        labTituloLayout.setHorizontalGroup(
            labTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, labTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(277, 277, 277)
                .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        labTituloLayout.setVerticalGroup(
            labTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(labTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, labTituloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titulo)
                .addGap(29, 29, 29))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        c_inventarios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));
        c_inventarios.setToolTipText("seleccione");
        c_inventarios.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                c_inventariosItemStateChanged(evt);
            }
        });

        ver.setText("Ver Inventarios");
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
                .addComponent(c_inventarios, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(ver)
                .addGap(28, 28, 28))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(c_inventarios, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ver))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        datosArticulo.setBackground(new java.awt.Color(255, 255, 255));
        datosArticulo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Artículo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Raleway", 1, 18), new java.awt.Color(0, 99, 174))); // NOI18N

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
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(datosArticuloLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
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
                .addGroup(datosArticuloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                            .addComponent(agregar)
                            .addComponent(actualizar)
                            .addComponent(delete)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inventrario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Raleway", 1, 18), new java.awt.Color(0, 99, 174))); // NOI18N

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

        ta_obser.setColumns(20);
        ta_obser.setRows(5);
        jScrollPane3.setViewportView(ta_obser);

        labObser.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        labObser.setText("Observaciones:");

        labInven.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        labInven.setText("Nombre");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labObser)
                    .addComponent(labInven))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 93, Short.MAX_VALUE)
                        .addComponent(deshacer, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(confirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nomInventario)
                    .addComponent(jScrollPane3))
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(inventario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labInven)
                    .addComponent(nomInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labObser)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmar)
                    .addComponent(deshacer))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                buscarKeyTyped(evt);
            }
        });

        cb_filro.setFont(new java.awt.Font("Raleway", 1, 12)); // NOI18N
        cb_filro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nombre", "Descripcion", "Area", "Estado", "Categoria" }));

        jLabel1.setFont(new java.awt.Font("Raleway", 1, 14)); // NOI18N
        jLabel1.setText("Buscar por:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(cb_filro, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_filro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ex.setFont(new java.awt.Font("Raleway", 1, 11)); // NOI18N
        ex.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/icos/excel.png"))); // NOI18N
        ex.setText("Exportar");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(ex, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(17, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(labTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
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

    private void verActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verActionPerformed
        imprimirArticulos();
    }//GEN-LAST:event_verActionPerformed

    private void c_inventariosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_c_inventariosItemStateChanged

    }//GEN-LAST:event_c_inventariosItemStateChanged

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        eliminar();
    }//GEN-LAST:event_deleteActionPerformed

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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labInven;
    private javax.swing.JLabel labObser;
    private javax.swing.JPanel labTitulo;
    private javax.swing.JTextField nomInventario;
    private javax.swing.JTextField nombre;
    private javax.swing.JTable t_articulos;
    private javax.swing.JTextArea ta_obser;
    private javax.swing.JLabel titulo;
    private javax.swing.JButton ver;
    // End of variables declaration//GEN-END:variables
}