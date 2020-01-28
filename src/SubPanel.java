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

    // JLabel resultLabel;
    JLabel titleLabel;
    JLabel resultLabel;
    JButton toMainBtn;

    // 背景色を決定する
    Color backGroundColor = new Color(255, 255, 240);

    public SubPanel(MainFrame m, String s) {
        mf = m;
        str = s;

        this.setName(s);
        this.setSize(360, 640);

        // ここからGUI関連を書く
        // 各種画像を読み込む
        /*
         * ImageIcon original_searchIcon = new ImageIcon("./icon/search.png");
         * 
         * // MediaTracker tracker = new MediaTracker(this);
         * 
         * // getScaledInstanceで大きさを変更 Image small_searchImg =
         * original_searchIcon.getImage() .getScaledInstance((int)
         * (original_searchIcon.getIconWidth() * 50 / 256), -1, Image.SCALE_SMOOTH);
         * 
         * // MediaTrackerで処理の終了を待つ tracker.addImage(small_searchImg, 1);
         * 
         * ImageIcon small_searchIcon = new ImageIcon(small_searchImg);
         * 
         * try { tracker.waitForAll(); } catch (InterruptedException e) {
         * System.out.println("画像読み込み時にエラーが発生しました"); }
         * 
         * // BKC学食ガチャ: タイトル画像+ラベルの表示 resultLabel = new JLabel("結果", small_searchIcon,
         * JLabel.CENTER); resultLabel.setOpaque(true); resultLabel.setFont(new
         * Font("Arial", Font.PLAIN, 35)); resultLabel.setBackground(backGroundColor);
         * resultLabel.setPreferredSize(new Dimension(340, 100)); // 大きさを変更 width,height
         * resultLabel.setHorizontalTextPosition(JLabel.RIGHT);
         */

        System.out.println("SubPanelが実行されています");

        // タイトル部分のラベルの作成
        titleLabel = new JLabel("ガチャ結果");
        //titleLabel.setOpaque(true);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        titleLabel.setBackground(backGroundColor);
        titleLabel.setPreferredSize(new Dimension(340, 100)); // 大きさを変更 width,height

        // 絞り込み結果のラベルの作成
        resultLabel = new JLabel(String.valueOf(mf.mp.valSlider.getValue())); //valSlider.getValue()
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        resultLabel.setBackground(backGroundColor);
        resultLabel.setPreferredSize(new Dimension(340, 100)); // 大きさを変更 width,height

        // ボタンの作成
        toMainBtn = new JButton("MainPanelに移動=もう一度やる！！");
        toMainBtn.setBounds(150, 50, 200, 40);
        toMainBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pc();
            }
        });

        // this.add(resultLabel);
        this.add(titleLabel);
        this.add(resultLabel);
        this.add(toMainBtn);

    }

    public void pc() {
        mf.PanelChange((JPanel) this, mf.PanelNames[0]);
    }
}
