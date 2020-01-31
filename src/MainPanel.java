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
import java.util.List;

public class MainPanel extends JPanel implements ChangeListener {
  MainFrame mf;
  String str;
  List<MealData> mealDataList = Main.loadCsv("mealData.csv");
  MealData result = mealDataList.get(0); // とりあえず何か初期値を入れておかないといけない　要変更

  // GUI部品
  JLabel titleLabel;
  JLabel placeLabel;
  String[] placeList = { "リンク", "ユニオン１階", "ユニオン２階" };
  JComboBox<String> placeCombo;
  JLabel valueLabel;
  JSlider valSlider;
  JLabel showValLabel;
  JLabel priorityLabel;
  String[] priorityList = { "ガチャ(ランダム)", "カロリーを低く", "カロリーを高く", "タンパク質を大きく",
                            "脂質を小さく", "カルシウムを大きく", "野菜量を大きく" };
  JComboBox<String> priorityCombo;
  JLabel blank;
  JButton toSubBtn;

  // 背景色を決定する
  Color backGroundColor = new Color(255, 255, 240);

  public MainPanel(MainFrame m, String s) {
    mf = m;
    str = s;
    this.setName(s);
    this.setSize(360, 640);

    // ここからGUI関連を書く
    // 各種画像を読み込む
    ImageIcon original_fork_spoonIcon = new ImageIcon("../icon/fork_spoon.png");
    ImageIcon original_locationIcon = new ImageIcon("../icon/location.png");
    ImageIcon original_yenIcon = new ImageIcon("../icon/yen.png");
    ImageIcon original_tagIcon = new ImageIcon("../icon/tag.png");
    MediaTracker tracker = new MediaTracker(this);

    // getScaledInstanceで大きさを変更
    Image small_fork_spoonImg = original_fork_spoonIcon.getImage()
        .getScaledInstance((int) (original_fork_spoonIcon.getIconWidth() * 40 / 256), -1, Image.SCALE_SMOOTH);

    Image small_locationImg = original_locationIcon.getImage()
        .getScaledInstance((int) (original_locationIcon.getIconWidth() * 25 / 256), -1, Image.SCALE_SMOOTH);

    Image small_yenImg = original_yenIcon.getImage()
        .getScaledInstance((int) (original_yenIcon.getIconWidth() * 25 / 256), -1, Image.SCALE_SMOOTH);

    Image small_tagImg = original_tagIcon.getImage()
        .getScaledInstance((int) (original_tagIcon.getIconWidth() * 25 / 256), -1, Image.SCALE_SMOOTH);

    // MediaTrackerで処理の終了を待つ
    tracker.addImage(small_fork_spoonImg, 1);
    tracker.addImage(small_locationImg, 2);
    tracker.addImage(small_yenImg, 3);
    tracker.addImage(small_tagImg, 4);

    ImageIcon small_fork_spoonIcon = new ImageIcon(small_fork_spoonImg);
    ImageIcon small_locationIcon = new ImageIcon(small_locationImg);
    ImageIcon small_yenIcon = new ImageIcon(small_yenImg);
    ImageIcon small_tagIcon = new ImageIcon(small_tagImg);

    try {
      tracker.waitForAll();
    } catch (InterruptedException e) {
      System.out.println("画像読み込み時にエラーが発生しました");
    }

    // BKC学食ガチャ: タイトル画像+ラベルの表示
    titleLabel = new JLabel("BKC学食ガチャ", small_fork_spoonIcon, JLabel.CENTER);
    titleLabel.setOpaque(true);
    titleLabel.setFont(new Font("Arial", Font.PLAIN, 30));
    titleLabel.setBackground(backGroundColor);
    titleLabel.setPreferredSize(new Dimension(340, 100)); // 大きさを変更 width,height
    titleLabel.setHorizontalTextPosition(JLabel.RIGHT);

    // 場所: タイトル画像+ラベルの表示
    placeLabel = new JLabel(" 場所", small_locationIcon, JLabel.LEFT);
    placeLabel.setOpaque(true);
    placeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
    placeLabel.setBackground(backGroundColor);
    placeLabel.setPreferredSize(new Dimension(300, 40)); // 大きさを変更 width,height
    placeLabel.setHorizontalTextPosition(JLabel.RIGHT);

    // 場所指定を行うコンボボックスの作成
    placeCombo = new JComboBox<String>(placeList);
    placeCombo.setFont(new Font("Arial", Font.PLAIN, 20));

    // 価格帯: タイトル画像+ラベルの表示
    valueLabel = new JLabel(" 価格帯", small_yenIcon, JLabel.LEFT);
    valueLabel.setOpaque(true);
    valueLabel.setFont(new Font("Arial", Font.PLAIN, 20));
    valueLabel.setBackground(backGroundColor);
    valueLabel.setPreferredSize(new Dimension(300, 40)); // 大きさを変更 width,height
    valueLabel.setHorizontalTextPosition(JLabel.RIGHT);

    // 価格バーの設定
    valSlider = new JSlider(200, 1000); // 価格バーを表示する
    valSlider.setFont(new Font("Arial", Font.PLAIN, 15));
    valSlider.addChangeListener(this);
    valSlider.setMajorTickSpacing(100); // | | |のメモリが登場する幅
    valSlider.setPaintTicks(true);
    valSlider.setLabelTable(valSlider.createStandardLabels(200)); // 実際の値が表示される幅
    valSlider.setPaintLabels(true);
    valSlider.setBackground(backGroundColor);
    valSlider.setPreferredSize(new Dimension(300, 70)); // 大きさを変更 width, height

    // 価格バーの実数値を表示するラベル
    showValLabel = new JLabel();
    showValLabel.setFont(new Font("Arial", Font.PLAIN, 18));
    showValLabel.setText("設定する価格： " + valSlider.getValue() + "円以内");

    // 栄養優先項目: タイトル画像+ラベルの表示
    priorityLabel = new JLabel(" 栄養優先項目", small_tagIcon, JLabel.LEFT);
    priorityLabel.setOpaque(true);
    priorityLabel.setFont(new Font("Arial", Font.PLAIN, 20));
    priorityLabel.setBackground(backGroundColor);
    priorityLabel.setPreferredSize(new Dimension(300, 40)); // 大きさを変更 width,height
    priorityLabel.setHorizontalTextPosition(JLabel.RIGHT);

    // 栄養優先事項指定を行うコンボボックスの作成
    priorityCombo = new JComboBox<String>(priorityList);
    priorityCombo.setFont(new Font("Arial", Font.PLAIN, 20));

    // 良くない書き方だが…　文字なしJLabelで空白を作る
    blank = new JLabel();
    blank.setFont(new Font("Arial", Font.PLAIN, 45));
    blank.setLayout(null); // レイアウトマネージャーを無効化
    blank.setBounds(0, 0, 200, 200); // x, y, width, height
    blank.setText("　　　　　　　");

    // 結果表示画面に移るボタンの作成
    toSubBtn = new JButton("この条件で検索する");
    toSubBtn.setBounds(0, 100, 360, 70);
    toSubBtn.setFont(new Font("Arial", Font.PLAIN, 20));
    toSubBtn.setPreferredSize(new Dimension(300, 80)); // 大きさを変更 width,height
    //toSubBtn.setMargin(new Insets(20, 20, 20, 20));
    toSubBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panelChangeToSubPanel(mf.PanelNames[1]);
      }
    });

    // 表現力をあげる無地の板 パネルにオブジェクトをaddしていく
    this.setBackground(backGroundColor);
    this.add(titleLabel);
    this.add(placeLabel);
    this.add(placeCombo);
    this.add(valueLabel);
    this.add(valSlider);
    this.add(showValLabel);
    this.add(priorityLabel);
    this.add(priorityCombo);
    this.add(blank);
    this.add(toSubBtn);

  }


  // バーの変化に合わせて表示する価格を変化させる関数
  public void stateChanged(ChangeEvent e) {
    showValLabel.setText("設定する価格： " + valSlider.getValue() + "円以内");
  }

  // SubPanelに遷移するためのメソッド
  public void panelChangeToSubPanel(String toPanelName) {
    // ここで条件から検索を行う
    System.out.println("+----------------------------+");
    System.out.println("設定場所: " + placeCombo.getSelectedItem());
    System.out.println("設定価格: " + valSlider.getValue() + "円");
    System.out.println("優先栄養: " + priorityCombo.getSelectedItem());
    System.out.println("で検索します...");
    System.out.println("+----------------------------+");

    result = Main.gacha(mealDataList, (String)placeCombo.getSelectedItem(), valSlider.getValue(),
            (String)priorityCombo.getSelectedItem());

    // 一応ターミナルにも表示する
    System.out.println("--- 絞り込み結果 ------------------------------");
    System.out.println(result);
    System.out.println("----------------------------------------------");

    mf.reloadPage(mf.PanelNames[1], result); // SubPanelを初期化する
    mf.showSubPanel((JPanel)this);
  }

}