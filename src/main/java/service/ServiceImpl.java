package service;

import dao.SomeDao;
import dao.SomeOtherContainer;

public class ServiceImpl implements Service{

    private final SomeDao myDao;

    public ServiceImpl(SomeDao dao){
        myDao = dao;
    }

    @Override
    public String getSomething() {
        return myDao.getAll().get(0);
    }

    public String getConnection(SomeOtherContainer container){
        return container.getRegisteredId();
    }
}
