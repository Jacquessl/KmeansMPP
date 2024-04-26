import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

class MyFrame
        extends JFrame
        implements ActionListener {

    private Container c;
    private JLabel title;
    private JLabel name;
    private JFormattedTextField dlugoscListka;
    private JFormattedTextField szerokoscListka;
    private JFormattedTextField dlugoscPlatka;
    private JFormattedTextField szerokoscPlatka;
    private JFormattedTextField k = new JFormattedTextField();
    private JButton sub;
    private JButton reset;
    private JButton zbiorTestowy;
    private JTextArea tout;
    private JScrollPane scrollPane;
    private List<String[]> data;
    private NumberFormatter intFormatter;
    private NumberFormatter formatter;
    private boolean workerDone = true;
    private List<String[]> testData;
    private int testIndex;
    private int accurateTest;
    private int possibleTest;
    private boolean wypisywacDokladnosc = false;
    public MyFrame(List<String[]> data)
    {
        this.data = data;

        NumberFormat floatFormat = DecimalFormat.getInstance();
        formatter = new NumberFormatter(floatFormat);
        formatter.setValueClass(Float.class);
        formatter.setMinimum(0.0f);
        formatter.setMaximum(Float.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);


        DecimalFormat decimalFormat = (DecimalFormat) floatFormat;
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        decimalFormat.setGroupingUsed(false);

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        intFormatter = new NumberFormatter(integerFormat);
        intFormatter.setValueClass(Integer.class);
        intFormatter.setMinimum(1);
        intFormatter.setMaximum(Integer.MAX_VALUE);
        intFormatter.setAllowsInvalid(true);
        intFormatter.setCommitsOnValidEdit(true);

        setTitle("Grupowanie k-means");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        title = new JLabel("Grupowanie k-means");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(400, 30);
        title.setLocation(250, 30);
        c.add(title);


        name = new JLabel("Ilość grup");
        name.setFont(new Font("Arial", Font.PLAIN, 20));
        name.setSize(200, 30);
        name.setLocation(20, 100);
        c.add(name);

        k.setText("3");
        k.setEditable(true);
        k.setFont(new Font("Arial", Font.PLAIN, 15));
        k.setSize(190, 30);
        k.setLocation(220, 100);
        c.add(k);

        sub = new JButton("Analizuj");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 20);
        sub.setLocation(150, 400);
        sub.addActionListener(this);
        c.add(sub);


        tout = new JTextArea();
        tout.setFont(new Font("Arial", Font.PLAIN, 15));
        tout.setLineWrap(true);
        tout.setWrapStyleWord(true);
        tout.setMargin(new Insets(10, 10, 10, 10));
        scrollPane = new JScrollPane(tout);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setSize(300, 400);
        scrollPane.setLocation(500, 100);
        c.add(scrollPane);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == sub) {
            AnalyzeData ad = new AnalyzeData(data, Integer.parseInt(k.getText()), tout);
            //try {
                String result = ad.analyze();

            //}catch (NumberFormatException ex){
                System.out.println(result);
                //tout.setText("Wpisz wartości");
                tout.setEditable(false);

           // }
        }
        else if (e.getSource() == reset) {
            formatter.setAllowsInvalid(true);
            intFormatter.setAllowsInvalid(true);
            String def = "";
            dlugoscListka.setText(def);
            szerokoscListka.setText(def);
            dlugoscPlatka.setText(def);
            szerokoscPlatka.setText(def);
            k.setText(def);
            tout.setText(def);
            formatter.setAllowsInvalid(false);
            intFormatter.setAllowsInvalid(false);
        }
        else if(e.getSource() == zbiorTestowy) {
            try {
                int kValue = (int) k.getValue();
                ReadData rd = new ReadData("test-set.txt");
                testData = rd.readData();
                accurateTest = 0;
                possibleTest = 1;
                JFormattedTextField[] textFields = {dlugoscListka, szerokoscListka, dlugoscPlatka, szerokoscPlatka};
                SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        for (String[] str : testData) {
                            reset.doClick();
                            for (int i = 0; i < textFields.length; i++) {
                                textFields[i].setText(str[i].replace(".", ","));
                                Thread.sleep(100);
                            }
                            wypisywacDokladnosc = true;
                            k.setText("" + kValue);
                            sub.doClick();
                            testIndex++;
                            possibleTest++;
                            Thread.sleep(2000);

                            wypisywacDokladnosc=false;
                        }
                        testIndex=0;
                        return null;
                    }

                    @Override
                    protected void done() {
                        workerDone = true;
                    }
                };
                worker.execute();
            } catch (NullPointerException exc) {
                tout.setText("Wpisz ilość sąsiadów");
            }
        }
    }
}
