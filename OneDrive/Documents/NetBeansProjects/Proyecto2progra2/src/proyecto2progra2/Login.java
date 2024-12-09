package proyecto2progra2;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login extends JFrame {

    private JPanel panel;
    private JLabel Titulo, userText, userPass;
    private JButton botonSubmit, botonAccount;
    private JTextField Username, Password;

    public Login() {
        this.setSize(1000, 700);
        setLocationRelativeTo(null);
        initializeComponents();
        initializeBottons();
    }

    private void initializeComponents() {
        //panel
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        this.getContentPane().add(panel);

        //labels
        Titulo = new JLabel();
        Titulo.setText("Login");
        Titulo.setBounds(430, 50, 200, 50);
        Titulo.setFont(new Font("arial", Font.BOLD, 40));
        panel.add(Titulo);

        userText = new JLabel();
        userText.setText("Username");
        userText.setBounds(430, 200, 200, 50);
        userText.setFont(new Font("arial", Font.BOLD, 20));
        panel.add(userText);

        userPass = new JLabel();
        userPass.setText("Password");
        userPass.setBounds(430, 350, 200, 50);
        userPass.setFont(new Font("arial", Font.BOLD, 20));
        panel.add(userPass);

        //TextFields
        Username = new JTextField();
        Username.setBounds(340, 250, 300, 40);
        panel.add(Username);

        Password = new JTextField();
        Password.setBounds(340, 400, 300, 40);
        panel.add(Password);
    }

    private void initializeBottons() {
        botonSubmit = new JButton("Submit");
        botonSubmit.setBounds(410, 500, 150, 40);
        botonSubmit.setFont(new Font("Arial", Font.PLAIN, 20));
        botonSubmit.setBackground(Color.LIGHT_GRAY);

        botonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = Username.getText();
                String password = Password.getText();

                if (!username.isEmpty() && !password.isEmpty()) {
                    try {
                        Usuario usuario = new Usuario(username, password);

                        if (usuario.carpetaExistente()) {
                            if (usuario.validarContraseña()) {
                                // Preguntar si desea ser admin
                                int option = JOptionPane.showConfirmDialog(null,
                                        "¿Quieres ser administrador?", "Pregunta",
                                        JOptionPane.YES_NO_OPTION);

                                if (option == JOptionPane.YES_OPTION) {
                                    usuario.setAdmin(true); // Establece como administrador
                                    JOptionPane.showMessageDialog(null, "¡Bienvenido, " + username + " como administrador!");
                                } else {
                                    usuario.setAdmin(false); // Establece como usuario normal
                                    JOptionPane.showMessageDialog(null, "¡Bienvenido de nuevo, " + username + "!");
                                }

                                // Cerrar la ventana de login y redirigir a HomeMenu
                                dispose(); 
                                HomeMenu hm = new HomeMenu(usuario);
                                hm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                hm.setVisible(true);

                            } else {
                                JOptionPane.showMessageDialog(null, "Contraseña incorrecta.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No existe una cuenta con este nombre de usuario.");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor ingresa un nombre de usuario y contraseña.");
                }
            }
        });

        panel.add(botonSubmit);

        botonAccount = new JButton("CreateAccount");
        botonAccount.setBounds(10, 610, 200, 40);
        botonAccount.setFont(new Font("Arial", Font.PLAIN, 15));
        botonAccount.setBackground(Color.LIGHT_GRAY);

        botonAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("Please enter your username:");
                if (username != null && !username.isEmpty()) {
                    String password = JOptionPane.showInputDialog("Enter Password:");

                    if (password != null && !password.isEmpty()) {
                        try {
                            Usuario usuario = new Usuario(username, password);
                            if (usuario.createArchives()) {
                                JOptionPane.showMessageDialog(null, "Cuenta creada exitosamente.");
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al crear la cuenta.");
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "Error al crear la cuenta.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "La contraseña no puede estar vacía.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El nombre de usuario no puede estar vacío.");
                }
            }
        });

        panel.add(botonAccount);
    }
}