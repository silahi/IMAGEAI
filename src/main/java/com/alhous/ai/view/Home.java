package com.alhous.ai.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.alhous.ai.model.Category;
import com.alhous.ai.model.Dataset;
import com.alhous.ai.model.IDatasetService;
import com.alhous.ai.model.LiveServiceImpl;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

public class Home {
    private JFrame frame;
    private Font font = new Font("System", Font.BOLD, 14);
    private Color color = Color.YELLOW;
    private int w = 800, h = 600;
    private Painter painter;
    private List<Category> categories;

    public Home(LiveServiceImpl liveService, IDatasetService datasetService) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
        categories = new ArrayList<>();
        frame = new JFrame("IMAGEAI");
        frame.setSize(w, h);
        frame.setLocationByPlatform(true);
        frame.getContentPane().setBackground(Color.WHITE);
        ImageIcon ii = new ImageIcon(this.getClass().getClassLoader().getResource("ricon.png"));
        frame.setIconImage(ii.getImage());
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!liveService.getLiveThread().isInterrupted()) {
                    Mat m = new Mat();
                    liveService.getVideoCapture().read(m);
                    painter.setImage(HighGui.toBufferedImage(m));
                    painter.repaint();
                }
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                th.start();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                th.interrupt();
                liveService.stop();
                liveService.getVideoCapture().release();
            }
        });

        painter = new Painter();
        painter.setLayout(new java.awt.BorderLayout());
        frame.add(painter);

        JPanel left_panel = new JPanel();
        JPanel p_left_panel = new JPanel();
        p_left_panel.setLayout(new java.awt.GridLayout(4, 1, 8, 8));
        JLabel ds_name_lbl = new JLabel("DATASET NAME");
        JLabel ct_name_lbl = new JLabel("CATEGORY NAME");
        JTextField ds_name_tf = new JTextField(15);
        JTextField ct_name_tf = new JTextField(15);
        p_left_panel.add(ds_name_lbl);
        p_left_panel.add(ds_name_tf);
        p_left_panel.add(ct_name_lbl);
        p_left_panel.add(ct_name_tf);
        left_panel.add(p_left_panel);

        JPanel right_panel = new JPanel();
        JPanel p_right_panel = new JPanel();
        p_right_panel.setLayout(new java.awt.GridLayout(2, 1, 15, 15));
        JButton capture_btn = new JButton("CAPTURE");
        JButton save_btn = new JButton("SAVE");
        p_right_panel.add(capture_btn);
        p_right_panel.add(save_btn);
        right_panel.add(p_right_panel);

        capture_btn.addActionListener(e -> {
            Mat img = new Mat();
            liveService.getVideoCapture().read(img);
            ImageIcon ic = new ImageIcon(HighGui.toBufferedImage(img));
            JLabel label = new JLabel(ic);
            JDialog dlg = new JDialog();
            dlg.setModal(true);
            dlg.setIconImage(frame.getIconImage());
            dlg.add(label);

            boolean cond = ds_name_tf.getText().isEmpty() || ct_name_tf.getText().isEmpty();
            if (cond) {
                JOptionPane.showMessageDialog(frame, "Dataset name and Category name fields are empty !");

            } else {
                JButton validate = new JButton("Validate");
                JButton cancel = new JButton("Cancel");
                validate.addActionListener(e1 -> {
                    categories.stream().filter(ct -> ct.getName() == ct_name_tf.getText()).findFirst().ifPresent(ct -> {
                        ct.getImages().add(img);
                    });
                    boolean v = categories.stream().filter(ct -> ct.getName() == ct_name_tf.getText()).findFirst()
                            .isPresent();
                    if (v == false) {
                        Category cat = new Category();
                        cat.setName(ct_name_tf.getText());
                        cat.getImages().add(img);
                        categories.add(cat);
                    }
                    dlg.setVisible(false);
                });
                cancel.addActionListener(e2 -> {
                    dlg.setVisible(false);
                });
                dlg.setTitle(ds_name_tf.getText() + " -> " + ct_name_tf.getText());
                JPanel p4 = new JPanel();

                p4.add(validate);
                p4.add(cancel);
                dlg.add(p4, "South");
                dlg.pack();
                dlg.setVisible(true);
            }

        });

        save_btn.addActionListener(e -> {
            Dataset ds = new Dataset();
            ds.setName(ds_name_tf.getText());
            ds.setCategories(categories);
            String msg = datasetService.saveDataset(ds);
            JOptionPane.showMessageDialog(frame, msg);
        });
        JPanel p1 = new JPanel();
        p1.setLayout(new java.awt.BorderLayout());
        p1.add(left_panel, "South");

        JPanel p2 = new JPanel();
        p2.setLayout(new java.awt.BorderLayout());
        p2.add(right_panel, "South");

        painter.add(p1, "West");
        painter.add(p2, "East");

        Arrays.asList(ds_name_lbl, ct_name_lbl).forEach(l -> {
            l.setFont(font);
            l.setForeground(color);
        });

        Arrays.asList(ds_name_tf, ct_name_tf).forEach(tf -> {
            tf.setFont(font);
            tf.setForeground(Color.WHITE);
            tf.setPreferredSize(new Dimension(100, 30));
            tf.setBackground(new Color(0, 0, 0, 100));
            // tf.setOpaque(false);
            tf.setBorder(BorderFactory.createEmptyBorder());
            tf.setCaretColor(Color.GREEN);
            tf.setPreferredSize(new Dimension(60, 30));

        });

        Arrays.asList(save_btn, capture_btn).forEach(btn -> {
            btn.setFont(font);
            btn.setFocusPainted(false);

        });

        Arrays.asList(p1, p2, right_panel, left_panel, p_right_panel, p_left_panel).forEach(p -> {
            p.setOpaque(false);
        });
    }

    public void addImage(Mat mat, String dataset_name, String category_name) {

    }

    class Painter extends JPanel {
        static final long serialVersionUID = 2;
        private java.awt.Image image;

        @Override
        protected void paintComponent(Graphics g) {
            if (image != null) {
                g.drawImage(image, 0, 0, w, h, this);
            }
        }

        public java.awt.Image getImage() {
            return image;
        }

        public void setImage(java.awt.Image image) {
            this.image = image;
        }
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

}
