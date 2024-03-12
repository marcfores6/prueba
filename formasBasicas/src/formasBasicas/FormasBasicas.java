package formasBasicas;
//MARC FORES
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FormasBasicas extends JPanel {

    private List<Forma> formas = new ArrayList<>();
    private Stack<Forma> formasEliminadas = new Stack<>();
    private Forma formaActual;

    private Color[] colores = { Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW, Color.CYAN, Color.GREEN, Color.ORANGE,
            new Color(128, 0, 128) };

    private Color colorSeleccionado = Color.BLACK;
    private TipoForma formaSeleccionada = TipoForma.LINEA;
    private boolean rellenarForma = false;

    public enum Colores {
        NEGRO, ROJO, AZUL, AMARILLO, CYAN, VERDE, NARANJA, MORADO;
    }

    public enum TipoForma {
        LINEA, CUADRADO, OVALO;
    }

    public FormasBasicas() {
        setLayout(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getY() > 40) {
                    formasEliminadas.clear(); // Limpiar la pantalla al dibujar nuevas formas
                    formaActual = crearForma();
                    formaActual.setColor(colorSeleccionado);
                    formaActual.setRelleno(rellenarForma);
                    formaActual.setPuntoInicio(e.getX(), e.getY());
                    formaActual.setPuntoFin(e.getX(), e.getY());
                    formas.add(formaActual);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (formaActual != null && e.getY() > 40) {
                    formaActual.setPuntoFin(e.getX(), e.getY());
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (formaActual != null) {
                    int y = Math.max(e.getY(), 50); // Asegurar que sea al menos 50
                    formaActual.setPuntoFin(e.getX(), y);
                    repaint();
                }
            }
        });

        JLabel lblCoordenadasXY = new JLabel("");
        lblCoordenadasXY.setBounds(0, 645, 270, 15);
        add(lblCoordenadasXY);

        JButton btnDeshacer = new JButton("Deshacer");
        btnDeshacer.setBounds(28, 12, 117, 25);
        btnDeshacer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deshacer();
            }
        });
        add(btnDeshacer);

        JButton btnRehacer = new JButton("Rehacer");
        btnRehacer.setBounds(177, 12, 117, 25);
        btnRehacer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rehacer();
            }
        });
        add(btnRehacer);

        JButton btnBorrar = new JButton("Borrar");
        btnBorrar.setBounds(332, 12, 117, 25);
        btnBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                borrarTodo();
            }
        });
        add(btnBorrar);

        JComboBox<Colores> comboBoxColores = new JComboBox<>(Colores.values());
        comboBoxColores.addItemListener(e -> {
            colorSeleccionado = colores[comboBoxColores.getSelectedIndex()];
            repaint();
        });
        comboBoxColores.setBounds(484, 12, 97, 24);
        add(comboBoxColores);

        JComboBox<TipoForma> comboBoxFiguras = new JComboBox<>(TipoForma.values());
        comboBoxFiguras.addItemListener(e -> {
            formaSeleccionada = (TipoForma) comboBoxFiguras.getSelectedItem();
            repaint();
        });
        comboBoxFiguras.setBounds(606, 12, 117, 24);
        add(comboBoxFiguras);

        JCheckBox chckbxRelleno = new JCheckBox("Relleno");
        chckbxRelleno.setBounds(737, 13, 129, 23);
        chckbxRelleno.addItemListener(e -> {
            rellenarForma = chckbxRelleno.isSelected();
            repaint();
        });
        add(chckbxRelleno);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent evento) {
                lblCoordenadasXY
                        .setText(String.format("Las coordenadas son X: %d   Y: %d", evento.getX(), evento.getY()));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        for (Forma forma : formas) {
            g2d.setColor(forma.getColor());
            if (forma.isRelleno()) {
                g2d.fill(forma.getForma());
            } else {
                g2d.draw(forma.getForma());
            }
        }
    }

    private Forma crearForma() {
        switch (formaSeleccionada) {
            case LINEA:
                return new Linea();
            case CUADRADO:
                return new Cuadrado();
            case OVALO:
                return new Ovalo();
            default:
                return new Linea();
        }
    }

    private interface Forma {
        void setColor(Color color);

        void setRelleno(boolean relleno);

        void setPuntoInicio(int x, int y);

        void setPuntoFin(int x, int y);

        Color getColor();

        boolean isRelleno();

        java.awt.Shape getForma();
    }

    private class Linea implements Forma {
        private Punto puntoInicio;
        private Punto puntoFin;
        private Color color;

        public Linea() {
            puntoInicio = new Punto();
            puntoFin = new Punto();
        }

        @Override
        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public void setRelleno(boolean relleno) {
            // Las líneas no se rellenan
        }

        @Override
        public void setPuntoInicio(int x, int y) {
            puntoInicio.setX(x);
            puntoInicio.setY(y);
        }

        @Override
        public void setPuntoFin(int x, int y) {
            puntoFin.setX(x);
            puntoFin.setY(y);
        }

        @Override
        public Color getColor() {
            return color;
        }

        @Override
        public boolean isRelleno() {
            return false;
        }

        @Override
        public java.awt.Shape getForma() {
            return new java.awt.geom.Line2D.Double(puntoInicio.getX(), puntoInicio.getY(), puntoFin.getX(), puntoFin.getY());
        }
    }

    private class Cuadrado implements Forma {
        private Punto puntoInicio;
        private Punto puntoFin;
        private Color color;
        private boolean relleno;

        public Cuadrado() {
            puntoInicio = new Punto();
            puntoFin = new Punto();
        }

        @Override
        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public void setRelleno(boolean relleno) {
            this.relleno = relleno;
        }

        @Override
        public void setPuntoInicio(int x, int y) {
            puntoInicio.setX(x);
            puntoInicio.setY(y);
        }

        @Override
        public void setPuntoFin(int x, int y) {
            puntoFin.setX(x);
            puntoFin.setY(y);
        }

        @Override
        public Color getColor() {
            return color;
        }

        @Override
        public boolean isRelleno() {
            return relleno;
        }

        @Override
        public java.awt.Shape getForma() {
            int ancho = Math.abs(puntoFin.getX() - puntoInicio.getX());
            int alto = Math.abs(puntoFin.getY() - puntoInicio.getY());
            int x = Math.min(puntoInicio.getX(), puntoFin.getX());
            int y = Math.min(puntoInicio.getY(), puntoFin.getY());

            return relleno ? new java.awt.geom.Rectangle2D.Double(x, y, ancho, alto): new java.awt.geom.Rectangle2D.Double(x, y, ancho, alto);
        }
    }

    private class Ovalo implements Forma {
        private Punto puntoInicio;
        private Punto puntoFin;
        private Color color;
        private boolean relleno;

        public Ovalo() {
            puntoInicio = new Punto();
            puntoFin = new Punto();
        }

        @Override
        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public void setRelleno(boolean relleno) {
            this.relleno = relleno;
        }

        @Override
        public void setPuntoInicio(int x, int y) {
            puntoInicio.setX(x);
            puntoInicio.setY(y);
        }

        @Override
        public void setPuntoFin(int x, int y) {
            puntoFin.setX(x);
            puntoFin.setY(y);
        }

        @Override
        public Color getColor() {
            return color;
        }

        @Override
        public boolean isRelleno() {
            return relleno;
        }

        @Override
        public java.awt.Shape getForma() {
            int ancho = Math.abs(puntoFin.getX() - puntoInicio.getX());
            int alto = Math.abs(puntoFin.getY() - puntoInicio.getY());
            int x = Math.min(puntoInicio.getX(), puntoFin.getX());
            int y = Math.min(puntoInicio.getY(), puntoFin.getY());

            return relleno ? new java.awt.geom.Ellipse2D.Double(x, y, ancho, alto): new java.awt.geom.Ellipse2D.Double(x, y, ancho, alto);
        }
    }

    private static class Punto {
        private int x;
        private int y;

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private void deshacer() {
        if (!formas.isEmpty()) {
            Forma formaDeshacer = formas.remove(formas.size() - 1);
            formasEliminadas.push(formaDeshacer);
            repaint();
        }
    }

    private void rehacer() {
        if (!formasEliminadas.isEmpty()) {
            Forma formaRehacer = formasEliminadas.pop();
            formas.add(formaRehacer);
            repaint();
        }
    }

    private void borrarTodo() {
        formas.clear();
        formasEliminadas.clear();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Formas Básicas");
            FormasBasicas objeto = new FormasBasicas();
            ventana.getContentPane().add(objeto);
            ventana.setSize(1000, 700);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
            ventana.setResizable(false);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}

