package com.antispam.Services.JMetal;


import org.uma.jmetal.runner.multiobjective.ABYSS;

import java.util.Iterator;
import java.util.Map;

public class AlgorithmExecutorESII {



    public void testRun(String algorithmName){
        String[] args = {};
        switch(algorithmName){
            case"NSGAIII":

                org.uma.jmetal.runner.multiobjective.NSGAIIIRunner.main(args);

                break;
            case"ABYSS":
                args = new String[]{};

                ABYSS ABYSS = new ABYSS();
                //pedir propriedades
                System.out.println("É necessario inserir as seguintes propriedades: ");
                Iterator it = ABYSS.getHmapProperty().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    System.out.println(pair.getKey());
                    it.remove(); // avoids a ConcurrentModificationException
                }


                //ABYSS.getHmapProperty();
                //inserir propriedades
                //ABYSS.setIntHmapProperty();

                try {
                    //ABYSS.main(args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


        }

    }
}
