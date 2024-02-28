import com.mysql.cj.jdbc.Driver;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * ClassName: ${NAME}
 *
 * @author 奈奈朵
 * @vesion 17
 * @Create 2024/2/26 19:22
 */
public class Main {
    public static final Scanner scan = new Scanner(System.in);
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/laboratory_charge","root","121400bjy.");
        Statement statement = connection.createStatement();
    try {

        menu(statement);
    }finally {
        connection.close();
        statement.close();
        scan.close();
    };




    }
    public static void menu(Statement statement) throws SQLException, ClassNotFoundException {
//        DriverManager.registerDriver(new Driver());
//        new Driver();

        boolean judge = true;
        System.out.println("*****欢迎使用实验室记账系统*****");
        System.out.println("******************************");
        while(judge) {
            System.out.println("     1.登录  2.退出 3.新用户      ");
            int cho = scan.nextInt();
            String master = null;

            if (cho != 3) {
                while (true) {
                    if (cho != 2) {
                        System.out.println("请输入用户名：");
                        String username = scan.next();
                        master = username;
                        System.out.println("请输入你的密码：");
                        String password = scan.next();
                        String sql = "select lm.password\n" +
                                "from lab_member lm\n" +
                                "where lm.name = '" + username + "'";
                        ResultSet resultSet = statement.executeQuery(sql);
                        String truepw = null;
                        while (resultSet.next()) {
                            truepw = resultSet.getString("password");
                            System.out.println(truepw);
                        }
                        if (password.equals(truepw)) {
                            System.out.println("*****登陆成功，欢迎使用！*****");

                            menu1(statement,master);

                            break;
                        } else {
                            System.out.println("密码错误！请选择");
                            System.out.println("        1.登录  2.退出         ");
                            cho = scan.nextInt();
                            if (cho == 2) {
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
            else {
                addmember(statement);
            }



    }

    }
    public static void menu1(Statement statement ,String master) throws SQLException {
        if ("小羽".equals(master)) {
            while (true) {
                System.out.println("--1.邀请成员 2.踢出成员--");
                System.out.println("--3.资金汇入 4.资金支出--");
                System.out.println("--5.查询余额 6.修改密码--");
                System.out.println("--7.查询记录 8.退出    --");
                System.out.println("选择你要执行的业务：");
                int choose = scan.nextInt();
                switch (choose) {
                    case 1:
                        addmember(statement);
                        break;
                    case 2:
                        deletemember(statement);
                        break;
                    case 3:
                        abmoney(statement);
                        break;
                    case 4:
                        usmoney(statement);
                        break;
                    case 5:
                        double balance = 0;
                        balance = getBalance(statement);
                        System.out.println("实验室剩余资金为"+ balance +"元！");
                        break;
                    case 6:
                        modifypas(statement,master);
                        break;
                    case 7:
                        alllist(statement);
                        break;
                    case 8:
                        break;
                }
                if (choose == 8) {
                    break;
                }
            }

        } else {

            while (true) {

                System.out.println("--1.资金汇入 2.资金支出--");
                System.out.println("--3.查询余额 4.修改密码--");
                System.out.println("--5.查询记录 6.退出    --");
                System.out.println("选择你要执行的业务：");
                int choose = scan.nextInt();
                switch (choose) {
                    case 1:
                        abmoney(statement);
                        break;
                    case 2:
                        usmoney(statement);
                        break;
                    case 3:
                        double balance = 0;
                        balance = getBalance(statement);
                        System.out.println("实验室剩余资金为"+ balance +"元！");
                        break;
                    case 4:
                        modifypas(statement,master);
                        break;
                    case 5:
                        alllist(statement);
                        break;
                    case 6:
                        break;
                }
                if (choose == 6) {
                    break;
                }
            }

        }

    }

    /**
     * 添加新成员
     *
     */
    public static void addmember(Statement statement) throws SQLException {
        System.out.println("正在创建新用户！");
        System.out.println("请输入你的姓名：");
        String username = scan.next();
        System.out.println("请输入你的密码（不能超过20个字符）：");
        String password = scan.next();
        System.out.println("请输入你的网名：");
        String vir_name = scan.next();
        System.out.println("请输入你的学号：");
        String stu_id= scan.next();
        System.out.println("请输入你的班级信息：");
        String grade= scan.next();
        String sql1 = "insert into stu_info values('"+username+"','" + stu_id + "','" +grade + "');";
        String sql2 = "insert into lab_member values('"+username+"','" + vir_name + "','" +password + "');";
        int i = statement.executeUpdate(sql1);
        int j = statement.executeUpdate(sql2);


        System.out.println("创建成功！");

    }

    /**
     * 删除成员
     * @param statement
     * @throws SQLException
     */
    public static void deletemember(Statement statement) throws SQLException {
        System.out.println("请输入删除姓名：");
        String username = scan.next();
        String sql1 =
                "delete from lab_member \n" +
                "where name = '"+ username  +"';";
        String sql2 =
                "delete from stu_info \n" +
                        "where name = '"+ username  +"';";
        int i = statement.executeUpdate(sql1);
        int j = statement.executeUpdate(sql2);
        System.out.println("删除成功！");
    }

    /**
     * 汇入资金
     * @param statement
     * @throws SQLException
     */
    public static void abmoney(Statement statement) throws SQLException {
        System.out.println("你的姓名是：");
        String name = scan.next();
        System.out.println("原因：");
        String reason = scan.next();
        System.out.println("所汇入的金额为：");
        double money = scan.nextDouble();
        double balance = getBalance(statement)+money;

        Date date = null;
        String sqlGetTime = "select current_date() ;";
        ResultSet resultSet = statement.executeQuery(sqlGetTime);
        while(resultSet.next()){
            date = resultSet.getDate(1);
        }
        String sql = "insert into charge_list (name,reason,time,u_money,a_money,balance)\n" +
                "values(\n" +
                "'毕竞羽',\n" +
                "'"+reason+"',\n" +
                "'"+date+"',\n" +
                "0,\n" +
                money+",\n" +
                balance+"\n" +
                ");";

        int i = statement.executeUpdate(sql);


        sql = "insert into Balance (balance)\n" +
                "values(\n" +
                balance+"\n" +
                ")";
        int j = statement.executeUpdate(sql);

        System.out.println("记录成功！");

    }

    /**
     * 使用资金
     * @param statement
     * @throws SQLException
     */
    public static void usmoney(Statement statement) throws SQLException {
        System.out.println("你的姓名是：");
        String name = scan.next();
        System.out.println("原因：");
        String reason = scan.next();
        System.out.println("所申请的金额为：");
        double money = scan.nextDouble();
        double balance = getBalance(statement)-money;

        Date date = null;
        String sqlGetTime = "select current_date() ;";
        ResultSet resultSet = statement.executeQuery(sqlGetTime);
        while(resultSet.next()){
            date = resultSet.getDate(1);
        }
        String sql = "insert into charge_list (name,reason,time,u_money,a_money,balance)\n" +
                "values(\n" +
                "'毕竞羽',\n" +
                "'"+reason+"',\n" +
                "'"+date+"',\n" +
                money+",\n" +
                "0,\n" +
                balance+"\n" +
                ");";

        int i = statement.executeUpdate(sql);


        sql = "insert into Balance (balance)\n" +
                "values(\n" +
                balance+"\n" +
                ")";
        int j = statement.executeUpdate(sql);

        System.out.println("记录成功！");

    }

    /**
     * 查询余额
     * @param statement
     * @return
     * @throws SQLException
     */
    public static double getBalance(Statement statement) throws SQLException {

        String sql = "select b.balance\n" +
                "from balance b \n" +
                "order by charge_number desc \n" +
                "limit 0,1;";
        ResultSet resultSet = statement.executeQuery(sql);
        double money =0;
        while(resultSet.next()){
            money = resultSet.getDouble("balance");
        }
        return money;
    }
    /**
    修改密码
     */
    public static void modifypas(Statement statement,String name) throws SQLException {
        String password = null;
        while(true){
            System.out.println("请输入新密码：");
             password = scan.next();
            System.out.println("请再次确认新密码：");
             String copy_password1 = scan.next();
             if(password.equals(copy_password1)){
                 break;
             }else{
                 System.out.println("两次密码不相同！请重新操作：");
             }
        }
        String sql = "\n" +
                "update lab_member \n" +
                "set password = '"+ password  +"' \n" +
                "where name = '"+ name +"';";
        int i = statement.executeUpdate(sql);
        System.out.println("修改成功！");

    };

    /**
     * 账单
     * @param statement
     */
    public static void alllist(Statement statement) throws SQLException {
        String sql = "select * \n" +
                "from charge_list ;";
        ResultSet resultSet = statement.executeQuery(sql);
        List<Map> list= new ArrayList<Map>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int clumn = metaData.getColumnCount();

        while(resultSet.next()){
            Map map = new HashMap();
            for (int i = 1; i < clumn; i++) {
                Object value = resultSet.getObject(i);
                String columnLabel = metaData.getColumnLabel(i);
                map.put(columnLabel,value);
            }
            list.add(map);
        }

        System.out.println(list);


    }
}