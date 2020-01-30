import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server implements controller {
    model Model;
    class userThread extends Thread
    {       String message;
            private BufferedWriter out;
            private BufferedReader in;
            public userThread(Socket socket) throws IOException {
                message = null;
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                start();
            }
            public void setMessage(String info)
            {
                message = info;
            }
        public void query(String stroka) throws IOException, ClassNotFoundException {
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
                    send(info);
                    break;
                }
                case("-create"):
                {
                    switch (subStr[1])
                    {
                        case("-department"):
                        {
                            if(subStr.length<4)
                                send("Ошибка формирования запроса.\nДля справки воспользуйтесь -h");
                            else
                                send(Model.createDepartment(subStr[2],subStr[3]));
                            break;
                        }
                        case("-employee"):
                        {
                            if(subStr.length<8)
                              send("Ошибка формирования запроса.\nДля справки воспользуйтесь -h");
                            else
                            {
                                Model.createEmployee(subStr[2],subStr[3],subStr[4],subStr[5],subStr[6],subStr[7]);
                            }
                            break;
                        }
                    }
                    break;
                }
                case("-show"):
                {
                    if (subStr[0].equals("-show") && subStr.length == 1)
                        send("Недостаточно аргументов.\nВоспользуйтесь справкой -h.");
                    else
                        switch (subStr[1])
                        {
                            case("-department"):
                            {
                                if(subStr.length==2)
                                {
                                    send(Model.showDepartments());
                                } else if (subStr.length==3)
                                {
                                    send(Model.showDepartment(subStr[2]));
                                }
                                else
                                {
                                   send("Неверное количество аргументов команды -show.\nВоспользуйтесь справкой -h");
                                }
                                break;
                            }
                            case("-employee"): {
                                if (subStr.length == 2) {

                                    send(Model.showEmployees());
                                } else if(subStr.length==3) {
                                    send(Model.showEmployee(subStr[2]));
                                }else
                                {
                                    send("Неверное количество аргументов команды -show.\nВоспользуйтесь справкой -h");
                                }
                                break;
                            }
                            default: {
                                send("Неверный аргумент "+subStr[1]+".\nВоспользуйтесь справкой -h.");
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
                                send(Model.deleteDepartment(subStr[2]));
                            }
                            break;
                        }
                        case("-employee"): {
                            if(subStr.length==3)
                            {
                                send(Model.deleteEmployee(subStr[2]));
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
                                Model.changeDepartment(subStr[2],subStr[3],subStr[4]);
                            }
                            break;
                        }
                        case("-employee"):
                        {
                            if(subStr.length==5)
                                Model.changeEmployee(subStr[2],subStr[3],subStr[4]);
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
                                send(Model.showDepartment(subStr[2]));
                            }
                            else if(subStr.length==4)
                            {
                               send(Model.searchDepartament(subStr[2],subStr[3]));
                            }
                            else
                            {
                                send("Неверное количество аргументов команды -search.\nВоспользуйтесь справкой -h");
                            }
                            break;
                        }
                        case("-employee"):
                        {
                            if(subStr.length == 3)
                            {
                                send(Model.showEmployee(subStr[2]));
                            }
                            if(subStr.length==4)
                            {
                                send(Model.searchEmployee(subStr[2],subStr[3]));
                            } else
                            {
                                send("Неверное количество аргументов команды -search.\nВоспользуйтесь справкой -h");
                            }
                            break;
                        }
                    }
                    else
                    {
                        send("Неверное количество аргументов команды -search.\nВоспользуйтесь справкой -h");
                    }
                    break;
                }
                case("-time"):
                {
                    send((new Date()).toString());
                    break;
                }
                case(""):break;
                default:
                {
                    send("Неверный аргумент: "+subStr[0]+"\nВоспользуйтесь справкой -h.");
                }
            }
        }
            @Override
            public void run()
            {
                String word;
                try
                {
                    while(true)
                    {
                        word = in.readLine();
                        if(word.equals("stop"))
                        {
                            this.send("NAXUI STOP NAJAL?");
                            break;
                        }
                        send("Вы запросили: "+word);
                        query(word);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        private void send(String msg)
        {
            try
            {
                out.write(msg+ "\n");
                out.flush();
                msg=null;
            }
            catch (IOException ignored){}
        }
    }
    private BufferedReader in;
    private BufferedWriter out;


    public Server() throws IOException {
        Model = new fileLoader();
        connect();
    }

    @Override
    public void connect() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(4400)) {
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


