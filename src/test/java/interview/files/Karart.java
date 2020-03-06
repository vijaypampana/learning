package interview.files;

import java.io.*;
import java.util.*;

public class Karart {

/*
    find_pairs(student_course_pairs_1) =>
    {
      [58, 17]: ["Software Design", "Linear Algebra"]
      [58, 94]: ["Economics"]
      [58, 25]: ["Economics"]
      [94, 25]: ["Economics"]
      [17, 94]: []
      [17, 25]: []
    }

    Additional test cases:

    Sample Input:

    student_course_pairs_2 = [
      ["42", "Software Design"],
      ["0", "Advanced Mechanics"],
      ["9", "Art History"],
    ]

    Sample output:

    find_pairs(student_course_pairs_2) =>
    {
      [0, 42]: []
      [0, 9]: []
      [9, 42]: []
    }
    */

        public static void main(String[] argv) {
            String[][] studentCoursePairs1 = {
                    {"58", "Linear Algebra"},
                    {"94", "Art History"},
                    {"94", "Operating Systems"},
                    {"17", "Software Design"},
                    {"58", "Mechanics"},
                    {"58", "Economics"},
                    {"17", "Linear Algebra"},
                    {"17", "Political Science"},
                    {"94", "Economics"},
                    {"25", "Economics"},
                    {"58", "Software Design"}

            };

            String[][] studentCoursePairs2 = {
                    {"42", "Software Design"},
                    {"0", "Advanced Mechanics"},
                    {"9", "Art History"},
            };

            getMatchingCourses(studentCoursePairs1).forEach(std ->
                    System.out.println( std.studentid1 + "," + std.studentid2 + " : " + std.matchingCourses.toString())
            );

            getMatchingCourses(studentCoursePairs2).forEach(std ->
                    System.out.println( std.studentid1 + "," + std.studentid2 + " : " + std.matchingCourses.toString())
            );



        }

        //This method will list the matching courses between students
        static List<Student> getMatchingCourses(String[][] studentCoursesPair) {

            List<String> uniqueStudents = new ArrayList<>();
            List<String> uniqueCourses = new ArrayList<>();
            List<Student> studentList = new ArrayList<>();

            for(int i = 0; i < studentCoursesPair.length; i++) {
                if(!uniqueStudents.contains(studentCoursesPair[i][0])) {
                    uniqueStudents.add(studentCoursesPair[i][0]);
                }
                if(!uniqueCourses.contains(studentCoursesPair[i][1])) {
                    uniqueCourses.add(studentCoursesPair[i][1]);
                }
            }

            for(int i =0; i < uniqueStudents.size(); i++ ) {
                for(int j = i+1; j< uniqueStudents.size(); j++) {
                    Student std = new Student();
                    std.studentid1 = uniqueStudents.get(i);
                    std.studentid2 = uniqueStudents.get(j);
                    List<String> courses = new ArrayList<>();

                    for(int k =0; k<studentCoursesPair.length; k++) {
                        if(studentCoursesPair[k][0].equalsIgnoreCase(std.studentid1) || studentCoursesPair[k][0].equalsIgnoreCase(std.studentid2) ) {
                            courses.add(studentCoursesPair[k][1]);
                        }
                    }

                    for(int u=0; u<courses.size(); u++) {
                        String course1 = courses.get(i);
                        courses.remove(course1);
                        if(courses.contains(course1)) {
                            std.matchingCourses.add(course1);
                        }
                    }

                    studentList.add(std);

                }
            }

            return studentList;

        }



        static class Student {
            public String studentid1 = "";
            public String studentid2 = "";
            public List<String> matchingCourses;

            public Student() {
                this.matchingCourses = new ArrayList<>();
            }
        }
    }


