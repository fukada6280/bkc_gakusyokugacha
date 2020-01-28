import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Container;

public class SubPanel extends JPanel {
    MainFrame mf;
    String str;

    JButton toMainBtn;

    // 背景色を決定する
    Color backGroundColor = new Color(255, 255, 240);

    public SubPanel(MainFrame m, String s) {
        mf = m;
        str = s;

        this.setName(s);
        this.setSize(360, 640);

        // ボタンの作成
        toMainBtn = new JButton("MainPanelに移動=もう一度やる！！");
        toMainBtn.setBounds(150, 50, 200, 40);
        toMainBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pc();
            }
        });

        this.add(toMainBtn);

    }

    public void pc() {
        mf.PanelChange((JPanel) this, mf.PanelNames[0]);
    }
}
