package com.demo.bbq.support.constant;

/**
 * <br/>Clase Constant que define expresiones regulares comunes para el proyecto.<br/>
 *
 * @author Miguel Armas Abt <br/>
 */
public class RegexConstant {

  private RegexConstant() {}

  public static final String ANY_STRING = "^([.0-9a-zA-ZŸÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÒÓÔÕÖ×ØÙÚÛÜÝàáâãäåæçèéêëìíîïòóôõöùúûüýÿÑñáéíóúÁÉÍÓÚ´‘-]+\\s)*[.0-9a-zA-ZŸÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÒÓÔÕÖ×ØÙÚÛÜÝàáâãäåæçèéêëìíîïòóôõöùúûüýÿÑñáéíóúÁÉÍÓÚ‘´-]+$";

}
