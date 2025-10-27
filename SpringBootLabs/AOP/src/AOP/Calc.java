package AOP;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;

/*
 간단한 사칙 연산기 만들기
 	주업무 : (주관심, 코어컨선) : 사칙연산(ADD, MUL, ...) 주기능
 */

// 요구 사항 연산에 걸린 시간을 측정해보라. - 보조기능(보조관심, 공통 관심)
// 요구 사항2 : 로그 출력(보조 기능, 공통 관심)
public class Calc {
    // 덧셈 기능
    public int Add(int x, int y) {
        return x + y;
    }

    // 곱셈 기능
    public int Mul(int x, int y) {
        return x * y;
    }
	
}
