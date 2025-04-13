package br.ufal.ic.jackut;
import java.io.IOException;

import easyaccept.EasyAccept;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] args2 = {
            "br.ufal.ic.jackut.facade.Facade",
            "src/test/user-stories/us1_1.txt",
        };
        
        EasyAccept.main(args2);
    }
}
