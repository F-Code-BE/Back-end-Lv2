package view;

import controller.*;

public class Main {

    public static void main(String[] args) {

        while (true) {
            int userCase = LogIn.logIn();
            var menu = new FapMenu();
            String userID = LogIn.getUserId();
            int userChoice;
            if (userCase == 1) {
                menu.add("View information");
                menu.add("View curriculum");
                menu.add("View attendance report");
                menu.add("View academic transcript");
                menu.add("View Timetable");
                menu.add("Change group");
                menu.add("Request");
                menu.add("Log out");
                do {
                    ViewInfo viewInfo = new ViewInfo();
                    userChoice = menu.getUserChoice();
                    
                    switch (userChoice) {
                        case 1:
                            viewInfo.viewStudentInfo(userID);
                            break;
                        case 2:
                            viewInfo.viewSyllabus(userID);
                            break;
                        case 3:
                            viewInfo.viewAttendance(userID);
                            break;
                        case 4:
                            viewInfo.viewMarkReport(userID);
                            break;
                        case 5:
                            viewInfo.viewTimetable(userID);
                            break;
                        case 6:
                            ChangeGroup.getAllCourses(userID);
                            break;
                        case 7:
                            StudentRequest request = new StudentRequest(userID);
                            request.showMenu();
                        default:
                            break;
                    }

                } while (userChoice != 8);
            } else if (userCase == 2) {
                menu.add("View Timetable");
                menu.add("Check attendance");
                menu.add("Change information");
                menu.add("Change subject");
                menu.add("Change slot");
                menu.add("Enter marks");
                menu.add("Log out");
                do {
                    userChoice = menu.getUserChoice();
                    switch (userChoice) {
                        case 2:
                            var checkAttendence = new CheckAttendence(LogIn.getUserId());
                            checkAttendence.showMenu();
                            break;
                        case 6:
                            var inputMark = new InputMark(LogIn.getUserId());
                            inputMark.showMenu();
                            break;
                        default:
                            break;
                    }
                } while (userChoice != 7);

            } else if (userCase == 3) {
                menu.add("Add account");
                menu.add("Delete account");
                menu.add("Update information base on request");
                menu.add("Log out");
                do {
                    userChoice = menu.getUserChoice();
                    switch (userChoice) {
                        case 1:
                            AccountModify.addingAccount();
                            break;
                        case 2:
                            AccountModify.deleteAccount();
                            break;
                        default:
                            break;
                    }
                } while (userChoice != 4);
            } else {
                break;
            }
        }
    }
}