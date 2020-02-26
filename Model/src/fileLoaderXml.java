import Exceptions.duplicateDirector;
import Exceptions.duplicateObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class fileLoaderXml implements model {
    private String dataBase = "C:\\Users\\Рафаэль\\Desktop\\";
    public void setDataBase(String str)
    {
        dataBase = str;
    }
    private  String emptyId(String k) {
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals(k))
                res.add(stro4ki.get(i));
        }
        boolean flag = false;
        for(int i=0;i<1000;i++)
        {
            flag = false;
            for(int j=0;j<res.size();j++)
            {
                if(Integer.parseInt(res.get(j)[1])==i)
                {
                    flag = true;
                }
            }
            if(!flag)
            {
                return Integer.toString(i);
            }
        }
        return "";
    }
    private Employee findEmployee(String id) throws IOException, ClassNotFoundException {
        if(new File(dataBase+"0_"+id).exists())
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+"0_"+id);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Employee read = (Employee) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            return read;
        }
        else
            return null;
    }
    private boolean checkExistsEmpID(String id)
    {
        boolean kek;
        File file = new File(dataBase+"0_"+id);
        kek = file.exists();
        return kek;
    }
    private boolean checkExistsDepID(String id)
    {
        boolean kek;
        File file = new File(dataBase+"1_"+id);
        kek = file.exists();
        return kek;
    }
    private boolean checkExistsDepartment(Department temp) throws IOException, ClassNotFoundException {
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("1"))
                res.add(stro4ki.get(i));
        }
        for(int i=0;i<res.size();i++) {
            FileInputStream fileInputStream = new FileInputStream(dataBase + res.get(i)[0] + "_" + res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Department read = (Department) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            if (read.getName().equals(temp.getName())) {
                return true;
            }
            if(read.getDirector().equals(temp.getDirector()))
                return true;
        }
        return false;
    }
    private  boolean checkExistsEmployee(Employee temp) throws IOException, ClassNotFoundException {
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("0"))
                res.add(stro4ki.get(i));
        }
        for(int i=0;i<res.size();i++)
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Employee read = (Employee) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            if(read.getMiddleName().equals(temp.getMiddleName()) && read.getLastName().equals(temp.getLastName()) && read.getFirstName().equals(temp.getFirstName()))
            return true;
        }
        return false;
    }
    public boolean createDepartment(String name, String id_dir) throws IOException, ClassNotFoundException
    {
        String emptyId = emptyId("1");
        Department temp = new Department();
        temp.setDirector(id_dir);
        temp.setName(name);
        if(checkExistsDepartment(temp)) return false;
        if(!checkExistsEmpID(id_dir)) return false;
        FileOutputStream outputStream = new FileOutputStream(dataBase+"1_"+emptyId);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(temp);
        objectOutputStream.close();
        return true;
    }
    public  boolean createEmployee(String firstName,String lastName, String middleName,String depId,String phone,String salary) throws IOException, ClassNotFoundException {
        try
        {
            Integer.parseInt(salary);
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
        String emptyID = emptyId("0");
        Employee temp = new Employee();
        temp.setFirstName(firstName);
        temp.setLastName(lastName);
        temp.setMiddleName(middleName);
        temp.setOtdel(Integer.parseInt(depId));
        temp.setPhone(phone);
        temp.setSalary(Integer.parseInt(salary));
        if (checkExistsEmployee(temp)) return false;
        if(!checkExistsDepID(depId)) return false;
        FileOutputStream outputStream = new FileOutputStream(dataBase+"0_"+emptyID);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(temp);
        objectOutputStream.close();
        return true;
    }

    public  empNode showEmployee(String id) throws IOException, ClassNotFoundException
    {
        if(new File(dataBase+"0_"+id).exists())
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+"0_"+id);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Employee read = (Employee) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            return new empNode(Integer.parseInt(id),read);
        }
        else
            return null;
    }
    public  ArrayList<empNode>  showEmployees() throws IOException, ClassNotFoundException, TransformerException, ParserConfigurationException
    {
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<empNode> array = new ArrayList<empNode>();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("0"))
                res.add(stro4ki.get(i));
        }
        boolean flag = false;
        for(int i=0;i<res.size();i++)
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Employee read = (Employee) objectInputStream.readObject();
            array.add(new empNode(Integer.parseInt(res.get(i)[1]),read));
            fileInputStream.close();
            objectInputStream.close();
            flag = true;
        }
        if(!flag) return null;
        return array;
    }
    public depNode showDepartment(String id) throws IOException, ClassNotFoundException
    {
        if(new File(dataBase+"1_"+id).exists())
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+"1_"+id);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Department read = (Department) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            return new depNode(Integer.parseInt(id),read);
        }
        else
            return null;
    }
    public ArrayList<depNode> showDepartments() throws IOException, ClassNotFoundException
    {
        ArrayList<depNode> array = new ArrayList<depNode>();
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("1"))
                res.add(stro4ki.get(i));
        }
        boolean flag = false;
        for(int i=0;i<res.size();i++)
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            array.add(new depNode(Integer.parseInt(res.get(i)[1]),(Department)objectInputStream.readObject()));
            fileInputStream.close();
            objectInputStream.close();
            flag = true;
        }
        if(!flag) return null;
        return array;
    }
    public  ArrayList<depNode> searchDepartament(String attr,String value) throws IOException, ClassNotFoundException
    {
        ArrayList<depNode> array = new ArrayList<depNode>();
        boolean flag = false;
        String resultat = "";
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("1"))
                res.add(stro4ki.get(i));
        }
        for(int i=0;i<res.size();i++)
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Department read = (Department) objectInputStream.readObject();
            switch (attr)
            {
                case("имя"):
                {
                    if(read.getName().equals(value))
                    {

                        array.add(new depNode(Integer.parseInt(res.get(i)[1]),read));
                        flag = true;
                    }
                    break;
                }
                case("директор"):
                {
                    if(value.equals(read.getDirector()))
                    {
                        array.add(new depNode(Integer.parseInt(res.get(i)[1]),read));
                        flag = true;
                    }
                    break;
                }
                default:
                {
                    i=res.size();
                    break;
                }
            }
        }
        if(!flag) return null;
        return array;
    }
    public  String deleteDepartment(String id) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(dataBase+"1_"+id);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Department read = (Department) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        File file = new File(dataBase+"1_"+id);
        if(checkExistsDepID(id))
        {
            ArrayList<empNode> empNodes =searchEmployee("отдел",id);
            for(int i=0;i<empNodes.size();i++)
            {
                changeEmployee(Integer.toString(empNodes.get(i).id),"отдел",Integer.toString(0));
            }
            file.delete();
            return "ID отдела:" + id + " успешно удалён";
        }
        return "Отдел № "+id+" не существует.";
    }
    public  String deleteEmployee(String id) throws IOException, ClassNotFoundException
    {
        if(checkExistsEmpID(id)) {
        FileInputStream fileInputStream = new FileInputStream(dataBase+"0_"+id);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Employee read = (Employee) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        if(id.equals(showDepartment(Integer.toString(read.getOtdel())).data.getDirector()))
            return "Сотрудник является директором "+read.getOtdel()+" отдела. Сначала назначьте ему замену";
        File file = new File(dataBase+"0_"+id);

            file.delete();
            return "ID сотрудника:" + id  + "успешно удалён";
        }
        else {
            return "Сотрудник с таким ID не существует";
        }
    }
    public  ArrayList<empNode> searchEmployee(String attr,String value) throws IOException, ClassNotFoundException
    {
        ArrayList<empNode> array = new ArrayList<empNode>();
        String resultat = "";
        boolean flag = false;
        File file = new File(dataBase);
        String[] test = file.list();
        ArrayList<String[]> stro4ki = new ArrayList<String[]>();
        for (int i = 0; i < test.length; i++)
            stro4ki.add(test[i].split("_"));
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (int i = 0; i < stro4ki.size(); i++)
        {
            if(stro4ki.get(i)[0].equals("0"))
                res.add(stro4ki.get(i));
        }
        for(int i=0;i<res.size();i++)
        {
            FileInputStream fileInputStream = new FileInputStream(dataBase+res.get(i)[0]+"_"+res.get(i)[1]);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Employee read = (Employee) objectInputStream.readObject();
            switch (attr)
            {
                case("имя"):
                {
                    if(read.getFirstName().equals(value))
                    {
                        array.add(new empNode(Integer.parseInt(res.get(i)[1]),read));
                        flag = true;
                    }
                    break;
                }
                case("фамилия"):
                {
                    if(read.getLastName().equals(value))
                    {
                        array.add(new empNode(Integer.parseInt(res.get(i)[1]),read));
                        flag = true;
                    }
                    break;
                }
                case("отчество"):
                {
                    if(read.getMiddleName().equals(value))
                    {
                        array.add(new empNode(Integer.parseInt(res.get(i)[1]),read));
                        flag = true;
                    }
                    break;
                }
                case("телефон"):
                {
                    if(read.getPhone().equals(value))
                    {
                        array.add(new empNode(Integer.parseInt(res.get(i)[1]),read));
                        flag = true;
                    }
                    break;
                }
                case("отдел"):
                {
                    if(read.getOtdel() == Integer.parseInt(value))
                    {
                        array.add(new empNode(Integer.parseInt(res.get(i)[1]),read));
                        flag = true;
                    }
                    break;
                }
                case("зарплата"):
                {
                    if(read.getSalary() == Integer.parseInt(value))
                    {
                        array.add(new empNode(Integer.parseInt(res.get(i)[1]),read));
                        flag = true;
                    }
                    break;
                }
                default:
                {
                    i=res.size();
                    return null;
                }
            }
            fileInputStream.close();
            objectInputStream.close();
        }
        if(!flag) return null;
        return array;
    }
    public  String changeDepartment(String id, String attr,String value) throws IOException, ClassNotFoundException {

        FileInputStream fileInputStream = new FileInputStream(dataBase+"1_"+id);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Department read = (Department) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        switch (attr) {
            case ("имя"): {
                read.setName(value);
                FileOutputStream outputStream = new FileOutputStream(dataBase+"1_"+id);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(read);
                objectOutputStream.close();
                return "Название отдела изменено";
            }
            case ("директор"): {
                if(checkExistsEmpID(value)){
                if (Integer.toString(showEmployee(value).data.getOtdel()).equals(id) || Integer.toString(showEmployee(value).data.getOtdel()).equals("0"))
                {
                    read.setDirector(value);
                    changeEmployee(value,"отдел",id);
                    FileOutputStream outputStream = new FileOutputStream(dataBase+"1_"+id);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(read);
                    objectOutputStream.close();
                    return "Директор отдела успешно изменён";
                }
                else return "Указанный сотрудник числится в другом отделе. Измените отдел сотрудника "+value;
            }
                else return "Сотрудник с указанным ID не существует";
            }
        }

        return null;
    }
    public  String changeEmployee(String id,String attr,String value) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(dataBase+"0_"+id);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Employee read = (Employee) objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
        switch (attr) {
            case ("имя"): {
                read.setFirstName(value);
                FileOutputStream outputStream = new FileOutputStream(dataBase+"0_"+id);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(read);
                objectOutputStream.close();
                return "Имя изменено";
            }
            case ("фамилия"): {
                read.setLastName(value);
                FileOutputStream outputStream = new FileOutputStream(dataBase+"0_"+id);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(read);
                objectOutputStream.close();
                return "Фамилия изменена";
            }
            case("отчество"):
            {
                read.setMiddleName(value);
                FileOutputStream outputStream = new FileOutputStream(dataBase+"0_"+id);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(read);
                objectOutputStream.close();
                return "Отчество изменено";
            }
            case("отдел"):
            {
                if(!checkExistsDepID(value)) return "Указанный отдел не существует";
                read.setOtdel(Integer.parseInt(value));
                FileOutputStream outputStream = new FileOutputStream(dataBase+"0_"+id);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(read);
                objectOutputStream.close();
                return "Отдел изменен";
            }
            case("зарплата"):
            {
                try{
                read.setSalary(Integer.parseInt(value));
                FileOutputStream outputStream = new FileOutputStream(dataBase+"0_"+id);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(read);
                objectOutputStream.close();
                return"Зарплата изменена";
                }
                catch (NumberFormatException ex)
                {
                    return "Неверно введена зарплата";
                }
            }
            case("телефон"):
            {
                read.setPhone(value);
                FileOutputStream outputStream = new FileOutputStream(dataBase+"0_"+id);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(read);
                objectOutputStream.close();
                return "Номер телефона изменен";
            }
        }

        return null;
    }
}
