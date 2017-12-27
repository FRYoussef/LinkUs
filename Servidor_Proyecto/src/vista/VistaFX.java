package vista;

import control.ControlJavaFX;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import logica.IObserver;


public class VistaFX implements IObserver{
	private boolean arrancado;
	private ControlJavaFX control;
	
	@FXML
	private Button btArrancar;
	
	@FXML
	private Button btParar;
	
	@FXML
	private Button btLimpiar;
	
	@FXML
	private TextArea taMensajes;
	
	@FXML
	private TextField tfPuerto;
	
	public VistaFX(){
		arrancado = false;
		control = new ControlJavaFX();
		control.annadeObserver(this);
	}
	
	/**
	 * Arrancará el servidor, para ello notifica al control
	 */
	
	@FXML
	public void onClickArrancar(){
		try{
			int puerto = Integer.parseInt(tfPuerto.getText());
			tfPuerto.clear();
			habilitaBotones();
			muestraTexto("Servidor arrancado....");
			control.arrancaServidor(puerto);
		}catch(NumberFormatException e){
			muestraTexto("Por favor, Introduzca un numero de puerto valido");
			tfPuerto.clear();
		}
	}
	
	/**
	 * Parará el servidor, notificando al control
	 */
	
	@FXML
	public void onClickParar(){
		control.paraServidor();
		habilitaBotones();
		muestraTexto("Servidor apagado.");
	}
	
	@FXML
	public void onClickLimpiar(){
		taMensajes.clear();
	}
	
	/**
	 * Servirá para habilitar o desabilitar los botones de
	 * arrancar y parar
	 */
	
	private void habilitaBotones(){
		if(arrancado){
			tfPuerto.setEditable(true);
			btArrancar.setDisable(false);
			btParar.setDisable(true);
		}else{
			tfPuerto.setEditable(false);
			btArrancar.setDisable(true);
			btParar.setDisable(false);
		}
		arrancado = !arrancado;
	}
	
	/**
	 * Muestra un texto en el text area
	 * @param texto a mostrar
	 */
	
	private synchronized void muestraTexto(String texto){
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				taMensajes.appendText(texto + "\n");
			}
		});
	}

	@Override
	public void onMuestraTexto(String texto) {
		muestraTexto(texto);
	}
}
