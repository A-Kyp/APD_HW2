import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.RecursiveAction;

public class ProductTask extends RecursiveAction {
    //passed arguments in constructor:
        // -> fileWriter = where to write
        // -> id comanda
        // -> nr produse
        // -> File input

    final FileWriter out;
    String idOrder;
    int nrProd;
    File inFile;

    Scanner inF;

    public ProductTask(FileWriter outFile, String idOrder, int nrProd, File inFile) {
        this.out = outFile;
        this.idOrder = idOrder;
        this.nrProd = nrProd;
        this.inFile = inFile;
    }

    //primeste id_comanda si id_produs
    //cauta in fisierul de produse
    //daca gaseste -> scrie in fisierul de iesire
    @Override
    protected void compute() {
        try {
            inF = new Scanner(inFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int found = 0;
        while (inF.hasNextLine()) {
            String line = inF.nextLine();
            String[] args = line.split(",");
            if (args[0].equals(idOrder)) {
                try {
                    synchronized (out){
                        out.write(line + ",shipped\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
