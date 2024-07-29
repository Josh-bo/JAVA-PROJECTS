import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotepadApp extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private UndoManager undoManager;

    public NotepadApp() {
        setTitle("Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        createMenuBar();
        createPopupMenu();

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New");
        newFile.addActionListener(e -> newFile());
        JMenuItem openFile = new JMenuItem("Open");
        openFile.addActionListener(e -> openFile());
        JMenuItem saveFile = new JMenuItem("Save");
        saveFile.addActionListener(e -> saveFile());
        JMenuItem saveAsFile = new JMenuItem("Save As");
        saveAsFile.addActionListener(e -> saveAsFile());
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> exitApp());

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(e -> textArea.cut());
        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(e -> textArea.copy());
        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(e -> textArea.paste());
        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(e -> textArea.selectAll());
        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(e -> undo());
        JMenuItem redo = new JMenuItem("Redo");
        redo.addActionListener(e -> redo());
        JMenuItem findReplace = new JMenuItem("Find/Replace");
        findReplace.addActionListener(e -> findReplace());

        editMenu.add(undo);
        editMenu.add(redo);
        editMenu.addSeparator();
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.addSeparator();
        editMenu.add(selectAll);
        editMenu.addSeparator();
        editMenu.add(findReplace);

        JMenu formatMenu = new JMenu("Format");
        JMenuItem wordWrap = new JCheckBoxMenuItem("Word Wrap");
        wordWrap.addActionListener(e -> textArea.setLineWrap(wordWrap.isSelected()));
        JMenuItem font = new JMenuItem("Font");
        font.addActionListener(e -> chooseFont());

        formatMenu.add(wordWrap);
        formatMenu.add(font);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);

        setJMenuBar(menuBar);
    }

    private void createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(e -> textArea.cut());
        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(e -> textArea.copy());
        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(e -> textArea.paste());
        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(e -> textArea.selectAll());

        popupMenu.add(cut);
        popupMenu.add(copy);
        popupMenu.add(paste);
        popupMenu.addSeparator();
        popupMenu.add(selectAll);

        textArea.setComponentPopupMenu(popupMenu);
    }

    private void newFile() {
        textArea.setText("");
        currentFile = null;
        setTitle("Notepad - New File");
    }

    private void openFile() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        }

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.read(reader, null);
                setTitle("Notepad - " + currentFile.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            saveAsFile();
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                textArea.write(writer);
                setTitle("Notepad - " + currentFile.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveAsFile() {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        }

        int returnValue = fileChooser.showSaveDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            if (!currentFile.getName().toLowerCase().endsWith(".txt")) {
                currentFile = new File(currentFile.getAbsolutePath() + ".txt");
            }
            saveFile();
        }
    }

    private void exitApp() {
        System.exit(0);
    }

    private void undo() {
        try {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        } catch (CannotUndoException e) {
            JOptionPane.showMessageDialog(this, "Nothing to undo", "Undo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void redo() {
        try {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        } catch (CannotRedoException e) {
            JOptionPane.showMessageDialog(this, "Nothing to redo", "Redo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void findReplace() {
        JDialog findReplaceDialog = new JDialog(this, "Find and Replace", false);
        findReplaceDialog.setSize(400, 200);
        findReplaceDialog.setLayout(new GridLayout(3, 2));

        JTextField findField = new JTextField();
        JTextField replaceField = new JTextField();

        JButton findButton = new JButton("Find");
        findButton.addActionListener(e -> {
            String findText = findField.getText();
            String content = textArea.getText();
            int index = content.indexOf(findText);
            if (index != -1) {
                textArea.setCaretPosition(index);
                textArea.select(index, index + findText.length());
            } else {
                JOptionPane.showMessageDialog(this, "Text not found", "Find", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton replaceButton = new JButton("Replace");
        replaceButton.addActionListener(e -> {
            String findText = findField.getText();
            String replaceText = replaceField.getText();
            textArea.setText(textArea.getText().replaceFirst(findText, replaceText));
        });

        findReplaceDialog.add(new JLabel("Find:"));
        findReplaceDialog.add(findField);
        findReplaceDialog.add(new JLabel("Replace:"));
        findReplaceDialog.add(replaceField);
        findReplaceDialog.add(findButton);
        findReplaceDialog.add(replaceButton);

        findReplaceDialog.setVisible(true);
    }

    private void chooseFont() {
        JDialog fontDialog = new JDialog(this, "Choose Font", true);
        fontDialog.setSize(400, 300);
        fontDialog.setLayout(new BorderLayout());

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();
        JList<String> fontList = new JList<>(fontNames);
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(fontList);
        fontDialog.add(scrollPane, BorderLayout.CENTER);

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(e -> {
            String selectedFont = fontList.getSelectedValue();
            if (selectedFont != null) {
                textArea.setFont(new Font(selectedFont, Font.PLAIN, 14));
                fontDialog.dispose();
            }
        });

        fontDialog.add(selectButton, BorderLayout.SOUTH);
        fontDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NotepadApp::new);
    }
}
