package AOP;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;

public class CalcProxy {
	 private final Calc target; // 실제 주업무 객체
	    private final Log log = LogFactory.getLog(this.getClass());

	    // 생성자 주입: 프록시가 감쌀 대상(Calc)을 받음
	    public CalcProxy(Calc target) {
	        this.target = target;
	    }

	    // 프록시 메서드: 실제 메서드를 호출하기 전에 시간 측정 + 로그 출력
	    public int Add(int x, int y) {
	        StopWatch sw = new StopWatch();
	        log.info("[타이머 시작] Add() 실행");
	        sw.start();

	        // 실제 타깃 메서드 실행
	        int result = target.Add(x, y);

	        sw.stop();
	        log.info("[타이머 종료] Add()");
	        log.info("[실행시간] " + sw.getTotalTimeMillis() + "ms");
	        return result;
	    }

	    public int Mul(int x, int y) {
	        StopWatch sw = new StopWatch();
	        log.info("[타이머 시작] Mul() 실행");
	        sw.start();

	        int result = target.Mul(x, y);

	        sw.stop();
	        log.info("[타이머 종료] Mul()");
	        log.info("[실행시간] " + sw.getTotalTimeMillis() + "ms");
	        return result;
	    }
}
