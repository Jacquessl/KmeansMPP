import javax.swing.*;
import java.util.List;

class Main {

    public static void main(String[] args)
    {
        ReadData rd = new ReadData("train-set.csv");
        List<String[]> data = rd.readData();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MyFrame(data);
            }
        });
    }
}
