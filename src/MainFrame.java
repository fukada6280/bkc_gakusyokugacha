import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    public String[] PanelNames = { "mp", "Sub" }; // この名前でPanel指定をする

    // 各種パネルを宣言
    MainPanel mp;
    SubPanel sp;
    MealData initResult = new MealData();


    public MainFrame() {
        // csvファイルを読み込む
        //mealDataList = Main.loadCsv("mealData.csv");

        mp = new MainPanel(this, PanelNames[0]);
        sp = new SubPanel(this, PanelNames[1], initResult);

        this.add(mp);
        mp.setVisible(true);

        this.add(sp);
        sp.setVisible(false);

        this.setResizable(false); // 大きさ変更不可にする
        this.setBounds(100, 100, 360, 640);
    }

    // ページの再描画用メソッド
    // SubPanelはこれを行い変更を反映させる
    // 逆にMainPanelでは行わず結果を保持する
    public void reloadPage(String reloadPanelName) {
        if(reloadPanelName.equals(PanelNames[0])) {
            this.remove(this.mp);
            MainPanel mp = new MainPanel(this, PanelNames[0]);
            this.add(mp);
        } else if (reloadPanelName.equals(PanelNames[1])) {
            this.remove(this.sp);
            SubPanel sp = new SubPanel(this, PanelNames[1], initResult);
            this.add(sp);
        }
    }

    public void reloadPage(String reloadPanelName, MealData result) {
        if(reloadPanelName.equals(PanelNames[0])) {
            this.remove(this.mp);
            MainPanel mp = new MainPanel(this, PanelNames[0]);
            this.add(mp);
        } else if (reloadPanelName.equals(PanelNames[1])) {
            this.remove(this.sp);
            SubPanel sp = new SubPanel(this, PanelNames[1], result);
            this.add(sp);
        }
    }

    // パネル遷移メソッド
    // 予め全パネルを作成して表示・非表示を切り替える方式

    // メインパネルを表示
    public void showMainPanel(JPanel nowPanel) {
        nowPanel.setVisible(false);
        mp.setVisible(true);
    }

    // サブパネルを表示
    public void showSubPanel(JPanel nowPanel) {
        nowPanel.setVisible(false);
    }

}