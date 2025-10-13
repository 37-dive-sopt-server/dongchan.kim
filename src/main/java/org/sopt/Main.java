package org.sopt;

import org.sopt.controller.MemberController;
import org.sopt.domain.Member;
import org.sopt.domain.enums.Gender;
import org.sopt.dto.member.MemberSignupRequest;
import org.sopt.exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        MemberController memberController = new MemberController();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n✨ --- DIVE SOPT 회원 관리 서비스 --- ✨");
            System.out.println("---------------------------------");
            System.out.println("1️⃣. 회원 등록 ➕");
            System.out.println("2️⃣. ID로 회원 조회 🔍");
            System.out.println("3️⃣. 전체 회원 조회 📋");
            System.out.println("4️⃣. 회원 삭제 🗑️");   // 번호 정합성 맞춤
            System.out.println("5️⃣. 종료 🚪");
            System.out.println("---------------------------------");
            System.out.print("메뉴를 선택하세요: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1": {
                        System.out.print("등록할 회원 이름을 입력하세요: ");
                        String name = scanner.nextLine().trim();
                        if (name.isEmpty()) {
                            System.out.println("⚠️ 이름을 입력해주세요.");
                            break;
                        }

                        System.out.print("이메일을 입력하세요: ");
                        String email = scanner.nextLine().trim();
                        if (email.isEmpty()) {
                            System.out.println("⚠️ 이메일을 입력해주세요.");
                            break;
                        }

                        System.out.print("성별을 입력하세요 (MALE/FEMALE): ");
                        String genderInput = scanner.nextLine().trim();
                        Gender gender = Gender.valueOf(genderInput.toUpperCase()); // 잘못 입력 시 IllegalArgumentException 발생

                        System.out.print("생년월일을 입력하세요 (yyyy-MM-dd): ");
                        String birthStr = scanner.nextLine().trim();
                        LocalDate birth = LocalDate.parse(birthStr); // 형식 오류 시 DateTimeParseException
                        LocalDateTime birthDateTime = birth.atStartOfDay();

                        MemberSignupRequest request = new MemberSignupRequest(
                                name, email, gender, birthDateTime
                        );

                        Long createdId = memberController.createMember(request);
                        System.out.println("✅ 회원 등록 완료 (ID: " + createdId + ")");
                        break;
                    }

                    case "2": {
                        System.out.print("조회할 회원 ID를 입력하세요: ");
                        Long id = Long.parseLong(scanner.nextLine().trim());

                        Optional<Member> foundMember = memberController.findMemberById(id);
                        if (foundMember.isPresent()) {
                            Member m = foundMember.get();
                            System.out.println("✅ 조회된 회원:");
                            System.out.println("   • ID=" + m.getId());
                            System.out.println("   • 이름=" + m.getName());
                            System.out.println("   • 이메일=" + m.getEmail());
                            System.out.println("   • 성별=" + m.getGender());
                            System.out.println("   • 생년월일=" + m.getBirthDate());
                        } else {
                            System.out.println("⚠️ 해당 ID의 회원을 찾을 수 없습니다.");
                        }
                        break;
                    }

                    case "3": {
                        List<Member> allMembers = memberController.getAllMembers();
                        if (allMembers.isEmpty()) {
                            System.out.println("ℹ️ 등록된 회원이 없습니다.");
                        } else {
                            System.out.println("--- 📋 전체 회원 목록 📋 ---");
                            for (Member member : allMembers) {
                                System.out.println(
                                        "👤 ID=" + member.getId()
                                                + ", 이름=" + member.getName()
                                                + ", 이메일=" + member.getEmail()
                                                + ", 성별=" + member.getGender()
                                                + ", 생년월일=" + member.getBirthDate()
                                );
                            }
                            System.out.println("--------------------------");
                        }
                        break;
                    }

                    case "4": {
                        System.out.print("삭제할 회원 ID를 입력하세요: ");
                        Long id = Long.parseLong(scanner.nextLine().trim());
                        memberController.deleteMember(id); // 존재하지 않으면 MemberNotFoundException 전파
                        System.out.println("🗑️ 회원 삭제 완료 (ID: " + id + ")");
                        break;
                    }

                    case "5":
                        System.out.println("👋 서비스를 종료합니다. 안녕히 계세요!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("🚫 잘못된 메뉴 선택입니다. 다시 시도해주세요.");
                }

            } catch (Throwable e) {
                ExceptionHandler.handle(e);
            }
        }
    }


}
