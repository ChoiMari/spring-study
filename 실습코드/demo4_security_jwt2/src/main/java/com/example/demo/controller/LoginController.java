package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController  //이 클래스 안에 있는 모든 함수 @ResponseBody 가진다
public class LoginController {

	@Autowired
	private JwtProps jwtProps; //key 를 가지고 오겠다(서명)
	
	//1. 클라이언트 username , password 받아서 토큰 발급
	//2. JWT (header , payload , secret key)
	
	//추가
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserMapper userMapper;
	
	
	

	//회원가입이 되었다는 조건하에 .....
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest request){
		
		//POSTMAN 사용  : method> post  ,  localhost:8090/login  , raw(JSON) {"username":"hong","password":"1004"}
		
		String username = request.getUsername();
		String password = request.getPassword();
		
		log.info("username :" + username);
		log.info("password :" + password );
		
		
			//DB연결 ..select 확인 .. 권한 정보 ... 했다고 하고
			/////////////////////////////////////////////////////////////////////////////////////////
			//회원가입 완료 ....
			//회원가입된 회원이 로그인 시도
			
			//사용자정의 mybatis  public UserDetails loadUserByUsername(String username) 사용
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			//userDetails 사용자 정보
			if(userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
			//사용자 인증 실패
			return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
			}
			//////////////////////////////////////////////////////////////////////////////////////////
			//회원이구나 
			//인증서 발급 (인증서 우리사이트 에서 놀아 : ) JWT (header , payload (권한) ,...)
			
			//payload 구성하기 위한 정보 ....userDetails
			List<String> roles = new ArrayList<>();
			
			Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
			
			//한명의 사용자가 여러개의 권한을 가질 수 있다  (admin > ROLE_USER , ROLE_ADMIN
			for(GrantedAuthority authority : authorities) {
			//권한명을 추가
				roles.add(authority.getAuthority());
			}
			
			//JWT 토큰 : payload > 사용자ID, 권한정보 , 만료타입 
			
			//***회원가입 (ROLE 이 정해지지 않아요)
			//1. 회원가입  : user (insert) , role(insert) @Transactional 
			//2. 회원가입  : user (insert) , role (Trigger)
			/*
			create or replace trigger users_roll
			after insert on users
			for each row
			
			begin
			INSERT INTO roll VALUES(:new.username,'ROLE_USER');
				if :new.userid = 'admin' then
					INSERT INTO roll VALUES(:new.userid,'ROLE_ADMIN');
				end if;
			end;  
			
			mysql 바꾸어주 ....  
			
			DELIMITER $$

			CREATE TRIGGER users_roll
			AFTER INSERT ON users
			FOR EACH ROW
			BEGIN
			    INSERT INTO roll (userid, role)
			    VALUES (NEW.username, 'ROLE_USER');
			
			    IF NEW.userid = 'admin' THEN
			        INSERT INTO roll (userid, role)
			        VALUES (NEW.userid, 'ROLE_ADMIN');
			    END IF;
			END$$

			DELIMITER ;

       */
		

		//payload (이름 ,권한)
		
		//재료준비
		//1. header 
		//2. payload (username , roles)
		//3. secretkey 식별값
		
		//토큰 생성
		
		//1. 서명
		String secretkey = jwtProps.getSecretkey();
		
		//서명이 사용될 때는 (토큰 byte[] 변환)
		byte[] signingkey = jwtProps.getSecretkey().getBytes(); //실제 토큰에 들어가는 값
		
		String jwt = Jwts.builder()
				    .signWith(Keys.hmacShaKeyFor(signingkey),Jwts.SIG.HS512)
				    .header().add("typ",SecurityConstants.TOKEN_TYPE).and()
				    .expiration(new Date(System.currentTimeMillis() + 1000*60*60*24*1))  //24시간
				    .claim("uid", username)
					.claim("rol", roles)
					.compact();
		log.info("jwt 완성본 " + jwt );
		
		/*
		토큰 만들기
		1. Keys.hmacShaKeyFor(signingKey) 키 제공하기 
		2. Jwts.SIG.HS512  알고리즘 설정 정보 
		3. SecurityConstants.TOKEN_TYPE >> public static final String TOKEN_TYPE="JWT";
		4. expiration 소멸타임 (memory , file 쿠키) : file 쿠키 (소멸시간) ...session 에 의존하는 memory cookie
		   expiration(new Date( System.currentTimeMillis() + 1000*60*60*24*1))
		 
		 Access Token의 한계와 Refresh Token의 필요성 고민 ^^
		/*
			[ 기초 용어 ]
			
			클레임(Claim) 토큰 기반의 인증
			클레임이란 [[ 사용자 정보나 데이터 속성 ]]등을 의미한다. 
			그래서 [[ 클레임 토큰 이라고 하면 토큰 안에 사용자 정보나 데이터 속성들을 담고있는 토큰 ]]이라 생각하면 되고, 
			이런 클레임을 기반한 토큰 중 가장 대표적인 것이 JWT가 있다
			
			Bearer	토큰을 가진 자격이 인증됨을 의미
			Bearer는 "운반자" 또는 "소지자"라는 뜻입니다.
            즉, **"이 토큰을 가진 자(Bearer)는 인증된 사용자로 간주한다"**는 의미
           
            Authorization 헤더는 여러 인증 방식이 가능하기 때문에,
            Bearer, Basic, Digest 등 어떤 인증 방식을 사용하는지 구분이 필요합니다.
            
            
            클라이언트(Client)와 서버(Server)간의 통신을   상태유지(Stateful) 하느냐, 상태유지하지않음(Stateless) 으로 하느냐


			Stateless (무상태)
			무상태는 반대로 클라이언트와 서버 관계에서 [[ 서버가 클라이언트의 상태를 보존하지 않음 ]]을 의미한다.

			Stateless 구조에서 
			서버는 단순히 요청이 오면 응답을 보내는 역할만 수행하며, [[상태 관리는 전적으로 클라이언트에게 책임]]이 있는 것이다.
			즉, 클라이언트와 서버간의 통신에 필요한 [[모든 상태 정보들은 클라이언트에서 가지고 있다가]] 서버와 통신할때 
			데이터를 실어 보내는 것이 [[무상태 구조이다]]
            
		*/
		
		
		return new ResponseEntity<String>(jwt,HttpStatus.OK);
	
	
	}
	
	/*
	   headers: {
	    Authorization: `Bearer ${localStorage.getItem('token')}`
	  } 
	  클라이언트 전송
	*/
	@GetMapping("/user/info")
	public ResponseEntity<?> userInfo(@RequestHeader(name="Authorization") String header){
		
		//header => `Bearer ${localStorage.getItem('token')}`
		log.info("header 정보");
		log.info("Authorization : " + header);
		
		//서명
		String secretkey = jwtProps.getSecretkey();
		//서명이 사용될 때는 (토큰 byte[] 변환)
		byte[] signingkey = jwtProps.getSecretkey().getBytes(); //실제 토큰에 들어가는 값
		
		//Authorization > Bearer dfaewgtawew121dsfas  전송된 데이터
		//public static final String TOKEN_PREFIX="Bearer ";
		String jwt = header.replace(SecurityConstants.TOKEN_PREFIX, "");
		
		//토큰 해석
		Jws<Claims> parsedToken = Jwts.parser()
				                  .verifyWith(Keys.hmacShaKeyFor(signingkey))
				                  .build()
				                  .parseSignedClaims(jwt);
		
		log.info("parsedToken : " + parsedToken);
		
		String username = parsedToken.getPayload().get("uid").toString();
		log.info("username : " + username);
		
		Claims claims = parsedToken.getPayload(); //데이터 username , rols
		
		Object roles = claims.get("rol");
		log.info("roles : " + roles );
		
		
		
		return new ResponseEntity<String>(parsedToken.toString(), HttpStatus.OK);
	}
	
	/*
	 header={typ=JWT, alg=HS512},
	 payload={exp=1761876584, uid=hong, rol=[ROLE_USER, ROLE_ADMIN]},
	 signature=MuzZXtG2rX2-SvOLhEhCC8rRmJesc1-zWbfO1ig-POy8G_ScTWlmxLPrLVgcuEUOoAbyyF9wDDdIWpqiQhxnLw 
	  
	 */
	
	
	//  /admin/info 요청에 대해서
	@GetMapping("/admin/info")  // 필터에 JwtAuthenticationFilter > AOP
	public ResponseEntity<?> amdinUserInfo(){
		log.info("관리자님 방가");
		
		return new ResponseEntity<String>("관리자님 방가",HttpStatus.OK);
		
	}
	
	
	
	//   /register  회원가입   POSTMAN 테스트 
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody AuthenticationRequest request){
		System.out.println("/register");
		String username = request.getUsername();
		String rawpassword = request.getPassword();
		
		//중복체크
		if(userMapper.findByUsername(username) != null) {
			return new ResponseEntity<String>("User already exists",HttpStatus.BAD_REQUEST);
		}
		
		//가입
		//비밀번호는 암호화
		String encodedPassword = passwordEncoder.encode(rawpassword);
		
		//저장DB
		User user = new User();
		user.setUsername(username);
		user.setPassword(encodedPassword);
		user.setRole("ROLE_USER");  //인증과 권한 (ROLE)
		
		userMapper.saveUser(user);
		
		return new ResponseEntity<String>("user registered successful",HttpStatus.CREATED);
	}
	
	
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

