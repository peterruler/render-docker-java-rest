package com.example.controllers;

import com.example.bean.Issue;
import com.example.bean.Project;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@SpringBootApplication
public class IssueTracker {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Autowired
    private DataSource dataSource2;

    /**
     * Project Endpoints
     */

    /**
     * DELETE Project
     */
    @CrossOrigin(maxAge = 3600)
    @RequestMapping(
            value = "/api/projects/{id}",
            produces = "application/json",
            method = {RequestMethod.DELETE})
    boolean deleteProject(@PathVariable int id) throws Exception {
        String deleteSql = "DELETE FROM Project WHERE id=?";
        int affectedrows = 0;
        try (Connection connection = dataSource2.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(deleteSql);
            pstmt.setInt(1, id);
            affectedrows = pstmt.executeUpdate();
            if (affectedrows > 0) {
                System.out.println("Issue delete success");
                return true;
            } else {
                System.out.println("Issue delete fail");
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * UPDATE Project
     */
    @CrossOrigin(maxAge = 3600)
    @RequestMapping(
            value = "/api/projects/{id}",
            produces = "application/json",
            method = {RequestMethod.PUT})
    List<Project> updateProject(@PathVariable int id, @RequestBody Map<String, Object> project) throws Exception {
        ArrayList<Project> output = new ArrayList<Project>();
        String client_id = (String) project.get("client_id");
        String title = (String) project.get("title");
        boolean active = (boolean) project.get("active");
        String putSql = "UPDATE Project SET client_id=?, title=?, active=? WHERE id=?";
        try (Connection connection = dataSource2.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(putSql);
            pstmt.setString(1, client_id);
            pstmt.setString(2, title);
            pstmt.setBoolean(3, active);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Project projectObj = new Project(id, client_id, title, active);

        output.add(projectObj);
        return output;
    }

    /**
     * CREATE Project
     */
    @CrossOrigin(maxAge = 3600)
    @PostMapping("/api/projects")
    String addProject(@RequestBody Map<String, Object> project) throws Exception {
        String client_id = (String) project.get("client_id");
        String title = (String) project.get("title");
        boolean active = (boolean) project.get("active");
        String postSql = "INSERT INTO Project (client_id, title, active) VALUES(?,?,?) RETURNING id";
        try (Connection connection = dataSource2.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(postSql);
            pstmt.setString(1, client_id);
            pstmt.setString(2, title);
            pstmt.setBoolean(3, active);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int id = rs.getInt(1);
            Project projectObj = new Project(id, client_id, title, active);
            System.out.println("POST: id=" + id + "project_id:" + client_id);
            return projectObj.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "undefined";
    }

    /**
     * READ Project BY ID
     * not implemented
     */

    /**
     * READ ALL Projects
     */
    @CrossOrigin(maxAge = 3600)
    @GetMapping("/api/projects")
    String getAllProjects(Map<String, Object> model) {
        String json = "";
        try (Connection connection = dataSource2.getConnection()) {
            int counter = 0;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, client_id, title, active FROM Project LIMIT 50;");
            json = "[";
            while (true) {
                try {
                    if (!rs.next()) {
                        break;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                int id = rs.getInt("id");
                String client_id = rs.getString("client_id");
                String title = rs.getString("title");
                boolean active = rs.getBoolean("active");
                Project projectObj = new Project(id, client_id, title, active);
                json += projectObj.toString();
                json += ",";
                counter++;
            }
            if (counter > 0) {
                return json.substring(0, json.length() - 1) + "]";
            } else {
                return "undefined";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "undefined";
    }

    /**
     * ISSUES
     */

    /**
     * CREATE ISSUE
     */
    @CrossOrigin(maxAge = 3600)
    @PostMapping("/api/projects/{project_id}/issues")
    String createIssue(@PathVariable int project_id, @RequestBody Map<String, Object> project) throws Exception {
        String client_id = (String) project.get("client_id");
        Boolean done = (Boolean) project.get("done");
        String title = (String) project.get("title");
        String due_date = (String) project.get("due_date");
        String priority = (String) project.get("priority");
        String project_client_id = (String) project.get("project_client_id");
        String postSql = "INSERT INTO Issue (client_id, project_id, done, title,  due_date, priority) VALUES(?,?,?,?,?,?) RETURNING id";
        try (Connection connection = dataSource2.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(postSql);
            pstmt.setString(1, client_id);
            pstmt.setDouble(2, project_id);
            pstmt.setBoolean(3, done);
            pstmt.setString(4, title);
            java.util.Date utilStartDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
            java.sql.Date due_date_sql = new java.sql.Date(utilStartDate1.getTime());
            pstmt.setDate(5, due_date_sql);
            pstmt.setString(6, priority);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int id = rs.getInt(1);
            Issue issue = new Issue(id, client_id, project_id, done, title, due_date_sql, priority, project_client_id);
            return issue.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "undefined";
    }

    /**
     * GET Issue
     * Not impmemented
     */

    /**
     * UPDATE Issue
     */
    @CrossOrigin
    @RequestMapping(
            value = "/api/projects/{project_id}/issues/{id}",
            produces = "application/json",
            method = {RequestMethod.PUT, RequestMethod.OPTIONS})
    String updateIssue(@PathVariable int project_id, @PathVariable int id, @RequestBody Map<String, Object> issue_param) throws Exception {
        String client_id = (String) issue_param.get("client_id");
        Boolean done = (Boolean) issue_param.get("done");
        String title = (String) issue_param.get("title");
        String due_date = (String) issue_param.get("due_date");
        String priority = (String) issue_param.get("priority");
        String putSql = "UPDATE Issue SET client_id=?, project_id=?, done=?, title=?, due_date=?, priority=? WHERE project_id=? AND id=?";
        try (Connection connection = dataSource2.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(putSql);
            pstmt.setString(1, client_id);
            pstmt.setDouble(2, project_id);
            pstmt.setBoolean(3, done);
            pstmt.setString(4, title);
            java.util.Date utilStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
            java.sql.Date date1 = new java.sql.Date(utilStartDate.getTime());
            pstmt.setDate(5, date1);
            pstmt.setString(6, priority);
            pstmt.setDouble(7, project_id);
            pstmt.setDouble(8, id);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return "undefined";
        }
        java.util.Date utilStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
        java.sql.Date due_date_sql = new java.sql.Date(utilStartDate.getTime());
        Issue issue = new Issue(id, client_id, project_id, done, title, due_date_sql, priority, "at todo");

        return issue.toString();
    }

    /**
     * DELETE Issue
     */
    @CrossOrigin
    @RequestMapping(
            value = "/api/projects/{project_id}/issues/{id}",
            produces = "application/json",
            method = {RequestMethod.DELETE})
    boolean deleteIssue(@PathVariable int id, @PathVariable int project_id) throws Exception {
        String deleteSql = "DELETE FROM Issue WHERE project_id=?";
        int affectedrows = 0;
        try (Connection connection = dataSource2.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(deleteSql);
            pstmt.setInt(1, project_id);
            System.out.println("Issue delete project id" + project_id);
            affectedrows = pstmt.executeUpdate();
            if (affectedrows > 0) {
                System.out.println("Issue delete success");
                return true;
            } else {
                System.out.println("Issue delete fail");
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * READ ALL Issues
     */
    @CrossOrigin(maxAge = 3600)
    @GetMapping("/api/projects/{project_id}")
    String getProjectById(@PathVariable int project_id) {
        try (Connection connection = dataSource2.getConnection()) {
            String sql = "SELECT id, client_id, project_id, done, title, due_date, priority FROM Issue WHERE project_id=?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, project_id);
            System.out.println("DEBUG : project_id=" + project_id);
            ResultSet rs = pstmt.executeQuery();
            int id = 0;
            String client_id = "";
            String project_id2 = "0";
            Boolean done = false;
            String title = "";
            Date due_date = null;
            String priority = "";
            String project_client_id = "";
            String json = "[";
            int counter = 0;
            String readProjSql = "SELECT id, client_id, title, active FROM Project WHERE id=?;";
            PreparedStatement pstmtProject = connection.prepareStatement(readProjSql);
            while (rs.next()) {
                id = rs.getInt("id");
                client_id = rs.getString("client_id");
                project_id2 = rs.getString("project_id");
                done = rs.getBoolean("done");
                title = rs.getString("title");
                due_date = rs.getDate("due_date");
                priority = rs.getString("priority");
                System.out.println("ROW : id=" + id + " client_id" + client_id + " project_id" + project_id2 + " title=" + title + "due_date" + due_date + "priority=" + priority);
                //read project_client_id
                pstmtProject.setInt(1, project_id);
                ResultSet rsProject = pstmtProject.executeQuery();
                rsProject.next();
                project_client_id = rsProject.getString("client_id");
                Issue issue = new Issue(id, client_id, project_id, done, title, due_date, priority, project_client_id);
                json += issue.toString();
                json += ",";
                counter++;
            }
            if (counter > 0) {
                return json.substring(0, json.length() - 1) + "]";
            } else {
                return "undefined";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "undefined";
    }

    @Bean
    public DataSource dataSource2() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            return new HikariDataSource();
        } else {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            String un = System.getenv().get("username");
            config.setUsername(un);
            String pw = System.getenv().get("password");
            config.setPassword(pw);

            return new HikariDataSource(config);
        }
    }
}

