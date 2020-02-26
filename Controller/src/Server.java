import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
class compareEmployee implements Comparator<empNode>
{
    @Override
    public int compare(empNode o1,empNode o2)
    {
        String temp1 = (o1.data.getLastName()+o1.data.getFirstName()+o1.data.getMiddleName()).toLowerCase();
        String temp2 = (o2.data.getLastName()+o2.data.getFirstName()+o2.data.getMiddleName()).toLowerCase();
        return temp1.compareTo(temp2);
    }
}
class compareDepartment implements Comparator<depNode>
{
    @Override
    public int compare(depNode o1,depNode o2)
    {

        return o1.data.getName().compareTo(o2.data.getName());
    }
}

public class Server implements controller {
    private model Model;
    class userThread extends Thread
    {
            Socket socket;
            private BufferedWriter out;
            private BufferedReader in;
            userThread(Socket socket) throws IOException {
                this.socket = socket;
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                start();
            }
        void query(String stroka) throws IOException, ClassNotFoundException, TransformerException, ParserConfigurationException {
            String[] subStr;
            subStr = stroka.split(" ");
            switch(subStr[0])
            {
                case("-help"):
                case("-h"):
                {
                    String info =
                            "-h - помощь \n-time - текущее время" +
                                    "\n-create - создает новый объект " +
                                    "\n\t-department [Имя отдела] [ID начальника] - отдел с соответстующими аргументам данными"+
                                    "\n\t-employee [Имя] [Фамилия] [Отчество] [Номер отдела] [Телефон] [Зар.плата] - сотрудника с соответстующими аргументам данными "+
                                    "\n-search - поиск" +
                                    "\n\t-employee [id|имя|фамилия|отчество|телефон|зарплата|отдел] {value} - либо по ID - сотрудника, либо по указанному атрибуту со значением {value}"+
                                    "\n\t-department [id|имя|директор] {value} - либо по ID - отдела, либо по указанному атрибуту со значением {value}"+
                                    "\n-show - показать информацию"+
                                    "\n\t-employee {id} - о сотруднике с указанным {id}(аналогичен -search -employee [id]), либо всех сотрудников"+
                                    "\n\t-department {id} - об отделе с указанным {id}(аналогичен -search -department [id]), либо все отделы "+
                                    "\n-delete - удалить" +
                                    "\n\t-employee [id] - сотрудника с указанным [id]"+
                                    "\n\t-department [id] - отдел с указанным [id]"+
                                    "\n-change - изменить"+
                                    "\n\t-department [id] [attr] [value] - значение параметра [attr](имя|директор) отдела [id] на новое значение [value]"+
                                    "\n\t-employee [id] [attr] [value] - значение параметра [attr](имя|фамилия|отчество|отдел|телефон|зарплата) сотрудника [id] на новое значение [value]";
                    send(xmlGenerator.message(info));
                    break;
                }
                case("-create"):
                {
                    switch (subStr[1])
                    {
                        case("-department"):
                        {
                            if(subStr.length<4)
                                send(xmlGenerator.message("Ошибка формирования запроса."));
                            else {

                                if(Model.createDepartment(subStr[2], subStr[3]))
                                    send(xmlGenerator.message("Объект успешно добавлен"));
                                else
                                    send(xmlGenerator.message("Неверно указаны параметры"));
                            }
                                break;
                        }
                        case("-employee"):
                        {
                            if(subStr.length<8)
                              send(xmlGenerator.message("Ошибка формирования запроса."));
                            else
                            {

                                if(Model.createEmployee(subStr[2],subStr[3],subStr[4],subStr[5],subStr[6],subStr[7]))
                                    send(xmlGenerator.message("Объект успешно добавлен"));
                                else
                                    send(xmlGenerator.message("Объект уже существует или сотрудник управляет другим отделом"));
                            }
                            break;
                        }
                    }
                    break;
                }
                case("-show"):
                {
                    if (subStr[0].equals("-show") && subStr.length == 1)
                        send(xmlGenerator.message("Недостаточно аргументов."));
                    else
                        switch (subStr[1])
                        {
                            case("-department"):
                            {
                                if(subStr.length==2)
                                {
                                    send(xmlGenerator.getDepartments(Model.showDepartments()));
                                } else if (subStr.length==3)
                                {
                                    ArrayList<depNode> t = new ArrayList<depNode>();
                                    t.add(Model.showDepartment(subStr[2]));
                                    send(xmlGenerator.getDepartments(t));
                                }
                                else
                                {
                                   send(xmlGenerator.message("Неверное количество аргументов команды -show."));
                                }
                                break;
                            }
                            case("-employee"): {
                                if (subStr.length == 2) {

                                    send(xmlGenerator.getEmployees(Model.showEmployees(),0));
                                } else if(subStr.length==3) {
                                    ArrayList<empNode> t = new ArrayList<empNode>();
                                    t.add(Model.showEmployee(subStr[2]));
                                    send(xmlGenerator.getEmployees(t,0));
                                }else
                                {
                                    send(xmlGenerator.message("Неверное количество аргументов команды -show."));
                                }
                                break;
                            }
                            default: {
                                send(xmlGenerator.message("Неверный аргумент "+subStr[1]));
                                break;
                            }
                        }
                    break;
                }
                case("-delete"):
                {
                    switch (subStr[1])
                    {
                        case("-department"):
                        {
                            if(subStr.length==3)
                            {
                                if(subStr[2].equals("0"))
                                {
                                    send(xmlGenerator.message("Нельзя удалить отдел \"0\""));
                                }
                                  else
                                      {
                                      send(xmlGenerator.message(Model.deleteDepartment(subStr[2])));
                                }
                            }
                            break;
                        }
                        case("-employee"): {
                            if(subStr.length==3)
                            {
                                send(xmlGenerator.message(Model.deleteEmployee(subStr[2])));
                            }
                            break;
                        }
                    }
                }
                case("-change"):
                {
                    switch (subStr[1]){
                        case("-department"):
                        {
                            if(subStr.length==5)
                            {
                                send(xmlGenerator.message(Model.changeDepartment(subStr[2],subStr[3],subStr[4])));
                            }
                            break;
                        }
                        case("-employee"):
                        {
                            if(subStr.length==5)
                                send(xmlGenerator.message(Model.changeEmployee(subStr[2],subStr[3],subStr[4])));
                            break;
                        }
                    }
                    break;
                }
                case("-search"):
                {
                    if(subStr.length>1)
                    switch (subStr[1]){
                        case("-department"):
                        {
                            if(subStr.length == 3)
                            {
                                ArrayList<depNode> t = new ArrayList<depNode>();
                                t.add(Model.showDepartment(subStr[2]));
                                send(xmlGenerator.getDepartments(t));
                            }
                            else if(subStr.length==4)
                            {
                               send(xmlGenerator.getDepartments(Model.searchDepartament(subStr[2],subStr[3])));
                            }
                            else
                            {
                                send(xmlGenerator.message("Неверное количество аргументов команды -search."));
                            }
                            break;
                        }
                        case("-employee"):
                        {
                            if(subStr.length == 3)
                            {
                                ArrayList<empNode> t = new ArrayList<empNode>();
                                t.add(Model.showEmployee(subStr[2]));
                                send(xmlGenerator.getEmployees(t,0));
                            }
                            else if(subStr.length==4)
                            {
                                ArrayList<empNode> t = Model.searchEmployee(subStr[2],subStr[3]);
                                if(t!=null)
                                send(xmlGenerator.getEmployees(t,0));
                                else
                                    send(xmlGenerator.message("Не удалось найти файлы с указанными параметрами: "+subStr[2]+":"+subStr[3]));
                            }
                            break;
                        }
                    }
                    break;
                }
                case("-time"):
                {
                    send(xmlGenerator.message((new Date()).toString()));
                    break;
                }
                case(""):break;
                default:
                {
                    send(xmlGenerator.message("Неверный аргумент: "+subStr[0]));
                }
            }
        }
            @Override
            public void run() {
                String word;
                try {
                    while (true) {
                        word = in.readLine();
                        if(word.equals("shutdown"))
                        {
                            out.close();
                            in.close();
                            socket.close();
                            break;
                        }
                        query(word);
                        word = null;
                    }
                } catch (IOException | ClassNotFoundException | TransformerException | ParserConfigurationException e) {
                    e.printStackTrace();
                }

            }
            private void send(Document doc)
        {
            try
            {
                DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
                LSSerializer lsSerializer = domImplementation.createLSSerializer();
                out.write(lsSerializer.writeToString(doc));
                out.newLine();
                out.flush();
            }
            catch (IOException ignored){}

        }
    }
    public Server() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        Model = new fileLoaderXml();
        fileChooser.setDialogTitle("Выбор директории БД");
        // Определение режима - только каталог
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(fileChooser);
        // Если директория выбрана, покажем ее в сообщении
        String temp = null;
        if (result == JFileChooser.APPROVE_OPTION )
            temp =  fileChooser.getSelectedFile().getPath();
        if(temp!=null)
            ((fileLoaderXml)Model).setDataBase(temp+"\\");
        connect();
    }
    @Override
    public void connect() throws IOException {
        boolean flag = true;
        int port = 4400;
        String dialogMessage = "Введите порт";
        while(flag)
        {
            String temp = JOptionPane.showInputDialog(dialogMessage);
            try {
                port = Integer.parseInt(temp);
                if (port > 10000 || port < 1) {
                    dialogMessage = "Введенный порт неверен. Повторите ввод";
                } else {
                    flag = false;
                }
                ServerSocket serverSocket = new ServerSocket(port);
                while (true) {
                    Socket socket = serverSocket.accept();
                    new userThread(socket);

                }

            }
            catch (NumberFormatException e)
            {
                flag = true;
            }
            catch (IOException e)
            {
                flag = true;
                dialogMessage = "Введенный порт занят, выберите другой";
            }

        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    new userThread(socket);
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }

}


