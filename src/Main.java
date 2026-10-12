import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import static java.lang.System.exit;

public class Main extends JFrame {

    private JProgressBar progressBar;
    private JTextArea area;

    public Main() {

        JMenuBar menu = new JMenuBar();
        JMenuItem saveFile = new JMenuItem("Zapisz Raport");
        JMenuItem exitBtn = new JMenuItem("Wyjście");
        menu.add(saveFile);
        menu.add(exitBtn);

        JPanel southPanel = new JPanel();

        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        area = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(area);

        JButton btn = new JButton("Generuj Raport w Tle");
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        southPanel.add(btn);
        southPanel.add(progressBar);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRaport();
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit(0);
            }
        });

        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTextToFile();
            }
        });

        setLayout(new BorderLayout());
        add(scrollPane,BorderLayout.CENTER);
        add(southPanel,BorderLayout.SOUTH);
        setJMenuBar(menu);
        setSize(500,400);
        setTitle("INF.04 - Generator Raportów");
        setVisible(true);
    }

    public void generateRaport(){
        SwingWorker<Void,Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                for(int i=1;i<=100;i++){
                    if(i%10 == 0){
                        Thread.sleep(300);
                        publish(i);
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                int lastValue = chunks.getLast();
                progressBar.setValue(lastValue);
                area.append(lastValue+"%\n");
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(null,"Koniec generowania pliku");
            }
        };
        worker.execute();
    }

    public String saveTextToFile(){
        JFileChooser chooser = new JFileChooser();
        int value = chooser.showOpenDialog(null);
        if(value == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            String fileName= file.getName();
            if(!fileName.contains("txt")){
                JOptionPane.showMessageDialog(null,"Musisz wybrać plik txt");
                return null;
            } else if(area.getText().length()<1){
                JOptionPane.showMessageDialog(null,"Text area jest puste");
                return null;
            }

            try(PrintWriter printWriter = new PrintWriter(file.getAbsolutePath())){
                printWriter.print(area.getText());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    static void main() {
        new Main();
    }
}
