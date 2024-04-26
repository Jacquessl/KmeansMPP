import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class AnalyzeData {
    private List<String[]> data;
    private int k;
    private Map<Integer, List<Double>> centroidy = new LinkedHashMap<>();
    private int counter = 0;
    private Map<Integer, List<String[]>> grupy = new LinkedHashMap<>();
    boolean takieSame = false;
    private JTextArea tout;
    private Map<Integer, List<Double>> kwadratyOdleglosci = new LinkedHashMap<>();
    public AnalyzeData(List<String[]> data, int k, JTextArea tout){
        this.data = data;
        this.tout = tout;
        this.k = k;
        for(int j = 0; j<k;j++) {
            grupy.put(j, new ArrayList<>());
            centroidy.put(j, new ArrayList<>());
            kwadratyOdleglosci.put(j, new ArrayList<>());
            for (int i = data.get(0).length-1; i > 0; i--) {
                centroidy.get(j).add(Math.random()*2*i);
            }
        }
    }
    public String analyze(){
        counter++;
        grupy = new LinkedHashMap<>();
        kwadratyOdleglosci = new LinkedHashMap<>();
        for(int j = 0; j<k;j++) {
            grupy.put(j, new ArrayList<>());
            kwadratyOdleglosci.put(j, new ArrayList<>());
        }
        for(String[] strArr : data){
            double[] sumyKwadratow = new double[k];
            int indexSumyKwadratow = 0;
            for(Map.Entry<Integer, List<Double>> entry : centroidy.entrySet()) {
                int counterKtoryStr = 0;
                for (String str : strArr) {
                    if(counterKtoryStr!=entry.getValue().size()) {
                        sumyKwadratow[indexSumyKwadratow] += Math.pow(Double.parseDouble(str) - entry.getValue().get(counterKtoryStr), 2);
                        counterKtoryStr++;
                    }
                }
                indexSumyKwadratow++;
            }
            double min = sumyKwadratow[0];
            int indexMin = 0;
            int index = 0;
            for(double suma : sumyKwadratow){
                if(suma<min){
                    min = suma;
                    indexMin = index;
                }
                index++;
            }
            grupy.get(indexMin).add(strArr);
            kwadratyOdleglosci.get(indexMin).add(min);
        }
        String toPrint = "";
        toPrint += "Iteracja "+ counter+": \n";
        for(Map.Entry<Integer, List<Double>> entry : kwadratyOdleglosci.entrySet()){
            toPrint += "Grupa " + (entry.getKey()+1) + ": \n";
            for(String[] str :grupy.get(entry.getKey())){
                for(String s : str){
                    toPrint += s + ", ";
                }
                toPrint += "\n";
            }
            double sum = 0;
            for(double d : entry.getValue()){
                sum += d;
            }
            toPrint += "Suma kwadratów: "+ sum + "\n";
        }
        tout.setText(toPrint);
        if(!takieSame) {
            liczCentroidy();
        }
        return "A";
    }
    private void liczCentroidy(){
        List<List<Double>> stareCentroidy = new ArrayList<>();
        for(Map.Entry<Integer, List<Double>> entry : centroidy.entrySet()){
            stareCentroidy.add(entry.getValue());
        }
        takieSame = true;
        centroidy = new LinkedHashMap<>();
        for(Map.Entry<Integer, List<String[]>> entry : grupy.entrySet()){
            try {
                for (int i = 0; i < entry.getValue().get(0).length-1; i++) {
                    double sum = 0;
                    for (int j = 0; j < entry.getValue().size(); j++) {
                        sum += Double.parseDouble(entry.getValue().get(j)[i]);
                    }
                    centroidy.putIfAbsent(entry.getKey(), new ArrayList<>());
                    centroidy.get(entry.getKey()).add(sum / entry.getValue().size());
                }
            }catch (IndexOutOfBoundsException e){
                centroidy.put(entry.getKey(), stareCentroidy.get(entry.getKey()));
            }
            if(entry.getValue().size()==0){
                takieSame = false;
                centroidy.put(entry.getKey(), new ArrayList<>());
                for (int i = data.get(0).length-1; i> 0; i--) {
                    centroidy.get(entry.getKey()).add(Math.random()*2*i);
                }
            }
        }
        int index = 0;

        for(Map.Entry<Integer, List<Double>> entry : centroidy.entrySet()){
            for(int i = 0; i<entry.getValue().size();i++){
                if(!Objects.equals(entry.getValue().get(i), stareCentroidy.get(index).get(i))){
                    takieSame = false;
                }
            }
            index++;

        }
        analyze();
    }
}
