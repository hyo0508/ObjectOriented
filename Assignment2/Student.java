public class Student implements Comparable<Student> {
    private String name;
    private String department;
    private int studentId;
    private String phoneNum;

    public Student(String[] arr) {
        this.name = arr[0];
        this.department = arr[1];
        this.studentId = Integer.parseInt(arr[2]);
        this.phoneNum = arr[3];
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getStudentId() { return studentId; }
    public String getPhoneNum() { return phoneNum; }

    public String toString() {
        return "name: " + name +
                ", department: " + department +
                ", studentId: " + studentId +
                ", phoneNum: " + phoneNum;
    }
    public int compareTo(Student otherStudent) {
        return name.compareTo(otherStudent.getName());
    }
}
