/*
* This file is copyright (c) 2018 Uis Yudha
* 
* This is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
 */
package otong;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class is use for text preprocessing that was classified in the input
 * file.
 *
 * @author uisyudha
 */
public class TextProcessing {

    // Input file
    private final String inputFile;

    // Output File
    private final String outputFile;

    // Object to write output filee
    private BufferedWriter writer = null;

    // Dictionary for filtering text
    public Map<String, List<String>> dictionary = null;
    public Set<String> listword = null;

    // Constructor
    public TextProcessing(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.dictionary = new HashMap<>();
        this.listword = new HashSet<>();

        List<String> aman = Arrays.asList("aman", "tenteram", "tentram", "tentrem");
        List<String> antre = Arrays.asList("antre", "antri", "antriannya", "mengantri", "ngantri");
        List<String> bagus = Arrays.asList("bagus", "bgs", "cantik", "cntk", "gaul",
                "gde", "gede", "gembira", "indah", "kece", "keren", "kren", "luas",
                "mantab", "mantap", "mantaps", "menarik", "menyenangkan", "nyaman",
                "ok", "oke", "senang", "sng", "super", "superr", "superrr");
        List<String> bahaya = Arrays.asList("bahaya", "berbahaya", "ati", "ati2",
                "ati-ati", "awas", "deras", "deres", "hati", "hati2", "hati-hati",
                "kecelakaan", "lgsor", "longsor", "rawan");
        List<String> berantakan = Arrays.asList("berantakan", "berserakan", "brantakan");
        List<String> bersih = Arrays.asList("bersih", "brsih", "wangi");
        List<String> bingung = Arrays.asList("bingung", "bgung", "bingungin", "membingungkan");
        List<String> bohong = Arrays.asList("bohong", "bhg", "boong", "dibohongi", "diboongin", "ditipu", "ketipu", "penipu", "penipuan");
        List<String> bosan = Arrays.asList("bosan", "bosen", "bosenin", "jenuh", "membosankan ");
        List<String> copet = Arrays.asList("copet", "copetnya", "dicopet", "kecopetan", "pencopet");
        List<String> dekat = Arrays.asList("dekat", "deket", "dket", "dkt");
        List<String> galak = Arrays.asList("galak", "cemberut", "judes", "jutek", "masam", "menyebalkan", "nyebelin", "sebal", "sebel");
        List<String> jauh = Arrays.asList("jauh", "jaauuhh", "jau", "jauhh", "jauuh", "jauuuhh", "jauuuuh");
        List<String> jelek = Arrays.asList("jelek", "buruk", "jlek", "jlk", "menyedihkan", "menyesal", "parah", "percuma", "prah", "sdh", "sdih", "sedih", "sempit", "sempt", "sia sia", "sia2", "smpit", "smpt");
        List<String> khas = Arrays.asList("khas", "unik");
        List<String> kotor = Arrays.asList("kotor", "bau", "jorok", "jrk", "jrok", "ktor", "ktr");
        List<String> lama = Arrays.asList("lama", "macet", "mact", "mcet");
        List<String> lancar = Arrays.asList("lancar", "cepat", "cepet", "cpt", "lncar", "lncr");
        List<String> mahal = Arrays.asList("mahal", "menguras", "mhal", "mhl");
        List<String> mudah = Arrays.asList("mudah", "gampang", "gmpg", "gmpng", "gmpang");
        List<String> murah = Arrays.asList("murah");
        List<String> panas = Arrays.asList("panas", "pans", "pnas", "terik");
        List<String> ramah = Arrays.asList("ramah", "jujur", "baik", "sopan");
        List<String> rapi = Arrays.asList("rapi", "beraturan", "rapih", "rp", "rpi", "rpih", "tertata", "tertib");
        List<String> sejuk = Arrays.asList("sejuk", "dgin", "dgn", "dingin", "mendg", "mendung", "mndg", "mndung", "rindang", "rndg", "segar", "seger", "segr", "sgar", "sger", "sgr");
        List<String> sesat = Arrays.asList("sesat", "tersesat", "nyasar");
        List<String> sulit = Arrays.asList("sulit", "susah");

        dictionary.put("aman", aman);
        dictionary.put("antre", antre);
        dictionary.put("bagus", bagus);
        dictionary.put("bahaya", bahaya);
        dictionary.put("berantakan", berantakan);
        dictionary.put("bersih", bersih);
        dictionary.put("bingung", bingung);
        dictionary.put("bohong", bohong);
        dictionary.put("bosan", bosan);
        dictionary.put("copet", copet);
        dictionary.put("dekat", dekat);
        dictionary.put("galak", galak);
        dictionary.put("jauh", jauh);
        dictionary.put("jelek", jelek);
        dictionary.put("khas", khas);
        dictionary.put("kotor", kotor);
        dictionary.put("lama", lama);
        dictionary.put("lancar", lancar);
        dictionary.put("mahal", mahal);
        dictionary.put("mudah", mudah);
        dictionary.put("murah", murah);
        dictionary.put("panas", panas);
        dictionary.put("ramah", ramah);
        dictionary.put("rapi", rapi);
        dictionary.put("sejuk", sejuk);
        dictionary.put("sesat", sesat);
        dictionary.put("sulit", sulit);

        dictionary.entrySet().forEach((entry) -> {
            List<String> values = entry.getValue();
            this.listword.addAll(values);
        });
    }

