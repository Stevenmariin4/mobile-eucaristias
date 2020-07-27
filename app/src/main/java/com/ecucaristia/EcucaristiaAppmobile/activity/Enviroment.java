package com.ecucaristia.EcucaristiaAppmobile.activity;

public class Enviroment {

    public String url = "http://207.244.245.18:3000";
    public String Iglesias = "/home/iglesias";
    public String Reservas = "/home/reserva";
    public String UpdateReservas = "/home/update/reserva";
    public String listReservas = "/home/list";


    public String getIglesias() {
        return url + Iglesias;
    }

    public String getReservas() {
        return url + Reservas;
    }

    public String getUpdateReservas() {
        return url + UpdateReservas;
    }

    public String getListReservas() {
        return url + listReservas;
    }
}
