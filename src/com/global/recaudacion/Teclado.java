package com.global.recaudacion;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 *
 * @author mvilches
 */
public class Teclado {

    private String texto;
    private String comandos;
    private Robot robotTeclado;

    public Teclado(String texto, String comandos) {
        this.texto = texto;
        this.comandos = comandos;
    }

    public Teclado() {
    }

    public Teclado(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getComandos() {
        return comandos;
    }

    public void setComandos(String comandos) {
        this.comandos = comandos;
    }

    public boolean teclearTexto() {
        try {
            robotTeclado = new Robot();
            for (int x = 0; x < this.texto.length(); x++) {
                robotTeclado.keyPress(this.texto.toUpperCase().codePointAt(x));
                robotTeclado.keyRelease(this.texto.toUpperCase().codePointAt(x));
            }
            return true;
        } catch (AWTException awEx) {
            System.out.println("Ha ocurrido un problema al manipular el teclado!");
            return false;
        }
    }
}
