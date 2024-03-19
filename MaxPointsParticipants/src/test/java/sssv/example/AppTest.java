package sssv.example;

import domain.Student;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.StudentFileRepository;
import repository.StudentXMLRepo;
import service.Service;
import validation.StudentValidator;
import validation.ValidationException;

import static org.mockito.Mockito.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    @Mock
    private StudentValidator studentValidator;

    @Mock
    private StudentXMLRepo studentFileRepository;

    private Service service;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        service = new Service(studentFileRepository,
                studentValidator,
                null,
                null,
                null,
                null); // Adjust constructor as necessary
    }


    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    /**
     * Test adding a new student
     */
    public void testAddNewStudent() {
        Student student = new Student("1", "Ghita", 933, "ghita@ghita.com"); // Adjust constructor as necessary
        when(studentFileRepository.save(student)).thenReturn(student);

        assertEquals(student, service.addStudent(student));
        verify(studentValidator).validate(student);
        verify(studentFileRepository).save(student);
    }

    /**
     * Test adding an existing student
     */
    public void testAddExistingStudent() {
        String expectedMessage = "Invalid student details";
        Student student1 = new Student("1", "GhitaInvalid", 933, "ghita@ghita.com"); // Adjust constructor as necessary
        doThrow(new ValidationException(expectedMessage)).when(studentValidator).validate(student1);
        try {
            // Act
            service.addStudent(student1);
            // Assert failure if the above method call does not throw the exception
            fail("Expected a ValidationException to be thrown");
        } catch (ValidationException e) {
            // Assert that the thrown exception is what we expect
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}
