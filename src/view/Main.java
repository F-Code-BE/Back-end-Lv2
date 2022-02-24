package view;

import Lib.Validation;
import controller.*;

public class Main {

    public static void main(String[] args) {
        while (true) {
            int userCase = LogIn.logIn();
            if (userCase == 0) {
                continue;
            }
            var menu = new FapMenu();
            String userID = LogIn.getUserId();
            int userChoice;
            if (userCase == 1) {
                menu.add("View information");
                menu.add("View curriculum");
                menu.add("View attendence report");
                menu.add("View academic transcript");
                menu.add("View Timetable");
                menu.add("Change group");
                menu.add("Send Request");
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
                            boolean check;
                            do {
                                ChangeGroup.getAllCourses(userID);
                                check = Validation.confirmYesNo("Do you want to continue?(Y/N)");
                            } while (check);
                            break;
                        case 7:
                            StudentRequest request = new StudentRequest(userID);
                            request.showMenu();
                            break;
                        default:
                            break;
                    }

                } while (userChoice != menu.size());
            } else if (userCase == 2) {
                menu.add("View information");
                menu.add("Check attendance");
                menu.add("Change information");
                menu.add("Enter marks");
                menu.add("Log out");
                do {
                    userChoice = menu.getUserChoice();
                    switch (userChoice) {
                        case 1:
                            ViewInfo viewInfo = new ViewInfo();
                            viewInfo.viewTeacherInfo(userID);
                            break;
                        case 2:
                            boolean check;
                            do {
                                var checkAttendence = new CheckAttendence(userID);
                                checkAttendence.showMenu();
                                check = Validation.confirmYesNo("Do you want to continue?(Y/N)");
                            } while (check);
                            break;
                        case 3:
                            do {
                                TeacherRequest request = new TeacherRequest(userID);
                                request.showMenu();
                                check = Validation.confirmYesNo("Do you want to continue?(Y/N)");
                            } while (check);
                            break;
                        case 4:
                            do {
                                var inputMark = new InputMark(userID);
                                inputMark.showMenu();
                                check = Validation.confirmYesNo("Do you want to continue?(Y/N)");
                            } while (check);
                            break;
                        default:
                            break;
                    }
                } while (userChoice != menu.size());

            } else if (userCase == 3) {
                menu.add("Add account");
                menu.add("Delete account");
                menu.add("Update information base on request");
                menu.add("Log out");
                do {
                    userChoice = menu.getUserChoice();
                    switch (userChoice) {
                        case 1:
                            Boolean check;
                            do {
                                AccountModify.addingAccount();
                                check = Validation.confirmYesNo("Do you want to continue?(Y/N)");
                            } while (check);
                            break;
                        case 2:
                            do {
                                AccountModify.deleteAccount();
                                check = Validation.confirmYesNo("Do you want to continue?(Y/N)");
                            } while (check);
                            break;
                        case 3:
                            do {
                                HandleRequest.showMenu();
                                check = Validation.confirmYesNo("Do you want to continue?(Y/N)");
                            } while (check);
                        default:
                            break;
                    }
                } while (userChoice != menu.size());
            } else {
                break;
            }
        }
    }
}