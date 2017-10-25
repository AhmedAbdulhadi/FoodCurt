package com.novent.foodordering.config;

/*
package com.igas.express.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;



/*
 * created by novent Group backend team (shakalns) 
 */
/*
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
	
	private static final String SUPPLIER = "SUPPLIER";
	private static final String USER = "USER";
	private static final String ADMIN = "ADMIN";


	@Override 
	public void configure(HttpSecurity httpSecurity ) throws Exception
	{

		httpSecurity.csrf().disable()
		  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		  .authorizeRequests().antMatchers("/api/v1/admin/*").hasRole(ADMIN)
		  .antMatchers("/api/v1/*").hasRole(USER)
		  .antMatchers("/api/v1/supplier/*").hasRole(SUPPLIER).and().httpBasic();
		 
		
	}
	
	
	@Autowired
	public void configureGlobel(AuthenticationManagerBuilder auth) throws Exception
	{
		
		auth.inMemoryAuthentication().withUser("user").password("123").roles(USER)
		.and().withUser("admin").password("123").roles(ADMIN)
		.and().withUser("supplier").password("123").roles(SUPPLIER);
		
	}

}
*/