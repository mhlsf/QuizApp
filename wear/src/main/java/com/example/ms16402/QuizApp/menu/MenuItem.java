package com.example.ms16402.QuizApp.menu;

/**
 * Created by ms16402 on 10/05/2016.
 */
public class MenuItem {
    String main;
    String info;

    MenuItem(String m, String i) {
        main = m;
        info = i;
    }

    MenuItem(String m){
        main = m;
        info = null;
    }

    public String getMain() {
        return main;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
