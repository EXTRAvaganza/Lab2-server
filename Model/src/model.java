import java.io.IOException;

public interface model {
    public String createDepartment(String name,String id_dir) throws IOException, ClassNotFoundException;
    public  void createEmployee(String firstName,String lastName, String middleName,String depId,String phone,String salary) throws IOException;
    public  String showEmployee(String id) throws IOException, ClassNotFoundException;
    public  String showEmployees() throws IOException, ClassNotFoundException;
    public String showDepartment(String id) throws IOException, ClassNotFoundException;
    public String showDepartments() throws IOException, ClassNotFoundException;
    public  String searchDepartament(String attr,String value) throws IOException, ClassNotFoundException;
    public  String deleteDepartment(String id) throws IOException, ClassNotFoundException;
    public  String deleteEmployee(String id) throws IOException, ClassNotFoundException;
    public  String searchEmployee(String attr,String value) throws IOException, ClassNotFoundException;
    public  void changeDepartment(String id, String attr,String value) throws IOException, ClassNotFoundException;
    public  void changeEmployee(String id,String attr,String value) throws IOException, ClassNotFoundException;
}
