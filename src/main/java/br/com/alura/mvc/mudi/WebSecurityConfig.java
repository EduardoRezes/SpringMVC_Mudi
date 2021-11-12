package br.com.alura.mvc.mudi;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/home/**").permitAll()
				.anyRequest().authenticated()
			.and()
				//Neste codigo abaixo podemos notar que o formLogin passando uma lambda com loginPage(que recebe a pagina a ser redirecionado) e a permissão.
				.formLogin(form -> form
					.loginPage("/login")
					.defaultSuccessUrl("/usuario/pedido", true)
					.permitAll()
				)
				.logout(logout -> {
					logout.logoutUrl("/logout")
						.logoutSuccessUrl("/home");
				});
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//instanciado um class para criptografar a senha
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		//Pega o usuario, criptografa a senha e envia ao Banco de Dados
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(encoder);
	}

}
