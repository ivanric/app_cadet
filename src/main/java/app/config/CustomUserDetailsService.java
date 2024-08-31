package app.config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.entity.RolEntity;
import app.entity.UsuarioEntity;
import app.repository.UsuarioRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService{

	 @Autowired
	 private UsuarioRepository usuarioRepository;
	
	//carga usuarios con sus datos
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UsuarioEntity usuario= usuarioRepository.findByUsername(username);
		if (usuario==null) {
			throw new UsernameNotFoundException("Usuario o password Invalido");
		}
		return new User(usuario.getUsername(),usuario.getPassword(), mapearAutoridadesRoles(usuario.getRoles()));
	}
	private Collection<? extends GrantedAuthority> mapearAutoridadesRoles(Collection<RolEntity> roles){
		return roles.stream().map(role-> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
	}
//	private String username;
//	private String password;
//	private Collection<? extends GrantedAuthority> roles;
//	private String 
	
	
}
