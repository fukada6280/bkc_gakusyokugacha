import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    public String[] PanelNames = { "mp", "Sub" };
    MainPanel mp = new MainPanel(this, PanelNames[0]);
    SubPanel sp = new SubPanel(this, PanelNames[1]);

    public MainFrame() {
        this.add(mp);
        mp.setVisible(true);
        this.add(sp);
        sp.setVisible(false);
        this.setResizable(false); // 大きさ変更不可にする
        this.setBounds(100, 100, 400, 200);
    }

    // パネル移動の時呼び出される
    public void PanelChange(JPanel jp, String str) {
        // System.out.println("You go out of " + jp.getName()); // 移動時に通知する
        String name = jp.getName();
        if (name == PanelNames[0]) {
            // メインフレームを離れるときに呼び出される
            System.out.println("設定価格: " + mp.placeCombo.getSelectedItem());
            System.out.println("設定価格: " + mp.valSlider.getValue() + "円");
            System.out.println("優先栄養: " + mp.priorityCombo.getSelectedItem());
            System.out.println("で検索します...");
            mp = (MainPanel) jp;
            mp.setVisible(false);
        } else if (name == PanelNames[1]) {
            // サブフレームを離れるときに呼び出される
            sp = (SubPanel) jp;
            sp.setVisible(false);
        }

        if (str == PanelNames[0]) {
            mp.setVisible(true);
        } else if (str == PanelNames[1]) {
            sp.setVisible(true);
        }
    }
}