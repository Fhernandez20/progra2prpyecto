package proyecto2progra2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javazoom.jl.player.Player;
import javax.swing.tree.*;

public class HomeMenu extends JFrame {

    private Usuario usuarioActual;
    
    private JTree tree;

    // 1. **Contenedores de paneles:**
    private JPanel panelContainer;
    private JPanel AdminPanel, gameListPanel, gameDetailPanel, gamePanel, musicPanel, adminPanel, chatPanel, profilePanel, addMusicPanel, songDetailPanel;
    private JPanel userGamesPanel,userSongsPanel, songListPanel, addGamePanel;

    // 2. **Botones para Navegación y Funcionalidades Generales:**
    private JButton addgame, gameButton, musicButton, adminButton, chatButton, profileButton, logoutButton;
    private JButton saveSongButton, chooseCoverButton, chooseMP3Button;
    private JButton addSong, playButton, pauseButton, stopButton;
    private JButton deleteButton, chooseImageButton, saveGameButton;

    // 3. **Campos de texto para detalles:**
    private JTextField titleField, artistField, albumField;
    private JTextField titleField2;
    private JTextField genreField, developerField, releaseDateField;

    // 4. **Etiquetas (Labels) para mostrar información:**
    private JLabel coverLabel, mp3Label, songDetailCoverLabel, songDetailInfoLabel;
    private JLabel gameDetailImageLabel, gameDetailInfoLabel;

    // 5. **Archivos y Datos Relacionados con la Música y Juegos:**
    private File mp3File, coverFile;
    private File imageFile;
    private File currentSongFile;  
    // 6. **Reproductor de Música y Estado:**
    private Player player;
    private Thread playerThread;
    private int pausedFrame = 0;
    private boolean isPaused = false; 

    private JScrollPane scrollPane, profileScrollPane;
    private static final String SONGS_DIRECTORY = "C:\\Users\\josue\\OneDrive\\Documents\\NetBeansProjects\\Proyecto2progra2\\songs";
    private static final String GAMES_DIRECTORY = "C:\\Users\\josue\\OneDrive\\Documents\\NetBeansProjects\\Proyecto2progra2\\games";

    public HomeMenu(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;

        this.setSize(1515, 1000);
        setLocationRelativeTo(null);
        initializeComponents();
        cargarCanciones();
        cargarJuegos();
    }

    private void initializeComponents() {
        this.setLayout(null);

        panelContainer = new JPanel(new CardLayout());
        panelContainer.setBounds(200, 0, 1300, 1000);
        this.getContentPane().add(panelContainer);

        gamePanel = new JPanel(new BorderLayout());
        musicPanel = new JPanel(new BorderLayout());
        adminPanel = new JPanel();
        chatPanel = new JPanel();
        profilePanel = new JPanel();
        addMusicPanel = new JPanel();
        addGamePanel = new JPanel();
        userSongsPanel = new JPanel();
userGamesPanel=new JPanel();
        gamePanel.setBackground(Color.GREEN);
        musicPanel.setBackground(Color.RED);
        adminPanel.setBackground(Color.YELLOW);
        chatPanel.setBackground(Color.PINK);
        profilePanel.setBackground(Color.CYAN);
        addMusicPanel.setBackground(Color.LIGHT_GRAY);

        panelContainer.add(gamePanel, "Game");
        panelContainer.add(musicPanel, "Music");
        panelContainer.add(adminPanel, "Admin");
        panelContainer.add(chatPanel, "Chat");
        panelContainer.add(profilePanel, "Profile");
        panelContainer.add(addMusicPanel, "AddMusic");
        panelContainer.add(addGamePanel, "AddGame");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, 0, 200, 1000);

        gameButton = new JButton("Game");
        musicButton = new JButton("Music");
        adminButton = new JButton("Admin");
        chatButton = new JButton("Chat");
        profileButton = new JButton("Profile");
        logoutButton = new JButton("Logout");

