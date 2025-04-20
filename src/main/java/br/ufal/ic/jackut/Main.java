package br.ufal.ic.jackut;
import java.io.IOException;

import easyaccept.EasyAccept;

public class Main {
    public static void main(String[] args) throws IOException {

        String userFacade =  "br.ufal.ic.jackut.facade.Facade";
        String[] userStories = {
            "src/test/user-stories/us1_1.txt",
            "src/test/user-stories/us1_2.txt",
            "src/test/user-stories/us2_1.txt",
            "src/test/user-stories/us2_2.txt",
        };
        
        for(String storie : userStories) {
            EasyAccept.main(new String[]{userFacade, storie});
        }
        
       
    }
}
