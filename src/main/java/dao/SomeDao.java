package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SomeDao {
    public List<String> getAll(){
        doSmth("test");
        return Arrays.asList("1", "2");
    }

    private void doSmth(String element){

    }
}

