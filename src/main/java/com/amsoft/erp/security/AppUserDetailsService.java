package com.amsoft.erp.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.amsoft.erp.model.GrupoPermissao;
import com.amsoft.erp.model.Usuario;
import com.amsoft.erp.repository.Usuarios;
import com.amsoft.erp.util.cdi.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuarios usuarios = CDIServiceLocator.getBean(Usuarios.class);
		Usuario usuario = usuarios.porEmail(email);
		
		UsuarioSistema user = null;
		
		if (usuario != null) {
			user = new UsuarioSistema(usuario, getGrupos(usuario));
		}
		
		return user;
	}

	private Collection<? extends GrantedAuthority> getGrupos(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		if (usuario.getEmpresaGrupo() != null) {
			for (GrupoPermissao grupo : usuario.getEmpresaGrupo().getGrupo().getPermissoes()) {
				authorities.add(new SimpleGrantedAuthority(grupo.getNome().toUpperCase()));
			}
		}
		
		
		return authorities;
	}

}
