package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.contants.SecurityConstants;
import com.example.demo.domain.AuthenticationRequest;
import com.example.demo.domain.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.prop.JwtProps;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController //-> 이 클래스 안에 있는 모든 메서드는
// @ResponsseBody를 가진다(뷰를 거치지 않고 클라이언트로 바로 데이터를 던짐(주로 JSON))
@Slf4j
@RequiredArgsConstructor
public class LoginController {
	private final JwtProps jwtProps; //키를 가지고 오겠다(서명, 시그니처)
	
	//1. DB를 연동했다고 가정하고,
	// 클라이언트에게 username, pw를 받아서 토큰을 발급
	
	//2. Jwt의 구조 3가지 형식을 가지고 있음
	// -> 헤더.페이로드(클레임즈).서명(시그니처, 시크릿 키)
	
	private final UserDetailsService userDetailsService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final UserMapper userMapper;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest request){
		//POSTMAN 사용 : method -> post, localhost:8090/login 
		//row데이터(JSON) { "username" : "hong", "password" : "1004" }
		String username = request.getUsername();
		String password = request.getPassword();
		
		log.info("usrename : " + username);
		log.info("password : " + password);
		
		/*
	    
		    ******************************************************************
		    *JwtAuthenticationFilter Token  발급된 사용자를 사이트 접속시 .... 
		    *비밀번호 확인 필요없어요 ... Token 자체 시그니쳐 확인 되면 통과 
		    *인증되어서 토큰을 가지고 있고 ... 내 사이트 특정 주소를 요청을 보내면 ... 검증 
		    ******************************************************************
		    
		    
			1. 이 필터는 OncePerRequestFilter를 상속하므로 HTTP 요청마다 한 번만 실행.
		    2. Authorization: Bearer <JWT> 형식의 헤더에서 JWT를 추출하고 검증.
		    3. 유효한 JWT가 있는 경우 → 사용자 정보 조회 + 인증 처리
		    4. 예외 URL (/login , /register) 필터 적용 제외
		    
		    
		    처리)
		    1. UserDetailsService 서비스 사용
		*/


		// DB연결 ..select 확인 .. 권한 정보 ... 했다고 하고
		/////////////////////////////////////////////////////////////////////////////////////////
		// 회원가입 완료 ....
		// 회원가입된 회원이 로그인 시도

		// 사용자정의 mybatis public UserDetails loadUserByUsername(String username) 사용
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		// userDetails 사용자 정보
		if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
			// 사용자 인증 실패
			return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
		}
		//////////////////////////////////////////////////////////////////////////////////////////
		// 회원이구나
		// 인증서 발급 (인증서 우리사이트 에서 놀아 : ) JWT (header , payload (권한) ,...)

		// payload 구성하기 위한 정보 ....userDetails
		List<String> roles = new ArrayList<>();

		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

		// 한명의 사용자가 여러개의 권한을 가질 수 있다 (admin > ROLE_USER , ROLE_ADMIN
		for (GrantedAuthority authority : authorities) {
			// 권한명을 추가
			roles.add(authority.getAuthority());
		}

		// JWT 토큰 : payload > 사용자ID, 권한정보 , 만료타입

		// 회원가입 (ROLE 이 정해지지 않아요)
		// 1. 회원가입 : user (insert) , role(insert) @Transactional
		// 2. 회원가입 : user (insert) , role (Trigger)
		/*
		 create or replace trigger users_roll 
		 after insert on users 
		 for each row
		 begin INSERT INTO roll VALUES(:new.username,'ROLE_USER'); 
			 if :new.userid = 'admin' then 
				 INSERT INTO roll 
				 VALUES(:new.userid,'ROLE_ADMIN'); 
			 end if;
		 end;
 
		 * mysql 바꾸어주 ....
		 * 
		 */
			

			//payload (이름 ,권한)
			
			//재료준비
			//1. header 
			//2. payload (username , roles)
			//3. secretkey 식별값
			
			//토큰 생성
		
		//권한 - 연습용
