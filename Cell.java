package bgp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Cell {
    private String content;
    private float contentFontSize;
    private JButton button;
    public int row, col;
    public BGPEventListener listener = null;
    Cell thisCell;
    private Color color;

    public Cell(JPanel panel, int r, int c, int width, int height) {
        create(panel, r, c, width, height, "", 0);
    }

    public Cell(JPanel panel, int r, int c, int width, int height, String content) {
        create(panel, r, c, width, height, content, 0);
    }

    public Cell(JPanel panel, int r, int c, int width, int height, String content, float fontSize) {
        create(panel, r, c, width, height, content, fontSize);
    }

    private void create(JPanel panel, int r, int c, int width, int height, String content, float fontSize) {
        this.thisCell = this;
        this.content = content;
        this.contentFontSize = fontSize;

        this.button = new JButton(content);
        this.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //thisCell.listener.clicked(thisCell, row, col);
                if (listener != null)
                    listener.clicked(row, col);
            }
        });
        this.button.setPreferredSize(new Dimension(width, height));
        if (fontSize > 0)
            this.button.setFont(this.button.getFont().deriveFont(fontSize));
        this.button.setBackground(Color.WHITE);
        this.color = Color.WHITE;
        this.button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),  // 1 -> border width 1
                BorderFactory.createLineBorder(Color.WHITE, 0)));  // 0 -> padding 0
        panel.add(button);

        this.row = r;
        this.col = c;
    }

    public void cellBackgroundColor(Color color) {
        this.button.setBackground(color);
        this.color = color;
    }

    public Color cellBackgroundColor() {
        return this.color;
    }

    public String cellContent() {
        return content;
    }

    public void cellContent(String newContent) {
        this.content = newContent;
        this.button.setText(newContent);
        if (this.contentFontSize > 0)
            this.button.setFont(this.button.getFont().deriveFont(this.contentFontSize));
    }

    public void cellContent(String newContent, float fontSize) {
        this.content = newContent;
        this.contentFontSize = fontSize;
        this.button.setText(newContent);
        if (this.contentFontSize > 0)
            this.button.setFont(this.button.getFont().deriveFont(this.contentFontSize));
    }

    public void clickEventListener(BGPEventListener listener) {
        this.listener = listener;
    }
  
  /*
  public void refreshActionListener()
  {
    for (ActionListener al: this.button.getActionListeners())
      this.button.removeActionListener(al);
    
    this.button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        listener.clicked(row, col); 
      }
    });
  }
  */

    public JButton getButton() {
        return this.button;
    }

    public void setNewPosition(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    public void setBorderWidth(int newWidth) {
        this.button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, newWidth),
                BorderFactory.createLineBorder(Color.WHITE, 0)));  // 0 -> padding 0
    }
}
