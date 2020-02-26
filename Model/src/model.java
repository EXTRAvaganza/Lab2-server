import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;

public interface model {
    public boolean createDepartment(String name,String id_dir) throws IOException, ClassNotFoundException;
    public  boolean createEmployee(String firstName,String lastName, String middleName,String depId,String phone,String salary) throws IOException, ClassNotFoundException;
    public  empNode showEmployee(String id) throws IOException, ClassNotFoundException;
    public ArrayList<empNode> showEmployees() throws IOException, ClassNotFoundException, TransformerException, ParserConfigurationException;
    public depNode showDepartment(String id) throws IOException, ClassNotFoundException;
    public ArrayList<depNode> showDepartments() throws IOException, ClassNotFoundException;
    public  ArrayList<depNode> searchDepartament(String attr,String value) throws IOException, ClassNotFoundException;
    public  String deleteDepartment(String id) throws IOException, ClassNotFoundException;
    public  String deleteEmployee(String id) throws IOException, ClassNotFoundException;
    public  ArrayList<empNode> searchEmployee(String attr,String value) throws IOException, ClassNotFoundException;
    public  String changeDepartment(String id, String attr,String value) throws IOException, ClassNotFoundException;
    public  String changeEmployee(String id,String attr,String value) throws IOException, ClassNotFoundException;
}
