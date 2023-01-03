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

    //1. receives id_order, total nr of products and the current number of processed products
    //2. search the input file for products
    //3. when the product is found write at the output file -> !!! needs of synchronisation

    final FileWriter out;
    String idOrder;
    int nrProd;
    int crtProd;
    File inFile;
    Scanner inF;

    public ProductTask(Scanner inF, FileWriter outFile, String idOrder, int nrProd, int crtProd,
                       File inFile) {
        this.inF = inF;
        this.out = outFile;
        this.idOrder = idOrder;
        this.nrProd = nrProd;
        this.crtProd = crtProd;
        this.inFile = inFile;
    }

    @Override
    protected void compute() {
        if (crtProd == 1) {
            try {
                inF = new Scanner(inFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        int found = 0;
        while (inF.hasNextLine()) {
            String line = inF.nextLine();
            String[] args = line.split(",");

            if (args[0].equals(idOrder)) {
                try {
                    synchronized (out) {
                        out.write(line + ",shipped\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (crtProd < nrProd) {
            ProductTask t = new ProductTask(inF, out, idOrder, nrProd, crtProd + 1, null);
            t.fork();
            t.join();
        }

        if(crtProd == 1) {
            inF.close();
        }
    }
}