        gameButton.setBounds(25, 100, 150, 40);
        musicButton.setBounds(25, 200, 150, 40);
        adminButton.setBounds(25, 300, 150, 40);
        chatButton.setBounds(25, 400, 150, 40);
        profileButton.setBounds(25, 500, 150, 40);
        logoutButton.setBounds(25, 600, 150, 40);
        if (usuarioActual.isAdmin()) {
            adminButton.setVisible(true);

        } else {
            adminButton.setVisible(false);
        }

        buttonPanel.add(gameButton);
        buttonPanel.add(musicButton);
        buttonPanel.add(adminButton);
        buttonPanel.add(chatButton);
        buttonPanel.add(profileButton);
        buttonPanel.add(logoutButton);

        deleteButton = new JButton("Eliminar");
        deleteButton.setBounds(25, 350, 150, 40);
        deleteButton.setEnabled(false); 
        buttonPanel.add(deleteButton);

        this.getContentPane().add(buttonPanel);

        musicButton.addActionListener(e -> showPanel("Music"));
        musicButton.addActionListener(e -> {
            showPanel("Music");
            cargarCanciones();
        });
        gameButton.addActionListener(e -> {
            showPanel("Game");  
            cargarJuegos();
        });
        adminButton.addActionListener(e -> showPanel("Admin"));
        adminButton.addActionListener(e -> {
            showPanel("Admin");
            actualizarTree();
        });
        chatButton.addActionListener(e -> showPanel("Chat"));
        profileButton.addActionListener(e -> {
            showPanel("Profile");
            cargarCancionesUsuario();
            cargarJuegosUsuario();
        });
        logoutButton.addActionListener(e -> logout());

