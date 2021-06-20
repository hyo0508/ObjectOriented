public class Professor implements Comparable<Professor> {
    private String name;
    private String department;
    private int professorId;
    private int classNum;

    public Professor(String[] arr) {
        this.name = arr[0];
        this.department = arr[1];
        this.professorId = Integer.parseInt(arr[2]);
        this.classNum = Integer.parseInt(arr[3]);
    }

    public String getName() { return name; }
    public int getProfessorId() { return professorId; }
    public int getClassNum() { return classNum; }

    public String toString() {
        return "name: " + name +
                ", department: " + department +
                ", professorId: " + professorId +
                ", classNum: " + classNum;
    }
    public int compareTo(Professor otherProfessor) {
        return Integer.compare(classNum, otherProfessor.getClassNum());
    }
}
