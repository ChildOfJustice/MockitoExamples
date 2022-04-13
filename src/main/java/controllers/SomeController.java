package controllers;

import dao.SomeDao;
import dao.SomeOtherContainer;

public class SomeController {

    private final String someData;

    public SomeController(String someData){
        this.someData = someData;
    }

    //needed to use @Spy
    public SomeController(){
        someData = null;
    }

    public String getSomeData(){
        return someData;
    }

    public void doSmthWithContainer(SomeOtherContainer container){
        //...
    }
}