        configurarMusicPanel();
        configurarAddMusicPanel();
        configurarGamePanel();
        configurarAddGamePanel();
        configurarAdminPanel();
    }

    private void configurarMusicPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout()); 
        leftPanel.setPreferredSize(new Dimension(600, 400));  
        leftPanel.setBackground(Color.RED); 

        musicPanel.add(leftPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 1000)); 
        rightPanel.setBackground(Color.LIGHT_GRAY);

        songListPanel = new JPanel();
        songListPanel.setLayout(new BoxLayout(songListPanel, BoxLayout.Y_AXIS));
        songListPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(songListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        rightPanel.add(scrollPane, BorderLayout.CENTER);

        musicPanel.add(rightPanel, BorderLayout.EAST);

        songDetailPanel = new JPanel();
        songDetailPanel.setLayout(null);  
        songDetailPanel.setBackground(Color.RED); 

        songDetailCoverLabel = new JLabel();
        songDetailCoverLabel.setBounds(360, 180, 300, 300);
        songDetailPanel.add(songDetailCoverLabel);

        songDetailInfoLabel = new JLabel();
        songDetailInfoLabel.setBounds(360, 500, 400, 100);
        songDetailPanel.add(songDetailInfoLabel);

        playButton = new JButton("Play");
        playButton.setBounds(360, 630, 80, 40);
        songDetailPanel.add(playButton);

        pauseButton = new JButton("Pause");
        pauseButton.setBounds(460, 630, 80, 40);
        pauseButton.addActionListener(e -> pauseSong());
        songDetailPanel.add(pauseButton);

        stopButton = new JButton("Stop");
        stopButton.setBounds(560, 630, 80, 40);
        stopButton.addActionListener(e -> stopSong());
        songDetailPanel.add(stopButton);

        addSong = new JButton("AddSong");
        addSong.addActionListener(e -> showPanel("AddMusic")); 
        addSong.setBounds(450, 20, 100, 50);  
        if(usuarioActual.isAdmin()){
            addSong.setVisible(true);
        }
        else{
            addSong.setVisible(false);
        }
        songDetailPanel.add(addSong);

        musicPanel.add(songDetailPanel, BorderLayout.CENTER);

        musicPanel.revalidate();
        musicPanel.repaint();

    }

    private void configurarAdminPanel() {
        File rootDirectory = new File("C:\\Users\\josue\\OneDrive\\Documents\\NetBeansProjects\\Proyecto2progra2");

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootDirectory.getName());
        tree = new JTree(createTreeNodes(rootDirectory));

        tree.setRootVisible(true);
        tree.setSelectionRow(0);

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                File selectedFile = new File(rootDirectory, selectedNode.getUserObject().toString());
                deleteButton.setEnabled(selectedFile.exists());
            }
        });

        JScrollPane treeView = new JScrollPane(tree);
        adminPanel.setLayout(new BorderLayout());
        adminPanel.add(treeView, BorderLayout.CENTER);

        deleteButton.addActionListener(e -> eliminarArchivoOCarpeta());
    }

    private DefaultMutableTreeNode createTreeNodes(File directory) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(directory.getName());

        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(createTreeNodes(file)); 
                    node.add(new DefaultMutableTreeNode(file.getName()));
                }
            }
        }

        return node;
    }

    private void eliminarArchivoOCarpeta() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            String fileName = selectedNode.getUserObject().toString();
            File selectedFile = new File("C:\\Users\\josue\\OneDrive\\Documents\\NetBeansProjects\\Proyecto2progra2", fileName);

            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este archivo o carpeta?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (selectedFile.exists()) {
                    try {
                        if (selectedFile.isFile()) {
                            boolean deleted = selectedFile.delete();
                            if (deleted) {
                                JOptionPane.showMessageDialog(this, "Archivo eliminado correctamente.");
                            } else {
                                JOptionPane.showMessageDialog(this, "No se pudo eliminar el archivo. Puede que esté en uso.");
                            }
                        } else {
                            eliminarCarpetaRecursivamente(selectedFile);
                            JOptionPane.showMessageDialog(this, "Carpeta eliminada correctamente.");
                        }

                        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
                        rootNode.removeAllChildren();
                        createTreeNodes(selectedFile);
                        actualizarTree();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void eliminarCarpetaRecursivamente(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                } else {
                    eliminarCarpetaRecursivamente(file);
                }
            }
        }
        folder.delete();
    }

    private void actualizarTree() {
        File rootDir = new File("C:\\Users\\josue\\OneDrive\\Documents\\NetBeansProjects\\Proyecto2progra2");
        DefaultMutableTreeNode rootNode = createTreeNodes(rootDir);  
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        tree.setModel(treeModel);  
    }

    private void configurarGamePanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        gamePanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 1000));
        rightPanel.setBackground(Color.LIGHT_GRAY);

        gameListPanel = new JPanel();
        gameListPanel.setLayout(new BoxLayout(gameListPanel, BoxLayout.Y_AXIS));
        gameListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(gameListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        rightPanel.add(scrollPane, BorderLayout.CENTER);
        gamePanel.add(rightPanel, BorderLayout.EAST);

        addgame = new JButton("Add Game");
        addgame.addActionListener(e -> showPanel("AddGame"));
        if (usuarioActual.isAdmin()) {
            addgame.setVisible(true);
        } else {
            addgame.setVisible(false);

        }
        leftPanel.add(addgame);

        gameDetailPanel = new JPanel();
        gameDetailPanel.setLayout(null);
        gameDetailPanel.setBackground(Color.WHITE);

        gameDetailImageLabel = new JLabel();
        gameDetailImageLabel.setBounds(360, 180, 300, 300);
        gameDetailPanel.add(gameDetailImageLabel);

        gameDetailInfoLabel = new JLabel();
        gameDetailInfoLabel.setBounds(360, 500, 400, 100);
        gameDetailPanel.add(gameDetailInfoLabel);

        gamePanel.add(gameDetailPanel, BorderLayout.CENTER);
    }

    private void cargarJuegos() {
    File folder = new File(GAMES_DIRECTORY);

    if (folder.exists() && folder.isDirectory()) {
        gameListPanel.removeAll();

        try (Stream<Path> paths = Files.walk(folder.toPath())) {
            paths.filter(Files::isRegularFile) 
                 .filter(path -> path.toString().endsWith(".ext")) 
                 .forEach(path -> {
                     try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
                         Juego juego = Juego.leer(raf);

                         File imageFile = new File(juego.getRutaImagen());
                         if (imageFile.exists()) {
                             ImageIcon icon = new ImageIcon(new ImageIcon(imageFile.getAbsolutePath())
                                     .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

                             JButton gameButton = new JButton(juego.getNombre(), icon);

                             gameButton.addActionListener(e -> mostrarDetallesJuego(juego, imageFile.getAbsolutePath()));

                             JButton addToUserButton = new JButton("Add");
                             addToUserButton.setFont(new Font("Arial", Font.PLAIN, 10)); 
                             addToUserButton.setPreferredSize(new Dimension(60, 20)); 
                             addToUserButton.addActionListener(e -> agregarJuegoAlUsuario(juego, imageFile.getAbsolutePath()));

                             JPanel gamePanel = new JPanel();
                             gamePanel.setLayout(new BorderLayout());
                             gamePanel.setMaximumSize(new Dimension(300, 60));
                             gamePanel.add(gameButton, BorderLayout.CENTER); 
                             gamePanel.add(addToUserButton, BorderLayout.EAST); 

                             gameListPanel.add(gamePanel);
                         } else {
                             System.out.println("Imagen no encontrada para el juego: " + juego.getNombre());
                         }
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 });

            gameListPanel.revalidate();
            gameListPanel.repaint();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al recorrer los archivos del directorio.");
        }
    } else {
        System.out.println("Directorio de juegos no encontrado: " + GAMES_DIRECTORY);
    }
}

    private void configurarAddGamePanel() {
        titleField2 = new JTextField();
        genreField = new JTextField();
        developerField = new JTextField();
        releaseDateField = new JTextField();

        addGamePanel.setLayout(new BoxLayout(addGamePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleField2.setMaximumSize(new Dimension(300, 30));
        titleField2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        genreField.setMaximumSize(new Dimension(300, 30));
        genreField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel developerLabel = new JLabel("Developer:");
        developerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        developerField.setMaximumSize(new Dimension(300, 30));
        developerField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel releaseDateLabel = new JLabel("Release Date (yyyy-mm-dd):");
        releaseDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        releaseDateField.setMaximumSize(new Dimension(300, 30));
        releaseDateField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botón para elegir imagen
        chooseImageButton = new JButton("Choose Image");
        chooseImageButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        chooseImageButton.addActionListener(e -> seleccionarImagenJuego());

        // Botón para guardar juego
        saveGameButton = new JButton("Save Game");
        saveGameButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveGameButton.addActionListener(e -> guardarJuego());

        addGamePanel.add(chooseImageButton);
        addGamePanel.add(Box.createVerticalStrut(10));
        addGamePanel.add(titleLabel);
        addGamePanel.add(titleField2);
        addGamePanel.add(genreLabel);
        addGamePanel.add(genreField);
        addGamePanel.add(developerLabel);
        addGamePanel.add(developerField);
        addGamePanel.add(releaseDateLabel);
        addGamePanel.add(releaseDateField);
        addGamePanel.add(Box.createVerticalStrut(10)); 
        addGamePanel.add(saveGameButton);
    }

    private void guardarJuego() {
        String titulo = titleField2.getText();
        String genero = genreField.getText();
        String desarrollador = developerField.getText();
        String fechaLanzamiento = releaseDateField.getText();

        if (imageFile != null && !titulo.isEmpty() && !genero.isEmpty() && !desarrollador.isEmpty() && !fechaLanzamiento.isEmpty()) {
            File gameDir = new File(GAMES_DIRECTORY);
            if (!gameDir.exists()) {
                gameDir.mkdirs();
            }

            String extension = getExtension(imageFile);

            String newImageName = titulo.replaceAll("\\s+", "") + "_cover." + extension;
            File renamedImageFile = new File(gameDir, newImageName);

            boolean renamed = imageFile.renameTo(renamedImageFile);

            if (renamed) {
                Juego.agregarJuego(renamedImageFile, titulo, genero, desarrollador, fechaLanzamiento);

                cargarJuegos();
                JOptionPane.showMessageDialog(this, "Juego guardado con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un error al renombrar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos y selecciona una imagen.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getExtension(File file) {
        String filename = file.getName();
        int extIndex = filename.lastIndexOf('.');
        if (extIndex > 0) {
            return filename.substring(extIndex + 1).toLowerCase(); 
        }
        return ""; 
    }

    private void seleccionarImagenJuego() {
        JFileChooser imageChooser = new JFileChooser();
        int result = imageChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imageFile = imageChooser.getSelectedFile();
        }
    }

    private void mostrarDetallesJuego(Juego juego, String coverPath) {
        try {
            ImageIcon icon = new ImageIcon(new ImageIcon(coverPath).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
            gameDetailImageLabel.setIcon(icon);
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }

        String gameInfo = "<html>Title: " + juego.getNombre() + "<br>Genre: " + juego.getGenero()
                + "<br>Developer: " + juego.getDesarrollador() + "<br>Release Date: " + juego.getFechaLanzamiento() + "</html>";
        gameDetailInfoLabel.setText(gameInfo); 

        for (ActionListener al : playButton.getActionListeners()) {
            playButton.removeActionListener(al);
        }
        playButton.addActionListener(e -> {
            System.out.println("Iniciando juego: " + juego.getNombre());
        });
    }

    private void configurarAddMusicPanel() {
        addMusicPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(50, 50, 100, 30);
        titleField = new JTextField();
        titleField.setBounds(150, 50, 200, 30);

        JLabel artistLabel = new JLabel("Artist:");
        artistLabel.setBounds(50, 100, 100, 30);
        artistField = new JTextField();
        artistField.setBounds(150, 100, 200, 30);

        JLabel albumLabel = new JLabel("Album:");
        albumLabel.setBounds(50, 150, 100, 30);
        albumField = new JTextField();
        albumField.setBounds(150, 150, 200, 30);

        JLabel durationLabel = new JLabel("Duration:");
        durationLabel.setBounds(50, 200, 100, 30);
        JTextField minutesField = new JTextField();
        minutesField.setBounds(150, 200, 50, 30);
        JLabel minLabel = new JLabel("min");
        minLabel.setBounds(210, 200, 30, 30);
        JTextField secondsField = new JTextField();
        secondsField.setBounds(250, 200, 50, 30);
        JLabel secLabel = new JLabel("sec");
        secLabel.setBounds(310, 200, 30, 30);

        chooseMP3Button = new JButton("Choose MP3");
        chooseMP3Button.setBounds(50, 250, 150, 30);
        chooseMP3Button.addActionListener(e -> seleccionarArchivoMP3());

        chooseCoverButton = new JButton("Choose Cover");
        chooseCoverButton.setBounds(50, 300, 150, 30);
        chooseCoverButton.addActionListener(e -> seleccionarPortada());

        saveSongButton = new JButton("Save Song");
        saveSongButton.setBounds(50, 350, 150, 40);

        coverLabel = new JLabel("No cover selected");
        coverLabel.setBounds(220, 300, 300, 30);

        mp3Label = new JLabel("No MP3 selected");
        mp3Label.setBounds(220, 250, 300, 30);

        addMusicPanel.add(titleLabel);
        addMusicPanel.add(titleField);
        addMusicPanel.add(artistLabel);
        addMusicPanel.add(artistField);
        addMusicPanel.add(albumLabel);
        addMusicPanel.add(albumField);
        addMusicPanel.add(durationLabel);
        addMusicPanel.add(minutesField);
        addMusicPanel.add(minLabel);
        addMusicPanel.add(secondsField);
        addMusicPanel.add(secLabel);
        addMusicPanel.add(chooseMP3Button);
        addMusicPanel.add(chooseCoverButton);
        addMusicPanel.add(saveSongButton);
        addMusicPanel.add(coverLabel);
        addMusicPanel.add(mp3Label);

        saveSongButton.addActionListener(e -> {
            try {
                int minutes = Integer.parseInt(minutesField.getText());
                int seconds = Integer.parseInt(secondsField.getText());
                int duration = (minutes * 60) + seconds; // Convertir a segundos
                guardarCancion(duration);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for minutes and seconds.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void seleccionarArchivoMP3() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("MP3 Files", "mp3"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            mp3File = fileChooser.getSelectedFile();
            mp3Label.setText("Selected: " + mp3File.getName());
        }
    }

    private void seleccionarPortada() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            coverFile = fileChooser.getSelectedFile();
            coverLabel.setText("Selected: " + coverFile.getName());
        }
    }

    private void guardarCancion(int duracion) {
    String titulo = titleField.getText();
    String artista = artistField.getText();
    String album = albumField.getText();

    if (mp3File != null && coverFile != null && !titulo.isEmpty() && !artista.isEmpty() && !album.isEmpty()) {
        Musica.agregarCancion(mp3File, titulo, artista, album, duracion);

        File songDir = new File(SONGS_DIRECTORY);
        String extensionCover = getFileExtension(coverFile);
        if (extensionCover != null && (extensionCover.equals("jpg") || extensionCover.equals("jpeg") || extensionCover.equals("png"))) {
            File newCoverFile = new File(songDir, mp3File.getName().replace(".mp3", "_cover." + extensionCover));
            try {
                Files.copy(coverFile.toPath(), newCoverFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            cargarCanciones();
            JOptionPane.showMessageDialog(this, "Canción guardada con éxito.");
        } else {
            JOptionPane.showMessageDialog(this, "El formato de la portada no es válido. Debe ser .jpg, .jpeg o .png", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos y selecciona un archivo MP3 y portada.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private String getFileExtension(File file) {
    String fileName = file.getName();
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex > 0) {
        return fileName.substring(dotIndex + 1).toLowerCase();
    }
    return null;
}

    private void cargarCanciones() {
        File folder = new File(SONGS_DIRECTORY);

        if (folder.exists() && folder.isDirectory()) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder.toPath(), "*.ext")) {
                songListPanel.removeAll(); 

                boolean foundSongs = false; 

                
                for (Path path : stream) {
                    File file = path.toFile(); 

                    try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                        Musica cancion = Musica.leer(raf);

                        String coverPath = file.getAbsolutePath().replace(".ext", "_cover.jpg");

                        ImageIcon icon = new ImageIcon(new ImageIcon(coverPath)
                                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)); 
                        JButton songButton = new JButton(cancion.getTitulo() + " - " + cancion.getArtista(), icon);
                        songButton.setFont(new Font("Arial", Font.PLAIN, 12));
                        songButton.setHorizontalAlignment(SwingConstants.LEFT); 

                        songButton.addActionListener(e -> mostrarDetallesCancion(cancion, coverPath, file));

                        JButton addToUserButton = new JButton("Add");
                        addToUserButton.setFont(new Font("Arial", Font.PLAIN, 10));
                        addToUserButton.setPreferredSize(new Dimension(60, 20)); 
                        addToUserButton.addActionListener(e -> agregarCancionAlUsuario(cancion, file, new File(coverPath)));

                        JPanel songPanel = new JPanel();
                        songPanel.setLayout(new BorderLayout());
                        songPanel.setMaximumSize(new Dimension(300, 40)); 
                        songPanel.add(songButton, BorderLayout.CENTER); 
                        songPanel.add(addToUserButton, BorderLayout.EAST);

                        songListPanel.add(songPanel);
                        foundSongs = true; 
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (!foundSongs) {
                    JLabel emptyLabel = new JLabel("La carpeta está vacía.");
                    songListPanel.add(emptyLabel);
                }

                songListPanel.revalidate();
                songListPanel.repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al acceder al directorio de canciones.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {

            songListPanel.removeAll();
            JLabel emptyLabel = new JLabel("La carpeta de canciones no existe.");
            songListPanel.add(emptyLabel);
            songListPanel.revalidate();
            songListPanel.repaint();
        }
    }

    private void agregarCancionAlUsuario(Musica cancion, File archivoCancion, File portada) {
        try {
            File carpetaUsuarioMusica = new File(usuarioActual.getCarpetaUsuario(), "Música");
            if (!carpetaUsuarioMusica.exists()) {
                carpetaUsuarioMusica.mkdirs();
            }

            File destinoCancion = new File(carpetaUsuarioMusica, archivoCancion.getName());
            Files.copy(archivoCancion.toPath(), destinoCancion.toPath(), StandardCopyOption.REPLACE_EXISTING);

            if (portada.exists()) {
                File destinoPortada = new File(carpetaUsuarioMusica, portada.getName());
                Files.copy(portada.toPath(), destinoPortada.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            JOptionPane.showMessageDialog(this, "Canción '" + cancion.getTitulo() + "' agregada al usuario.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al agregar la canción al usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void agregarJuegoAlUsuario(Juego juego, String coverPath) {
    try {
        File carpetaUsuarioJuegos = new File(usuarioActual.getCarpetaUsuario(), "Juegos");
        if (!carpetaUsuarioJuegos.exists()) {
            carpetaUsuarioJuegos.mkdirs(); 
        }

        File destinoJuego = new File(carpetaUsuarioJuegos, juego.getNombre() + ".ext");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(destinoJuego))) {
            oos.writeObject(juego);  
        }

        File portadaDestino = new File(carpetaUsuarioJuegos, juego.getNombre() + "_cover." + getFileExtension(new File(coverPath)));
        File portadaOrigen = new File(coverPath);  
        if (portadaOrigen.exists()) {
            Files.copy(portadaOrigen.toPath(), portadaDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        JOptionPane.showMessageDialog(this, "Juego '" + juego.getNombre() + "' guardado correctamente.");
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al guardar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}



    private void mostrarDetallesCancion(Musica cancion, String coverPath, File songFile) {
        ImageIcon icon = new ImageIcon(new ImageIcon(coverPath).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
        songDetailCoverLabel.setIcon(icon);

        int minutos = cancion.getDuracion() / 60;
        int segundos = cancion.getDuracion() % 60;

        songDetailInfoLabel.setText("<html>Title: " + cancion.getTitulo()
                + "<br>Artist: " + cancion.getArtista()
                + "<br>Album: " + cancion.getAlbum()
                + "<br>Duration: " + minutos + " min " + segundos + " sec</html>");

        for (ActionListener al : playButton.getActionListeners()) {
            playButton.removeActionListener(al);
        }
        playButton.addActionListener(e -> playSongFromFile(new File(cancion.getRutaArchivo())));
    }

    private void playSongFromFile(File songFile) {
        stopSong();

        currentSongFile = songFile;

        isPaused = false;

        try {
            FileInputStream fileInputStream = new FileInputStream(currentSongFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            player = new Player(bufferedInputStream);

            playerThread = new Thread(() -> {
                try {
                    player.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Iniciar el hilo de reproducción
            playerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseSong() {
        if (player != null) {
            isPaused = true;
            stopSong();
        }
    }

    private void stopSong() {
        try {
           
            if (player != null) {
                player.close();
            }

            if (playerThread != null && playerThread.isAlive()) {
                playerThread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            player = null;
            playerThread = null;

            if (!isPaused) {
                pausedFrame = 0;
                currentSongFile = null;
            }
        }
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (panelContainer.getLayout());
        cl.show(panelContainer, panelName);
    }

    private void cargarCancionesUsuario() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this, "No hay un usuario logueado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            userSongsPanel.removeAll();

            File carpetaMusica = new File(usuarioActual.getCarpetaUsuario(), "Música");

            if (!carpetaMusica.exists() || !carpetaMusica.isDirectory()) {
                JOptionPane.showMessageDialog(this, "No se encontró la carpeta de música del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            userSongsPanel.setLayout(new BoxLayout(userSongsPanel, BoxLayout.Y_AXIS));
            userSongsPanel.setBackground(Color.WHITE);

            profileScrollPane = new JScrollPane(userSongsPanel);
            profileScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            profileScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            profilePanel.add(profileScrollPane, BorderLayout.CENTER);

            try (Stream<Path> paths = Files.walk(carpetaMusica.toPath())) {
                paths.filter(Files::isRegularFile) // Filtrar solo archivos
                        .filter(path -> path.toString().endsWith(".ext")) // Filtrar archivos .dat
                        .forEach(path -> {
                            try (RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")) {
                                Musica cancion = Musica.leer(raf);

                                // Ruta de la portada
                                String coverPath = path.toFile().getAbsolutePath().replace(".ext", "_cover.jpg");

                                // Crear botón para mostrar detalles
                                ImageIcon icon = new ImageIcon(new ImageIcon(coverPath)
                                        .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

                                // Ajustar tamaño del botón
                                JButton songButton = new JButton(cancion.getTitulo() + " - " + cancion.getArtista(), icon);
                                songButton.setPreferredSize(new Dimension(250, 60));  // Tamaño fijo para los botones
                                songButton.setFont(new Font("Arial", Font.PLAIN, 12));
                                songButton.setHorizontalAlignment(SwingConstants.LEFT);

                                // Acción para mostrar detalles
                                songButton.addActionListener(e -> mostrarDetallesCancion(cancion, coverPath, path.toFile()));

                                // Crear un panel para los botones
                                JPanel songPanel = new JPanel();
                                songPanel.setLayout(new BorderLayout());
                                songPanel.setMaximumSize(new Dimension(300, 60)); // Tamaño máximo del panel
                                songPanel.add(songButton, BorderLayout.CENTER); // Botón principal

                                // Añadir el panel al contenedor principal
                                userSongsPanel.add(songPanel);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                // Actualizar el panel después de añadir las canciones
                userSongsPanel.revalidate();
                userSongsPanel.repaint();

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cargar las canciones del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar las canciones del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
   private void cargarJuegosUsuario() {
    if (usuarioActual == null) {
        JOptionPane.showMessageDialog(this, "No hay un usuario logueado.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Limpiar el panel de juegos del usuario antes de añadir nuevos juegos
        userGamesPanel.removeAll();

        // Obtener la carpeta de juegos del usuario
        File carpetaJuegos = new File(usuarioActual.getCarpetaUsuario(), "Juegos");

        // Comprobamos si la carpeta existe
        if (!carpetaJuegos.exists() || !carpetaJuegos.isDirectory()) {
            JOptionPane.showMessageDialog(this, "No se encontró la carpeta de juegos del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear el panel para los juegos del usuario
        userGamesPanel.setLayout(new BoxLayout(userGamesPanel, BoxLayout.Y_AXIS));
        userGamesPanel.setBackground(Color.WHITE);

        // Crear el JScrollPane
        profileScrollPane = new JScrollPane(userGamesPanel);
        profileScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        profileScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Añadir el JScrollPane al panel de perfil
        profilePanel.add(profileScrollPane, BorderLayout.CENTER);

        // Utilizar Directory Stream para recorrer los archivos de juegos
        try (Stream<Path> paths = Files.walk(carpetaJuegos.toPath())) {
            paths.filter(Files::isRegularFile) // Filtrar solo archivos
                    .filter(path -> path.toString().endsWith(".dat")) // Filtrar archivos .dat (juegos)
                    .forEach(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            // Leer el objeto Juego
                            Juego juego = (Juego) ois.readObject();
                            
                            // Ruta de la portada del juego
                            String coverPath = path.toFile().getAbsolutePath().replace(".ext", "_cover.jpg");
                            
                            // Verificar si la imagen de portada existe
                            File coverImage = new File(coverPath);

                            
                            // Crear botón para mostrar detalles
                            ImageIcon icon = new ImageIcon(new ImageIcon(coverPath)
                                    .getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

                            // Ajustar tamaño del botón
                            JButton gameButton = new JButton(juego.getNombre(), icon);
                            gameButton.setPreferredSize(new Dimension(250, 60));  // Tamaño fijo para los botones
                            gameButton.setFont(new Font("Arial", Font.PLAIN, 12));
                            gameButton.setHorizontalAlignment(SwingConstants.LEFT);

                            // Acción para mostrar detalles
                            gameButton.addActionListener(e -> mostrarDetallesJuego(juego, coverPath));

                            // Crear un panel para los botones
                            JPanel gamePanel = new JPanel();
                            gamePanel.setLayout(new BorderLayout());
                            gamePanel.setMaximumSize(new Dimension(300, 60)); // Tamaño máximo del panel
                            gamePanel.add(gameButton, BorderLayout.CENTER); // Botón principal

                            // Añadir el panel al contenedor principal
                            userGamesPanel.add(gamePanel);
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });

            // Actualizar el panel después de añadir los juegos
            userGamesPanel.revalidate();
            userGamesPanel.repaint();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los juegos del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar los juegos del usuario.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}



    private void logout() {

        Login lg = new Login();
        lg.setVisible(true);
        dispose();
    }

}
