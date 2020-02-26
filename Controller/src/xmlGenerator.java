import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;

public class xmlGenerator {
    public static Document getEmployees(ArrayList<empNode> array, int type) throws ParserConfigurationException, TransformerException, FileNotFoundException {
        Comparator<empNode> compareEmployee = new compareEmployee();
        array.sort(compareEmployee);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("root");
        root.setAttribute("docType", "Employee");
        root.setAttribute("eventType", "list");
        Element employees = document.createElement("Employees");
        Element[] employee = new Element[array.size()];
        for (int i = 0; i < array.size(); i++) {
            employee[i] = document.createElement("employee");
            employee[i].setAttribute("firstName", array.get(i).data.getFirstName());
            employee[i].setAttribute("middleName", array.get(i).data.getMiddleName());
            employee[i].setAttribute("lastName", array.get(i).data.getLastName());
            employee[i].setAttribute("salary", Integer.toString(array.get(i).data.getSalary()));
            employee[i].setAttribute("phone", array.get(i).data.getPhone());
            employee[i].setAttribute("department", Integer.toString(array.get(i).data.getOtdel()));
            employee[i].setAttribute("id",Integer.toString(array.get(i).id));
            employees.appendChild(employee[i]);
        }
        root.appendChild(employees);
        document.appendChild(root);
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        return document;
    }
    public static Document getDepartments(ArrayList<depNode> array) throws ParserConfigurationException, TransformerException, FileNotFoundException {
        Comparator<depNode> compareDepartment = new compareDepartment();
        array.sort(compareDepartment);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("root");
        root.setAttribute("docType", "departments");
        Element departments = document.createElement("departments");
        Element[] department = new Element[array.size()];
        for (int i = 0; i < array.size(); i++) {
            department[i] = document.createElement("department");
            department[i].setAttribute("name", array.get(i).data.getName());
            department[i].setAttribute("director", array.get(i).data.getDirector());
            department[i].setAttribute("id",Integer.toString(array.get(i).id));
            departments.appendChild(department[i]);
        }
        root.appendChild(departments);
        document.appendChild(root);
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        return document;
    }
    public static Document message(String info) throws TransformerConfigurationException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("root");
        root.setAttribute("docType", "message");
        Element child = document.createElement("message");
        document.appendChild(root);
        root.appendChild(child);
        child.setAttribute("info",info);
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        return document;
    }

}
