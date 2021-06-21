import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader student_csv = new BufferedReader(new FileReader("student.csv"));
        BufferedReader professor_csv = new BufferedReader(new FileReader("professor.csv"));
        BufferedReader room_csv = new BufferedReader(new FileReader("room.csv"));
        BufferedReader reservation_csv = new BufferedReader(new FileReader("reservation.csv"));

        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Professor> professors = new ArrayList<>();
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Reservation> reservations = new ArrayList<>();

        String line;
        student_csv.readLine();
        while ((line = student_csv.readLine()) != null) {
            students.add(new Student(line.split(",")));
        }
        professor_csv.readLine();
        while ((line = professor_csv.readLine()) != null) {
            professors.add(new Professor(line.split(",")));
        }
        room_csv.readLine();
        while ((line = room_csv.readLine()) != null) {
            rooms.add(new Room(line.split(",")));
        }
        reservation_csv.readLine();
        while ((line = reservation_csv.readLine()) != null) {
            reservations.add(new Reservation(line.split(",")));
        }

        while (true) {
            System.out.println("==== 강의실 대여 및 인적사항 조회 ====");
            System.out.println("1. 전체 예약 현황 조회");
            System.out.println("2. 호실 예약 현황 조회");
            System.out.println("3. 예약하기 및 예약 취소하기");
            System.out.println("4. 나의 예약 조회");
            System.out.println("5. 학생 인적사항 변경");
            System.out.println("6. 교수 인적사항 조회");
            System.out.println("7. 종료");
            System.out.print(">>> ");

            Scanner keyboard = new Scanner(System.in);
            try {
                switch (keyboard.nextInt()) {
                    case 1 -> new TotalReservation().run(reservations);
                    case 2 -> new RoomReservation().run(reservations);
                    case 3 -> new NewReservation().run(students, professors, reservations);
                    case 4 -> new MyReservation().run();
                    case 5 -> new StudentInformation().run(students);
                    case 6 -> new ProfessorInformation().run(professors);
                    case 7 -> new EndProgram().run();
                }
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    //1
    public static class TotalReservation {
        public void run(ArrayList<Reservation> reservations) {
            System.out.println("[1. 전체 예약 현황 조회]");

            ArrayList<Integer>[] rooms = new ArrayList[14];
            for (int i = 0; i < 14; i++)
                rooms[i] = new ArrayList<Integer>();
            for (Reservation reservation : reservations) {
                for (int i = 0; i < 14; i++) {
                    if (reservation.getPossible(i))
                        rooms[i].add(reservation.getRoomNum());
                }
            }
            for (int i = 0; i < 14; i++) {
                Collections.sort(rooms[i]);
                String time = i % 2 == 0 ? "6/" + (i/2 + 1) + "\n오전: " : "오후: ";
                System.out.print(time);
                for (int room : rooms[i]) {
                    System.out.print(room + ", ");
                }
                System.out.println("...");
                if (i % 2 != 0) System.out.println();
            }
        }
    }

    //2
    public static class RoomReservation {
        public void run(ArrayList<Reservation> reservations) {
            System.out.println("[2. 호실 예약 현황 조회]");

            int roomNum;
            Scanner keyboard = new Scanner(System.in);
            System.out.print(">>> ");
            roomNum = keyboard.nextInt();
            System.out.print("가능한 시간: ");
            for (Reservation reservation : reservations) {
                if (reservation.getRoomNum() == roomNum) {
                    for (int i = 0; i < 14; i++) {
                        if (reservation.getPossible(i))
                            System.out.print("6/" + (i / 2 + 1) + (i % 2 == 0 ? "오전, " : "오후, "));
                    }
                    System.out.println("...");
                }
            }
        }
    }

    //3
    public static class NewReservation {
        public void run(ArrayList<Student> students,
                        ArrayList<Professor> professors,
                        ArrayList<Reservation> reservations) throws Exception {
            System.out.println("[3. 예약하기 및 취소하기]");
            System.out.println("1. 예약하기");
            System.out.println("2. 예약 취소하기");
            System.out.print(">>> ");

            BufferedReader recordInput;
            BufferedWriter recordOutput;
            String name, time, reason;
            int id, roomNum, timeIndex;

            Scanner keyboard = new Scanner(System.in);
            if (keyboard.nextInt() == 1) {
                System.out.println("1.예약하기");
                System.out.println("이름, 아이디, 호실번호, 시간, 사유 입력");
                System.out.print(">>> ");

                name = keyboard.next();
                id = keyboard.nextInt();
                roomNum = Integer.parseInt(keyboard.next().substring(0, 3));
                time = keyboard.next();
                reason = keyboard.next();

                boolean check = false;
                for (Student student : students) {
                    if (name.equals(student.getName())) {
                        if (id == student.getStudentId()) {
                            check = true;
                        }
                    }
                }
                for (Professor professor : professors) {
                    if (name.equals(professor.getName())) {
                        if (id == professor.getProfessorId()) {
                            check = true;
                        }
                    }
                }

                if (check) {
                    for (Reservation reservation : reservations) {
                        if (roomNum == reservation.getRoomNum()) {
                            timeIndex = Integer.parseInt(time.substring(2, 3)) * 2 - 2;
                            if (time.charAt(4) == '후') timeIndex++;

                            if (reservation.getPossible(timeIndex)) {
                                reservation.setPossible(timeIndex, false);
                                
                                recordOutput =  new BufferedWriter(
                                        new FileWriter("reservationRecord.csv", true));
                                recordOutput.write(name + "," + id + "," + roomNum + "," + time + "," + reason + "\r\n");
                                recordOutput.close();
                                
                                reservationUpdate(reservation);
                                
                                System.out.println(roomNum + "호 " + time + "에 예약되었습니다.");
                                return;
                            } else break;
                        }
                    }
                }
                throw new Exception("예약 실패!");
            }
            else {
                System.out.println("2. 예약 취소하기");
                System.out.println("이름, 아이디, 호실번호, 시간 입력");
                System.out.print(">>> ");

                name = keyboard.next();
                id = keyboard.nextInt();
                roomNum = Integer.parseInt(keyboard.next().substring(0, 3));
                time = keyboard.next();

                recordInput = new BufferedReader(new FileReader("reservationRecord.csv"));
                String line;
                StringBuilder dummy = new StringBuilder();
                while ((line = recordInput.readLine()) != null) {
                    String[] arr = line.split(",");
                    if (name.equals(arr[0]) && id == Integer.parseInt(arr[1]) &&
                            roomNum == Integer.parseInt(arr[2]) && time.equals(arr[3])) {
                        for (Reservation reservation : reservations) {
                            if (roomNum == reservation.getRoomNum()) {
                                timeIndex = Integer.parseInt(time.substring(2, 3)) * 2 - 2;
                                if (time.charAt(4) == '후') timeIndex++;
                                if (!reservation.getPossible(timeIndex)) {
                                    reservation.setPossible(timeIndex, true);
                                    
                                    reservationUpdate(reservation);
                                }
                            }
                        }
                        line = recordInput.readLine();
                    }
                    if (line != null)
                        dummy.append(line).append("\r\n");
                }
                recordInput.close();

                recordOutput = new BufferedWriter(new FileWriter("reservationRecord.csv"));
                recordOutput.write(dummy.toString());
                recordOutput.flush();
                recordOutput.close();

                System.out.println("예약이 취소되었습니다.");
            }
        }
        public static void reservationUpdate(Reservation reservation) throws IOException {
            BufferedReader reservationInput;
            BufferedWriter reservationOutput;

            reservationInput = new BufferedReader(new FileReader("reservation.csv"));
            int roomNum = reservation.getRoomNum();
            String line;
            StringBuilder lineBuilder = new StringBuilder();
            StringBuilder dummy = new StringBuilder();

            dummy.append(reservationInput.readLine()).append("\r\n");
            while ((line = reservationInput.readLine()) != null) {
                if (roomNum == Integer.parseInt(line.split(",")[0])) {
                    for (int i = 0; i < 14; i++) {
                        lineBuilder.append(",").append(reservation.getPossible(i) ? "possible" : "impossible");
                    }
                    line = roomNum + lineBuilder.toString();
                }
                dummy.append(line).append("\r\n");
            }
            reservationInput.close();

            reservationOutput = new BufferedWriter(new FileWriter("reservation.csv"));
            reservationOutput.write(dummy.toString());
            reservationOutput.flush();
            reservationOutput.close();
        }
    }

    //4
    public static class MyReservation{
        public void run() throws IOException {
            System.out.println("[4. 나의 예약조회]");
            System.out.println("이름과 아이디(학번 또는 교직원 번호) 입력");
            System.out.print(">>> ");

            Scanner keyboard = new Scanner(System.in);
            String name = keyboard.next();
            int id = keyboard.nextInt();

            BufferedReader recordInput = new BufferedReader(new FileReader("reservationRecord.csv"));
            String line;
            boolean check = true;
            while ((line = recordInput.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ",");
                if (name.equals(st.nextToken()) && id == Integer.parseInt(st.nextToken())) {
                    System.out.println("이름: " + name +
                            ", 아이디: " + id +
                            ", 호실번호: " + st.nextToken() +
                            ", 시간: " + st.nextToken() +
                            ", 예약사유: " + st.nextToken());
                    check = false;
                }
            }
            if (check)
                System.out.println("없는 예약입니다. 이름과 아이디를 다시 확인해주세요.");
        }
    }

    //5
    public static class StudentInformation{
        public void run(ArrayList<Student> students) throws IOException {
            System.out.println("[5. 학생 인적사항 변경]");
            System.out.println("1. 인적사항 조회");
            System.out.println("2. 이름 변경");
            System.out.println("3. 학생 삭제");
            System.out.print(">>> ");

            Scanner keyboard = new Scanner(System.in);
            switch (keyboard.nextInt()) {
                case 1 -> {
                    System.out.println("1. 인적사항 조회");

                    Collections.sort(students);
                    for (Student student : students)
                        System.out.println(student);
                }
                case 2 -> {
                    System.out.println("2. 이름 변경");
                    System.out.print(">>> ");

                    String nameBefore = keyboard.next();
                    for (Student student : students) {
                        if (nameBefore.equals(student.getName())) {
                            System.out.println("어떤 이름으로 변경하시겠습니까?");
                            System.out.print(">>> ");

                            String nameAfter = keyboard.next();
                            BufferedReader studentInput = new BufferedReader(new FileReader("student.csv"));
                            String line;
                            StringBuilder dummy = new StringBuilder();
                            while ((line = studentInput.readLine()) != null) {
                                StringTokenizer st = new StringTokenizer(line, ",");
                                if (nameBefore.equals(st.nextToken()))
                                    line = nameAfter + "," + student.getDepartment() + "," +
                                            student.getStudentId() + "," + student.getPhoneNum();
                                dummy.append(line).append("\r\n");
                            }
                            studentInput.close();

                            BufferedWriter studentOutput = new BufferedWriter(new FileWriter("student.csv"));
                            studentOutput.write(dummy.toString());
                            studentOutput.flush();
                            studentOutput.close();

                            student.setName(nameAfter);
                            System.out.println("이름 변경이 완료되었습니다.");
                            return;
                        }
                    }
                    System.out.println("없는 이름입니다. 다시 확인해주세요.");
                }
                case 3 -> {
                    System.out.println("3. 학생 삭제");
                    System.out.print(">>> ");

                    String name = keyboard.next();
                    for (Student student : students) {
                        if (name.equals(student.getName())) {
                            BufferedReader studentInput = new BufferedReader(new FileReader("student.csv"));
                            String line;
                            StringBuilder dummy = new StringBuilder();
                            while ((line = studentInput.readLine()) != null) {
                                StringTokenizer st = new StringTokenizer(line, ",");
                                if (name.equals(st.nextToken()))
                                    line = studentInput.readLine();
                                if (line != null)
                                    dummy.append(line).append("\r\n");
                            }
                            studentInput.close();

                            BufferedWriter studentOutput = new BufferedWriter(new FileWriter("student.csv"));
                            studentOutput.write(dummy.toString());
                            studentOutput.flush();
                            studentOutput.close();

                            students.remove(student);
                            System.out.println("삭제되었습니다.");
                            return;
                        }
                    }
                    System.out.println("없는 이름입니다. 다시 확인해주세요.");
                }
            }
        }
    }

    //6
    public static class ProfessorInformation{
        public void run(ArrayList<Professor> professors) {
            System.out.println("[6. 교수 인적사항 조회]");

            Collections.sort(professors);
            for (Professor professor : professors)
                System.out.println(professor);
        }
    }

    //7
    public static class EndProgram {
        public void run() {
            System.out.println("[7. 종료]");
            System.out.println("종료합니다.");

            System.exit(0);
        }
    }
}