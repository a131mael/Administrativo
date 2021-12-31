/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.service.administrativo.util;

import java.util.Properties;

import javax.naming.InitialContext;

/**
 *
 * @author Abimael Fidencio
 */
public class ServiceLocator {

	private InitialContext jndiContext;
	private static ServiceLocator instance;

	private ServiceLocator() {
		try {

			Properties props = new Properties();

			jndiContext = new InitialContext(props);

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public static ServiceLocator getInstance() {
		if (instance == null || instance.jndiContext == null) {
			instance = new ServiceLocator();
		}
		return instance;
	}

}
