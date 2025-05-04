package br.ufal.ic.jackut;
import java.io.IOException;

import easyaccept.EasyAccept;

public class Main {
    public static void main(String[] args) throws IOException {

        String userFacade =  "br.ufal.ic.jackut.facade.Facade";
        String[] userUS= {
            "src/test/user-stories/us1_1.txt",
            "src/test/user-stories/us1_2.txt",
            "src/test/user-stories/us2_1.txt",
            "src/test/user-stories/us2_2.txt",
            "src/test/user-stories/us3_1.txt",
            "src/test/user-stories/us3_2.txt",
            "src/test/user-stories/us4_1.txt",
            "src/test/user-stories/us4_2.txt",
            "src/test/user-stories/us5_1.txt",
            "src/test/user-stories/us5_2.txt",
            "src/test/user-stories/us6_1.txt",
            "src/test/user-stories/us6_2.txt",
            "src/test/user-stories/us7_1.txt",
            "src/test/user-stories/us7_2.txt",
            "src/test/user-stories/us8_1.txt",
            "src/test/user-stories/us8_2.txt",
            "src/test/user-stories/us9_1.txt",
            //"src/test/user-stories/us9_2.txt",
        };
        
        for(String storie : userUS) {
            EasyAccept.main(new String[]{userFacade, storie});
        }
    }
}
