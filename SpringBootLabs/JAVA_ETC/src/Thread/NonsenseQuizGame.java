package Thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 단일 클래스 멀티스레드 넌센스 퀴즈 게임
 *
 * 목적:
 * - 외부 파일이나 별도 클래스 없이 "이 파일 하나"로 구동되는 콘솔 게임
 * - 멀티스레드(타이머, 힌트, 입력, 효과음) 혼합
 * - 제한시간 내 정답을 맞추면 점수/콤보/보너스 적용
 *
 * 주요 기능:
 * 1) 라운드(문제)당 제한시간(예: 15초) 내에 정답 입력
 * 2) 힌트 스레드가 일정 간격으로 힌트를 공개
 * 3) 효과음(틱틱틱) 스레드가 게임 진행감을 제공
 * 4) 입력 스레드가 사용자 입력을 감시, 맞추면 즉시 승리 처리
 * 5) 시간 초과/오답/포기(quit) 처리
 *
 * 설계 포인트(왜 이렇게 썼는지):
 * - 공유 상태(정답 여부, 종료 플래그, 남은 시간)는 Atomic 계열과 volatile로 관리
 * - 스레드 간 종료 동기화는 CountDownLatch 및 플래그로 처리
 * - 입력은 Blocking I/O 이므로 입력 스레드 분리
 * - 라운드 마다 스레드들을 새로 만들고, 종료 시 플래그로 안전하게 정리
 *
 * 실무적 고려:
 * - 콘솔 게임 특성상 UI/입력 경합이 생길 수 있어 메시지 최소 충돌 형식
 * - try-with-resources 대신, 전역 stdin은 한 번만 열어 공유(입력 스레드 내부에서만 사용)
 * - 장시간 블로킹을 피하려면 System.in.available() 활용도 가능하지만, 교차 OS 호환성 위해 단순화
 */
public class NonsenseQuizGame {

    /**
     * 퀴즈 데이터 구조
     * - question: 문제
     * - answers: 정답 문자열 배열(여러 변형 허용)
     * - hints: 힌트 단계별 문자열
     */
    private static class QA {
        final String question;
        final List<String> answers; // 소문자 비교
        final List<String> hints;

        QA(String q, String[] a, String[] h) {
            this.question = q;
            this.answers = Arrays.stream(a).map(s -> s.toLowerCase(Locale.ROOT).trim()).toList();
            this.hints = Arrays.asList(h);
        }
    }

    // 예시 넌센스 문제들
    private static final List<QA> POOL = List.of(
        new QA("세상에서 가장 센 사자(獅子)는?", new String[]{"바로 옆에 있는 사자", "옆사자", "옆에 있는 사자", "옆 사자"},
               new String[]{"'센'은 힘이 세다가 아니다.", "말장난이다.", "방향이 힌트다."}),
        new QA("바나나가 웃으면?", new String[]{"바나나킥", "킥"},
               new String[]{"과자 이름", "운동 동작 단어 포함"}),
        new QA("컴퓨터가 싫어하는 간식은?", new String[]{"쿠키", "cookies", "cookie"},
               new String[]{"웹과 친함", "브라우저 설정에 남는다."}),
        new QA("자동차가 울면?", new String[]{"카카오", "car-카오"},
               new String[]{"말장난", "메신저 아님"}),
        new QA("도둑이 좋아하는 아이스크림은?", new String[]{"누가바", "누가", "누가바 아이스크림"},
               new String[]{"옛날 과자", "브랜드명 자체가 힌트"})
    );

    // 콘솔 입력용
    private static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
    private static final Random RND = new Random();

    // 게임 설정값(필요시 바꿔서 튜닝)
    private static final int ROUND_LIMIT_SECONDS = 15;     // 라운드 제한시간(초)
    private static final int HINT_INTERVAL_SECONDS = 5;    // 힌트 공개 간격(초)
    private static final int TICK_INTERVAL_MS = 500;       // 효과음 간격(ms)
    private static final int ROUNDS = 5;                   // 총 라운드 수
    private static final int BASE_SCORE = 100;             // 기본 점수
    private static final int TIME_BONUS_PER_SEC = 5;       // 남은 1초당 보너스
    private static final int COMBO_BONUS = 30;             // 연속 정답 보너스

