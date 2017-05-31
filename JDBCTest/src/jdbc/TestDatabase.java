package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import dao.StudentDAO;
import model.Student;

public class TestDatabase {

	static final String DRIVER = "org.postgresql.Driver";
	static final String DB = "jdbc:postgresql://localhost/test_db";

	static final String USER = "postgres";
	static final String PASS = "admin";

	public static void main(String[] args) throws SQLException {

		Connection conn = null;
		
		try {
			// loading driver
			System.out.print("Loading driver...");
			Class.forName(DRIVER);
			System.out.println("success!\n");
			// getting connection
			System.out.print("Obtaining PostgreSQL connection...");
			conn = DriverManager.getConnection(DB, USER, PASS);
			System.out.println("success!\n");
			
			StudentDAO sdao = new StudentDAO(conn);
			
			//cleaning table of old attempts
			System.out.println("Cleaning up database. Don't try this at home!");
			System.out.println("Deleting all students, rows deleted: " + sdao.deleteAllStudents() + "\n");
			
			//adding a few students
			System.out.print("Adding a few students...");
			Student st = new Student("Ionel", "Georgescu", 22);
			sdao.saveStudent(st);
			st = new Student("Costel", "Ionescu", 25);
			sdao.saveStudent(st);
			st = new Student("Alex", "Popescu", 21);
			sdao.saveStudent(st);
			st = new Student("Paul", "Atreides", 19);
			sdao.saveStudent(st);
			System.out.println("success!\n");
			
			
			//getting all students and printing them
			Collection<Student> students = sdao.getAllStudents();
			System.out.println("Initial list of students:");
			for (Student student : students) {
				System.out.println(student.toString());
			}
			System.out.println();

		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			ex.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQL error!");			
			e.printStackTrace();
		} finally {
			System.out.print("Cleaning up resources...");
			try {
				if (conn != null)
					conn.close();
				System.out.println("success!\n\n");
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}	

		try {
			System.out.println("Here we go again:\n");
			System.out.print("Loading driver...");
			Class.forName(DRIVER);
			System.out.println("success!\n");
			// getting connection
			System.out.print("Obtaining PostgreSQL connection...");
			conn = DriverManager.getConnection(DB, USER, PASS);
			System.out.println("success!\n");
			
			//setting autocommit to false
			conn.setAutoCommit(false);		
			
			StudentDAO sdao = new StudentDAO(conn);			
			
			System.out.print("Modifying some students and adding some more...");
			Student st = sdao.getStudentByLastName("Popescu");
			st.setFirstName("Edmond");
			st.setLastName("Dantes");
			st.setAge(24);
			sdao.saveStudent(st);
			
			st = sdao.getStudentByLastName("Georgescu");
			st.setFirstName("Luke");
			st.setLastName("Skywalker");
			st.setAge(23);
			sdao.saveStudent(st);
						
			//adding two more students for good measure
			st = new Student("Clark", "Kent", 20);
			sdao.saveStudent(st);
			
			st = new Student("Bruce", "Wayne", 21);
			sdao.saveStudent(st);
			System.out.println("success!\n");
			
			//deleting one for fun
			System.out.print("Deleting student Ionescu (he flunked all his exams!)...");
			if (sdao.deleteStudentByLastName("Ionescu")){
				System.out.println("success!");
			} else {
				//this line of code will probably never be executed because there will be an SQL error if the deletion fails.
				System.out.println("failed.");
			}
			System.out.println();

			//getting updated list and printing it
			System.out.println("Printing final list of students:");
			Collection<Student> students = sdao.getAllStudents();
			for (Student student : students) {
				System.out.println(student.toString());
			}
			System.out.println();
			
			//here's the commit™ I was forgetting about during the interview :)
			conn.commit();
			System.out.println("All changes successful. Have a nice day! :)\n");

		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			ex.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQL error!");			
			if(conn != null) {
				conn.rollback();
			}
			e.printStackTrace();
		} finally {
			System.out.print("Cleaning up resources...");
			try {
				if (conn != null)
					conn.close();
				System.out.println("success!");
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

}