//		List<String> roles = new ArrayList<>();
//		roles.add("ROLE_USER");
//		roles.add("ROLE_ADMIN");
		
		//페이로드에 사용자 이름과 권한을 넣음
		// 토큰 만들 재료
		//1. header
		//2. 페이로드(클레임즈) - username, roles
		//3. 서명(시크릿키) 식별값
		
		//-> 토큰 생성
		//서명 가지고 오기
		String secretkey = jwtProps.getSecretKey();
		
		// 서명이 사용될 땐, 토큰 사용 시에는 반드시 바이트 배열로 변환시켜야한다(약속)
		// byte[]
		byte[] signingkey = jwtProps.getSecretKey().getBytes(); 
		// 실제 토큰에 들어갈 값
		
		//토큰 조합
		String jwt = Jwts.builder().signWith(Keys.hmacShaKeyFor(signingkey), Jwts.SIG.HS512)
				.header().add("typ", SecurityConstants.TOKEN_TYPE)
				.and()
				.expiration(new Date(System.currentTimeMillis() + 1000*60*60*24*1))//토큰 소멸 시간 지정
				// 최소시간잡고 재발급 형태로 
				.claim("uid", username)
				.claim("role", roles)
				.compact();
		log.info("jwt : " + jwt);
		
		return new ResponseEntity<String>(jwt, HttpStatus.OK);
	}
	
	
	/*
	react 에서는 받는 토큰을 어떻게 할까요 ^^
	 
	// 로그인 요청 후 토큰 저장
	axios.post('/api/login', { username, password })
	  .then(res => {
	    const token = res.data.token;
	    localStorage.setItem('token', token);
	  });

	// 요청 시 토큰 헤더에 추가
	axios.get('/api/user', {
	  headers: {
	    Authorization: `Bearer ${localStorage.getItem('token')}`
	  }
	}); 
	  
	  
	 */
	
	/*
	토큰 만들기
	1. Keys.hmacShaKeyFor(signingKey) 키 제공하기 
	2. Jwts.SIG.HS512  알고리즘 설정 정보 
	3. SecurityConstants.TOKEN_TYPE 
 		-> public static final String TOKEN_TYPE="JWT";
	4. expiration 소멸타임 (memory , file 쿠키) : 
		file 쿠키 (소멸시간) ...session 에 의존하는 memory cookie
	   expiration(new Date( System.currentTimeMillis() + 1000*60*60*24*1))
	 
	 Access Token의 한계와 Refresh Token의 필요성 고민 ^^
	/*
		[ 기초 용어 ]
		
		클레임(Claim) 토큰 기반의 인증
		클레임이란 [[ 사용자 정보나 데이터 속성 ]]등을 의미한다. 
		그래서 [[ 클레임 토큰 이라고 하면 토큰 안에 사용자 정보나 데이터 속성들을 
		담고있는 토큰 ]]이라 생각하면 되고, 
		이런 클레임을 기반한 토큰 중 가장 대표적인 것이 JWT가 있다
		
		Bearer	토큰을 가진 자격이 인증됨을 의미
		Bearer는 "운반자" 또는 "소지자"라는 뜻입니다.
        즉, **"이 토큰을 가진 자(Bearer)는 인증된 사용자로 간주한다"**는 의미
       
        Authorization 헤더는 여러 인증 방식이 가능하기 때문에,
        Bearer, Basic, Digest 등 어떤 인증 방식을 사용하는지 
        구분이 필요합니다.
        
        
        클라이언트(Client)와 서버(Server)간의 통신을   
        상태유지(Stateful) 하느냐, 상태유지하지않음(Stateless) 으로 하느냐


		Stateless (무상태)
		무상태는 반대로 클라이언트와 서버 관계에서 
		[[ 서버가 클라이언트의 상태를 보존하지 않음 ]]을 의미한다.

		Stateless 구조에서 
		서버는 단순히 요청이 오면 응답을 보내는 역할만 수행하며, 
		[[상태 관리는 전적으로 클라이언트에게 책임]]이 있는 것이다.
		즉, 클라이언트와 서버간의 통신에 필요한 
		[[모든 상태 정보들은 클라이언트에서 가지고 있다가]] 
		서버와 통신할 때 
		데이터를 실어 보내는 것이 [[무상태 구조이다]]
        
	*/
	
	
	/*
	 전제 조건
	 클라이언트에서 이렇게 전송함
	headers: {
	    Authorization: `Bearer ${localStorage.getItem('token')}` 
	 */
	//@RequestHeader  : 헤더 값을 받을 수 있음
	@GetMapping("/user/info")
	public ResponseEntity<?> userInfo(@RequestHeader(name="Authorization") String header){
		//header : Bearer ${localStorage.getItem('token')
		log.info("[header 정보] Authorization : {}", header);
		
		//서명 가지고 오기
		String secretkey = jwtProps.getSecretKey();
		
		// 서명이 사용될 땐, 토큰 사용 시에는 반드시 바이트 배열로 변환시켜야한다(약속)
		byte[] signingkey = jwtProps.getSecretKey().getBytes(); // 실제 토큰에 들어갈 값
		
		//전송된 데이터 -> Bearer ${localStorage.getItem('token')
		//Bearer와 공백을 제거
		String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "");
		//TOKEN_PREFIX값을 빈문자열로 바꿈
				
		//토큰 해석
		Jws<Claims> parsedToken = Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(signingkey))
				.build()
				.parseSignedClaims(jwt);
		
		log.info("parsedToken : {}", parsedToken);
		
		//페이로드만 뽑아냄
		String username = parsedToken.getPayload().get("uid").toString();
		log.info("username : {}", username);
		
		Claims claims = parsedToken.getPayload();
		//데이터 username, roles
		
		Object roles = claims.get("role");
		log.info("roles : {}", roles);
		
		return new ResponseEntity<String>(parsedToken.toString(), HttpStatus.OK);
	}
	
	/*
	 header={typ=JWT, alg=HS512},
	 payload={exp=1761876596, uid=hong, role=[ROLE_USER, ROLE_ADMIN]},
	 signature=jQC00rZft5nrip26fH0ayRAHq9mPybodsjBK9KkanUVj148Y8H9EBE9rMgN5gk0DtHgF8lR2n4-YhgmtI-lUcw 
	 */
	
	// /admin/info
	@GetMapping("/admin/info") // 필터에 의해서 걸림(JwtAuthenticationFilter) -> AOP중 하나
	public ResponseEntity<?> adminUserInfo(){
		log.info("관리자 앗눙");
		return new ResponseEntity<String>("관리자 안눙", HttpStatus.OK);
	}
	
	// /register 회원가입
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody AuthenticationRequest request){
		
		String username = request.getUsername();
		String rawpassword = request.getPassword();
		
		//중복 체크
		if(userMapper.findByUsername(username)
				!= null) {
			return new ResponseEntity<String>("User already exists", HttpStatus.BAD_REQUEST);
		}
		//가입 - 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(rawpassword);
		
		//DB저장
		User user = new User();
		user.setUsername(username);
		user.setPassword(encodedPassword);
		user.setRole("ROLE_USER");
		
		userMapper.saveUser(user);
		
		return new ResponseEntity<String>("user registerd success", HttpStatus.CREATED);
	}
	
}
