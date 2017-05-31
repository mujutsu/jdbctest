package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import model.Student;

public class StudentDAO {

	private Connection connection;

	public StudentDAO(Connection connection) {
		this.connection = connection;
	}

	public Student saveStudent(Student student) throws SQLException {
		if (student.getId() == null) {
			return insertStudent(student);
		} else {
			return updateStudent(student);
		}
	}

	public Student insertStudent(Student student) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("INSERT INTO students(first_name, last_name, age) VALUES(?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, student.getFirstName());
			ps.setString(2, student.getLastName());
			ps.setInt(3, student.getAge());

			if (ps.executeUpdate() == 1) {
				ResultSet rset = ps.getGeneratedKeys();
				if (rset.next()) {
					student.setId(rset.getLong(1));
				}
				return student;
			} else {
				return student;
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public Student updateStudent(Student student) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("UPDATE students SET first_name=?, last_name=?, age=? WHERE id=?");
			ps.setString(1, student.getFirstName());
			ps.setString(2, student.getLastName());
			ps.setInt(3, student.getAge());
			ps.setLong(4, student.getId());

			if (ps.executeUpdate() == 1) {
				return student;
			} else {
				return student;
			}
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public boolean deleteStudentById(Long id) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("DELETE FROM students WHERE id=?");
			ps.setLong(1, id);

			if (ps.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}
	
	public boolean deleteStudentByLastName(String lastName) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement("DELETE FROM students WHERE last_name=?");
			ps.setString(1, lastName);
			
			if (ps.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public int deleteAllStudents() throws SQLException {
		Statement st = null;
		try {
			st = connection.createStatement();
			return st.executeUpdate("DELETE FROM students");

		} finally {
			if (st != null) {
				st.close();
			}
		}
	}

	public Student getStudentById(Long id) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rset = null;
		try {
			ps = connection.prepareStatement("SELECT id, first_name, last_name, age FROM students WHERE id=?");
			ps.setLong(1, id);
			rset = ps.executeQuery();
			if (rset.next()) {
				Student student = new Student();
				student.setId(rset.getLong(1));
				student.setFirstName(rset.getString(2));
				student.setLastName(rset.getString(3));
				student.setAge(rset.getInt(4));
				return student;
			} else {
				return null;
			}

		} finally {
			if (rset != null) {
				rset.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}

	public Student getStudentByLastName(String lastName) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rset = null;
		try {
			ps = connection.prepareStatement("SELECT id, first_name, last_name, age FROM students WHERE last_name=?");
			ps.setString(1, lastName);
			rset = ps.executeQuery();
			if (rset.next()) {
				Student student = new Student();
				student.setId(rset.getLong(1));
				student.setFirstName(rset.getString(2));
				student.setLastName(rset.getString(3));
				student.setAge(rset.getInt(4));
				return student;
			} else {
				return null;
			}

		} finally {
			if (rset != null) {
				rset.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}

	public Collection<Student> getAllStudents() throws SQLException {
		PreparedStatement ps = null;
		ResultSet rset = null;
		try {
			ps = connection.prepareStatement("SELECT id, first_name, last_name, age FROM students");
			rset = ps.executeQuery();
			Collection<Student> result = new ArrayList<>();
			while (rset.next()) {
				Student student = new Student();
				student.setId(rset.getLong(1));
				student.setFirstName(rset.getString(2));
				student.setLastName(rset.getString(3));
				student.setAge(rset.getInt(4));
				result.add(student);
			}
			return result;

		} finally {
			if (rset != null) {
				rset.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}

}
