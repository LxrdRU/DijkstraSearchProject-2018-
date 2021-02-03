package bgp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board {
    private int N, M, CELL_HEIGHT, CELL_WIDTH;
    private Color boardCellColor;
    public Cell cells[][];
    private JPanel panelBoard, panelInput, panelOutput;
    private JFrame frame;
    private JTextField textField;
    private JButton button1 = null, button2 = null;
    private BGPEventListener button1Listener, button2Listener;
    private JTextArea textArea;

    public Board(int rows, int columns, int width, int height, String style, Color color) {
        create(rows, columns, width, height, style, color);
    }

    public Board(int rows, int columns, int width, int height) {
        create(rows, columns, width, height, "Line", Color.WHITE);
    }

    private void create(int rows, int columns, int width, int height, String style, Color color) {
        N = rows;
        M = columns;
        CELL_HEIGHT = height / rows;
        CELL_WIDTH = width / columns;

        boardCellColor = color;

        panelBoard = new JPanel();
        panelBoard.setPreferredSize(new Dimension(width, height));
        panelBoard.setLayout(new GridLayout(N, M));
        panelBoard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createLineBorder(Color.WHITE, 0)));  // 0 -> padding 0

        textField = new JTextField(20);
        button1 = new JButton("Button1");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (button1Listener != null)
                    button1Listener.clicked(-1, -1);
            }
        });
        button2 = new JButton("Button2");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (button2Listener != null)
                    button2Listener.clicked(-1, -1);
            }
        });

        panelInput = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInput.setPreferredSize(new Dimension(width, 60));
        panelInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0),
                BorderFactory.createLineBorder(Color.WHITE, 10)));  // 0 -> padding 0
        panelInput.add(textField);
        panelInput.add(button1);
        panelInput.add(button2);

        panelOutput = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOutput.setPreferredSize(new Dimension(width, 110));
        panelOutput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 0),
                BorderFactory.createLineBorder(Color.WHITE, 10)));  // 0 -> padding 0
        textArea = new JTextArea(5, 40);
        textArea.setEditable(false);
        panelOutput.add(textArea);


        cells = new Cell[N][];
        for (int r = 0; r < N; r++) {
            cells[r] = new Cell[M];
            for (int c = 0; c < M; c++) {
                cells[r][c] = new Cell(panelBoard, r, c, CELL_WIDTH, CELL_HEIGHT, "", 0);
                cells[r][c].cellBackgroundColor(boardCellColor);
                if (style.equals("NoLine") || style.equals("NO_LINE"))
                    cells[r][c].setBorderWidth(0);
            }
        }

        frame = new JFrame("TRU Board Games Playground");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLayout(new GridLayout(3, 1));
        frame.getContentPane().add(panelBoard, BorderLayout.NORTH);
        frame.getContentPane().add(panelInput, BorderLayout.CENTER);
        frame.getContentPane().add(panelOutput, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    public void dispose() {
        frame.dispose();
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public void refresh() {
        panelBoard.removeAll();

        for (int r = 0; r < N; r++)
            for (int c = 0; c < M; c++) {
                panelBoard.add(cells[r][c].getButton());
                //cells[r][c].getButton().revalidate();
            }

        //panelBoard.invalidate();  // It does not work.
        panelBoard.revalidate();  // It does work.
        panelBoard.repaint();

        // Let's give Java Event Manager some time so that all the changes in BGP can be properly rendered.
        sleep(5);
    }

    public void cellsClickEventListener(BGPEventListener listener) {
        for (int r = 0; r < N; r++)
            for (int c = 0; c < M; c++)
                cells[r][c].clickEventListener(listener);
    }

    public Color cellBackgroundColor(int row, int col) {
        return cells[row][col].cellBackgroundColor();
    }

    public void cellBackgroundColor(int row, int col, Color color) {
        cells[row][col].cellBackgroundColor(color);
    }

    public String cellContent(int row, int col) {
        return cells[row][col].cellContent();
    }

    public void cellContent(int row, int col, String content) {
        cells[row][col].cellContent(content);
    }

    public void cellContent(int row, int col, String content, float fontSize) {
        cells[row][col].cellContent(content, fontSize);
    }

    public void switchCells(int row0, int col0, int row1, int col1) {
        Cell tmp = cells[row0][col0];

        cells[row0][col0] = cells[row1][col1];
        cells[row0][col0].setNewPosition(row0, col0);  // Not row1 and col1
        cells[row1][col1] = tmp;
        cells[row1][col1].setNewPosition(row1, col1);

        refresh();
    }

    public void button1ClickEventListener(BGPEventListener listener) {
        button1Listener = listener;
    }

    public void button1SetName(String name) {
        button1.setText(name);
    }

    public void button2ClickEventListener(BGPEventListener listener) {
        button2Listener = listener;
    }

    public void button2SetName(String name) {
        button2.setText(name);
    }

    public void showMessageDialog(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String s) {
        textField.setText(s);
    }

    public void appendText(String text) {
        textArea.append(text + "\n");
        // textArea.setCaretPosition(textArea.getDocument().getLength() - 1);  // It does not work
    }

    public void clearText() {
        textArea.setText("");
    }

    public void sleep(long ms) {
        panelBoard.revalidate();  // It does work.
        panelBoard.repaint();

        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }
}
