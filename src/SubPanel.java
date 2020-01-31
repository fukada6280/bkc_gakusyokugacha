import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;

public class SubPanel extends JPanel {
    MainFrame mf;
    String str;

    JLabel titleLabel;
    JLabel resultNameLabel;
    JLabel itemizeIconLabel;
    JLabel itemizeLabel;
    JButton toMainBtn;

    // 背景色を決定する
    Color backGroundColor = new Color(255, 255, 240);

    public SubPanel(MainFrame m, String s, MealData result) {
        mf = m;
        str = s;

        this.setName(s);
        this.setSize(360, 640);

        ImageIcon original_searchIcon = new ImageIcon("../icon/search.png");
        ImageIcon original_receiptIcon = new ImageIcon("../icon/receipt.png");
        MediaTracker tracker = new MediaTracker(this);

        Image small_searchImg = original_searchIcon.getImage()
                .getScaledInstance((int) (original_searchIcon.getIconWidth() * 40 / 256), -1, Image.SCALE_SMOOTH);

        Image small_receiptImg = original_receiptIcon.getImage()
                .getScaledInstance((int) (original_receiptIcon.getIconWidth() * 25 / 256), -1, Image.SCALE_SMOOTH);

        tracker.addImage(small_searchImg, 5);
        tracker.addImage(small_receiptImg, 6);

        ImageIcon small_searchIcon = new ImageIcon(small_searchImg);
        ImageIcon small_receiptIcon = new ImageIcon(small_receiptImg);

        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            System.out.println("画像読み込み時にエラーが発生しました");
        }

        // 結果: タイトル画像+ラベルの表示
        titleLabel = new JLabel("ガチャ結果", small_searchIcon, JLabel.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        titleLabel.setBackground(backGroundColor);
        titleLabel.setPreferredSize(new Dimension(340, 100)); // 大きさを変更 width,height
        titleLabel.setHorizontalTextPosition(JLabel.RIGHT);

        // 結果を表示する
        resultNameLabel = new JLabel();
        resultNameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        resultNameLabel.setLayout(null); // レイアウトマネージャーを無効化
        resultNameLabel.setBounds(0, 0, 200, 200); // x, y, width, height
        resultNameLabel.setText(result.getName());

        // 内訳: 画像+ラベルの表示
        itemizeIconLabel = new JLabel("内訳", small_receiptIcon, JLabel.LEFT);
        itemizeIconLabel.setOpaque(true);
        itemizeIconLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        itemizeIconLabel.setBackground(backGroundColor);
        itemizeIconLabel.setPreferredSize(new Dimension(300, 40)); // 大きさを変更 width,height
        itemizeIconLabel.setHorizontalTextPosition(JLabel.RIGHT);

        // 内訳の詳細を表示する
        itemizeLabel = new JLabel();
        itemizeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        itemizeLabel.setLayout(null); // レイアウトマネージャーを無効化
        itemizeLabel.setBounds(0, 0, 200, 200); // x, y, width, height
        itemizeLabel.setText("<html>・合計価格： " + result.getValue() + "円<br>" +
                                   "・合計カロリー：" + result.getKcal() + "kcal<br>" +
                "・合計タンパク質： " + result.getProtein() + "g<br>" +
                "・合計脂質： " + result.getLipid() + "g<br>" +
                "・合計炭水化物： " + result.getCarbohydrate() + "g<br>" +
                "・合計食塩量： " + result.getSalt() + "g<br>" +
                "・合計カルシウム量： " + result.getCalcium() + "mg<br>" +
                "・合計野菜量： " + result.getVegetable() + "g");


        // ボタンの作成
        toMainBtn = new JButton("もう一度やる！！");
        toMainBtn.setBounds(150, 50, 200, 40);
        toMainBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        toMainBtn.setPreferredSize(new Dimension(300, 80)); // 大きさを変更 width,height
        toMainBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                panelChangeToMainPanel(mf.PanelNames[0]);
            }
        });

        this.setBackground(backGroundColor);
        this.add(titleLabel);
        this.add(resultNameLabel);
        this.add(itemizeIconLabel);
        this.add(itemizeLabel);
        this.add(toMainBtn);



    }

    // MainPanelに遷移するためのメソッド
    public void panelChangeToMainPanel(String toPanelName) {
        mf.showMainPanel((JPanel)this);
    }

}
