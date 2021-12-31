package br.com.aff.Administrativo.CNAB_240;

import java.io.File;

public class CONSTANTES {

	public static int projetoTefamel = 1;
	public static int projetoAdonai = 2;
	public static String nomeProjeto = "adonai";
	public static String PATH_ENVIAR_CNAB = File.separator+"home"+File.separator+"ubuntu" + File.separator+"Skyunix" + File.separator +"outbox" + File.separator;
	//public static String PATH_ENVIAR_CNAB = File.separator+"home"+File.separator+"ubuntu" + File.separator+"cnab" + File.separator +nomeProjeto + File.separator + "enviar" + File.separator;
	/*public static String PATH_ENVIAR_CNAB = "C:\\Sicoobnet\\RetornoCNAB\\"+nomeProjeto + File.separator + "enviar" + File.separator;*/
	public static String PATH_ENVIAR_BAIXA = File.separator+"home"+File.separator+"ubuntu" + File.separator+"cnab" + File.separator +nomeProjeto + File.separator + "enviar" + File.separator +"baixa" +  File.separator;
	/*public static String PATH_ENVIAR_BAIXA = "C:\\Sicoobnet\\RetornoCNAB\\"+nomeProjeto + File.separator + "enviar" + File.separator +"baixa" + File.separator;*/
	
	public static String PATH_ENVIAR_BAIXA_CANCELADOS = File.separator+"home"+File.separator+"ubuntu" + File.separator+"cnab" + File.separator +nomeProjeto + File.separator + "enviar" + File.separator +"baixaCancelados" +  File.separator;
	/*public static String PATH_ENVIAR_BAIXA_CANCELADOS = "C:\\Sicoobnet\\RetornoCNAB\\"+nomeProjeto + File.separator + "enviar" + File.separator +"baixaCancelados" + File.separator;*/
	
	/*public static String LOCAL_ARMAZENAMENTO_REMESSA = "C:\\Sicoobnet\\RetornoCNAB\\";*/
	public static String LOCAL_ARMAZENAMENTO_REMESSA = File.separator+"home"+File.separator + "ubuntu" + File.separator+"Skyunix" + File.separator+"inbox"+ File.separator;
	//public static String LOCAL_ARMAZENAMENTO_REMESSA_IMPORTADA = "C:\\Sicoobnet\\RetornoCNABIMPORTADO\\";
	public static String LOCAL_ARMAZENAMENTO_REMESSA_IMPORTADA = File.separator+"home" + File.separator +"ubuntu" + File.separator+"Skyunix" + File.separator+"importado"+ File.separator;
	
	
	
	public static String emailFinanceiro = "financeiro32424194@gmail.com";
	public static String senhaEmailFinanceiro = "8419260428";
}