    public void run() throws FileNotFoundException, IOException {
        File file = new File(this.outputFile);
        writer = new BufferedWriter(new FileWriter(file));
        List<String> commentar = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(this.inputFile));
        String[] line;

        CSVReader csvreader = new CSVReader(new FileReader(this.inputFile));

        while ((line = csvreader.readNext()) != null) {
            // Replace full stop
            line[2] = line[2].replace(".", "");
            // Removing new line
            line[2] = line[2].replace("\n", " ");
            // Case folding
            line[2] = line[2].toLowerCase();

            //Filtering
            String[] s = line[2].split(" ");
            Set<String> sentence = new HashSet<>(Arrays.asList(s));
            sentence.retainAll(this.listword);

            //Klasifikasi kategori
            LinkedHashSet<String> kategori = new LinkedHashSet<>(); //Unique remove duplicity
            if (!sentence.isEmpty()) {
                sentence.forEach((str) -> {
                    dictionary.entrySet().forEach((entry) -> {
                        if(entry.getValue().contains(str)){
                            kategori.add(entry.getKey());
                        }
                    });
                });
                kategori.add("aakm");
            }
            //Add aakm to the item set
            
            //System.out.println("S: " + sentence);
            //System.out.println("K: " + kategori);


            if (!kategori.isEmpty()) {
                commentar.add(String.join(" ", kategori));
            }
        }

        for (String str : commentar) {
            writer.write(str);
            writer.newLine();
        }
        writer.close();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        TextProcessing tp = new TextProcessing("tripadvisoralunalun.csv", "output.txt");
        tp.run();
        
        /**System.getProperty("user.dir") + "\\" + this.outputFile)
         * String[] DICT = new String[]{"aku", "cinta", "kamu"}; String[] STC =
         * new String[]{"aku", "sayang", "kamu"}; Set<String> dictionary = new
         * HashSet<>(Arrays.asList(DICT)); Set<String> sentence = new
         * HashSet<>(Arrays.asList(STC));
         *
         * System.out.println("sentece before intersection: " + sentence);
         *
         * sentence.retainAll(dictionary);
         *
         * System.out.println("sentence after intersection: " + sentence);
         *
         * tp.dictionary.entrySet().forEach((entry) -> { String key =
         * entry.getKey();
         *
         * List<String> values = entry.getValue();
         *
         * System.out.println("Key = " + key);
         *
         * System.out.println("Values = " + values + "\n"); });
         *
         */
    }
}
