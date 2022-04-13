package service;

import controllers.SomeController;
import dao.SomeDao;
import dao.SomeOtherContainer;
import org.mockito.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class ServiceImplTest {

    private SomeDao myDao;
    private ServiceImpl service;
    private SomeController partiallyMockedController;

    @BeforeMethod
    public void setUp() {
        // you can use annotations (@Mock, @Spy and @InjectMocks)
        // but beware that if the constructor changes even a little, everything will fail to auto-inject mocks
        // call this to init everything:
//        MockitoAnnotations.initMocks(this);

        //or you can just create everything explicitly:
        myDao = mock(SomeDao.class);
        partiallyMockedController = spy(SomeController.class);

        service = new ServiceImpl(myDao);
    }

    @Test
    public void simpleExample() {

        when(myDao.getAll()).thenReturn(Arrays.asList("Test1", "Test2"));

        String test1 = service.getSomething();

        assertEquals(test1, "Test1");

        verify(myDao).getAll();
    }

    @Test
    public void mockAnObjectToPassAround(){
        SomeOtherContainer container = when(mock(SomeOtherContainer.class)
                .getRegisteredId())
                .thenReturn("ID")
                .getMock();

        String testId = service.getConnection(container);
        assertEquals(testId, "ID");
        verify(container).getRegisteredId();
    }


    //Sometimes argument matching for mocked calls needs to be a little more complex
    //than just a fixed value or anyString().
    //For that cases Mockito has its matcher class that is used with argThat(ArgumentMatcher<>).
    @Test
    public void argumentMatching() {
        //default matcher:
        SomeOtherContainer container = new SomeOtherContainer();

        // This will result in error since service is NOT a mock object, it has a real implementation
//        when(service.getConnection(any(SomeOtherContainer.class)))
//                .thenReturn("ID");
        // The exception will be thrown just here ^


        ServiceImpl service = mock(ServiceImpl.class);
        when(service.getConnection(any(SomeOtherContainer.class)))
                .thenReturn("ID");

        String id = service.getConnection(container);
        assertEquals(id, "ID");
    }

    @Test
    public void complexArgMatcher(){
        SomeOtherContainer container = new SomeOtherContainer();

        //complex matcher:
        ServiceImpl service1 = mock(ServiceImpl.class);

        service1.getConnection(container);

        verify(service1).getConnection(ArgumentMatchers.argThat(
                        argument -> argument.getRegisteredId().equals("")
                )
        );

        //will result in error, since default constructor has id = ""
//        verify(service1).getConnection(ArgumentMatchers.argThat(
//                        argument -> argument.getRegisteredId().equals("SOME FAKE ID")
//                )
//        );
    }





    //Mockito allows partial mocking (a mock that uses the real implementation instead of mocked method calls in some of its methods) in two ways.
    //You can either use .thenCallRealMethod() in a normal mock method call definition,
    //or you can create a spy instead of a mock in which case the default behavior for that will be to call the real implementation in all non-mocked methods.
    @Test
    public void partialMocking(){

        SomeOtherContainer container = when(mock(SomeOtherContainer.class)
                .getRegisteredId())
                .thenReturn("ID")
                .getMock();

        when(partiallyMockedController.getSomeData()).thenReturn("Part is mocked");

        String smth = partiallyMockedController.getSomeData();
        partiallyMockedController.doSmthWithContainer(container);

        assertEquals(smth, "Part is mocked");

        verify(partiallyMockedController).getSomeData();

        //if you use @Mock instead of @Spy on partiallyMockedService, this will fail:
        verify(partiallyMockedController).doSmthWithContainer(ArgumentMatchers.argThat(
                        argument -> argument.getRegisteredId().equals("ID")
                )
        );
    }
}