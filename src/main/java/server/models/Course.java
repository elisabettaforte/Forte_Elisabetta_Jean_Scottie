package server.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

    private String name;
    private String code;
    private String session;
    private static List<Course> allCourses = new ArrayList<>();

    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
        allCourses.add(this);

    }

    public List<Course> filterBySession(String session) {
        List<Course> filteredCourses = new ArrayList<>();
        for (Course course : allCourses) {
            if (course.getSession().equals(session)) {
                filteredCourses.add(course);
            }
        }
        return filteredCourses;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
