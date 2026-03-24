package com.pao.laboratory03.exercise;

import java.util.*;

public class StudentService {

    private static StudentService instance;
    private List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equals(name)) {
                throw new RuntimeException("Studentul '" + name + "' există deja");
            }
        }
        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student s : students) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost găsit");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student s = findByName(studentName);
        s.addGrade(subject, grade);
    }

    public void printAllStudents() {
        for (Student s : students) {
            System.out.println(s);
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
            }
        }
    }

    public void printTopStudents() {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort((s1, s2) -> Double.compare(s2.getAverage(), s1.getAverage()));
        for (Student s : sorted) {
            System.out.println(s);
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sum = new HashMap<>();
        Map<Subject, Integer> count = new HashMap<>();
        for (Student s : students) {
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                Subject subject = entry.getKey();
                double grade = entry.getValue();

                sum.put(subject, sum.getOrDefault(subject, 0.0) + grade);
                count.put(subject, count.getOrDefault(subject, 0) + 1);
            }
        }
        Map<Subject, Double> result = new HashMap<>();
        for (Subject subject : sum.keySet()) {
            result.put(subject, sum.get(subject) / count.get(subject));
        }
        return result;
    }
}