    public static void main(String[] args) {
        System.out.println("=== 멀티스레드 넌센스 퀴즈 ===");
        System.out.println("설명: 제한시간 내에 정답을 입력하세요. 힌트가 주기적으로 공개됩니다.");
        System.out.println("종료하려면 언제든 'quit' 입력");
        System.out.println("--------------------------------------");

        int score = 0;
        int combo = 0;

        for (int round = 1; round <= ROUNDS; round++) {
            System.out.println();
            System.out.println("---------- Round " + round + " / " + ROUNDS + " ----------");

            QA qa = POOL.get(RND.nextInt(POOL.size()));

            // 라운드 공유 상태
            AtomicBoolean answered = new AtomicBoolean(false);      // 정답 여부
            AtomicBoolean timeUp = new AtomicBoolean(false);        // 시간 종료 여부
            AtomicBoolean quit = new AtomicBoolean(false);          // 사용자가 quit 입력
            AtomicInteger remainingSec = new AtomicInteger(ROUND_LIMIT_SECONDS); // 남은 시간
            CountDownLatch doneLatch = new CountDownLatch(1);       // 라운드 종료 동기화

            // 1) 타이머 스레드: 남은 시간 카운트다운, 0되면 timeUp=true
            Thread timerThread = new Thread(() -> {
                try {
                    int sec = ROUND_LIMIT_SECONDS;
                    while (!answered.get() && !quit.get() && sec > 0) {
                        Thread.sleep(1000);
                        sec--;
                        remainingSec.set(sec);
                    }
                    if (!answered.get() && !quit.get() && remainingSec.get() <= 0) {
                        timeUp.set(true);
                    }
                } catch (InterruptedException ignored) {
                    // 인터럽트는 라운드 종료로 간주
                }
            }, "timer");

            // 2) 힌트 스레드: HINT_INTERVAL_SECONDS마다 힌트 공개
            Thread hintThread = new Thread(() -> {
                try {
                    int idx = 0;
                    while (!answered.get() && !timeUp.get() && !quit.get() && idx < qa.hints.size()) {
                        Thread.sleep(HINT_INTERVAL_SECONDS * 1000L);
                        if (answered.get() || timeUp.get() || quit.get()) break;
                        System.out.println("[힌트] " + qa.hints.get(idx));
                        idx++;
                    }
                } catch (InterruptedException ignored) {
                }
            }, "hint");

            // 3) 효과음 스레드: 틱틱틱 출력(진행감)
            Thread tickThread = new Thread(() -> {
                try {
                    while (!answered.get() && !timeUp.get() && !quit.get()) {
                        Thread.sleep(TICK_INTERVAL_MS);
                        System.out.print(".");
                    }
                } catch (InterruptedException ignored) {
                }
            }, "ticker");

            // 4) 입력 스레드: 사용자의 입력을 받아 정답/포기 판단
            Thread inputThread = new Thread(() -> {
                try {
                    while (!answered.get() && !timeUp.get() && !quit.get()) {
                        // 콘솔 입력(블로킹). 라운드 종료 시에는 상위에서 플래그로 판단해 스레드를 끊는다.
                        System.out.println();
                        System.out.print("정답 입력 > ");
                        String line = BR.readLine();
                        if (line == null) {
                            // 입력 스트림 종료(환경에 따라 발생 가능). 라운드 포기 처리.
                            quit.set(true);
                            break;
                        }
                        String v = line.toLowerCase(Locale.ROOT).trim();
                        if (v.equals("quit")) {
                            quit.set(true);
                            break;
                        }
                        if (qa.answers.contains(v)) {
                            answered.set(true);
                            break;
                        } else {
                            System.out.println("오답입니다.");
                        }
                    }
                } catch (IOException e) {
                    System.out.println("입력 에러가 발생했습니다: " + e.getMessage());
                    quit.set(true);
                }
            }, "input");

            // 5) 진행(오케스트레이션): 문제 출력하고 스레드 시작, 종료 조건 감시
            System.out.println("[문제] " + qa.question);
            System.out.println("(제한시간: " + ROUND_LIMIT_SECONDS + "초, 힌트는 " + HINT_INTERVAL_SECONDS + "초마다 공개)");

            long start = System.nanoTime();

            // 스레드 시작
            timerThread.start();
            hintThread.start();
            tickThread.start();
            inputThread.start();

            // 라운드 종료 감시 루프
            new Thread(() -> {
                try {
                    while (true) {
                        if (answered.get() || timeUp.get() || quit.get()) {
                            break;
                        }
                        Thread.sleep(100);
                    }
                } catch (InterruptedException ignored) {
                } finally {
                    // 스레드 종료 신호: 인터럽트로 빠르게 깨어나도록
                    timerThread.interrupt();
                    hintThread.interrupt();
                    tickThread.interrupt();
                    // inputThread는 BR.readLine() 블로킹이므로 인터럽트로 즉시 깨우기 어렵다.
                    // 그러나 플래그에 의해 라운드가 끝났음을 알리고, 다음 라운드 진입 전 대기 없이 진행.
                    // 콘솔 블로킹 해제를 강제하려면 OS 별 터미널 설정이 필요하므로 여기서는 단순 처리.
                    doneLatch.countDown();
                }
            }, "round-guard").start();

            // 라운드 종료까지 블록
            try {
                doneLatch.await();
            } catch (InterruptedException ignored) {
            }

            long elapsedMs = Duration.ofNanos(System.nanoTime() - start).toMillis();
            int remain = Math.max(0, remainingSec.get());

            // 라운드 결과 처리
            if (quit.get()) {
                System.out.println();
                System.out.println("사용자 종료 요청으로 게임을 종료합니다.");
                break;
            } else if (timeUp.get()) {
                System.out.println();
                System.out.println("시간 초과! 정답을 맞추지 못했습니다.");
                System.out.println("정답 예시: " + qa.answers.get(0));
                combo = 0;
            } else if (answered.get()) {
                int gained = BASE_SCORE + (remain * TIME_BONUS_PER_SEC) + (combo > 0 ? COMBO_BONUS : 0);
                score += gained;
                combo++;
                System.out.println();
                System.out.println("정답! 걸린 시간: " + (elapsedMs / 1000.0) + "초");
                System.out.println("획득 점수: " + gained + " (기본 " + BASE_SCORE
                        + " + 시간보너스 " + (remain * TIME_BONUS_PER_SEC)
                        + " + 콤보보너스 " + (combo > 1 ? COMBO_BONUS : 0) + ")");
            } else {
                // 이 분기는 거의 오지 않지만, 안전망
                System.out.println();
                System.out.println("라운드가 비정상 종료되었습니다.");
                combo = 0;
            }

            System.out.println("현재 점수: " + score + " / 현재 콤보: " + combo);
            System.out.println("--------------------------------------");
        }

        System.out.println();
        System.out.println("=== 게임 종료 ===");
        System.out.println("최종 점수: " + (/*추가 가중치 가능*/ 0 + getFinalScoreWithStreakBonus(score)) );
        System.out.println("플레이해 주셔서 감사합니다.");
    }

    /**
     * 최종 점수 후처리 로직(예: 긴 플레이 보너스, 난이도 보정 등)
     * 현재는 샘플로 5% 보너스 적용.
     *
     * @param base 기존 점수
     * @return 보정된 최종 점수
     */
    private static int getFinalScoreWithStreakBonus(int base) {
        // 왜 5%? 간단한 후처리 예시. 실제 게임 규칙에 맞게 조정하면 된다.
        return (int) Math.round(base * 1.05);
    }
}

