package AOP;

public class Program {

	public static void main(String[] args) {
        Calc calc = new Calc();               // 원본 객체
        CalcProxy proxy = new CalcProxy(calc); // 프록시 객체로 감싸기

        proxy.Add(10000, 200000);
        proxy.Mul(50, 30);

	}

}
